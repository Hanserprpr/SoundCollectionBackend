package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.data.repository.SoundRepository;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.RecommendService;
import com.iqiongzhi.SCB.service.SoundService;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Service
@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    /**
     * 热门声音（分页）
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/hot")
    public ResponseEntity<Result> getHotSounds(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return recommendService.getHotSound(page, size);
    }

    // 最新声音（分页）
    @GetMapping("/latest")
    public ResponseEntity<Result> getLatestSounds(@RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return recommendService.getLatestSound(page, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Result> searchSounds(@RequestParam String keyword,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return recommendService.searchSound(keyword, page, size);
    }

}
