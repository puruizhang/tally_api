package com.bestzpr.tally.util;

import org.springframework.http.ResponseEntity;

/**
 * @className ApiResponseUtil
 * @Desc
 * @Author 张埔枘
 * @Date 2023/12/23 17:48
 * @Version 1.0
 */
public class ApiResponseUtil {
    public static <T> ResponseEntity<ApiResponse> createApiResponse(boolean success, T data) {
        ApiResponse apiResponse = new ApiResponse(success, data);
        return ResponseEntity.ok(apiResponse);
    }
}
