package com.iqiongzhi.SCB.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface FollowMapper {
    @Insert("insert into follows (follower, following, created_at) values (#{follower}, #{following})")
    void addFollow(String follower, String following);

    @Delete("delete from follows where follower=#{follower} and following=#{following}")
    int unfollow(String follower, String following);

    @Select("select count(*) from follows where follower=#{id}")
    int getFollowCount(String id);

    @Select("select count(*) from follows where following=#{id}")
    int getFansCount(String id);


    /**
     * 判断是否关注
     * @param follower 关注者
     * @param following 被关注者
     * @return 是否关注
     */
    @Select("SELECT COUNT(*) FROM follows WHERE follower = #{follower} AND following = #{following}")
    boolean isFollowing(@Param("follower") String follower, @Param("following") String following);
}
