package com.iqiongzhi.SCB.utils;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptUtils {

    // 默认工作因子（计算强度），值越大哈希越慢，安全性越高
    private static final int WORK_FACTOR = 12;

    /**
     * 对密码进行哈希加密
     *
     * @param plainPassword 明文密码
     * @return 哈希后的密码
     */
    public static String encrypt(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(WORK_FACTOR));
    }

    /**
     * 验证密码是否匹配
     *
     * @param plainPassword  明文密码
     * @param hashedPassword 哈希后的密码
     * @return 如果匹配返回 true，否则返回 false
     */
    public static boolean verifyPasswd(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}