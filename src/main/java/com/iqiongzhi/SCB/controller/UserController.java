package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.CollectionService;
import com.iqiongzhi.SCB.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userservice;
    @Autowired
    private CollectionService collectionService;

    /**
     * 显示他人主页
     * @param id 主页用户id
     * @return 显示结果
     */
    @GetMapping("/{id}")
    public ResponseEntity<Result> getUser(@PathVariable String id) {
        String userId = (String) request.getAttribute("userId");
        return userservice.getUser(id, userId);
    }

    /**
     * 获取他人的收藏夹
     * @param id 主页用户id
     * @param page 页码
     * @param size 每页数量
     * @return 用户收藏夹
     */
    @Auth
    @GetMapping("/{id}/get_collection")
    public ResponseEntity<Result> getMyCollections(@PathVariable String id,@RequestParam int page,@RequestParam int size) {
        return collectionService.getCollection(id,page,size);
    }

    /**
     * 获取他人的收藏列表
     * @param id 主页用户id
     * @param page 页码
     * @param collection_id 收藏夹id
     * @param size 每页数目
     * @return 用户收藏列表
     */
    @Auth
    @GetMapping("/{id}/get_collectionSounds")
    public ResponseEntity<Result> getMyCollections(@PathVariable String id, @RequestParam int page, @RequestParam int collection_id,@RequestParam int size) {
        return collectionService.getCollectionSounds(id,collection_id,page,size);
    }

    /**
     * 获取用户的关注
     * @param id 主页用户id
     * @param page 页码
     * @param size 每页数量
     * @return 用户关注对象列表
     */
    @Auth
    @GetMapping("/{id}/follows")
    public ResponseEntity<Result> getFollows(@PathVariable String id,@RequestParam int page,@RequestParam int size) {
        return userservice.getFollows(id, page,size);
    }

    /**
     * 获取用户的粉丝
     * @param id 主页用户id
     * @param page 页码
     * @param size 每页数量
     * @return 用户关注粉丝列表
     */
    @Auth
    @GetMapping("/{id}/fans")
    public ResponseEntity<Result> getFans(@PathVariable String id,@RequestParam int page,@RequestParam int size) {
        return userservice.getFans(id, page,size);
    }

    /**
     * 获取是否已经关注该用户
     * @param id 主页用户id
     * @return 用户关注对象列表
     */
    @Auth
    @GetMapping("/{id}/is_following")
    public ResponseEntity<Result> getFollowStatus(@PathVariable String id) {
        String userId = (String) request.getAttribute("userId");
        return userservice.isFollowing(userId,id);
    }


}
