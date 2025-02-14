package com.iqiongzhi.SCB.mapper;

import com.iqiongzhi.SCB.data.po.CollectionSounds;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CollectionSoundsMapper {
    @Insert("insert into collection_sounds (collection_id, sound_id) values (#{collectionId}, #{soundId})")
    void addSound(int collectionId, int soundId);

    @Select("select count(*) from collection_sounds where sound_id=#{soundId}")
    int collectionCnt(String soundId);

    @Delete("delete from collection_sounds where collection_id =#{collectionId} and sound_id=#{soundId}")
    void removeSound(int collectionId, int soundId);

    @Select("""
                select c.*, s.name as soundName,s.cover_url as soundCoverUrl
                from collection_sounds as c
                left join sound as s
                on s.id=c.sound_id
                where collection_id = #{collectionId}
                LIMIT #{offset}, #{limit}
            """)
    List<CollectionSounds> getCollectionById(int collectionId, @Param("offset") int offset, @Param("limit") int limit);
}


