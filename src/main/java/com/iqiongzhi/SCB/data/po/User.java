package com.iqiongzhi.SCB.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String email;

    private String password;

    private String wechat;

    private String studentId;

    private String bio;

    private String avatarUrl;

    // background url
    private String bgUrl;

    private String createdAt;

    private String updatedAt;

    private String lastLoginAt;

    private Integer status;
}
