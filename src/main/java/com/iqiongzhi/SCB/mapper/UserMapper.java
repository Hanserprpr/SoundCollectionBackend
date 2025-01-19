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

    @Insert("insert into user (username, password, student_id) values (#{username}, #{password}, #{stuId})")
    void addUser(String username, String password, String stuId);
    @Insert("insert into user (wechat) values (#{openid})")
    void addWXUser(String openid);


}
