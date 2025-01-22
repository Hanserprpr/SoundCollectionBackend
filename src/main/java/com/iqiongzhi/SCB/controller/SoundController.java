package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.SoundService;
import jakarta.servlet.http.HttpServletRequest;
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

    /**
     * 上传声音数据
     * @param sound 音频信息
     * @return 上传结果
     */
    @Auth
    @PostMapping("/upload")
    public ResponseEntity<Result> upload(@RequestBody Sound sound) {
        String userId = (String) request.getAttribute("userId");
        return soundService.upload(userId, sound);
    }

    /**
     * 删除声音数据
     * @param soundId 音频id
     * @return 删除结果
     */
    @Auth
    @PostMapping("/del")
    public ResponseEntity<Result> delSound(@RequestParam String soundId) {
        String userId = (String) request.getAttribute("userId");
        return soundService.delSound(userId, soundId);
    }
}
