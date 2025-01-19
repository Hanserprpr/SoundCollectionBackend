package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.LoginService;
import com.iqiongzhi.SCB.data.po.WeChatUserDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    LoginService loginService;
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public Result login(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map) {
        String stuId = null;
        String password = null;
        try {
            stuId = map.get("stuId").toString();
            password = map.get("password").toString();
        } catch (Exception e) {
            return Result.error(400,"Bad Requests");
        }
        Result loginResult = loginService.SDUIdentify(stuId,password);
        if (loginResult.getCode() == 200) {
            return loginResult;
        } else {
            return Result.error(401,"密码错误");
        }
    }

    @RequestMapping(value = "/WXLogin", method = {RequestMethod.POST})
    public Result WXLogin(HttpServletRequest request, HttpServletResponse response,@RequestBody WeChatUserDTO dto) {
        System.out.println(dto);
        Result loginResult = loginService.appWxLogin(dto.getCode());
        if (loginResult.getCode() == 200) {
            return Result.success(loginResult.getData(),"登录成功");
        } else {
            return new Result(loginResult.getCode(),null,"登录失败");
        }
    }

    @PostMapping("/refreshToken")
    public Result refresh(@RequestHeader String refreshToken){
        Result result = loginService.refresh(refreshToken);
        return result;
    }


}
