package com.iqiongzhi.SCB.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FollowMapper {

    @Select("select count(*) from follows where follower=#{id}")
    int getFollowCount(String id);

    @Select("select count(*) from follows where following=#{id}")
    int getFansCount(String id);

    /**
     * 判断是否关注
     * @param follower
     * @param following
     * @return
     */
    @Select("SELECT COUNT(*) FROM follows WHERE follower = #{follower} AND following = #{following}")
    boolean isFollowing(@Param("follower") String follower, @Param("following") String following);
}
