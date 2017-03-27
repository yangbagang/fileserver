package com.ybg.common.fileserver.util.file.path;

/**
 * 类       名:     不合法的路径格式异常
 */
public class IllegalPathFormatException extends RuntimeException {

    /**
     * 构造方法
     * @param message 信息
     */
    public IllegalPathFormatException(String message) {
        super(message);
    }
}
