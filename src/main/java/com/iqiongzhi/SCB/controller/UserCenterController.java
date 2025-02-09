package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.po.User;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.PrivacyService;
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
    @Autowired
    PrivacyService privacyService;
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

    /**
     * 获取用户的收藏
     * @return 用户收藏列表
     */
    @Auth
    @GetMapping("/myCollections/{page}")
    public ResponseEntity<Result> getMyCollections(@PathVariable int page) {
        String userId = (String) request.getAttribute("userId");
        return userCenterService.getMyCollections(userId,page,15);//默认size15
    }

    /**
     * 获取用户的粉丝
     * @return 用户粉丝列表
     */
    @Auth
    @GetMapping("/myFans/{page}")
    public ResponseEntity<Result> getMyFans(@PathVariable int page) {
        String userId = (String) request.getAttribute("userId");
        return userCenterService.getMyFans(userId,page,15);//默认size15
    }

    /**
     * 获取用户的关注
     * @return 用户关注对象列表
     */
    @Auth
    @GetMapping("/myFollows/{page}")
    public ResponseEntity<Result> getMyFollows(@PathVariable int page) {
        String userId = (String) request.getAttribute("userId");
        return userCenterService.getMyFollows(userId,page,15);//默认size15
    }
    /**
     * 隐藏收藏
     * @return 隐藏收藏结果
     */
    @Auth
    @PostMapping("/privacy_collections/hide")
    public ResponseEntity<Result> hideCollection() {
        String userId = (String) request.getAttribute("userId");
        return privacyService.hideCollections(userId);
    }
    /**
     * 隐藏关注
     * @return 隐藏关注结果
     */
    @Auth
    @PostMapping("/privacy_follows/hide")
    public ResponseEntity<Result> hideFollows() {
        String userId = (String) request.getAttribute("userId");
        return privacyService.hideFollows(userId);
    }

    /**
     * 隐藏关注我的粉丝
     * @return 隐藏粉丝结果
     */
    @Auth
    @PostMapping("/privacy_fans/hide")
    public ResponseEntity<Result> hideFans() {
        String userId = (String) request.getAttribute("userId");
        return privacyService.hideFans(userId);
    }
    /**
     * 显示收藏
     * @return 显示收藏结果
     */
    @Auth
    @PostMapping("/privacy_collections/display")
    public ResponseEntity<Result> displayCollection() {
        String userId = (String) request.getAttribute("userId");
        return privacyService.displayCollections(userId);
    }
    /**
     * 显示关注
     * @return 显示关注结果
     */
    @Auth
    @PostMapping("/privacy_follows/display")
    public ResponseEntity<Result> displayFollows() {
        String userId = (String) request.getAttribute("userId");
        return privacyService.displayFollows(userId);
    }

    /**
     * 显示关注我的粉丝
     * @return 显示粉丝结果
     */
    @Auth
    @PostMapping("/privacy_fans/display")
    public ResponseEntity<Result> displayFans() {
        String userId = (String) request.getAttribute("userId");
        return privacyService.displayFans(userId);
    }





}
