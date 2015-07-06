package com.dianping.midasx.base.io.disk;

import com.dianping.midasx.base.model.core.IHandle;
import com.dianping.midasx.base.io.core.IOReadable;
import com.dianping.midasx.base.model.Result;

import java.io.*;

/**
 * 磁盘可读式句柄
 */
public class DiskReadableHandle implements IHandle, IOReadable {
    /**
     * 默认缓冲区大小
     */
    public final static int BUFFER_SIZE_DEFAULT = 1024;


    /**
     * 读缓冲尺寸
     */
    protected int readBufferSize = BUFFER_SIZE_DEFAULT;
    /**
     * 文件对象
     */
    protected File file = null;
    /**
     * 输入流
     */
    protected InputStream inputStream = null;
    /**
     * 缓冲输入流
     */
    protected BufferedInputStream bufferedInputStream = null;


    /**
     * 获取文件对象
     *
     * @return 文件对象
     */
    public File getFile() {
        return file;
    }

    /**
     * 设置文件对象
     *
     * @param file 文件对象
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * 打开
     *
     * @return 是否打开成功
     */
    @Override
    public boolean open() {
        if(null == file || !file.exists()) {
            throw new IllegalArgumentException();
        }
        try {
            inputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(inputStream);
        }
        catch (FileNotFoundException ex) {
            return false;
        }
        return true;
    }

    /**
     * 打开
     *
     * @return 是否打开成功
     */
    public boolean open(File file) {
        this.file = file;
        return open();
    }

    /**
     * 关闭
     */
    @Override
    public void close() {
        try {
            if(null != bufferedInputStream) {
                bufferedInputStream.close();
                bufferedInputStream = null;
            }
            if(null != inputStream) {
                inputStream.close();
                inputStream = null;
            }
        }
        catch (IOException ex) { }
    }

    /**
     * 读取文件
     *
     * @return 结果对象，布尔表示是否未到文件尾
     */
    @Override
    public Result<Boolean, byte[]> read() throws Exception {
        byte[] buffer = new byte[readBufferSize];
        int size = bufferedInputStream.read(buffer);
        if(-1 == size) {
            return new Result<Boolean, byte[]>(false, null);
        }
        if(readBufferSize == size) {
            return new Result<Boolean, byte[]>(true, buffer);
        }
        Result<Boolean, byte[]> result = new Result<Boolean, byte[]>();
        result.status = true;
        result.info = new byte[size];
        System.arraycopy(buffer, 0, result.info, 0, size);
        return result;
    }

    /**
     * 获取一次读取的数据大小
     *
     * @return 一次读取的数据大小
     */
    @Override
    public int getReadBufferSize() {
        return readBufferSize;
    }

    /**
     * 设置一次读取的数据大小
     *
     * @param readBufferSize 一次读取的数据大小
     */
    public void setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }
}
