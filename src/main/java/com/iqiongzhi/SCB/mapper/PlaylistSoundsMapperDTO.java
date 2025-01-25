package com.iqiongzhi.SCB.mapper;

import com.iqiongzhi.SCB.data.dto.PlaylistSoundDTO;
import com.iqiongzhi.SCB.data.po.PlaylistSounds;
import com.iqiongzhi.SCB.data.po.Sound;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaylistSoundsMapperDTO {
    @Mapping(source = "playlistSound.soundId", target = "soundId")
    @Mapping(source = "playlistSound.addedAt", target = "addedAt")
    @Mapping(source = "sound.title", target = "title")
    @Mapping(source = "sound.description", target = "description")
    PlaylistSoundDTO toDTO(PlaylistSounds playlistSound, Sound sound);
}