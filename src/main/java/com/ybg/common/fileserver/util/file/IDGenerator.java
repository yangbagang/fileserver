package com.ybg.common.fileserver.util.file;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 类       名:     id生成器
 */
public class IDGenerator {

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    /**
     * UUID长度
     */
    public static final int LENGTH_UUID = 32;

    /**
     * 日期长度
     */
    public static final int LENGTH_DATE = LENGTH_UUID >> 2;

    /**
     * 分割符
     */
    public static final char FID_SEPARATOR = '_';

    /**
     * 随机UUID字符串
     *
     * @return
     */
    public static String randomUUIDString() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 随机文件ID
     *
     * @param folder           文件夹
     * @param originalFilename 原文件名
     * @return
     */
    public static String randomFIDString(String folder, String originalFilename) {
        return folder + File.separator + sdf.format(new Date()) + File.separator +
                randomUUIDString() + FID_SEPARATOR + originalFilename;
    }

    /**
     * 随机文件ID
     *
     * @param folder           文件夹
     * @return
     */
    public static String randomWebpFID(String folder) {
        return folder + File.separator + sdf.format(new Date()) + File.separator +
                randomUUIDString() + FID_SEPARATOR + System.currentTimeMillis() + ".webp";
    }
    
    /**
     * 随机文件ID
     *
     * @param folder           文件夹
     * @return
     */
    public static String randomString(String folder) {
        return folder + File.separator + sdf.format(new Date()) + File.separator;
    }
}
