package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.po.User;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.UserCenterService;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@CrossOrigin
@RestController
@RequestMapping("/self")
public class UserCenterController {
    @Autowired
    UserCenterService userCenterService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 获取用户信息
     * @return 用户信息
     */
    @Auth
    @GetMapping("/profile")
    public ResponseEntity<Result> getMe() {
        String userId = (String) request.getAttribute("userId");
        return userCenterService.getMe(userId);
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新结果
     */
    @Auth
    @PutMapping("/update")
    public ResponseEntity<Result> update(@RequestBody User user) {
        String userId = (String) request.getAttribute("userId");
        return userCenterService.update(userId, user);
    }

    /**
     * 用户登出
     * @return 登出结果
     */
    @Auth
    @PostMapping("/logout")
    public ResponseEntity<Result> logout() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");
        String token;
        if (authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // 去掉前 7 个字符
        } else {
            return ResponseUtil.build(Result.error(401, "token格式错误"));
        }
        return userCenterService.logout(token);
    }

    /**
     * 获取用户上传的声音
     * @return 用户上传的声音
     */
    @Auth
    @GetMapping("/myVoices")
    public ResponseEntity<Result> getMyVoices() {
        String userId = (String) request.getAttribute("userId");
        return userCenterService.getMyVoices(userId);
    }

}
