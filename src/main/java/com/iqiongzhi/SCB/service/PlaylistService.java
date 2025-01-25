package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.dto.PlaylistSoundDTO;
import com.iqiongzhi.SCB.data.po.Playlist;
import com.iqiongzhi.SCB.data.po.PlaylistSounds;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.mapper.PlaylistMapper;
import com.iqiongzhi.SCB.mapper.PlaylistSoundsMapper;
import com.iqiongzhi.SCB.mapper.PlaylistSoundsMapperDTO;
import com.iqiongzhi.SCB.mapper.SoundMapper;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistMapper playlistMapper;

    @Autowired
    private PlaylistSoundsMapper playlistSoundsMapper;

    @Autowired
    private PlaylistSoundsMapperDTO playlistSoundsMapperDTO;

    @Autowired
    private SoundMapper soundMapper;

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

    public ResponseEntity<Result> delPlaylist(String userId, @NotNull String playlistId) {
        String id = playlistMapper.getOwner(Integer.valueOf(playlistId));
        if (!userId.equals(id)) {
            return ResponseUtil.build(Result.error(403, "无权限"));
        }
        playlistMapper.delPlaylist(playlistId);
        return ResponseUtil.build(Result.ok());
    }

    public ResponseEntity<Result> getPlaylistById(String playlistId) {
        List<PlaylistSounds> sounds = playlistSoundsMapper.getPlaylistById(playlistId);
        List<Long> soundIds = sounds.stream()
                .map(PlaylistSounds::getSoundId)
                .map(Integer::longValue)
                .toList();
        List<Sound> soundDetails = soundMapper.getSoundsByIds(soundIds);
        Map<Integer, Sound> soundMap = soundDetails.stream()
                .collect(Collectors.toMap(Sound::getId, Function.identity()));

        List<PlaylistSoundDTO> result = sounds.stream()
                .map(playlistSound -> playlistSoundsMapperDTO.toDTO(playlistSound, soundMap.get(playlistSound.getSoundId())))
                .filter(Objects::nonNull)
                .toList();

        return ResponseUtil.build(Result.success(result, "获取成功"));
    }

    public ResponseEntity<Result> addSound(int user_id, int playlistId, int soundId) {
        String owner = playlistMapper.getOwner(playlistId);
        if (user_id != Integer.parseInt(owner)) {
            return ResponseUtil.build(Result.error(403, "无权限"));
        }
        playlistSoundsMapper.addSound(playlistId, soundId);

        return ResponseUtil.build(Result.ok());
    }
}
