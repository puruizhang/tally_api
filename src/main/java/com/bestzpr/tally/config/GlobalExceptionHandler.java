package com.bestzpr.tally.config;

import com.bestzpr.tally.config.exception.JoinRoomException;
import com.bestzpr.tally.config.exception.NotFoundException;
import com.bestzpr.tally.config.exception.UnfinishedGameException;
import com.bestzpr.tally.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @className GlobalExceptionHandler
 * @Desc 全局异常处理器
 * @Author 张埔枘
 * @Date 2023/12/23 17:41
 * @Version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFoundException(NotFoundException ex) {
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnfinishedGameException.class)
    public ResponseEntity<ApiResponse> handleUnfinishedGameException(UnfinishedGameException ex) {
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JoinRoomException.class)
    public ResponseEntity<ApiResponse> handleJoinRoomException(JoinRoomException ex) {
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        // 构建一个适当的响应实体
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
