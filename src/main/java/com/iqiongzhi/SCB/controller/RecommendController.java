package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.RecommendService;
import com.iqiongzhi.SCB.service.SoundService;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    /**
     * 热门声音（分页）
     * @param page 页码
     * @param size 每页大小
     * @return ResponseEntity<Result>
     */
    @GetMapping("/hot")
    public ResponseEntity<Result> getHotSounds(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return recommendService.getHotSound(page, size);
    }

    /**
     * 最新声音（分页）
     * @param page 页码
     * @param size 每页大小
     * @return ResponseEntity<Result>
     */
    @GetMapping("/latest")
    public ResponseEntity<Result> getLatestSounds(@RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return recommendService.getLatestSound(page, size);
    }

    /**
     * 获取每周热门声音
     * @return ResponseEntity<Result>
     */
    @GetMapping("/weeklyHot")
    public ResponseEntity<Result> getWeeklyHotSounds() {
        return recommendService.getWeeklyHotSound();
    }

    /**
     * 手动更新每周热门声音
     * @return ResponseEntity<Result>
     */
    @GetMapping("/updateWeeklyHot")
    public ResponseEntity<Result> updateWeeklyHotSounds() {
        recommendService.updateHotnessManual();
        return ResponseUtil.build(Result.ok());
    }

}
