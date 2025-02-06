package com.iqiongzhi.SCB.service;


import com.iqiongzhi.SCB.mapper.FollowMapper;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import com.iqiongzhi.SCB.data.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class FollowService {
    @Autowired
    private FollowMapper followMapper;

    public ResponseEntity<Result> follow(String follower, String following) {
        try {
            followMapper.addFollow(follower, following);
            return ResponseUtil.build(Result.ok());
        } catch (DuplicateKeyException e) {
            return ResponseUtil.build(Result.error(409, "已经关注过了ovo"));
        } catch (DataIntegrityViolationException e) {
            return ResponseUtil.build(Result.error(404, "用户不存在"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "关注失败qwq"));
        }
    }

    public ResponseEntity<Result> disfollow(String follower, String following) {
        try {
            int rowsAffected = followMapper.unfollow(follower, following);
            if (rowsAffected == 0) {
                throw new DataIntegrityViolationException("您还没有关注Ta");
            }
            return ResponseUtil.build(Result.ok());
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "取消关注失败qwq"));
        }
    }
}
