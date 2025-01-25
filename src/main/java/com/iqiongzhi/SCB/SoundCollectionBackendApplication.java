package com.iqiongzhi.SCB;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.iqiongzhi.SCB.mapper", annotationClass = org.apache.ibatis.annotations.Mapper.class)
public class SoundCollectionBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoundCollectionBackendApplication.class, args);
    }

}
