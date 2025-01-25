package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.po.Playlist;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.PlaylistMapper;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistMapper playlistMapper;

    public ResponseEntity<Result> createPlaylist(String userId, @NotNull Playlist playlist) {
        playlist.setUserId(userId);
        playlistMapper.createPlaylist(playlist);
        return ResponseUtil.build(Result.ok());
    }

    public ResponseEntity<Result> getPlaylist(String userId) {
        return ResponseUtil.build(Result.success(playlistMapper.getPlaylist(userId), "获取成功"));
    }

    public ResponseEntity<Result> editPlaylist(String userId, @NotNull Playlist playlist) {
        String id = playlistMapper.getOwner(playlist.getId());
        if (!userId.equals(id)) {
            return ResponseUtil.build(Result.error(403, "无权限"));
        }
        playlist.setUserId(userId);
        playlistMapper.editPlaylist(playlist);
        playlistMapper.updateEditTime(playlist.getId());
        return ResponseUtil.build(Result.ok());
    }

    public ResponseEntity<Result> delPlaylist(String userId, String playlistId) {
        String id = playlistMapper.getOwner(Integer.valueOf(playlistId));
        if (!userId.equals(id)) {
            return ResponseUtil.build(Result.error(403, "无权限"));
        }
        playlistMapper.delPlaylist(playlistId);
        return ResponseUtil.build(Result.ok());
    }
}
