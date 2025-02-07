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

    public ResponseEntity<Result> searchSound(String keyword, int page, int size) {
        // 将页码调整为从 0 开始
        page -= 1;

        // 获取搜索结果列表
        List<Sound> sounds = soundRepository.findByTitleContainingOrDescriptionContainingOrLocationContainingOrderByHotScoreDesc(keyword, keyword, keyword);

        // 计算分页的起始和结束索引
        int fromIndex = page * size;
        if (fromIndex >= sounds.size()) {
            // 如果起始索引超出范围，返回空列表
            return ResponseUtil.build(Result.success(Collections.emptyList(), "获取成功"));
        }
        int toIndex = Math.min((page + 1) * size, sounds.size());

        return ResponseUtil.build(Result.success(sounds.subList(fromIndex, toIndex), "获取成功"));
    }
}
