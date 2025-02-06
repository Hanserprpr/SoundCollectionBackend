package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.UserService;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userservice;

    @Auth
    @GetMapping("/{id}")
    public ResponseEntity<Result> getUser(@PathVariable String id) {
        String userId = (String) request.getAttribute("userId");
        return userservice.getUser(id, userId);
    }

}
