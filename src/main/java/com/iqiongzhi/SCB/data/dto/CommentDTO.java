package com.iqiongzhi.SCB.data.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@TableName("comments")
public class CommentDTO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer commentId;

    private String userId;
    private String content;
    private Timestamp createdAt;
    private int likes;
}
