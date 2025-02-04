package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
@RequestMapping("/sys")
public class SysController {

    @GetMapping("/version")
    public ResponseEntity<Result> system() {
        return ResponseUtil.build(Result.success("v0.1.1-SNAPSHOT", "系统正常"));
    }
}
