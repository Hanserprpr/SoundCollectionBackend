package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.PrivacyMapper;
import com.iqiongzhi.SCB.mapper.UserMapper;
import com.iqiongzhi.SCB.utils.EmailSender;
import com.iqiongzhi.SCB.utils.BcryptUtils;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;

@Service
public class SignupService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    private PrivacyMapper privacyMapper;

    private final Map<String, Map<String, String>> ticketDataMap = new ConcurrentHashMap<>();
    @Autowired
    private VerificationService verificationService;
    /**
     * 用于邮箱注册
     * @param email 邮箱地址
     * @param password 密码
     * @return ticket
     */
    public ResponseEntity<Result> signup(String email, String password) {
        // 检查邮箱是否已经注册
        Integer count = userMapper.findUserByEmail(email);
        if (count != null && count > 0) {
            return ResponseUtil.build(Result.error(409, "邮箱已经注册"));
        }
        // 注册
        String[] verify = verificationService.generateVerification();
        String code = verify[0];
        String ticket = verify[1];
        String passwd = BcryptUtils.encrypt(password);
        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("password", passwd);
        ticketDataMap.put(ticket, data);

        try{
        // 发送邮件
        EmailSender.sendEmail(email, "声音博物馆注册验证码", "您的验证码是：" + code);}
        catch(Exception e){
            return ResponseUtil.build(Result.error(554, "邮件发送失败"));
        }
        return ResponseUtil.build(Result.success(ticket, "验证码已发送"));
    }

    /**
     * 验证邮箱注册
     * @param ticket 注册时返回的 ticket
     * @param code 验证码
     * @return 验证结果
     */
    public ResponseEntity<Result> verify(String ticket, String code) {
        if (verificationService.verifyCode(ticket, code)) {
            Map<String, String> data = ticketDataMap.get(ticket);
            String email = data.get("email");
            String password = data.get("password");
            userMapper.addEmailUser(email, password);
            int id = userMapper.getUserId(email, "email");
            privacyMapper.insertPrivacy(id);
            ticketDataMap.remove(ticket);
            return ResponseUtil.build(Result.ok());
        } else {
            return ResponseUtil.build(Result.error(401, "验证码错误"));
        }
    }
}
