package com.iqiongzhi.SCB.data.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sound")
public class Sound {
    private Integer id;

    private Integer userId;

    private String title;

    private String description;

    private String category;

    private String fileUrl;

    private String coverUrl;

    private String location;

    private String createdAt;

    private String updatedAt;
}
