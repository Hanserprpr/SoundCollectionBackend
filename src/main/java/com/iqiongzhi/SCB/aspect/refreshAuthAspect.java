package com.iqiongzhi.SCB.aspect;

import com.auth0.jwt.JWT;
import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.utils.JWTUtil;
import com.iqiongzhi.SCB.utils.ResponseUtil;

import com.iqiongzhi.SCB.utils.TokenUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
@Aspect
public class refreshAuthAspect {
    @Autowired
    private JWTUtil jwtUtil;

    @Around("@annotation(com.iqiongzhi.SCB.annotation.refreshAuth)") // 拦截带有 @refreshAuth 注解的方法
    public Object verifyToken(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = TokenUtil.extractToken(request);
        // 校验 Token 的有效性
        try {
            String userId = jwtUtil.getUserId(token, JWTUtil.REFRESH_SECRET_KEY);
            request.setAttribute("userId", userId); // 将 userId 添加到请求上下文
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(401, String.valueOf(e.getMessage())));
        }

        // 继续执行目标方法
        return joinPoint.proceed();
    }
}
