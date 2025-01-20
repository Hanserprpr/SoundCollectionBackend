package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.LoginService;
import com.iqiongzhi.SCB.data.dto.WeChatUserDTO;
import com.iqiongzhi.SCB.utils.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    LoginService loginService;
    @RequestMapping(value = "/SDUlogin", method = {RequestMethod.POST})
    public ResponseEntity<Result> SDUlogin(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map) {
        String stuId = null;
        String password = null;
        try {
            stuId = map.get("stuId").toString();
            password = map.get("password").toString();
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400,"Bad Requests"));
        }
        return loginService.SDUIdentify(stuId,password);
    }

    @RequestMapping(value = "/WXLogin", method = {RequestMethod.POST})
    public ResponseEntity<Result> WXLogin(HttpServletRequest request, HttpServletResponse response,@RequestBody WeChatUserDTO dto) {
        // System.out.println(dto);
        return loginService.appWxLogin(dto.getCode());

    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public ResponseEntity<Result> login(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map) {
        String identifier = null;
        String password = null;
        try {
            identifier = map.get("identifier").toString();
            password = map.get("password").toString();
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400,"Bad Requests"));
        }
        return loginService.simpleLogin(identifier,password);

    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Result> refresh(@RequestHeader String refreshToken){
        return loginService.refresh(refreshToken);
    }
}