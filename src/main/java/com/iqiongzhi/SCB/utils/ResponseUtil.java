package com.iqiongzhi.SCB.utils;

import org.springframework.http.ResponseEntity;
import com.iqiongzhi.SCB.data.vo.Result;

public class ResponseUtil {
    public static ResponseEntity<Result> build(Result result) {
        return ResponseEntity
                .status(result.httpStatus())
                .body(result);
    }
}
