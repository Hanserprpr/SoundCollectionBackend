package com.iqiongzhi.SCB.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private Object data;
    private String msg;


    public static Result success(Object data, String msg) {
        return new Result(200, data, msg);
    }

    public static Result ok() {
        return new Result(200, null, "成功");
    }


    public static Result error(Integer code, String msg) {
        return new Result(code, null, msg);
    }
}