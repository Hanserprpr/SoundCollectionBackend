package com.iqiongzhi.SCB.data.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("playlistSounds")
public class PlaylistSounds {
    private Integer id;

    private Integer playlistId;

    private Integer soundId;

    private String addedAt;
}
