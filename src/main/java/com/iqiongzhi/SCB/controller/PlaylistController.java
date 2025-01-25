package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.po.Playlist;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.PlaylistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
     * 创建播放列表
     * @param playlist 播放列表信息
     * @return 创建结果
     */
    @Auth
    @PostMapping("/create")
    public ResponseEntity<Result> createPlaylist(@Valid @RequestBody Playlist playlist) {
        String userId = (String) request.getAttribute("userId");
        return playlistService.createPlaylist(userId, playlist);
    }

    /**
     * 获取播放列表
     * @return 播放列表信息
     */
    @Auth
    @GetMapping("/getPlaylist")
    public ResponseEntity<Result> getPlaylist() {
        String userId = (String) request.getAttribute("userId");
        return playlistService.getPlaylist(userId);
    }

    /**
     * 编辑播放列表
     * @param playlist 播放列表信息
     * @return 编辑结果
     */
    @Auth
    @PostMapping("/edit")
    public ResponseEntity<Result> editPlaylist(@Valid @RequestBody Playlist playlist) {
        String userId = (String) request.getAttribute("userId");
        return playlistService.editPlaylist(userId, playlist);
    }

    /**
     * 删除播放列表
     * @param playlistId 播放列表id
     * @return 删除结果
     */
    @Auth
    @DeleteMapping("/del")
    public ResponseEntity<Result> delPlaylist(@RequestParam String playlistId) {
        String userId = (String) request.getAttribute("userId");
        return playlistService.delPlaylist(userId, playlistId);
    }
}
