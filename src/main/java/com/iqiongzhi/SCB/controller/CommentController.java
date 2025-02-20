package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.po.Comment;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CommentService commentService;

    /**
     * 添加评论
     *
     * @param comment 评论
     * @return 添加结果
     */
    @Auth
    @PostMapping("/add")
    public ResponseEntity<Result> addComment(@RequestBody Comment comment) {
        String userId = (String) request.getAttribute("userId");
        return commentService.addComment(userId, comment);
    }

    /**
     * 删除评论
     *
     * @param comment 评论
     * @return 删除结果
     */
    @Auth
    @PostMapping("/delete")
    public ResponseEntity<Result> deleteComment(@RequestBody Comment comment) {
        String userId = (String) request.getAttribute("userId");
        return commentService.deleteComment(userId, comment);
    }

    /**
     * 点赞评论
     *
     * @param commentId 评论id
     * @return 点赞结果
     */
    @Auth
    @PostMapping("/like/{commentId}")
    public ResponseEntity<Result> likeComment(@PathVariable String commentId) {
        String userId = (String) request.getAttribute("userId");
        return commentService.likeComment(userId, commentId);
    }

    /**
     * 取消点赞评论
     *
     * @param commentId 评论id
     * @return 取消点赞结果
     */
    @Auth
    @DeleteMapping("/unlike/{commentId}")
    public ResponseEntity<Result> unlikeComment(@PathVariable String commentId) {
        String userId = (String) request.getAttribute("userId");
        return commentService.unlikeComment(userId, commentId);
    }

    /**
     * 最新评论
     *
     * @param soundId 声音id
     * @param page    页码
     * @return ResponseEntity<Result>
     */
    @GetMapping("/latest")
    public ResponseEntity<Result> latestComment(@RequestParam String soundId,
                                                @RequestParam int page) {
        String userId = (String) request.getAttribute("userId");
        return commentService.latestComment(soundId, page, userId);
    }

    /**
     * 热门评论
     *
     * @param soundId 声音id
     * @param page    页码
     * @return ResponseEntity<Result>
     */
    @GetMapping("/hot")
    public ResponseEntity<Result> hotComment(@RequestParam String soundId,
                                             @RequestParam int page) {
        String userId = (String) request.getAttribute("userId");
        return commentService.hotComment(soundId, page, userId);
    }
}
