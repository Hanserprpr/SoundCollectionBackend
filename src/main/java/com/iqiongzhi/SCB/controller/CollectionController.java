package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.po.Collection;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.CollectionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/collection")
public class CollectionController {
    @Autowired
    private HttpServletRequest request;

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    /**
     * 创建收藏夹
     * @param collection 抽藏夹信息
     * @return 创建结果
     */
    @Auth
    @PostMapping("/create")
    public ResponseEntity<Result> createCollection(@Valid @RequestBody Collection collection) {
        String userId = (String) request.getAttribute("userId");
        return collectionService.createCollection(userId, collection);
    }


    /**
     * 编辑收藏夹
     * @param collection 抽藏夹信息
     * @return 编辑结果
     */
    @Auth
    @PostMapping("/edit")
    public ResponseEntity<Result> editCollection(@Valid @RequestBody Collection collection) {
        String userId = (String) request.getAttribute("userId");
        return collectionService.editCollection(userId, collection);
    }

    /**
     * 删除收藏夹
     * @param collectionId 收藏夹Id
     * @return 删除结果
     */
    @Auth
    @DeleteMapping("/del")
    public ResponseEntity<Result> delCollection(@RequestParam String collectionId) {
        String userId = (String) request.getAttribute("userId");
        return collectionService.delCollection(userId, collectionId);
    }

    /**
     * 向收藏夹中添加音频
     * @param collectionId 收藏夹id
     * @param soundId 音频id
     * @return 添加结果
     */
    @Auth
    @PostMapping("/addSound")
    public ResponseEntity<Result> addSound(@RequestParam int collectionId, @RequestParam int soundId) {
        String userId = (String) request.getAttribute("userId");
        return collectionService.addSound(Integer.parseInt(userId), collectionId, soundId);
    }

    /**
     * 从收藏夹中移除音频
     * @param collectionId 收藏夹id
     * @param soundId 音频id
     * @return 移除结果
     */
    @Auth
    @PostMapping("/removeSound")
    public ResponseEntity<Result> removeSound(@RequestParam int collectionId, @RequestParam int soundId) {
        String userId = (String) request.getAttribute("userId");
        return collectionService.addSound(Integer.parseInt(userId), collectionId, soundId);
    }
}
}
