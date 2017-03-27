package com.ybg.common.fileserver.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 类       名:     编码器
 */
public class Coder {

    /**
     * 字符编码
     */
    public static final String STR_ENCODING = "UTF-8";

    /**
     * 编码
     *
     * @param str 编码字符串
     * @return 编码后的字符串
     * @throws UnsupportedEncodingException 不支持的编码字符
     */
    public static String encode(String str) throws UnsupportedEncodingException {
        String result;
        try {
            result =  new String(Base64.encodeBase64(
                    URLEncoder.encode(str, STR_ENCODING).getBytes()), STR_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException("不支持的编码字符！");
        }
        return result;
    }

    /**
     * 解码
     *
     * @param str 解码字符串
     * @return 解码后的字符串
     * @throws UnsupportedEncodingException 不支持的编码字符
     */
    public static String decode(String str) throws UnsupportedEncodingException {
        String result;
        try {
            result = URLDecoder.decode(
                    new String(Base64.decodeBase64(str.getBytes()), STR_ENCODING), STR_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException("不支持的编码字符！");
        }
        return result;
    }
}
