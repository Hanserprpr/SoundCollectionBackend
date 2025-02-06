package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.SoundMapper;
import com.iqiongzhi.SCB.data.repository.SoundRepository;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SoundService {
    @Autowired
    private SoundMapper soundMapper;

    @Autowired
    private SoundRepository soundRepository;

    public ResponseEntity<Result> upload(String userId, Sound sound) {
        try {
            sound.setUserId(userId); // 防止篡改表单id
            soundMapper.insertSound(sound);
            soundRepository.save(sound);
            return ResponseUtil.build(Result.success(null, "上传成功"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "上传失败" + e));
        }
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
            return ResponseUtil.build(Result.success(soundMapper.getSound(soundId), "获取成功"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "获取失败" + e));
        }
    }

    public ResponseEntity<Result> getSoundList(String category, int page, int size) {
        try {
            int offset = (page - 1) * size;
            return ResponseUtil.build(Result.success(soundMapper.getSoundList(category, offset, size), "获取成功"));
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
    }
}
