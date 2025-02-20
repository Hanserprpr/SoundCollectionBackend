package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.cache.IGlobalCache;
import com.iqiongzhi.SCB.data.dto.CommentDTO;
import com.iqiongzhi.SCB.data.po.Comment;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.CommentMapper;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private IGlobalCache redis;

    /**
     * 添加评论
     *
     * @param userId  用户id
     * @param comment 评论
     * @return 添加结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Result> addComment(String userId, Comment comment) {
        comment.setUserId(userId);
        commentMapper.addComment(comment);
        redis.hincr("comment", comment.getSoundId(), 1);
        return ResponseUtil.build(Result.ok());
    }

    /**
     * 删除评论
     *
     * @param userId  用户id
     * @param comment 评论
     * @return 删除结果
     */
    public ResponseEntity<Result> deleteComment(String userId, Comment comment) {
        try {
            commentMapper.delComment(comment.getCommentId(), userId);
            redis.hdecr("comment", comment.getSoundId(), 1);
            return ResponseUtil.build(Result.ok());
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(403, "无权限或评论不存在"));
        }
    }

    /**
     * 点赞评论
     *
     * @param userId    用户id
     * @param commentId 评论id
     * @return 点赞结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Result> likeComment(String userId, String commentId) {
        commentMapper.likeComment(userId, commentId);
        commentMapper.likeCommentCount(commentId);
        return ResponseUtil.build(Result.ok());
    }

    /**
     * 取消点赞
     *
     * @param userId    用户id
     * @param commentId 评论id
     * @return 取消点赞结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Result> unlikeComment(String userId, String commentId) {
        commentMapper.unlikeComment(userId, commentId);
        commentMapper.unlikeCommentCount(commentId);
        return ResponseUtil.build(Result.ok());
    }

    /**
     * 获取最新评论
     *
     * @param soundId 声音id
     * @return 最新评论
     */
    public ResponseEntity<Result> latestComment(String soundId, int page, String userId) {
        int offset = (page - 1) * 15;
        List<CommentDTO> data  = commentMapper.getLatestComments(soundId, offset, userId);
        if (data.isEmpty()) {
            return ResponseUtil.build(Result.error(404, "没有评论"));
        }
        return ResponseUtil.build(Result.success(data, "获取成功"));
    }

    /**
     * 获取热门评论
     *
     * @param page 页码
     * @return 热门评论
     */
    public ResponseEntity<Result> hotComment(String soundId, int page, String userId) {
        int offset = (page - 1) * 15;
        List<CommentDTO> data = commentMapper.getHotComments(soundId, offset, userId);
        if (data.isEmpty()) {
            return ResponseUtil.build(Result.error(404, "没有评论"));
        }
        return ResponseUtil.build(Result.success(data, "获取成功"));
    }
}
