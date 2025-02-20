package com.iqiongzhi.SCB.mapper;

import com.iqiongzhi.SCB.data.po.PlaylistSounds;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlaylistMapper {

    @Delete("delete from playlists where user_id = #{id}")
    void delPlaylist(String id);

    @Delete("delete from playlists where user_id = #{userId} and sound_id = #{soundId}")
    void delSound(String userId, int soundId);

    @Insert("insert into playlists (user_id, sound_id) values (#{userId}, #{soundId})")
    void addSound(String userId, int soundId);

    @Select("select * from playlists where user_id = #{userId}")
    List<PlaylistSounds> getPlaylistById(String userId);
}

