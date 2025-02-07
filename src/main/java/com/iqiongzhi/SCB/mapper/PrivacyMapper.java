package com.iqiongzhi.SCB.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PrivacyMapper {

    @Insert("INSERT INTO privacy_settings (user_id) VALUES (#{userId})")
    void insertPrivacy(int userId);

}
