package com.example.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.enums.SyncTaskStatusEnum;
import com.example.domain.common.Result;
import com.example.domain.dto.SyncTaskChartDto;
import com.example.domain.entity.SyncTask;
import com.example.domain.entity.SyncTaskData;
import com.example.domain.vo.BasePageVO;
import com.example.domain.vo.SyncTaskChartVO;
import com.example.service.business.SyncTaskDataService;
import com.example.service.business.SyncTaskService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description Task 管理中心
 * @PackagePath com.example.controller.TasksManagerCenterController
 * @Author YINZHIYU
 * @Date 2020/6/3 15:55
 * @Version 1.0.0.0
 **/
@Api(tags = "Task 管理中心")
@Controller
@RequestMapping(value = "/tasks")
public class TasksManagerCenterController {

    @Resource
    private SyncTaskService syncTaskService;

    @Resource
    private SyncTaskDataService syncTaskDataService;

    @ApiOperation(value = "Task任务页面", notes = "Task任务页面", httpMethod = "GET")
    @RequestMapping(value = "/pages")
    public String pages() {
        return "tasks_manager";
    }

    @ApiOperation(value = "Task任务信息分页", notes = "Task任务信息分页", httpMethod = "GET")
    @RequestMapping(value = "/listPage")
    @ResponseBody
    public Map<String, Object> listPage(@ApiParam(name = "分页", required = true) BasePageVO basePageVO, @ApiParam(name = "任务信息", required = true) SyncTask syncTask) {
        Page<SyncTask> page = new Page<>(basePageVO.getPageNumber(), basePageVO.getPageSize());
        QueryWrapper<SyncTask> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotEmpty(syncTask.getTaskStatus())) {
            queryWrapper.eq(SyncTask.COL_TASK_STATUS, syncTask.getTaskStatus());
        }
        if (ObjectUtil.isNotEmpty(syncTask.getTaskType())) {
            queryWrapper.eq(SyncTask.COL_TASK_TYPE, syncTask.getTaskType());
        }
        if (ObjectUtil.isNotEmpty(syncTask.getTaskDesc())) {
            queryWrapper.like(SyncTask.COL_TASK_DESC, syncTask.getTaskDesc());
        }
        queryWrapper.orderByDesc(SyncTask.COL_ID);
        IPage<SyncTask> pages = syncTaskService.getBaseMapper().selectPage(page, queryWrapper);

        List<SyncTask> taskList = pages.getRecords();
        if (ObjectUtil.isNotEmpty(taskList)) {
            taskList.forEach(item -> {
                QueryWrapper<SyncTaskData> query = new QueryWrapper<>();
                query.eq(SyncTaskData.COL_TASK_ID, item.getId());
                List<SyncTaskData> dataList = syncTaskDataService.list(query);
                item.setSyncTaskDataList(dataList);
            });
        }

        //图表统计
        List<String> labels = Lists.newArrayList();
        for (int i = 6; i >= 0; i--) {
            String queryDate = DateUtil.format(DateUtil.offsetDay(DateUtil.date(), i * -1), DatePattern.NORM_DATE_PATTERN);
            labels.add(queryDate);
        }

        List<SyncTaskChartDto> taskChartDtos = getChartData(labels);

        //bootstrap-table要求服务器返回的json须包含：total，rows
        Map<String, Object> map = new HashMap<>();
        map.put("total", pages.getTotal());
        map.put("rows", pages.getRecords());
        map.put("labels", labels);
        map.put("chartDatas", taskChartDtos);
        return map;
    }

    /*
     * @Description 获取报表数据
     * @Params ==>
     * @Param labels 日期标签
     * @Return java.util.List<cn.wangoon.domain.dto.SyncTaskChartDto>
     * @Date 2020/6/12 15:12
     * @Auther YINZHIYU
     */
    private List<SyncTaskChartDto> getChartData(List<String> labels) {

        List<SyncTaskChartDto> taskChartDtos = Lists.newArrayList();

        List<SyncTaskChartVO> chartVOList = syncTaskService.getTaskChartsList(null);

        Map<String, SyncTaskChartVO> maps = convertMap(chartVOList);

        if (ObjectUtil.isEmpty(maps)) {
            return taskChartDtos;
        }

        for (SyncTaskStatusEnum value : SyncTaskStatusEnum.values()) {
            if (Objects.equals(value, SyncTaskStatusEnum.FINISH)) {
                continue;
            }
            SyncTaskChartDto syncTaskChartDto = new SyncTaskChartDto();
            syncTaskChartDto.setName(value.getRemark());
            syncTaskChartDto.setLine_width(2);
            syncTaskChartDto.setColor(value.getColor());

            List<Integer> integers = Lists.newArrayList();
            labels.forEach(date -> {
                SyncTaskChartVO taskChartVO = maps.get(String.format("%s_%s", date, value.getTaskStatus()));
                if (ObjectUtil.isNotEmpty(taskChartVO)) {
                    integers.add(taskChartVO.getNumbers());
                } else {
                    integers.add(0);
                }
            });
            syncTaskChartDto.setValue(integers);

            taskChartDtos.add(syncTaskChartDto);
        }
        return taskChartDtos;
    }

    /*
     * @Description 转换map
     * @Params ==>
     * @Param sysBaseConfigs
     * @Return void
     * @Date 2020/4/26 11:01
     * @Auther YINZHIYU
     */
    private Map<String, SyncTaskChartVO> convertMap(List<SyncTaskChartVO> syncTaskChartVOS) {
        Map<String, SyncTaskChartVO> maps = null;

        if (ObjectUtil.isNotEmpty(syncTaskChartVOS)) {
            maps = syncTaskChartVOS.stream().collect(Collectors.toMap(entity -> {
                String key = new StringBuffer()
                        .append(entity.getDate())
                        .append("_")
                        .append(entity.getTaskStatus())
                        .toString();
                return key;
            }, Function.identity(), (v1, v2) -> v1));
        }
        return maps;
    }


    /*
     * @Description 重置
     * @Params ==>
     * @Param sysJobConfig
     * @Return cn.wangoon.domain.common.Result
     * @Date 2020/4/27 18:05
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "重置", notes = "重置", httpMethod = "POST")
    @PostMapping(value = "/reset")
    @ResponseBody
    public Result start(@RequestBody @ApiParam(name = "任务信息", required = true) SyncTask syncTask) {

        try {
            syncTask.setTaskStatus(SyncTaskStatusEnum.WAIT.getTaskStatus());
            syncTask.setProcessCount(0);
            boolean result = syncTaskService.updateById(syncTask);

            if (result) {
                return Result.ok("处理成功");
            } else {
                return Result.fail("处理失败");
            }
        } catch (Exception e) {
            return Result.exception(e.getMessage());
        }
    }
}
