$(function ($) {
    initTable();
    queryTable();

    Utils.get("queryBtn").bind("click", queryTable);

    //清空按钮
    Utils.get('cancelBtn').click(function () {
        Utils.get("id").val("");
        Utils.get("jobName").val("");
        Utils.get("jobClassBeanName").val("");
        Utils.get("jobStatus").val("");
        Utils.get("shardingTotalCount").val("");
        Utils.get("shardingItemParams").val("");
        Utils.get("cronExpression").val("");
        Utils.get("remark").val("");
        Utils.get('defaultForm').data('bootstrapValidator').resetForm(true);
        hideUpdateJobConfig();
    });

    //基本信息-异步提交表单
    Utils.get('submitBtn').click(function () {
        Utils.get('defaultForm').bootstrapValidator('validate');

        if (!Utils.get('defaultForm').data("bootstrapValidator").isValid()) {
            return;
        }

        //组装数据
        var data = {
            "id": Utils.get("id").val(),
            "jobName": Utils.get("jobName").val(),
            "jobClassBeanName": Utils.get("jobClassBeanName").val(),
            "jobStatus": Utils.get("jobStatus").val(),
            "shardingTotalCount": Utils.get("shardingTotalCount").val(),
            "shardingItemParams": Utils.get("shardingItemParams").val(),
            "cronExpression": Utils.get("cronExpression").val(),
            "remark": Utils.get("remark").val()
        };
        submitUpdate(data);
    });

    Utils.get('defaultForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            shardingTotalCount: {
                validators: {
                    notEmpty: {
                        message: '分片数不能为空'
                    },
                    regexp: {
                        regexp: /^[1-9]\d*$/,
                        message: '分片数 > 0 整数'
                    }
                }
            },

            // shardingItemParams: {
            //     validators: {
            //         notEmpty: {
            //             message: '分片参数不能为空'
            //         }
            //     }
            // },

            cronExpression: {
                validators: {
                    notEmpty: {
                        message: 'cron表达式不能为空'
                    }
                }
            }
        }
    });
});

function queryTable() {
    Utils.get("table").bootstrapTable('refresh');
}

function initTable() {
    /*boostrap table*/
    Utils.get('table').bootstrapTable({
        columns: [
            {
                // field: 'Number',//可不加
                title: '序号',//标题  可不加
                width: 50,
                align: "center",
                formatter: function (value, row, index) {
                    return index + 1;
                }
            },
            {
                field: 'jobName',
                title: 'Job名称',
                width: 220,
                align: "left",
                formatter: paramsMatter
            }, {
                field: 'jobClassBeanName',
                title: 'Bean名称',
                width: 220,
                align: "left",
                formatter: paramsMatter
            }, {
                field: 'shardingTotalCount',
                title: '分片数',
                width: 60,
                align: "center"
            }, {
                field: 'shardingItemParams',
                title: '分片参数',
                align: "left",
                formatter: paramsMatter
            }, {
                field: 'cronExpression',
                title: 'Cron表达式',
                width: 120,
                align: "center",
                formatter: paramsMatter
            }, {
                field: 'remark',
                title: '备注',
                width: 200,
                align: "left",
                formatter: paramsMatter
            }, {
                field: 'jobStatus',
                title: '状态',
                width: 50,
                align: "center",
                formatter: jobStatusFormatter
            }, {
                field: 'jobStatus',
                title: '操作',
                width: 140,
                align: "center",
                formatter: operateFormatter
            }],

        method: 'get',
        contentType: "application/x-www-form-urlencoded",//必须要有！因为bootstap table使用的是ajax方式获取数据，这时会将请求的content type默认设置为 text/plain，这样在服务端直接通过 @RequestParam参数映射是获取不到的。
        url: "/com-example/jobs/listPage",//要请求数据的文件路径,加时间戳，防止读取缓存数据
        pagination: true,//是否开启分页（*）启动分页，必须设为true
        queryParamsType: '',//注意:查询参数组织方式，默认值为 'limit',在默认情况下 传给服务端的参数为：offset,limit,sort 。 设置为 '' 在这种情况下传给服务器的参数为：pageSize,pageNumber
        queryParams: queryParams,//请求服务器时所传的参数
        pageNumber: 1,//初始化加载第一页，默认第一页
        pageSize: 10,//每页的记录行数（*）
        pageList: [10, 15, 20, 25, 30],//可供选择的每页的行数（*）
        sidePagination: "server" //分页方式：client客户端分页，server服务端分页（*）
    });

    //得到查询的参数
    function queryParams(params) {
        return {  //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.pageSize,  //页面大小
            pageNumber: params.pageNumber, //页码
            jobStatus: Utils.get("status").val()
        };
    }
}

function start(value) {
    var d = dialog({
        title: '消息',
        width: 200,
        content: '确定启动吗？',
        ok: function () {
            $.ajax({
                //请求方式
                type: "POST",
                //请求的媒体类型
                contentType: "application/json;charset=UTF-8",
                //请求地址
                url: "/com-example/jobs/start",
                //数据，json字符串
                data: JSON.stringify({"id": value}),
                //请求成功
                success: function (result) {
                    if (result.success) {
                        var s = dialog({
                            title: '消息',
                            width: 200,
                            content: result.datas,
                            cancel: false,
                            okValue: '确定',
                            ok: function () {
                                queryTable();
                            }
                        });
                        s.showModal();
                    } else {
                        var f = dialog({
                            title: '消息',
                            width: 200,
                            content: result.error
                        });
                        f.showModal();
                    }
                },
                //请求失败，包含具体的错误信息
                error: function (e) {

                }
            });
            return true;
        },
        okValue: "确定",
        cancel: function () {
            return true;
        },
        cancelValue: "取消"
    });
    d.showModal();
}

function stop(value) {
    var d = dialog({
        title: '消息',
        width: 200,
        content: '确定停止吗？',
        ok: function () {
            $.ajax({
                //请求方式
                type: "POST",
                //请求的媒体类型
                contentType: "application/json;charset=UTF-8",
                //请求地址
                url: "/com-example/jobs/stop",
                //数据，json字符串
                data: JSON.stringify({"id": value}),
                //请求成功
                success: function (result) {
                    if (result.success) {
                        var s = dialog({
                            title: '消息',
                            width: 200,
                            content: result.datas,
                            cancel: false,
                            okValue: '确定',
                            ok: function () {
                                queryTable();
                            }
                        });
                        s.showModal();
                    } else {
                        var f = dialog({
                            title: '消息',
                            width: 200,
                            content: result.error
                        });
                        f.showModal();
                    }
                },
                //请求失败，包含具体的错误信息
                error: function (e) {

                }
            });
            return true;
        },
        okValue: "确定",
        cancel: function () {
            return true;
        },
        cancelValue: "取消"
    });
    d.showModal();
}

function update(row) {
    Utils.get("id").val(row.id);
    Utils.get("jobName").val(row.jobName);
    Utils.get("jobClassBeanName").val(row.jobClassBeanName);
    Utils.get("jobStatus").val(row.jobStatus);
    Utils.get("shardingTotalCount").val(row.shardingTotalCount);
    Utils.get("shardingItemParams").val(row.shardingItemParams);
    Utils.get("cronExpression").val(row.cronExpression);
    Utils.get("remark").val(row.remark);
    showUpdateJobConfig();
}

function submitUpdate(value) {
    var d = dialog({
        title: '消息',
        width: 200,
        zIndex: 999999999,
        content: '确定提交更新吗？',
        ok: function () {
            $.ajax({
                //请求方式
                type: "POST",
                //请求的媒体类型
                contentType: "application/json;charset=UTF-8",
                //请求地址
                url: "/com-example/jobs/update",
                //数据，json字符串
                data: JSON.stringify(value),
                //请求成功
                success: function (result) {
                    if (result.success) {
                        var s = dialog({
                            title: '消息',
                            width: 200,
                            zIndex: 999999995,
                            content: result.datas,
                            cancel: false,
                            okValue: '确定',
                            ok: function () {
                                hideUpdateJobConfig();
                                queryTable();
                                Utils.get('defaultForm').data('bootstrapValidator').resetForm(true);
                            }
                        });
                        s.showModal();
                    } else {
                        var f = dialog({
                            title: '消息',
                            width: 200,
                            zIndex: 999999999,
                            content: result.error
                        });
                        f.showModal();
                    }
                },
                //请求失败，包含具体的错误信息
                error: function (e) {

                }
            });
            return true;
        },
        okValue: "确定",
        cancel: function () {
            return true;
        },
        cancelValue: "取消"
    });
    d.showModal();
}

function jobStatusFormatter(value) {
    switch (value) {
        case 0:
            return "<span class='glyphicon glyphicon-remove' style='color: red;font-size: large'></span>";

        case 1:
            return "<span class='glyphicon glyphicon-ok' style='color: steelblue;font-size: large'></span>";

        default:
            return "-";
    }
}

function operateFormatter(value, row, index) {
    switch (value) {
        case 0:
            if ("SystemListenerJob" === row.jobName) {
                return [
                    "<div class=’form-inline‘>",
                    "<button type='button' class='btn btn-primary' onclick='update(" + JSON.stringify(row) + ")'>编辑</button>",
                    "</div>"
                ].join('');
            } else {
                return [
                    "<div class=’form-inline‘>",
                    "<button type='button' class='btn btn-primary' onclick='update(" + JSON.stringify(row) + ")'>编辑</button>",
                    "<button style='visibility: hidden'/>",
                    "<button type='button' class='btn btn-primary' onclick='start(" + row.id + ")'>启动</button>",
                    "</div>"
                ].join('');
            }
        case 1:
            if ("SystemListenerJob" === row.jobName) {
                return [
                    "<div class=’form-inline‘>",
                    "<button type='button' class='btn btn-primary' onclick='update(" + JSON.stringify(row) + ")'>编辑</button>",
                    "</div>"
                ].join('');
            } else {
                return [
                    "<div class=’form-inline‘>",
                    "<button type='button' class='btn btn-primary' onclick='update(" + JSON.stringify(row) + ")'>编辑</button>",
                    "<button style='visibility: hidden'/>",
                    "<button type='button' class='btn btn-primary' onclick='stop(" + row.id + ")'>停止</button>",
                    "</div>"
                ].join('');
            }
        default:
            return "-";
    }
}

//展示
function showUpdateJobConfig() {
    Utils.get('updateJobConfig').modal({backdrop: 'static', keyboard: false});
}

//隐藏
function hideUpdateJobConfig() {
    Utils.get('updateJobConfig').modal('hide');
}
