package com.iqiongzhi.SCB.mapper;

import com.iqiongzhi.SCB.data.po.Sound;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SoundMapper {
    @Select("select id, user_id, title, description, category, file_url, cover_url, location, created_at, updated_at from sound where user_id=#{Id}")
    List<Sound> getMySounds(String userId);
    @Select("select user_id from sound where id=#{soundId}")
    String getSoundOwner(String soundId);
    @Select("select * from sound where id=#{soundId}")
    Sound getSound(String soundId);
    @Select("select id, user_id, title, description, category, cover_url, location from sound where category=#{category}  LIMIT #{offset}, #{limit}")
    List<Sound> getSoundList(@Param("category") String category, @Param("offset") int offset, @Param("limit") int limit);

    @Insert("insert into sound (user_id, title, description, category, file_url, cover_url, location) values (#{userId}, #{title}, #{description}, #{category}, #{fileUrl}, #{coverUrl}, #{location})")
    void insertSound(Sound sound);

    @Delete("delete from sound where id=#{id}")
    void delSound(String soundId);


}
