package com.iqiongzhi.SCB.mapper;

import com.iqiongzhi.SCB.data.po.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select username, email, wechat, bio, avatar_url, created_at, updated_at, last_login_at, status from user where student_id=#{stuId}")
    List<User> findUserByStuId(String stuId);
    @Select("select username from user where wechat=#{openid}")
    List<User> findUserByOpenId(String openid);
    @Select("select password from user where username=#{identifier} or email=#{identifier}")
    String getPasswd(String identifier);
    @Select("select id from user where wechat=#{id} or student_id=#{id} or username=#{id}")
    int getUserId(String id);

    @Insert("insert into user (username, password, student_id) values (#{username}, #{password}, #{SDUId})")
    void addUser(String username, String password, String SDUId);
    @Insert("insert into user (wechat) values (#{openid})")
    void addWXUser(String openid);


}
