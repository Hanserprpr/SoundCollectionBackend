package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.cache.IGlobalCache;
import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.SoundMapper;
import com.iqiongzhi.SCB.data.repository.SoundRepository;
import com.iqiongzhi.SCB.mapper.TagMapper;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import com.iqiongzhi.SCB.utils.SoundUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SoundService {
    @Autowired
    private SoundMapper soundMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private SoundUtils soundUtils;
    @Autowired
    private IGlobalCache redis;

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Result> upload(String userId, Sound sound) {
            sound.setUserId(userId); // 防止篡改表单id
            soundMapper.insertSound(sound);
            int soundId = sound.getId();
            if (sound.getTags() != null && !sound.getTags().isEmpty()) {
                List<String> tags = sound.getTags().stream()
                        .map(String::trim)
                        .filter(tag -> !tag.isEmpty())
                        .distinct()
                        .collect(Collectors.toList());

                if (!tags.isEmpty()) {
                    tagMapper.batchInsertTags(tags);
                    List<Integer> tagIds = tags.stream()
                            .map(tagMapper::getTagId)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    if (!tagIds.isEmpty()) {
                        tagMapper.batchInsertSoundTags(soundId, tagIds);
                    }
                }
            }
            return ResponseUtil.build(Result.success(null, "上传成功"));
    }

    public ResponseEntity<Result> delSound(String userId, String soundId) {
        try {
            if (!soundMapper.getSoundOwner(soundId).equals(userId)) {
                return ResponseUtil.build(Result.error(403, "无权限"));
            }
            soundMapper.delSound(soundId);
            return ResponseUtil.build(Result.success(null, "删除成功"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "删除失败" + e));
        }
    }

    public ResponseEntity<Result> getSound(String soundId) {
        try {
            Sound sound = soundMapper.getSound(soundId);
            List<String> tags = tagMapper.getTagsBySoundId(soundId);
            sound.setTags(tags);
            return ResponseUtil.build(Result.success(sound, "获取成功"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "获取失败" + e));
        }
    }

    public ResponseEntity<Result> getSoundList(String category, int page, int size) {
        try {
            int offset = (page - 1) * size;
            List<Sound> sounds = soundMapper.getSoundList(category, offset, size);
            return ResponseUtil.build(Result.success(soundUtils.addTags(sounds), "获取成功"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "获取失败" + e));
        }
    }

    public ResponseEntity<Result> update(String userId,  Sound sound) {
        try {
            String soundId = sound.getId().toString();
            if (!soundMapper.getSoundOwner(soundId).equals(userId)) {
                return ResponseUtil.build(Result.error(403, "无权限"));
            }
            soundMapper.updateSound(sound);
            return ResponseUtil.build(Result.ok());
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "更新失败" + e));
        }
    }

    @Transactional
    public void increaseViews(Integer soundId) {
        soundMapper.increaseViews(soundId);
        redis.hincr("view", soundId.toString(), 1);
    }
}
