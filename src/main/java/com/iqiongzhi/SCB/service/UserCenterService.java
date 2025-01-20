package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.UserMapper;
import com.iqiongzhi.SCB.utils.JWTUtil;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserCenterService {
    @Autowired
    UserMapper userMapper;

    public ResponseEntity<Result> getMe(String id) {
        return ResponseUtil.build(Result.success(userMapper.findUserById(id), "获取成功"));

    }

}
