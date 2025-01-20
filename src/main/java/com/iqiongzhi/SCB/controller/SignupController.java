package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.data.dto.VerifyRequestDTO;
import com.iqiongzhi.SCB.service.SignupService;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.data.dto.SignupRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/signup")
public class SignupController {

    private final SignupService signupService;

    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    /**
     * 邮箱注册
     * @param signupRequest 包含 email 和 password 的请求体
     * @return 注册结果
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<Result> signup(@RequestBody SignupRequestDTO signupRequest) {
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();
        return signupService.signup(email, password);
    }

    /**
     * 验证验证码
     * @param verifyRequest 包含 ticket 和 code 的请求体
     * @return 验证结果
     */
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public ResponseEntity<Result> verify(@RequestBody VerifyRequestDTO verifyRequest) {
        String ticket = verifyRequest.getTicket();
        String code = verifyRequest.getCode();
        return signupService.verify(ticket, code);
    }
}
