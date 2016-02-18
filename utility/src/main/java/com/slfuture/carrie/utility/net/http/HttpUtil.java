package com.slfuture.carrie.utility.net.http;

import com.slfuture.carrie.base.type.core.ILink;
import com.slfuture.carrie.base.type.core.ITable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * HTTP工具类
 */
public class HttpUtil {
    /**
     * 分割符
     */
    private static final String HTTP_BOUNDARY_STRING = "-------------------205f2a74205f2a74";
    /**
     *  下载块大小
     */
    private static final int SIZE_UPLOAD = 1024 * 10;
    /**
     *  下载块大小
     */
    private static final int METHOD_GET = 1;
    /**
     *  下载块大小
     */
    private static final int METHOD_POST = 2;


    /**
     * 隐藏构造函数
     */
    private HttpUtil() { }

    /**
     * 投递请求
     *
     * @param url 地址
     * @param parameters POST参数
     * @param option 选项
     * @return 投递结果
     */
    public static String send(String url, ITable<String, Object> parameters, Option option) throws IOException {
        int method = 0;
        if(null == parameters) {
            method = METHOD_GET;
        }
        else {
            method = METHOD_POST;
        }
        int timeout = 0;
        if(null != option) {
            timeout = option.timeout;
        }
        HttpURLConnection connection = connect(url, method, timeout);
        // 上传
        if(null != parameters && parameters.size() > 0) {
            try {
                upload(connection.getOutputStream(), parameters);
            }
            catch(IOException ex) {
                return null;
            }
        }
        if(200 != connection.getResponseCode()) {
            connection = null;
        }
        if(null == connection) {
            return null;
        }
        // 下载
        return convert(connection.getInputStream());
    }

    /**
     * 连接
     *
     * @param url 地址
     * @param timeout 连接超时时间
     * @return 成功返回连接对象，失败返回null
     */
    private static HttpURLConnection connect(String url, int method, int timeout) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) (new URL(url)).openConnection();
            connection.setUseCaches(false);
            if (timeout > 0) {
                connection.setConnectTimeout(timeout);
            }
            connection.setDoInput(true);
            if (METHOD_POST == method) {
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + HTTP_BOUNDARY_STRING);
                connection.connect();
            }
        }
        catch(IOException e) {
            throw e;
        }
        return connection;
    }

    /**
     * 上传数据
     *
     * @param stream 输出流
     * @param parameters POST参数集
     */
    private static void upload(OutputStream stream, ITable<String, Object> parameters) throws IOException {
        DataOutputStream outStream = null;
        try {
            outStream = new DataOutputStream(stream);
            for (ILink<String, Object> link : parameters) {
                if(null == link.destination()) {
                    continue;
                }
                if(link.destination() instanceof File) {
                    continue;
                }
                outStream.writeBytes("--" + HTTP_BOUNDARY_STRING + "\r\n");
                outStream.writeBytes("Content-Disposition: form-data; name=\"" + link.origin() + "\"\r\n\r\n");
                outStream.writeBytes(URLEncoder.encode(link.destination().toString(), "UTF-8"));
                outStream.writeBytes("\r\n");
            }
            for (ILink<String, Object> link : parameters) {
                if(null == link.destination()) {
                    continue;
                }
                if(!(link.destination() instanceof File)) {
                    continue;
                }
                File file = (File) link.destination();
                outStream.writeBytes("--" + HTTP_BOUNDARY_STRING + "\r\n");
                outStream.writeBytes("Content-Disposition: form-data; name=\"" + link.origin() + "\"; filename=\"" + URLEncoder.encode(file.getName(), "UTF-8") + "\"\r\n");
                outStream.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
                //
                FileInputStream fileStream = new FileInputStream(file);
                byte[] fileBuffer = new byte[SIZE_UPLOAD];
                int fileIndex = 0;
                while ((fileIndex = fileStream.read(fileBuffer)) != -1) {
                    outStream.write(fileBuffer, 0, fileIndex);
                }
                fileStream.close();
                outStream.writeBytes("\r\n");
            }
            outStream.writeBytes("--" + HTTP_BOUNDARY_STRING + "--\r\n\r\n");
        }
        catch (IOException ex) {
            throw ex;
        }
        finally {
            if(null != outStream) {
                outStream.close();
            }
        }
    }

    /**
     * 输入流转字符串
     *
     * @param stream 输入流
     * @return 字符串
     */
    public static String convert(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }
        catch (IOException ex) {
            throw ex;
        }
        finally {
            if(null != reader) {
                reader.close();
            }
        }
        return builder.toString();
    }
}
