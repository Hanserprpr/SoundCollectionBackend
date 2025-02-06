package com.iqiongzhi.SCB.mapper;


import org.apache.ibatis.annotations.*;


@Mapper
public interface LikeMapper {

    /*
    @Select("select sound_id from likes where user_id=#{userId} LIMIT #{offset}, #{limit}")
    List<Integer> findLikeById(String userId,@Param("offset") int offset, @Param("limit") int limit);
    List<Sound> getLikedSoundList(@Param("list") List<Integer> ids);
    */

    @Select("select count(*) from likes where sound_id=#{soundId}")
    int soundCnt(String soundId);

    @Insert("insert into likes (user_id, sound_id, created_at) values (#{user_id}, #{sound_id}, now())")
    void addLike(String user_id, String sound_id);

    @Delete("delete from likes where user_id=#{userId} and sound_id=#{soundId}")
    int unlike(String userId, String soundId);
}
