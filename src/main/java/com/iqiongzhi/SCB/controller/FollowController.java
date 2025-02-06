package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/follows")
public class FollowController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FollowService followService;

    /**
     * 关注
     * @param following 关注对象id
     * @return 关注结果
     */
    @Auth
    @PostMapping("/follow/{following}")
    public ResponseEntity<Result> collect(@PathVariable String following) {
        String userId = (String) request.getAttribute("userId");
        return followService.follow(userId, following);
    }


    /**
     * 取消关注
     * @param following 关注对象
     * @return 取消结果
     */
    @Auth
    @DeleteMapping("/disfollow/{following}")
    public ResponseEntity<Result> delete(@PathVariable String following) {
        String userId = (String) request.getAttribute("userId");
        return followService.disfollow(userId, following);
    }
}
