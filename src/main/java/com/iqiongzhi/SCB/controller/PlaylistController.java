package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.PlaylistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/playlist")
public class PlaylistController {
    @Autowired
    private HttpServletRequest request;

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    /**
     * 清空播放列表
     * @return 删除结果
     */
    @Auth
    @DeleteMapping("/del")
    public ResponseEntity<Result> delPlaylist() {
        String userId = (String) request.getAttribute("userId");
        return playlistService.delPlaylist(userId);
    }

    /**
     * 获取播放列表
     * @return 音频信息
     */
    @Auth
    @GetMapping("/getPlaylistById")
    public ResponseEntity<Result> getPlaylistById() {
        String userId = (String) request.getAttribute("userId");
        return playlistService.getPlaylistById(userId);
    }

    /**
     * 向播放列表中添加音频
     * @param soundId 音频id
     * @return 添加结果
     */
    @Auth
    @PostMapping("/addSound")
    public ResponseEntity<Result> addSound(@RequestParam int soundId) {
        String userId = (String) request.getAttribute("userId");
        return playlistService.addSound(userId, soundId);
    }

    /**
     * 从播放列表中删除音频
     * @param soundId 音频id
     * @return 删除结果
     */
    @Auth
    @DeleteMapping("/delSound")
    public ResponseEntity<Result> delSound(@RequestParam int soundId) {
        String userId = (String) request.getAttribute("userId");
        return playlistService.delSound(userId, soundId);
    }
}
