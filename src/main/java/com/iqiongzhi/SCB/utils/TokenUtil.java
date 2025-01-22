package com.iqiongzhi.SCB.utils;

import jakarta.servlet.http.HttpServletRequest;

public class TokenUtil {

    public static String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        // 校验是否提供 Authorization 头
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            throw new IllegalArgumentException("token未提供");
        }

        // 校验格式并提取 token
        if (authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // 去掉 "Bearer " 前缀
        } else {
            throw new IllegalArgumentException("token格式错误");
        }
    }
}
