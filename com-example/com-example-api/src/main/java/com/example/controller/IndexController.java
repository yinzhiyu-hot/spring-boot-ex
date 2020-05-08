package com.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description 首页管理
 * @PackagePath com.example.controller.IndexController
 * @Author YINZHIYU
 * @Date 2020/5/8 13:44
 * @Version 1.0.0.0
 **/
@Api(tags = "首页")
@Controller
@RequestMapping(value = "/index")
public class IndexController {

    @ApiOperation(value = "首页", notes = "首页", httpMethod = "GET")
    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

}
