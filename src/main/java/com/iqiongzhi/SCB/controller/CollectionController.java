package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.CollectionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/collection")
public class CollectionController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CollectionService collectionService;

    /**
     * 收藏声音
     * @param soundId 声音id
     * @return 收藏结果
     */
    @Auth
    @PostMapping("/collect/{soundId}")
    public ResponseEntity<Result> collect(@PathVariable String soundId) {
        String userId = (String) request.getAttribute("userId");
        return collectionService.collect(userId, soundId);
    }

    /**
     * 获取收藏列表
     * @param page 页码
     * @param size 每页大小
     * @return 收藏列表
     */
    @Auth
    @GetMapping("/collections")
    public ResponseEntity<Result> collections(@RequestParam int page, @RequestParam int size) {
        String userId = (String) request.getAttribute("userId");
        return collectionService.collections(userId, page, size);
    }

    /**
     * 删除收藏
     * @param soundId 声音id
     * @return 删除结果
     */
    @Auth
    @DeleteMapping("/delete/{soundId}")
    public ResponseEntity<Result> delete(@PathVariable String soundId) {
        String userId = (String) request.getAttribute("userId");
        return collectionService.delete(userId, soundId);
    }
}
