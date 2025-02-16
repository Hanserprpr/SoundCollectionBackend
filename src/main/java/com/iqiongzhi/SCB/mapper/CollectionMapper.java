//package com.iqiongzhi.SCB.mapper;
//
//import com.iqiongzhi.SCB.data.po.Sound;
//import org.apache.ibatis.annotations.*;
//
//import java.util.List;
//
//@Mapper
//public interface CollectionMapper {
//    @Insert("insert into collections (user_id, sound_id) values (#{userId}, #{soundId})")
//    void collect(String userId, String soundId);
//
//    @Select("select count(*) from collections where user_id=#{userId} and sound_id=#{soundId}")
//    int isCollected(String userId, String soundId);
//    @Select("select sound_id from collections where user_id=#{userId} LIMIT #{offset}, #{limit}")
//    List<Integer> getCollectedSound(String userId, @Param("offset") int offset, @Param("limit") int limit);
//
//    List<Sound> getCollectedSoundList(@Param("list") List<Integer> ids);
//
//    @Delete("delete from collections where user_id=#{userId} and sound_id=#{soundId}")
//    int delete(String userId, String soundId);
//}



package com.iqiongzhi.SCB.mapper;

import com.iqiongzhi.SCB.data.po.Collection;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CollectionMapper {
    @Insert("insert into collections (user_id, title, cover_url, description) values (#{userId}, #{title},#{coverUrl}, #{description})")
    void createCollection(Collection collection);

    @Select("select * from collections where user_id = #{userId} LIMIT #{offset}, #{limit}")
    List<Collection> getCollection(String userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select("select user_id from collections where id = #{id}")
    String getOwner(Integer id);

    @Update("update collections set title = #{title},cover_url=#{coverUrl}, description = #{description} where id = #{id}")
    void editCollection(Collection collection);

    @Update("update collections set updated_at = now() where id = #{id}")
    void updateEditTime(Integer id);

    @Delete("delete from collections where id = #{id}")
    void delCollection(String id);
}