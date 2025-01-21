package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.po.User;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.UserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserCenterController {
    @Autowired
    UserCenterService userCenterService;
    @Autowired
    private HttpServletRequest request;

    @Auth
    @GetMapping("/profile")
    public ResponseEntity<Result> getMe() {
        String userId = (String) request.getAttribute("userId");
        return userCenterService.getMe(userId);
    }

    @Auth
    @PutMapping("/update")
    public ResponseEntity<Result> update(@RequestBody User user) {
        String userId = (String) request.getAttribute("userId");
        return userCenterService.update(userId, user);
    }
}
