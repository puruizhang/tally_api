package com.bestzpr.tally.config.exception;

/**
 * @className JoinRoomException
 * @Desc 加入房间异常
 * @Author 张埔枘
 * @Date 2023/12/23 17:58
 * @Version 1.0
 */
public class JoinRoomException extends RuntimeException {
    public JoinRoomException(String message) {
        super(message);
    }
}