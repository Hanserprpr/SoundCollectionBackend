package com.iqiongzhi.SCB.data.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    public String session_key;
    public String errmsg;
    public String openid;
    public String errcode;
}
