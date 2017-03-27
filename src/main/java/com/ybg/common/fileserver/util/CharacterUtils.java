package com.ybg.common.fileserver.util;

/**
 * 类       名:     字符工具类
 */
public class CharacterUtils {

    /**
     * 查找，字符在字符数组中的下标
     * @param chars 字符数组
     * @param searchChar 字符
     * @return 下标
     */
    public static int indexOf(char[] chars, char searchChar) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == searchChar) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 从开始下标，查找字符在字符数组中的下标
     * @param chars 字符数组
     * @param searchChar 字符
     * @return 下标
     */
    public static int indexOf(char[] chars, int beginIndex, char searchChar) {
        for (int i = beginIndex; i < chars.length; i++) {
            if (chars[i] == searchChar) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 在开始下标，从后往前查找字符在字符数组中的下标
     * @param chars 字符数组
     * @param searchChar 字符
     * @return 下标
     */
    public static int lastIndexOf(char[] chars, int beginIndex, char searchChar) {
        for (int i = chars.length; i >= beginIndex; i--) {
            if (chars[i] == searchChar) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 字符数组反转
     * @param chars 字符数组
     * @param beginIndex 开始下标
     * @param endIndex 结束下标
     */
    public static char[] reverse(char[] chars, int beginIndex, int endIndex) {
        while (beginIndex < endIndex) {
            char temp = chars[beginIndex];
            chars[beginIndex] = chars[endIndex];
            chars[endIndex] = temp;
            beginIndex++;
            endIndex--;
        }
        return chars;
    }
}
