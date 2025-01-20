package com.iqiongzhi.SCB.data.dto;

import lombok.Data;

@Data
public class VerifyRequestDTO {
    private String ticket;
    private String code;

}
