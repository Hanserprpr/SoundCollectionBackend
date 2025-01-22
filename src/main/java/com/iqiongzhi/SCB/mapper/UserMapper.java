package com.iqiongzhi.SCB.mapper;

import com.iqiongzhi.SCB.data.po.User;
import com.iqiongzhi.SCB.data.po.Sound;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select id from user where email=#{email}")
    Integer findUserByEmail(String email);
    @Select("select id, username, email, wechat, bio, avatar_url, created_at, updated_at, last_login_at, status from user where id=#{id}")
    User findUserById(String id);
    @Select("select username from user where wechat=#{openid}")
    List<User> findUserByOpenId(String openid);
    @Select("select password from user where username=#{username}")
    String getPasswdByName(String username);
    @Select("select password from user where email=#{email}")
    String getPasswdByEmail(String email);
    @SelectProvider(type = UserSQLProvider.class, method = "buildGetUserIdQuery")
    Integer getUserId(@Param("id") String id, @Param("type") String type);
    @Select("select id, user_id, title, description, category, file_url, cover_url, location, created_at, updated_at from sound where user_id=#{Id}")
    List<Sound> getMyVoices(String userId);
    @Select("select user_id from sound where id=#{soundId}")
    String getSoundOwner(String soundId);

    @Insert("insert into user (username, password, student_id) values (#{username}, #{password}, #{SDUId})")
    void addUser(String username, String password, String SDUId);
    @Insert("insert into user (wechat) values (#{openid})")
    void addWXUser(String openid);
    @Insert("insert into user (username, email, password) values (#{email}, #{email}, #{password})")
    void addEmailUser(String email, String password);
    @Insert("insert into sound (user_id, title, description, category, file_url, cover_url, location) values (#{userId}, #{title}, #{description}, #{category}, #{fileUrl}, #{coverUrl}, #{location})")
    void insertSound(Sound sound);

    @Update("update user set last_login_at = now() where id = #{id}")
    void updateLastLoginTime(int id);

    @UpdateProvider(type = UserSQLProvider.class, method = "updateById")
    void updateById(@Param("id") Integer id, @Param("user") User user);

    @Delete("delete from sound where id=#{id}")
    void delSound(String soundId);
}
