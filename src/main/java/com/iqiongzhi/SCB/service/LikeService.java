package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.utils.ResponseUtil;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.LikeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class LikeService {
    @Autowired
    private LikeMapper likeMapper;

    public ResponseEntity<Result> like(String userId, String soundId) {
        try {
            likeMapper.addLike(userId, soundId);
            return ResponseUtil.build(Result.ok());
        } catch (DuplicateKeyException e) {
            return ResponseUtil.build(Result.error(409, "已经点赞过啦ovo"));
        } catch (DataIntegrityViolationException e) {
            return ResponseUtil.build(Result.error(404, "声音不存在"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "点赞失败qwq"));
        }
    }


    public ResponseEntity<Result> likesCnt(String soundId) {
        try{
            int cnt=likeMapper.soundCnt(soundId);
            return ResponseUtil.build(Result.success(cnt,"获取声音点赞数成功"));
        }catch (DataIntegrityViolationException e) {
            return ResponseUtil.build(Result.error(404, "声音不存在"));
        }
    }

    public ResponseEntity<Result> dislike(String userId, String soundId) {
        try {
            int rowsAffected = likeMapper.unlike(userId, soundId);
            if (rowsAffected == 0) {
                throw new DataIntegrityViolationException("还没有点赞哦ovo");
            }
            return ResponseUtil.build(Result.ok());
        }  catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "取消点赞失败qwq"));
        }
    }
}
