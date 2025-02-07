package com.iqiongzhi.SCB.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface PrivacyMapper {
    @Insert("insert into privacy_settings (user_id) values (#{userId})")
    void insertPrivacy(int userId);

    @Update("update privacy_settings set show_follows=0 where user_id=#{userId} ")
    void hideFollows(String userId);

    @Update("update privacy_settings set show_fans=0 where user_id=#{userId} ")
    void hideFans(String userId);

    @Update("update privacy_settings set show_collections=0 where user_id=#{userId} ")
    void hideCollections(String userId);

    @Update("update privacy_settings set show_follows=1 where user_id=#{userId} ")
    void displayFollows(String userId);

    @Update("update privacy_settings set show_fans=1 where user_id=#{userId} ")
    void displayFans(String userId);

    @Update("update privacy_settings set show_collections=1 where user_id=#{userId} ")
    void displayCollections(String userId);

    @Select("select show_follows from privacy_settings where user_id=#{userId}")
    Integer getFollowsPriById(String userId);

    @Select("select show_fans from privacy_settings where username=#{username}")
    Integer getFansPriById(String userId);

    @Select("select show_collections from privacy_settings where username=#{username}")
    Integer getCollectionsPriById(String userId);


}
