package com.bestzpr.tally.config.exception;

/**
 * @className WeChatServiceException
 * @Desc
 * @Author 张埔枘
 * @Date 2023/12/23 18:14
 * @Version 1.0
 */
public class WeChatServiceException extends RuntimeException {
    public WeChatServiceException(String message) {
        super(message);
    }
}