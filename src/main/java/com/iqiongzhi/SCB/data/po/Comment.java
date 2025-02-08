package com.iqiongzhi.SCB.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comments")
public class Comment {
    @TableId(value = "id", type = IdType.AUTO)
    private String commentId;

    private String userId;
    private String soundId;
    private String content;
}
