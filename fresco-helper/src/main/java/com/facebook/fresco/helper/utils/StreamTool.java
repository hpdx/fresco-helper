package com.facebook.fresco.helper.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 功能描述：数据流处理工具类
 *
 * Created by android_ls on 16/9/10.
 */
public final class StreamTool {

    /**
     * 拷贝图片文件
     * @param oldPath 原图片所在路径
     * @param newPath 新图片所在路径
     * @throws IOException
     */
    public static void copy(String oldPath, String newPath) throws IOException {
        FileInputStream inputStream = new FileInputStream(oldPath);
        FileOutputStream fileOutputStream = new FileOutputStream(newPath);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        inputStream.close();
    }

    /**
     * 将byte[]写入指定的文件
     * @param filePath 指定文件的路径
     * @param data byte[]
     * @throws IOException
     */
    public static void write(String filePath, byte[] data) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        fileOutputStream.write(data);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    /**
     * 根据文件路径获取byte[]
     * @param path 文件路径
     * @return
     * @throws IOException
     */
    public static byte[] read(String path) throws IOException {
        return read(new FileInputStream(path));
    }
    
    /**
     * 从输入流读取数据
     * 
     * @param inStream
     * @return  byte[]
     * @throws IOException
     */
    public static byte[] read(InputStream inStream) throws IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.flush();
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    /**
     * 将Bitmap对象转换成byte[]
     * @param bitmap Bitmap
     * @return byte[]
     * @throws IOException
     */
    public static byte[] read(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        baos.flush();
        baos.close();
        return baos.toByteArray();
    }

}
