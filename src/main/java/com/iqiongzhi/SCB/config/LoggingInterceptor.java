package com.iqiongzhi.SCB.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        // 获取客户端 IP 地址
        String ipAddress = request.getRemoteAddr();
        // 获取请求方法（GET、POST 等）
        String method = request.getMethod();
        // 获取请求 URI
        String uri = request.getRequestURI();
        // 获取查询参数
        String queryString = request.getQueryString();

        // 记录日志
        logger.info("IP: {}, Method: {}, URI: {}, Query: {}", ipAddress, method, uri, queryString);
        return true;
    }
}