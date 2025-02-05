package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.RecommendMapper;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RecommendService {
    @Autowired
    private RecommendMapper recommendMapper;

    public ResponseEntity<Result> getLatestSound() {
        return ResponseUtil.build(Result.success(recommendMapper.getLatestSound(), "获取成功"));
    }
}
