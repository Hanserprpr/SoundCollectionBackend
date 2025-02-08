package com.iqiongzhi.SCB.data.repository;

import com.iqiongzhi.SCB.data.po.Sound;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@Repository
public interface SoundRepository extends ElasticsearchRepository<Sound, Integer> {

    // 按热度排序（热门推荐）
    List<Sound> findByOrderByHotScoreDesc();

    // 按时间排序（最新声音）
    List<Sound> findByOrderByCreatedAtDesc();

    // 关键词搜索 + 热门排序
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title\", \"description\", \"location\", \"category\", \"tags\"]}}")
    Page<Sound> searchByKeyword(String keyword, Pageable pageable);

    @NotNull Page<Sound> findAll(@NotNull Pageable pageable);


}
