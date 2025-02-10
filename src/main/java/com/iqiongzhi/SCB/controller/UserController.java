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

    /**
     * 显示他人主页
     * @return 显示结果
     */
    @GetMapping("/{id}")
    public ResponseEntity<Result> getUser(@PathVariable String id) {
        String userId = (String) request.getAttribute("userId");
        return userservice.getUser(id, userId);
    }

    /**
     * 获取用户的收藏
     * @return 用户收藏列表
     */
    @GetMapping("/{id}/collection/{page}")
    public ResponseEntity<Result> getCollection(@PathVariable String id,@PathVariable int page) {
        return userservice.getCollections(id, page,15);//默认size15
    }

    /**
     * 获取用户的粉丝
     * @return 用户粉丝列表
     */
    @GetMapping("/{id}/fans/{page}")
    public ResponseEntity<Result> getFans(@PathVariable String id,@PathVariable int page) {
        return userservice.getFans(id, page,15);//默认size15
    }

    /**
     * 获取用户的关注
     * @return 用户关注对象列表
     */
    @GetMapping("/{id}/follows/{page}")
    public ResponseEntity<Result> getFollows(@PathVariable String id,@PathVariable int page) {
        return userservice.getFollows(id, page,15);//默认size15
    }


}
