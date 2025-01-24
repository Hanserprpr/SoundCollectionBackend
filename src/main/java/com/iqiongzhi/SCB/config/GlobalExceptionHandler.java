package com.iqiongzhi.SCB.config;

import com.iqiongzhi.SCB.utils.ResponseUtil;
import com.iqiongzhi.SCB.data.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 捕获 Redis 连接失败异常
     */
    @ExceptionHandler(org.springframework.data.redis.RedisConnectionFailureException.class)
    public ResponseEntity<Result> handleRedisException(Exception ex) {
        log.error("Redis 连接异常: {}", ex.getMessage(), ex);
        return ResponseUtil.build(Result.error(500, "Redis 服务不可用，请稍后再试"));
    }

    // 捕获 NullPointerException
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Result> handleNullPointerException(NullPointerException ex) {
        log.error("空指针异常: {}", ex.getMessage(), ex);
        return ResponseUtil.build(Result.error(500, "系统内部错误，请稍后再试"));
    }

    // 捕获 IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("参数非法: {}", ex.getMessage(), ex);
        return ResponseUtil.build(Result.error(400, "非法参数: " + ex.getMessage()));
    }

    /**
     * 捕获参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("参数类型不匹配: 参数名={}, 期望类型={}, 异常信息={}",
                ex.getName(), ex.getRequiredType(), ex.getMessage(), ex);
        return ResponseUtil.build(Result.error(400, "参数类型不匹配: " + ex.getName()));
    }


    /**
     * 捕获所有其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleGenericException(Exception ex) {
        log.error("未知异常捕获: {}", ex.getMessage(), ex);
        return ResponseUtil.build(Result.error(500, "服务器内部错误，请稍后再试"));
    }
}
