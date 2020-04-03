package com.example.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @PackagePath com.example.controller.ComExampleAppController
 * @Author YINZHIYU
 * @Date 2020-04-03 11:11:00
 * @Version 1.0.0.0
 **/
@RestController
@RequestMapping(value = "/rest")
public class ComExampleAppController {

    @RequestMapping(value = "/restEx/{id}")
    public String restEx(@PathVariable(name = "id") String id) {
        return String.format("%s,%s", "Hello", id);
    }
}
