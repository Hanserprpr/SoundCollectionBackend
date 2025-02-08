package com.iqiongzhi.SCB.data.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sound")
@Document(indexName = "sound")
public class Sound {

    @Id
    private Integer id;

    private String userId;

    private String username;

    @NotBlank(message = "标题不能为空")
    @Field(type = FieldType.Text)
    private String title;

    private String description;

    @NotBlank(message = "类别不能为空")
    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Text)
    private List<String> tags;

    @NotBlank(message = "文件地址不能为空")
    private String fileUrl;

    private String coverUrl;

    @Field(type = FieldType.Text)
    private String location;

    private String duration;

    @Field(type = FieldType.Integer)
    private Integer views;  // 播放量

    @Field(type = FieldType.Integer)
    private Integer likes;  // 点赞数

    @Field(type = FieldType.Integer)
    private Integer comments;  // 评论数

    @Field(type = FieldType.Double)
    private Double hotScore;  // 热度分数

    @Field(type = FieldType.Date)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdAt;

    @Field(type = FieldType.Date)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updatedAt;
}
