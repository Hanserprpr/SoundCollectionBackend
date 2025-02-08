package com.iqiongzhi.SCB.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface TagMapper {

    Integer getTagId(String tag);

    void batchInsertTags(@Param("tags") List<String> tags);

    void batchInsertSoundTags(@Param("soundId") int soundId, @Param("tagIds") List<Integer> tagIds);

    @Select("SELECT t.name FROM tags t " +
            "INNER JOIN sound_tags st ON t.id = st.tag_id " +
            "WHERE st.sound_id = #{soundId}")
    List<String> getTagsBySoundId(@Param("soundId") String soundId);

    List<Map<String, Object>> getTagsBySoundIds(@Param("soundIds") List<Integer> soundIds);
}
