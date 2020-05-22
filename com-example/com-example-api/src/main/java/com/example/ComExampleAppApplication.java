package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description 应用入口
 * @PackagePath com.example.ComExampleAppApplication
 * @Author YINZHIYU
 * @Date 2020/5/8 13:43
 * @Version 1.0.0.0
 **/
@SpringBootApplication
@Slf4j
public class ComExampleAppApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ComExampleAppApplication.class, args);
        } catch (Exception e) {
            log.error(String.format("ComExampleAppApplication ==> SpringApplication.run(ComExampleAppApplication.class, args) ==> 异常：%s", e));
        }
    }
}
