package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.UserMapper;
import com.iqiongzhi.SCB.data.po.User;
import com.iqiongzhi.SCB.utils.ResponseUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserCenterService {
    @Autowired
    UserMapper userMapper;

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
}
