package com.iqiongzhi.SCB.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SearchMapper {
    @Insert("INSERT INTO search_logs (user_id, keyword)" +
            "VALUES (#{userId}, #{keyword})" +
            "ON DUPLICATE KEY UPDATE searched_at = NOW();")
    void insertSearchLog(String userId, String keyword);

    @Select("SELECT keyword FROM search_logs WHERE user_id = #{userId} ORDER BY searched_at DESC LIMIT 7")
    List<String> getSearchHistory(String userId);
}
