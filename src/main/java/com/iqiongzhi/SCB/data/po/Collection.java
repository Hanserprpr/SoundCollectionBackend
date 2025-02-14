package com.iqiongzhi.SCB.data.po;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("collection")
public class Collection {
    private Integer id;

    private String userId;

    @NotBlank(message = "标题不能为空")
    private String title;

    private String coverUrl;

    private String description;

    private String createdAt;

    private String updatedAt;
}
