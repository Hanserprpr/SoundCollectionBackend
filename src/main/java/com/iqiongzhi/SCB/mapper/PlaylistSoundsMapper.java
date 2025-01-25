package com.iqiongzhi.SCB.mapper;

import com.iqiongzhi.SCB.data.dto.PlaylistSoundDTO;
import com.iqiongzhi.SCB.data.po.PlaylistSounds;
import com.iqiongzhi.SCB.data.po.Sound;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface PlaylistSoundsMapper {
    @Insert("insert into playlist_sounds (playlist_id, sound_id) values (#{playlistId}, #{soundId})")
    void addSound(int playlistId, int soundId);

    @Select("select * from playlist_sounds where playlist_id = #{playlistId}")
    List<PlaylistSounds> getPlaylistById(String playlistId);
}


