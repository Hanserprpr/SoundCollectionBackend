//package com.iqiongzhi.SCB.service;
//
//import com.iqiongzhi.SCB.data.po.Sound;
//import com.iqiongzhi.SCB.utils.ResponseUtil;
//import com.iqiongzhi.SCB.data.vo.Result;
//import com.iqiongzhi.SCB.mapper.CollectionMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CollectionService {
//    @Autowired
//    private CollectionMapper collectionMapper;
//
//    public ResponseEntity<Result> collect(String userId, String soundId) {
//        try {
//            collectionMapper.collect(userId, soundId);
//            return ResponseUtil.build(Result.ok());
//        } catch (DuplicateKeyException e) {
//            return ResponseUtil.build(Result.error(409, "已经收藏过了"));
//        } catch (DataIntegrityViolationException e) {
//            return ResponseUtil.build(Result.error(404, "声音不存在"));
//        } catch (Exception e) {
//            return ResponseUtil.build(Result.error(400, "添加收藏失败"));
//        }
//    }
//
//    public ResponseEntity<Result> collections(String userId, int page, int size) {
//        try {
//            int offset = (page - 1) * size;
//            List<Integer> soundIds = collectionMapper.getCollectedSound(userId, offset, size);
//            if (soundIds.isEmpty()) {
//                return ResponseUtil.build(Result.error(404, "你还没有收藏QAQ"));
//            }
//            List<Sound> result = collectionMapper.getCollectedSoundList(soundIds);
//            return ResponseUtil.build(Result.success(result, "获取收藏成功"));
//        } catch (Exception e) {
//            return ResponseUtil.build(Result.error(400, "获取收藏失败" + e));
//        }
//    }
//
//    public ResponseEntity<Result> delete(String userId, String soundId) {
//        try {
//            int rowsAffected = collectionMapper.delete(userId, soundId);
//            if (rowsAffected == 0) {
//                throw new DataIntegrityViolationException("收藏不存在");
//            }
//            return ResponseUtil.build(Result.ok());
//        } catch (DataIntegrityViolationException e) {
//            return ResponseUtil.build(Result.error(404, "该收藏不存在"));
//        } catch (Exception e) {
//            return ResponseUtil.build(Result.error(400, "删除收藏失败"));
//        }
//    }
//}

package com.iqiongzhi.SCB.service;


import com.iqiongzhi.SCB.data.po.Collection;
import com.iqiongzhi.SCB.data.po.CollectionSounds;
import com.iqiongzhi.SCB.data.vo.Result;

import com.iqiongzhi.SCB.mapper.CollectionMapper;
import com.iqiongzhi.SCB.mapper.CollectionSoundsMapper;

import com.iqiongzhi.SCB.mapper.PrivacyMapper;

import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionService {
    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private CollectionSoundsMapper collectionSoundsMapper;

    @Autowired
    private PrivacyMapper privacyMapper;

    public ResponseEntity<Result> createCollection(String userId, @NotNull Collection collection) {
        collection.setUserId(userId);
        collectionMapper.createCollection(collection);
        return ResponseUtil.build(Result.ok());
    }

    public ResponseEntity<Result> getCollection(String userId,int page,int size) {
        try {
            int offset = (page - 1) * size;
            if(privacyMapper.getCollectionsPriById(userId)==1) {
                List<Collection> result=collectionMapper.getCollection(userId,offset,size);
                if (result.isEmpty()) {
                    return ResponseUtil.build(Result.success(null, "TA还没有收藏QAQ"));
                }
                return ResponseUtil.build(Result.success(result, "获取收藏成功"));
            }
            else{
                return ResponseUtil.build(Result.success(null, "TA的收藏不可见"));
            }
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "获取收藏失败" + e));
        }
    }

    public ResponseEntity<Result> editCollection(String userId, @NotNull Collection collection) {
        String id = collectionMapper.getOwner(collection.getId());
        if (!userId.equals(id)) {
            return ResponseUtil.build(Result.error(403, "无权限"));
        }
        collection.setUserId(userId);
        collectionMapper.editCollection(collection);
        collectionMapper.updateEditTime(collection.getId());
        return ResponseUtil.build(Result.ok());
    }

    public ResponseEntity<Result> delCollection(String userId, @NotNull String collectionId) {
        String id = collectionMapper.getOwner(Integer.valueOf(collectionId));
        if (!userId.equals(id)) {
            return ResponseUtil.build(Result.error(403, "无权限"));
        }
        collectionMapper.delCollection(collectionId);
        return ResponseUtil.build(Result.ok());
    }

    public ResponseEntity<Result> getCollectionSounds(String userId, int collectionId, int page, int size) {
        try {
            if(privacyMapper.getCollectionsPriById(userId)==1) {
                int offset = (page - 1) * size;
                List<CollectionSounds> result=collectionSoundsMapper.getCollectionById(collectionId,offset,size);
                if (result.isEmpty()) {
                    return ResponseUtil.build(Result.success(null, "TA的收藏夹为空QAQ"));
                }
                return ResponseUtil.build(Result.success(result, "获取收藏列表成功"));
            }
            else{
                return ResponseUtil.build(Result.success(null, "TA的收藏不可见"));
            }
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "获取收藏失败" + e));
        }
    }


    public ResponseEntity<Result> addSound(int user_id, int collectionId, int soundId) {
        String owner = collectionMapper.getOwner(collectionId);
        if (user_id != Integer.parseInt(owner)) {
            return ResponseUtil.build(Result.error(403, "无权限"));
        }
        collectionSoundsMapper.addSound(collectionId, soundId);

        return ResponseUtil.build(Result.ok());
    }
}

