package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.dto.UserProfileDTO;
import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.data.po.User;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.FollowMapper;
import com.iqiongzhi.SCB.mapper.PrivacyMapper;
import com.iqiongzhi.SCB.mapper.SoundMapper;
import com.iqiongzhi.SCB.mapper.UserMapper;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PrivacyMapper privacyMapper;
    @Autowired
    private SoundMapper soundMapper;

    /**
     * 获取用户信息
     * @param id 用户id
     * @param userId 当前用户id
     * @return 用户信息
     */
    public ResponseEntity<Result> getUser(String id, String userId) {
        User user = userMapper.getUserInfo(id);
        if (user == null) {
            return ResponseUtil.build(Result.error(404, "用户不存在"));
        }
        int follow = followMapper.getFollowCount(id);
        int fans = followMapper.getFansCount(id);
        boolean isFollow = followMapper.isFollowing(userId, id);
        List<Sound> soundList = soundMapper.getUserSounds(id);
        UserProfileDTO userProfileDTO = new UserProfileDTO(user, follow, fans, isFollow, soundList);
        return ResponseUtil.build(Result.success(userProfileDTO, "获取用户信息成功"));
    }


    public ResponseEntity<Result> getFans(String userId, int page, int size) {
        try {
            if(privacyMapper.getFansPriById(userId)==1) {
                int offset = (page - 1) * size;
                List<Integer> fansIds = followMapper.getFans(userId, offset, size);
                if (fansIds.isEmpty()) {
                    return ResponseUtil.build(Result.success(null, "TA还没有粉丝QAQ"));
                }
                List<User> result = followMapper.getFollowsList(fansIds);
                return ResponseUtil.build(Result.success(result, "获取粉丝成功"));
            }
            else{
                return ResponseUtil.build(Result.success(null, "TA的粉丝不可见"));
            }
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "获取粉丝失败" + e));
        }
    }

    public ResponseEntity<Result> getFollows(String userId, int page, int size) {
        try {
            if(privacyMapper.getFollowsPriById(userId)==1) {
                int offset = (page - 1) * size;
                List<Integer> followIds = followMapper.getFollows(userId, offset, size);
                if (followIds.isEmpty()) {
                    return ResponseUtil.build(Result.success(null, "TA还没有关注对象QAQ"));
                }
                List<User> result = followMapper.getFollowsList(followIds);
                return ResponseUtil.build(Result.success(result, "获取关注对象成功"));
            }
            else{
                return ResponseUtil.build(Result.success(null, "TA的关注对象不可见"));
            }
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "获取关注对象失败" + e));
        }
    }

    public ResponseEntity<Result> isFollowing(String follower,String following) {

        return ResponseUtil.build(Result.success(followMapper.isFollowing(follower,following), "获取关注状态"));
    }
}
