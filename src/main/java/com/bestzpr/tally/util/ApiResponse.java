package com.bestzpr.tally.util;

import lombok.Data;

/**
 * @className ApiResponse
 * @Desc
 * @Author 张埔枘
 * @Date 2023/12/11 21:12
 * @Version 1.0
 */
@Data
public class ApiResponse<T> {
    private int code;
    private boolean success = false;
    private String message = "成功";
    private T data;

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        if(code == 200){
            this.success = true;
        }
        this.message = message;
        this.data = data;
    }

    public ApiResponse(boolean success, T data) {
        if(success){
            this.code = 200;
        }
        this.success = success;
        this.data = data;
    }
}
