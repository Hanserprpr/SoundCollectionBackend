package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.CollectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionService {
    @Autowired
    private CollectionMapper collectionMapper;

    public ResponseEntity<Result> collect(String userId, String soundId) {
        try {
            collectionMapper.collect(userId, soundId);
            return ResponseUtil.build(Result.ok());
        } catch (DuplicateKeyException e) {
            return ResponseUtil.build(Result.error(409, "已经收藏过了"));
        } catch (DataIntegrityViolationException e) {
            return ResponseUtil.build(Result.error(404, "声音不存在"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "添加收藏失败"));
        }
    }

    public ResponseEntity<Result> collections(String userId, int page, int size) {
        try {
            int offset = (page - 1) * size;
            List<Integer> soundIds = collectionMapper.getCollectedSound(userId, offset, size);
            if (soundIds.isEmpty()) {
                return ResponseUtil.build(Result.error(404, "你还没有收藏QAQ"));
            }
            List<Sound> result = collectionMapper.getCollectedSoundList(soundIds);
            return ResponseUtil.build(Result.success(result, "获取收藏成功"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "获取收藏失败" + e));
        }
    }

    public ResponseEntity<Result> delete(String userId, String soundId) {
        try {
            int rowsAffected = collectionMapper.delete(userId, soundId);
            if (rowsAffected == 0) {
                throw new DataIntegrityViolationException("收藏不存在");
            }
            return ResponseUtil.build(Result.ok());
        } catch (DataIntegrityViolationException e) {
            return ResponseUtil.build(Result.error(404, "该收藏不存在"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "删除收藏失败"));
        }
    }
}
