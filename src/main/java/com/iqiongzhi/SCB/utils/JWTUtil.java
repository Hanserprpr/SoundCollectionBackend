package com.iqiongzhi.SCB.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.refresh-secret-key}")
    private String refreshSecretKey;

    public static String SECRET_KEY;
    public static String REFRESH_SECRET_KEY;

    // 初始化静态变量
    @PostConstruct
    public void init() {
        SECRET_KEY = secretKey;
        REFRESH_SECRET_KEY = refreshSecretKey;
    }

    public static final int EXPIRE_TIME = 30 * 24 * 3600;//Token过期时间
    public static final int REFRESH_EXPIRE_TIME = 30 * 24 * 3600;//RefreshToken过期时间

    //生成token
    public static String getToken(String accountId, int expireTime, String key) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", accountId);
        //获取时间，设置token过期时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, expireTime);

        //JWT添加payload
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);

        //JWT过期时间 + signature
        return builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(key));
    }


    //返回token内容
    public static DecodedJWT getTokenInfo(String token, String key) throws UnsupportedEncodingException {
        return JWT.require(Algorithm.HMAC256(key)).build().verify(token);
    }

    // 获取用户 ID
    public static String getUserId(String token) {
        try {
            // 验证并解析 Token
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build()
                    .verify(token);

            // 提取用户 ID
            return decodedJWT.getClaim("user_id").asString();

        } catch (TokenExpiredException e) {
            // Token 过期
            throw new RuntimeException("Token 已过期，请重新登录");

        } catch (JWTVerificationException e) {
            // Token 验证失败（签名无效等）
            throw new RuntimeException("Token 无效，请检查后重试");

        } catch (Exception e) {
            // 其他异常
            throw new RuntimeException("解析 Token 时发生错误，请稍后重试");
        }
    }
}
