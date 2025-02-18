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

    @Insert("insert into likes (user_id, sound_id) values (#{user_id}, #{sound_id})")
    void addLike(@Param("user_id") String user_id, @Param("sound_id")String sound_id);

    @Delete("delete from likes where user_id=#{userId} and sound_id=#{soundId}")
    int unlike(@Param("userId") String user_id, @Param("soundId")String sound_id);

    @Select("SELECT COUNT(*) FROM likes WHERE user_id = #{userId} AND sound_id = #{soundId}")
    boolean isLikinging(@Param("userId") String userId, @Param("soundId") String soundId);
}
