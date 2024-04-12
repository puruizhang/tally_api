package com.bestzpr.tally.config.exception;

/**
 * @className NotFoundException
 * @Desc 数据不存在异常
 * @Author 张埔枘
 * @Date 2023/12/23 17:56
 * @Version 1.0
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
