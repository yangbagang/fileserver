package com.ybg.common.fileserver.util.file.path;

/**
 * 类       名:     路径片段
 */
public class PathFragment {

    /**
     * 片段
     */
    private StringBuilder fragment = new StringBuilder();

    /**
     * 片段下一个下标
     */
    private int searchIndex = -1;

    /**
     * 值由字符数组，字符数组中的字符到指定字符之前，截取成片段
     *
     * @param chars      字符数组
     * @param searchChar 字符
     * @return 下标
     */
    public static PathFragment valueOfCharArray(char[] chars, char searchChar) {
        PathFragment fragment = new PathFragment();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == searchChar) {
                fragment.setSearchIndex(i);
                break;
            }
            fragment.fromChar(chars[i]);
        }
        return fragment;
    }

    /**
     * 值由字符数组，从开始下标，字符数组中的字符到指定字符之前，截取成片段
     *
     * @param chars      字符数组
     * @param searchChar 字符
     * @return 下标
     */
    public static PathFragment valueOfCharArray(char[] chars, int beginIndex, char searchChar) {
        PathFragment fragment = new PathFragment();
        for (int i = beginIndex; i < chars.length; i++) {
            if (chars[i] == searchChar) {
                fragment.setSearchIndex(i);
                break;
            }
            fragment.fromChar(chars[i]);
        }
        return fragment;
    }

    /**
     * 值由字符数组，从开始下标到最后一个字符，截取成片段
     *
     * @param chars      字符数组
     * @param beginIndex 开始下标
     * @return 下标
     */
    public static PathFragment valueOfCharArray(char[] chars, int beginIndex) {
        PathFragment fragment = new PathFragment();
        int length = chars.length;
        for (int i = beginIndex; i < chars.length; i++) {
            fragment.fromChar(chars[i]);
        }
        fragment.setSearchIndex(length);
        return fragment;
    }

    /**
     * 值由字符数组，从后往前找，字符数组中的字符到开始字符，截取成片段
     *
     * @param chars      字符数组
     * @param searchChar 字符
     * @return 下标
     */
    public static PathFragment lastValueOfCharArray(char[] chars, int beginIndex, char searchChar) {
        PathFragment fragment = new PathFragment();
        StringBuilder builder = new StringBuilder();
        for (int i = chars.length - 1; i >= beginIndex; i--) {
            if (chars[i] == searchChar) {
                fragment.setSearchIndex(i);
                break;
            }
            builder.append(chars[i]);
        }
        fragment.fromString(builder.reverse().toString());
        return fragment;
    }

    /**
     * 获取片段
     *
     * @return 片段
     */
    public String getFragment() {
        return fragment.toString();
    }

    /**
     * 来自字符
     *
     * @param c 字符
     */
    private void fromChar(char c) {
        this.fragment.append(c);
    }

    /**
     * 来自字符串
     *
     * @param s 字符
     */
    private void fromString(String s) {
        this.fragment.append(s);
    }

    /**
     * 获取下一个下标
     *
     * @return 下一个下标
     */
    public int getSearchIndex() {
        return searchIndex;
    }

    /**
     * 设置下一个下标
     *
     * @param searchIndex 搜索的下标
     */
    public void setSearchIndex(int searchIndex) {
        this.searchIndex = searchIndex;
    }
}
