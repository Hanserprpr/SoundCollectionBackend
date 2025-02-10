package com.iqiongzhi.SCB.mapper;


import com.iqiongzhi.SCB.data.po.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FollowMapper {
    @Insert("insert into follows (follower, following) values (#{follower}, #{following})")
    void addFollow(@Param("follower") String follower, @Param("following") String following);

    @Delete("delete from follows where follower=#{follower} and following=#{following}")
    int unfollow(@Param("follower") String follower, @Param("following") String following);

    @Select("select count(*) from follows where follower=#{id}")
    int getFollowCount(String id);

    @Select("select count(*) from follows where following=#{id}")
    int getFansCount(String id);

    @Select("select follower from follows where following=#{userId} LIMIT #{offset}, #{limit}")
    List<Integer> getFans(String userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select("select follower from follows where follower=#{userId} LIMIT #{offset}, #{limit}")
    List<Integer> getFollows(String userId, @Param("offset") int offset, @Param("limit") int limit);

    List<User> getFollowsList(@Param("list") List<Integer> follows);


    /**
     * 判断是否关注
     * @param follower 关注者
     * @param following 被关注者
     * @return 是否关注
     */
    @Select("SELECT COUNT(*) FROM follows WHERE follower = #{follower} AND following = #{following}")
    boolean isFollowing(@Param("follower") String follower, @Param("following") String following);
}
