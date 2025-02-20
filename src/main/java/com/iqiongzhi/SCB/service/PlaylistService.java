package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.dto.PlaylistSoundDTO;
import com.iqiongzhi.SCB.data.po.PlaylistSounds;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.mapper.PlaylistMapper;
import com.iqiongzhi.SCB.mapper.PlaylistSoundsMapperDTO;
import com.iqiongzhi.SCB.mapper.SoundMapper;
import com.iqiongzhi.SCB.utils.ResponseUtil;
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
    private PlaylistSoundsMapperDTO playlistSoundsMapperDTO;

    @Autowired
    private SoundMapper soundMapper;


    public ResponseEntity<Result> delPlaylist(String userId) {
        playlistMapper.delPlaylist(userId);
        return ResponseUtil.build(Result.ok());
    }

    public ResponseEntity<Result> getPlaylistById(String userId) {
        List<PlaylistSounds> sounds = playlistMapper.getPlaylistById(userId);
        List<Integer> soundIds = sounds.stream()
                .map(PlaylistSounds::getSoundId)
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

    public ResponseEntity<Result> addSound(String user_id, int soundId) {
        playlistMapper.addSound(user_id, soundId);
        return ResponseUtil.build(Result.ok());
    }

    public ResponseEntity<Result> delSound(String userId, int soundId) {
        playlistMapper.delSound(userId, soundId);
        return ResponseUtil.build(Result.ok());
    }
}
