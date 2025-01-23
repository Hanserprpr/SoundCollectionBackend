package com.iqiongzhi.SCB.mapper;

import com.iqiongzhi.SCB.data.po.Sound;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CollectionMapper {
    @Insert("insert into collections (user_id, sound_id) values (#{userId}, #{soundId})")
    void collect(String userId, String soundId);

    @Select("select count(*) from collections where user_id=#{userId} and sound_id=#{soundId}")
    int isCollected(String userId, String soundId);
    @Select("select sound_id from collections where user_id=#{userId} LIMIT #{offset}, #{limit}")
    List<Integer> getCollectedSound(String userId, @Param("offset") int offset, @Param("limit") int limit);

    List<Sound> getCollectedSoundList(@Param("list") List<Integer> ids);
}
