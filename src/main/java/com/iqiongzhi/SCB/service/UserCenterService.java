package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.dto.UserProfileDTO;
import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.*;
import com.iqiongzhi.SCB.data.po.User;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import com.iqiongzhi.SCB.cache.IGlobalCache;

import com.iqiongzhi.SCB.utils.SoundUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserCenterService {
    @Autowired
    private IGlobalCache redis;
    @Autowired
    UserMapper userMapper;
    @Autowired
    private SoundMapper soundMapper;
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private SoundUtils soundUtils;
    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    private CommentMapper commentMapper;


    /**
     * 获取用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    public ResponseEntity<Result> getMe(String id) {
        User user = userMapper.findUserById(id);
        int follow = followMapper.getFollowCount(id);
        int fans = followMapper.getFansCount(id);
        UserProfileDTO userProfileDTO = new UserProfileDTO(user, follow, fans);
        return ResponseUtil.build(Result.success(userProfileDTO, "获取成功"));
    }

    /**
     * 更新用户信息
     *
     * @param id   用户id
     * @param user 用户信息
     * @return 更新结果
     */
    public ResponseEntity<Result> update(String id, User user) {
        try {
            userMapper.updateById(Integer.parseInt(id), user);
            return ResponseUtil.build(Result.success(null, "更新成功"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "更新失败" + e));
        }
    }

    /**
     * 用户登出
     *
     * @param token 用户token
     * @return 登出结果
     */
    public ResponseEntity<Result> logout(String token) {
        try {
            redis.del(token);
            return ResponseUtil.build(Result.success(null, "登出成功"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "登出失败" + e));
        }
    }

    /**
     * 获取用户上传的声音
     *
     * @param userId 用户id
     * @return 用户上传的声音
     */
    public ResponseEntity<Result> getMyVoices(String userId) {
        List<Sound> sounds = soundMapper.getMySounds(userId);
        if (sounds.isEmpty()) {
            return ResponseUtil.build(Result.error(404, "您还没有上传声音"));
        }
        for (Sound sound : sounds) {
            String SoundId = String.valueOf(sound.getId());
            sound.setLikes(likeMapper.soundCnt(SoundId));
            sound.setComments(commentMapper.getCommentCount(SoundId));
        }
        return ResponseUtil.build(Result.success(soundUtils.addTags(sounds), "获取成功"));
    }


    public ResponseEntity<Result> getMyFans(String userId, int page, int size) {
        try {
            int offset = (page - 1) * size;
            List<Integer> fansIds = followMapper.getFans(userId, offset, size);
            if (fansIds.isEmpty()) {
                return ResponseUtil.build(Result.error(404, "您还没有粉丝QAQ"));
            }
            List<User> result = followMapper.getFollowsList(fansIds);
            return ResponseUtil.build(Result.success(result, "获取粉丝成功"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "获取粉丝失败" + e));
        }
    }

    public ResponseEntity<Result> getMyFollows(String userId, int page, int size) {
        try {
            int offset = (page - 1) * size;
            List<Integer> followIds = followMapper.getFollows(userId, offset, size);
            if (followIds.isEmpty()) {
                return ResponseUtil.build(Result.error(404, "您还没有关注对象QAQ"));
            }
            List<User> result = followMapper.getFollowsList(followIds);
            return ResponseUtil.build(Result.success(result, "获取关注对象成功"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "获取关注对象失败" + e));
        }
    }
}
