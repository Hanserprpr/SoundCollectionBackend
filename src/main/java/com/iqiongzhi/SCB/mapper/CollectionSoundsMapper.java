package com.iqiongzhi.SCB.mapper;

import com.iqiongzhi.SCB.data.po.CollectionSounds;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CollectionSoundsMapper {
    @Insert("insert into collection_sounds (collection_id, sound_id) values (#{collectionId}, #{soundId})")
    void addSound(int collectionId, int soundId);

    @Select("select * from collection_sounds where collection_id = #{collectionId} LIMIT #{offset}, #{limit}")
    List<CollectionSounds> getCollectionById(int collectionId, @Param("offset") int offset, @Param("limit") int limit);
}


