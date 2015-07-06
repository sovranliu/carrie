package com.dianping.midasx.base.io.disk;

import com.dianping.midasx.base.model.core.IHandle;
import com.dianping.midasx.base.io.core.IOWritable;

import java.io.*;

/**
 * 磁盘可写式句柄
 */
public class DiskWritableHandle implements IHandle, IOWritable {
    /**
     * 文件对象
     */
    protected File file = null;
    /**
     * 输出流
     */
    protected OutputStream outputStream = null;
    /**
     * 缓冲输出流
     */
    protected BufferedOutputStream bufferedOutputStream = null;


    /**
     * 创建
     *
     * @return 执行是否成功
     */
    public boolean create(File file) {
        if(null == file || !file.exists()) {
            throw new IllegalArgumentException();
        }
        try {
            outputStream = new FileOutputStream(file, false);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
        }
        catch (FileNotFoundException ex) {
            return false;
        }
        catch (Exception ex) {
            return false;
        }
        this.file = file;
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
            outputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
        }
        catch (FileNotFoundException ex) {
            return false;
        }
        return true;
    }

    /**
     * 关闭
     */
    @Override
    public void close() {
        try {
            if(null != bufferedOutputStream) {
                bufferedOutputStream.close();
                bufferedOutputStream = null;
            }
            if(null != outputStream) {
                outputStream.close();
                outputStream = null;
            }
        }
        catch (IOException ex) { }
    }

    /**
     * 写入文件
     *
     * @param data 数据
     */
    @Override
    public void write(byte[] data) throws IOException {
        bufferedOutputStream.write(data);
    }
}
