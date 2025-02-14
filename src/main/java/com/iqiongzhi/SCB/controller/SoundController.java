package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.LikeService;
import com.iqiongzhi.SCB.service.SoundService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/sound")
public class SoundController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SoundService soundService;
    @Autowired
    private LikeService likeService;

    /**
     * 上传声音数据
     * @param sound 音频信息
     * @return 上传结果
     */
    @Auth
    @PostMapping("/upload")
    public ResponseEntity<Result> upload(@Valid @RequestBody Sound sound) {
        String userId = (String) request.getAttribute("userId");
        return soundService.upload(userId, sound);
    }

    /**
     * 更新声音数据
     * @param sound 音频信息
     * @return 更新结果
     */
    @Auth
    @PutMapping("/update")
    public ResponseEntity<Result> update(@Valid @RequestBody Sound sound) {
        String userId = (String) request.getAttribute("userId");
        return soundService.update(userId, sound);
    }

    /**
     * 删除声音数据
     * @param soundId 音频id
     * @return 删除结果
     */
    @Auth
    @DeleteMapping("/del")
    public ResponseEntity<Result> delSound(@RequestParam String soundId) {
        String userId = (String) request.getAttribute("userId");
        return soundService.delSound(userId, soundId);
    }

    /**
     * 获取声音数据
     * @param soundId 音频id
     * @return 音频信息
     */
    @Auth
    @GetMapping("/getSound")
    public ResponseEntity<Result> getSound(@RequestParam String soundId) {
        soundService.increaseViews(Integer.parseInt(soundId));
        return soundService.getSound(soundId);
    }

    /**
     * 获取是否点赞某个声音
     * @param soundId 音频id
     * @return 音频点赞信息
     */
    @Auth
    @GetMapping("/liking")
    public ResponseEntity<Result> isLiking(@RequestParam String soundId) {
        String userId = (String) request.getAttribute("userId");
        return likeService.isLiking(userId,soundId);
    }

    /**
     * 获取声音点赞数
     * @param soundId 声音id
     * @return 点赞数
     */
    @Auth
    @GetMapping("/likes")
    public ResponseEntity<Result> likes(@RequestParam String soundId) {
        return likeService.likesCnt(soundId);
    }

    /**
     * 获取指定类型声音列表
     * @param category 类别
     * @param page 页码
     * @param size 每页数量
     * @return 音频列表
     */
    @GetMapping("/getSoundList")
    public ResponseEntity<Result> getSoundList(String category, int page, int size) {
        return soundService.getSoundList(category, page, size);
    }
}
