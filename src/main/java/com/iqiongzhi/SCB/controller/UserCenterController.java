package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.po.User;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.CollectionService;
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
    @Autowired
    CollectionService collectionService;
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
     * 获取自己·的收藏夹
     * @param page 页码
     * @param size 每页数量
     * @return 用户收藏夹
     */
    @Auth
    @GetMapping("/my_collections")
    public ResponseEntity<Result> getMyCollections(@RequestParam int page,@RequestParam int size) {
        String userId = (String) request.getAttribute("userId");
        return collectionService.getCollection(userId,page,size);
    }

    /**
     * 获取自己的收藏列表
     * @param page 页码
     * @param collection_id 收藏夹id
     * @param size 每页数量
     * @return 用户收藏列表
     */
    @Auth
    @GetMapping("/my_collectionSounds")
    public ResponseEntity<Result> getMyCollectionSounds(@RequestParam int page,@RequestParam int collection_id,@RequestParam int size) {
        String userId = (String) request.getAttribute("userId");
        return collectionService.getCollectionSounds(userId,collection_id,page,size);
    }

    /**
     * 获取自己的粉丝
     * @param page 页码
     * @param size 每页数量
     * @return 用户自己列表
     */
    @Auth
    @GetMapping("/myFans")
    public ResponseEntity<Result> getMyFans(@RequestParam int page,@RequestParam int size) {
        String userId = (String) request.getAttribute("userId");
        return userCenterService.getMyFans(userId,page,size);
    }

    /**
     * 获取自己的关注
     * @param page 页码
     * @param size 每页数量
     * @return 自己关注对象列表
     */
    @Auth
    @GetMapping("/myFollows")
    public ResponseEntity<Result> getMyFollows(@RequestParam int page,@RequestParam int size) {
        String userId = (String) request.getAttribute("userId");
        return userCenterService.getMyFollows(userId,page,size);
    }
    /**
     * 隐藏收藏夹
     * @return 隐藏收藏结果
     */
    @Auth
    @PostMapping("/privacy_collections/hide")
    public ResponseEntity<Result> hideCollection() {
        String userId = (String) request.getAttribute("userId");
        return privacyService.hideCollections(userId);
    }
    /**
     * 隐藏关注列表
     * @return 隐藏关注结果
     */
    @Auth
    @PostMapping("/privacy_follows/hide")
    public ResponseEntity<Result> hideFollows() {
        String userId = (String) request.getAttribute("userId");
        return privacyService.hideFollows(userId);
    }

    /**
     * 隐藏粉丝列表
     * @return 隐藏粉丝结果
     */
    @Auth
    @PostMapping("/privacy_fans/hide")
    public ResponseEntity<Result> hideFans() {
        String userId = (String) request.getAttribute("userId");
        return privacyService.hideFans(userId);
    }
    /**
     * 取消隐藏收藏夹
     * @return 显示收藏结果
     */
    @Auth
    @PostMapping("/privacy_collections/display")
    public ResponseEntity<Result> displayCollection() {
        String userId = (String) request.getAttribute("userId");
        return privacyService.displayCollections(userId);
    }
    /**
     * 取消隐藏关注列表
     * @return 显示关注结果
     */
    @Auth
    @PostMapping("/privacy_follows/display")
    public ResponseEntity<Result> displayFollows() {
        String userId = (String) request.getAttribute("userId");
        return privacyService.displayFollows(userId);
    }

    /**
     * 取消隐藏粉丝列表
     * @return 显示粉丝结果
     */
    @Auth
    @PostMapping("/privacy_fans/display")
    public ResponseEntity<Result> displayFans() {
        String userId = (String) request.getAttribute("userId");
        return privacyService.displayFans(userId);
    }

    /**
     * 获取粉丝列表隐私状态
     * @return 隐私状态1/0
     */
    @Auth
    @GetMapping("/privacy_fans")
    public ResponseEntity<Result> getFansPri() {
        String userId = (String) request.getAttribute("userId");
        return privacyService.getFansPri(userId);
    }

    /**
     * 获取关注列表隐私状态
     * @return 隐私状态1/0
     */
    @Auth
    @GetMapping("/privacy_follows")
    public ResponseEntity<Result> getFollowsPri() {
        String userId = (String) request.getAttribute("userId");
        return privacyService.getFollowsPri(userId);
    }

    /**
     * 获取收藏隐私状态
     * @return 隐私状态1/0
     */
    @Auth
    @GetMapping("/privacy_collections")
    public ResponseEntity<Result> getCollectionsPri() {
        String userId = (String) request.getAttribute("userId");
        return privacyService.getCollectionsPri(userId);
    }


}
