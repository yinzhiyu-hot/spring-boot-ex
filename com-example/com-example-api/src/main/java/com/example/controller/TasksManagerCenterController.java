package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.enums.SyncTaskStatusEnum;
import com.example.common.utils.StringUtils;
import com.example.domain.common.Result;
import com.example.domain.entity.SyncTask;
import com.example.domain.entity.SyncTaskData;
import com.example.domain.vo.BasePageVO;
import com.example.service.business.SyncTaskDataService;
import com.example.service.business.SyncTaskService;
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

/**
 * @Description Task 管理中心
 * @PackagePath com.example.controller.TasksManagerCenterController
 * @Author YINZHIYU
 * @Date 2020/5/8 13:44
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

    @ApiOperation(value = "Task任务信息分页", notes = "Task任务信息分页", httpMethod = "GET")
    @RequestMapping(value = "/pages")
    public String pages() {
        return "tasks_manager";
    }

    @ApiOperation(value = "列表", notes = "列表", httpMethod = "GET")
    @RequestMapping(value = "/listPage")
    @ResponseBody
    public Map<String, Object> listPage(@ApiParam(name = "分页", required = true) BasePageVO basePageVO, @ApiParam(name = "任务信息", required = true) SyncTask syncTask) {
        Page<SyncTask> page = new Page<SyncTask>(basePageVO.getPageNumber(), basePageVO.getPageSize());
        QueryWrapper<SyncTask> queryWrapper = new QueryWrapper<SyncTask>();
        if (ObjectUtil.isNotEmpty(syncTask.getTaskStatus())) {
            queryWrapper.eq("task_status", syncTask.getTaskStatus());
        }
        if (StringUtils.notBlank(syncTask.getTaskType())) {
            queryWrapper.eq("task_type", syncTask.getTaskType());
        }
        if (StringUtils.notBlank(syncTask.getTaskDesc())) {
            queryWrapper.like("task_desc", syncTask.getTaskDesc());
        }
        IPage<SyncTask> pages = syncTaskService.getBaseMapper().selectPage(page, queryWrapper);

        List<SyncTask> taskList = pages.getRecords();
        if (ObjectUtil.isNotEmpty(taskList)) {
            taskList.forEach(item -> {
                QueryWrapper<SyncTaskData> query = new QueryWrapper<SyncTaskData>();
                query.eq("task_id", item.getId());
                List<SyncTaskData> dataList = syncTaskDataService.list(query);
                item.setSyncTaskDataList(dataList);
                System.out.println(item.getTaskData());
            });
        }

        //bootstrap-table要求服务器返回的json须包含：total，rows
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", pages.getTotal());
        map.put("rows", pages.getRecords());
        return map;
    }


    /*
     * @Description 重置
     * @Params ==>
     * @Param sysJobConfig
     * @Return Result
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
