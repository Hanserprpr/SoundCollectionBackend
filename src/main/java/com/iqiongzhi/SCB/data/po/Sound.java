package com.iqiongzhi.SCB.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sound")
public class Sound {
    @Id
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String userId;

    @NotBlank(message = "标题不能为空")
    private String title;

    private String description;

    @NotBlank(message = "类别不能为空")
    private String category;

    @NotBlank(message = "文件地址不能为空")
    private String fileUrl;

    private String coverUrl;

    private String location;

    private String duration;

    private String createdAt;

    private String updatedAt;
}
