package com.example.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.constants.RedisConstants;
import com.example.common.utils.RedisUtils;
import com.example.domain.dto.RedisLogDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 日志 管理中心
 * @PackagePath com.example.controller.LogManagerCenterController
 * @Author YINZHIYU
 * @Date 2020/6/3 15:54
 * @Version 1.0.0.0
 **/
@Api(tags = "日志 管理中心")
@Controller
@RequestMapping(value = "/logs")
public class LogManagerCenterController {
    @Resource
    private RedisUtils redisUtils;

    @ApiOperation(value = "日志页面", notes = "日志页面", httpMethod = "GET")
    @RequestMapping(value = "/pages")
    public String pages(Model model) {
        List<String> queryDateList = new ArrayList<>();
        for (int i = 0; i < RedisConstants.LOGS_EXPIRE_DAYS; i++) {
            String queryDate = DateUtil.format(DateUtil.offsetDay(DateUtil.date(), 0 - i), "yyyy-MM-dd");
            queryDateList.add(queryDate);
        }
        model.addAttribute("queryDateList", queryDateList);
        return "logs_manager";
    }

    @ApiOperation(value = "日志查询", notes = "日志查询", httpMethod = "GET")
    @RequestMapping(value = "/listPage")
    @ResponseBody
    public Map<String, Object> listPage(@ApiParam(name = "日志", required = true) RedisLogDto redisLogDto) {
        List<RedisLogDto> redisLogDtoList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(redisLogDto.getHashKey())) {
            redisLogDtoList.addAll(redisUtils.getValue(RedisConstants.SYS_LOGS + redisLogDto.getQueryDate(), redisLogDto.getHashKey()));
        } else {
            List<List<RedisLogDto>> list = redisUtils.getValues(RedisConstants.SYS_LOGS + redisLogDto.getQueryDate());
            for (List<RedisLogDto> item : list) {
                redisLogDtoList.addAll(item);
            }
        }
        //不在redisLogDtoList 基础上进行反转
        redisLogDtoList = redisLogDtoList.stream().sorted(Comparator.comparing(RedisLogDto::getRecordDate).reversed()).collect(Collectors.toList());

        //bootstrap-table要求服务器返回的json须包含：total，rows,采取客户端分页，服务端提供全部数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", ObjectUtil.isNotEmpty(redisLogDtoList) ? redisLogDtoList.size() : 0);
        map.put("rows", redisLogDtoList);
        return map;
    }
}
