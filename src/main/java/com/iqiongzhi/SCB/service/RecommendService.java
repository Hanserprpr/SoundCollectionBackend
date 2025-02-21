package com.iqiongzhi.SCB.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.iqiongzhi.SCB.cache.IGlobalCache;
import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.data.po.SoundHotness;
import com.iqiongzhi.SCB.data.repository.SoundRepository;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.SoundMapper;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import com.iqiongzhi.SCB.utils.SoundUtils;

@Service
public class RecommendService {

    @Autowired
    private SoundRepository soundRepository;
    @Autowired
    private IGlobalCache redisService;
    @Autowired
    private SoundMapper soundMapper;
    @Autowired
    private SoundUtils soundUtils;

    public ResponseEntity<Result> getHotSound(int page, int size) {
        return ResponseUtil.build(Result.success(soundRepository.findByOrderByHotScoreDesc()
                .subList((page - 1) * size, (int) Math.min((long) page * size, soundRepository.count())), "获取成功"));
    }

    public ResponseEntity<Result> getLatestSound(int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Sound> soundPage = soundRepository.findAll(pageable);

        List<Sound> sounds = soundPage.getContent();
        if (sounds.isEmpty()) {
            return ResponseUtil.build(Result.error(404, "暂无数据"));
        }
        return ResponseUtil.build(Result.success(sounds, "获取成功"));
    }

    public ResponseEntity<Result> getTagSound(String tag, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.DESC, "HotScore"));

        Page<Sound> soundPage = soundRepository.searchTags(tag, pageable);

        List<Sound> sounds = soundPage.getContent();
        if (sounds.isEmpty()) {
            return ResponseUtil.build(Result.error(404, "暂无数据"));
        }
        return ResponseUtil.build(Result.success(sounds, "获取成功"));
    }


    private List<Sound> getWeeklyHotSoundUtil() {
        Map<String, Integer> viewMap = redisService.hmgetAsIntegerMap("view");

        Map<String, Integer> likeMap = redisService.hmgetAsIntegerMap("like");

        Map<String, Integer> commentMap = redisService.hmgetAsIntegerMap("comment");

        // 获取所有声音数据
        List<Sound> sounds = soundMapper.getAllSounds();

        // 计算每个声音的热度
        List<SoundHotness> hotnessList = new ArrayList<>();
        for (Sound sound : sounds) {
            int views = viewMap.getOrDefault(sound.getId().toString(), 0);
            int likes = likeMap.getOrDefault(sound.getId().toString(), 0);
            int comments = commentMap.getOrDefault(sound.getId().toString(), 0);

            double hotness = views * 0.5 + likes * 0.2 + comments * 0.3;

            // 存入热度列表
            hotnessList.add(new SoundHotness(sound, hotness));
        }

        // 排序：按热度降序排序
        hotnessList.sort((a, b) -> Double.compare(b.getHotness(), a.getHotness()));

        // 取前 9 名
        List<Integer> topSoundIds = new ArrayList<>();
        redisService.del("weekly_hot");  // 清空 Redis 之前的热度数据
        for (int i = 0; i < Math.min(9, hotnessList.size()); i++) {
            SoundHotness soundHotness = hotnessList.get(i);
            Sound sound = soundHotness.getSound();
            double hotness = soundHotness.getHotness();

            // 存入 Redis ZSet
            redisService.zAdd("weekly_hot", sound.getId().toString(), hotness);

            // 记录最终前 9 名的声音
            topSoundIds.add(sound.getId());
        }
        List<Sound> topSounds = soundMapper.getSoundsByIds(topSoundIds);
        for (Sound sound : topSounds) {
            sound.setFileUrl(null);
        }
        return soundUtils.addTagsAndUsername(topSounds);
    }

    private static final String REDIS_KEY = "weekly_hot_articles";

    @Scheduled(cron = "0 0 0 ? * MON")  // 每周一 00:00 运行
    public void updateHotness() {

        List<Sound> topSounds = getWeeklyHotSoundUtil();

        if (topSounds.isEmpty()) {
            return;
        }

        Map<String, Object> soundMap = new HashMap<>();
        for (int i = 0; i < topSounds.size(); i++) {
            soundMap.put(String.valueOf(i), topSounds.get(i));
        }

        // 存入 Redis，过期时间 7 天
        redisService.hmset(REDIS_KEY, soundMap, 7 * 24 * 60 * 60);

        System.out.println("Redis 热门推荐声音已更新！");
    }

    public ResponseEntity<Result> getWeeklyHotSound() {
        Map<Object, Object> soundMap = redisService.hmget(REDIS_KEY);
        List<Sound> sounds = soundMap.values().stream()
                .map(o -> (Sound) o)
                .collect(Collectors.toList());
        if (sounds.isEmpty()) {
            return ResponseUtil.build(Result.error(404, "暂无数据"));
        }
        return ResponseUtil.build(Result.success(sounds, "获取成功"));
    }

    public void updateHotnessManual() {

        List<Sound> topSounds = getWeeklyHotSoundUtil();

        if (topSounds.isEmpty()) {
            return;
        }

        Map<String, Object> soundMap = new HashMap<>();
        for (int i = 0; i < topSounds.size(); i++) {
            soundMap.put(String.valueOf(i), topSounds.get(i));
        }

        // 存入 Redis，过期时间 7 天
        redisService.hmset(REDIS_KEY, soundMap, 7 * 24 * 60 * 60);

        System.out.println("Redis 热门推荐声音已更新！");

        redisService.del("view");
        redisService.del("like");
        redisService.del("comment");
    }
}
