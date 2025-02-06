package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LikeService likeService;

    /**
     * 点赞声音
     * @param soundId 声音id
     * @return 点赞结果
     */
    @Auth
    @PostMapping("/addlike/{soundId}")
    public ResponseEntity<Result> collect(@PathVariable String soundId) {
        String userId = (String) request.getAttribute("userId");
        return likeService.like(userId, soundId);
    }


    /**
     * 获取声音点赞数
     * @param soundId 声音id
     * @return 点赞数
     */
    @Auth
    @GetMapping("/likes/{soundId}")
    public ResponseEntity<Result> likes(@PathVariable String soundId) {
        return likeService.likesCnt(soundId);
    }

    /**
     * 取消点赞
     * @param soundId 声音id
     * @return 删除结果
     */
    @Auth
    @DeleteMapping("/dislike/{soundId}")
    public ResponseEntity<Result> delete(@PathVariable String soundId) {
        String userId = (String) request.getAttribute("userId");
        return likeService.dislike(userId, soundId);
    }
}
