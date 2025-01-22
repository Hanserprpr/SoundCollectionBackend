package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.UserMapper;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SoundService {
    @Autowired
    private UserMapper userMapper;

    public ResponseEntity<Result> upload(String userId, Sound sound) {
        try {
            sound.setUserId(userId); // 防止篡改表单id
            userMapper.insertSound(sound);
            return ResponseUtil.build(Result.success(null, "上传成功"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "上传失败" + e));
        }
    }

    public ResponseEntity<Result> delSound(String userId, String soundId) {
        try {
            if (!userMapper.getSoundOwner(soundId).equals(userId)) {
                return ResponseUtil.build(Result.error(403, "无权限"));
            }
            userMapper.delSound(soundId);
            return ResponseUtil.build(Result.success(null, "删除成功"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "删除失败" + e));
        }
    }
}
