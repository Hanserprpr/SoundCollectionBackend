package com.iqiongzhi.SCB.data.repository;

import com.iqiongzhi.SCB.data.po.Sound;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SoundRepository extends ElasticsearchRepository<Sound, Integer> {

    // 按热度排序（热门推荐）
    List<Sound> findByOrderByHotScoreDesc();

    // 按时间排序（最新声音）
    List<Sound> findByOrderByCreatedAtDesc();

    // 关键词搜索 + 热门排序
    List<Sound> findByTitleContainingOrDescriptionContainingOrLocationContainingOrderByHotScoreDesc(String title, String description, String location);

    @Query("{\"match_all\": {}}")
    List<Sound> findAllSounds();




}
