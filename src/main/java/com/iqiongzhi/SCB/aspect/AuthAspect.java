package com.iqiongzhi.SCB.aspect;

import com.auth0.jwt.JWT;
import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.utils.JWTUtil;
import com.iqiongzhi.SCB.utils.ResponseUtil;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class AuthAspect {
    @Autowired
    private JWTUtil jwtUtil;

    @Around("@annotation(com.iqiongzhi.SCB.annotation.Auth)") // 拦截带有 @Auth 注解的方法
    public Object verifyToken(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取 HTTP 请求中的 Token
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            return ResponseUtil.build(Result.error(401, "token未提供"));
        }

        String token;
        if (authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // 去掉前 7 个字符
        } else {
            return ResponseUtil.build(Result.error(401, "token格式错误"));
        }

        // 校验 Token 的有效性
        try {
            String userId = jwtUtil.getUserId(token);
            request.setAttribute("userId", userId); // 将 userId 添加到请求上下文
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(401, String.valueOf(e.getMessage())));
        }

        // 继续执行目标方法
        return joinPoint.proceed();
    }
}
