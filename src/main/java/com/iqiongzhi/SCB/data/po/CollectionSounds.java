package com.iqiongzhi.SCB.data.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("collectionSounds")
public class CollectionSounds {
    private Integer id;

    private Integer collectionId;

    private Integer soundId;

    private String addedAt;
}
