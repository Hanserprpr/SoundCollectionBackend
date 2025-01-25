package com.iqiongzhi.SCB.mapper;

import com.iqiongzhi.SCB.data.po.Playlist;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PlaylistMapper {
    @Insert("insert into playlists (user_id, title, description) values (#{userId}, #{title}, #{description})")
    void createPlaylist(Playlist playlist);

    @Select("select * from playlists where user_id = #{userId}")
    List<Playlist> getPlaylist(String userId);
    @Select("select user_id from playlists where id = #{id}")
    String getOwner(Integer id);

    @Update("update playlists set title = #{title}, description = #{description} where id = #{id}")
    void editPlaylist(Playlist playlist);
    @Update("update playlists set updated_at = now() where id = #{id}")
    void updateEditTime(Integer id);
}

