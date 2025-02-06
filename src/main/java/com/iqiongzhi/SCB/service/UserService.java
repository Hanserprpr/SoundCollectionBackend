package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.dto.UserProfileDTO;
import com.iqiongzhi.SCB.data.po.User;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.FollowMapper;
import com.iqiongzhi.SCB.mapper.UserMapper;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 获取用户信息
     * @param id 用户id
     * @param userId 当前用户id
     * @return 用户信息
     */
    public ResponseEntity<Result> getUser(String id, String userId) {
        User user = userMapper.findUserById(id);
        if (user == null) {
            return ResponseUtil.build(Result.error(404, "用户不存在"));
        }
        int follow = followMapper.getFollowCount(id);
        int fans = followMapper.getFansCount(id);
        boolean isFollow = followMapper.isFollowing(userId, id);
        UserProfileDTO userProfileDTO = new UserProfileDTO(user, follow, fans, isFollow);
        return ResponseUtil.build(Result.success(userProfileDTO, "获取用户信息成功"));
    }
}
