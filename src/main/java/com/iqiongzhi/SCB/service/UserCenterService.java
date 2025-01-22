package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.SoundMapper;
import com.iqiongzhi.SCB.mapper.UserMapper;
import com.iqiongzhi.SCB.data.po.User;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import com.iqiongzhi.SCB.cache.IGlobalCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class UserCenterService {
    @Autowired
    private IGlobalCache redis;
    @Autowired
    UserMapper userMapper;
    @Autowired
    private SoundMapper soundMapper;

    /**
     * 获取用户信息
     * @param id 用户id
     * @return 用户信息
     */
    public ResponseEntity<Result> getMe(String id) {
        return ResponseUtil.build(Result.success(userMapper.findUserById(id), "获取成功"));
    }

    /**
     * 更新用户信息
     * @param id 用户id
     * @param user 用户信息
     * @return 更新结果
     */
    public ResponseEntity<Result> update(String id, User user) {
        try {
        userMapper.updateById(Integer.parseInt(id), user);
        return ResponseUtil.build(Result.success(null, "更新成功"));
    }
        catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "更新失败"+e));
        }
    }

    /**
     * 用户登出
     * @param token 用户token
     * @return 登出结果
     */
    public ResponseEntity<Result> logout(String token) {
        try {
            redis.del(token);
            return ResponseUtil.build(Result.success(null, "登出成功"));
        }catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "登出失败"+e));
        }
    }

    /**
     * 获取用户上传的声音
     * @param userId 用户id
     * @return 用户上传的声音
     */
    public ResponseEntity<Result> getMyVoices(String userId) {
        return ResponseUtil.build(Result.success(soundMapper.getMyVoices(userId), "获取成功"));
    }
}
