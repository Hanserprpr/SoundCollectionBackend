package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.data.repository.SoundRepository;
import com.iqiongzhi.SCB.mapper.SearchMapper;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private SoundRepository soundRepository;
    @Autowired
    private SearchMapper searchMapper;

    /**
     * 搜索声音
     * @param keyword 关键词
     * @param page 页码
     * @param size 每页大小
     * @return 搜索结果
     */
    public ResponseEntity<Result> searchSound(String userId, String keyword, int page, int size) {
        // 确保 page 不会小于 0
        page = Math.max(0, page - 1);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "hotScore"));
        Page<Sound> soundPage = soundRepository.searchByKeyword(keyword, pageable);
        List<Sound> data = soundPage.getContent();
        searchMapper.insertSearchLog(userId, keyword);
        if (data.isEmpty()) {
            return ResponseUtil.build(Result.error(404, "暂无数据"));
        }
        return ResponseUtil.build(Result.success(data, "获取成功"));
    }

    /**
     * 搜索历史
     * @param userId 用户id
     * @return 搜索历史
     */
    public ResponseEntity<Result> searchHistory(String userId) {
        return ResponseUtil.build(Result.success(searchMapper.getSearchHistory(userId), "获取成功"));
    }
}
