package com.iqiongzhi.SCB.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistSoundDTO {
    private Integer soundId; // Sound ID
    private String title;
    private String description;
    private String category;
    private String fileUrl;
    private String coverUrl;
    private String location;
    private String createdAt;
    private String updatedAt;
    private String addedAt; // 加入时间
}
