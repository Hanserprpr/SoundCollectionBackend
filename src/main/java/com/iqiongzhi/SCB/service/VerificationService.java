package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.cache.IGlobalCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VerificationService {
    @Autowired
    private IGlobalCache redis;

    /**
     * 生成验证码和 ticket
     * @return 返回包含验证码和 ticket 的数组
     */
    public String[] generateVerification() {
        // 生成验证码（6 位数字）
        String code = String.format("%06d", (int) (Math.random() * 1000000));

        // 生成唯一的 ticket
        String ticket = UUID.randomUUID().toString();

        // 存储到 Redis 中，设置过期时间（例如 5 分钟）
        redis.set("VERIFICATION_CODE:" + ticket, code, 5 * 60);

        return new String[]{code, ticket};
    }

    /**
     * 校验验证码
     * @param ticket 唯一 ticket
     * @param code 用户输入的验证码
     * @return 校验结果
     */
    public boolean verifyCode(String ticket, String code) {
        // 获取 Redis 中存储的验证码
        String key = "VERIFICATION_CODE:" + ticket;
        Object storedCode = redis.get(key);

        if (storedCode != null && storedCode.equals(code)) {
            redis.del(key);
            return true;
        }
        return false;
    }
}
