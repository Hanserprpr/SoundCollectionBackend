package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.data.repository.SoundRepository;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.RecommendMapper;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RecommendService {
    @Autowired
    private SoundRepository soundRepository;

    public ResponseEntity<Result> getHotSound(int page, int size) {
        return ResponseUtil.build(Result.success(soundRepository.findByOrderByHotScoreDesc()
                .subList((page - 1) * size, (int) Math.min((long) page * size, soundRepository.count())), "获取成功"));
    }

    public ResponseEntity<Result> getLatestSound(int page, int size) {
        return ResponseUtil.build(Result.success(soundRepository.findByOrderByCreatedAtDesc()
                .subList((page - 1) * size, (int) Math.min((long) page * size, soundRepository.count())), "获取成功"));
    }


}
