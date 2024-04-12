package com.bestzpr.tally.config.exception;

/**
 * @className UnfinishedGameException
 * @Desc 游戏未结束异常
 * @Author 张埔枘
 * @Date 2023/12/23 17:57
 * @Version 1.0
 */
public class UnfinishedGameException extends RuntimeException {
    public UnfinishedGameException(String message) {
        super(message);
    }
}
