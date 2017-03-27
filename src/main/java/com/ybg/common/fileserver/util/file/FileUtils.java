package com.ybg.common.fileserver.util.file;

import com.luciad.imageio.webp.WebPWriteParam;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 类       名:     文件工具
 */
public class FileUtils {

    /**
     * 结束标志
     */
    private static final int EOF = -1;

    /**
     * 写
     *
     * @param os 输出
     * @param is 输入
     * @throws IOException 输入输出异常
     */
    public static void write(OutputStream os, InputStream is) throws IOException {
        int len;
        while ((len = is.read()) != EOF) {
            os.write(len);
        }
        close(os, is);
    }

    /**
     * 写
     *
     * @param os 输出
     * @param is 输入
     * @throws IOException 输入输出异常
     */
    public static void writeCloseInput(OutputStream os, InputStream is) throws IOException {
        int len;
        while ((len = is.read()) != EOF) {
            os.write(len);
        }
        close(is);
    }

    /**
     * 写
     *
     * @param os 输出
     * @throws IOException 输入输出异常
     */
    public static void write(OutputStream os, String str) throws IOException {
        write(os, str.getBytes());
    }

    /**
     * 写
     *
     * @param os 输出
     * @throws IOException 输入输出异常
     */
    public static void write(OutputStream os, byte[] buffer) throws IOException {
        os.write(buffer);
        close(os);
    }

    /**
     * 关闭 （防止内存异常）
     *
     * @param os 输出
     * @param is 输入
     */
    public static void close(OutputStream os, InputStream is) {
        close(os);
        close(is);
    }

    /**
     * 关闭 （防止内存异常）
     *
     * @param is 输入
     */
    public static void close(InputStream is) {
        try {
            is.close();
        } catch (Exception e) {
            //Ignore
        }
    }

    /**
     * 关闭 （防止内存异常）
     *
     * @param os 输出
     */
    public static void close(OutputStream os) {
        try {
            os.close();
        } catch (Exception e) {
            //Ignore
        }
    }

    /**
     * 预览格式扩展
     */
    public static final String[] PREVIEW_EXTENSIONS = {
            "jpg", "gif", "png", "jpeg", "bmp", "pdf", "webp"
    };

    /**
     * 是否，预览格式扩展
     *
     * @param str 字符串
     * @return 是否
     */
    public static boolean isPreviewExtension(String str) {
        int length = str.length();
        for (String extension : PREVIEW_EXTENSIONS) {
            if (extension.equalsIgnoreCase(str.substring(
                    length - extension.length(), length))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 文件命名中，不能存在的特殊字符
     */
    public static final String[] SPECIAL_SYMBOLS = {
            "\\", "/", ":", "*", "?", "\"", "<", ">", "|"
    };

    /**
     * 是否包含，命名中不能存在的特殊字符
     * @param str 字符串
     * @return 是否
     */
    public static boolean ofContainsSymbol(String str) {
        for (String symbol : SPECIAL_SYMBOLS) {
            if (str.contains(symbol)) {
                return true;
            }
        }
        return false;
    }

    public static void saveToWebp(File inputFile, File outputFile) {
        try {
            // 输出准备
            File outDir = outputFile.getParentFile();
            if (!outDir.exists()) {
                if (!outDir.mkdirs()) {
                    throw new RuntimeException("文件夹创建失败！");
                }
            }
            // Obtain an image to encode from somewhere
            BufferedImage image = ImageIO.read(inputFile);

            // Obtain a WebP ImageWriter instance
            ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

            // Configure encoding parameters
            WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
            writeParam.setCompressionMode(WebPWriteParam.MODE_EXPLICIT);

            // Configure the output on the ImageWriter
            writer.setOutput(new FileImageOutputStream(outputFile));

            // Encode
            writer.write(null, new IIOImage(image, null, null), writeParam);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveToWebp(InputStream inputFile, File outputFile) {
        try {
            // 输出准备
            File outDir = outputFile.getParentFile();
            if (!outDir.exists()) {
                if (!outDir.mkdirs()) {
                    throw new RuntimeException("文件夹创建失败！");
                }
            }
            // Obtain an image to encode from somewhere
            BufferedImage image = ImageIO.read(inputFile);

            // Obtain a WebP ImageWriter instance
            ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

            // Configure encoding parameters
            WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
            writeParam.setCompressionMode(WebPWriteParam.MODE_EXPLICIT);

            // Configure the output on the ImageWriter
            writer.setOutput(new FileImageOutputStream(outputFile));

            // Encode
            writer.write(null, new IIOImage(image, null, null), writeParam);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
