package com.iqiongzhi.SCB.mapper;

import com.iqiongzhi.SCB.data.po.Sound;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RecommendMapper {

    @Select("SELECT * FROM sound ORDER BY id DESC LIMIT 10")
    List<Sound> getLatestSound();
}
