package com.example;

import com.example.common.utils.LogUtils;
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

            LogUtils.info("系统启动完成");
        } catch (Exception e) {
            LogUtils.error(args, "SpringApplication.run(ComExampleAppApplication.class, args) ==> 异常", e);
            System.exit(0);//正常退出
            //System.exit(1);//强制退出
        }
    }
}
