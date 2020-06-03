$(function ($) {
    initTable();
    queryTable();

    //关闭按钮
    Utils.get('closeBtn').click(function () {
        Utils.get("taskData").val("");
        hideTaskDataInfo();
    });
    Utils.get("queryBtn").bind("click", queryTable);
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
            }, {
                field: 'id',
                title: '任务id',
                width: 100,
                align: "left"
            },
            {
                field: 'taskType',
                title: '任务类型',
                align: "left",
                formatter: paramsMatter
            }, {
                field: 'taskDesc',
                title: '任务描述',
                align: "left",
                formatter: paramsMatter
            }, {
                field: 'taskStatus',
                title: '任务状态',
                align: "center",
                width: 100,
                formatter: taskStatusFormatter
            }, {
                field: 'processCount',
                title: '执行次数',
                align: "center",
                width: 100
            }, {
                field: 'createDate',
                title: '创建时间',
                width: 180,
                align: "center",
                formatter: dataFormatter
            }, {
                field: 'finishDate',
                title: '完成时间',
                width: 180,
                align: "center",
                formatter: dataFormatter
            }, {
                field: 'taskStatus',
                title: '操作',
                width: 140,
                align: "center",
                formatter: operateFormatter
            }],

        method: 'get',
        contentType: "application/x-www-form-urlencoded",//必须要有！因为bootstap table使用的是ajax方式获取数据，这时会将请求的content type默认设置为 text/plain，这样在服务端直接通过 @RequestParam参数映射是获取不到的。
        url: "/com-example/tasks/listPage",//要请求数据的文件路径,加时间戳，防止读取缓存数据
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
            taskType: Utils.get("taskType").val(),
            taskDesc: Utils.get("taskDesc").val(),
            taskStatus: Utils.get("taskStatus").val()
        };
    }
}

function reset(value) {
    var d = dialog({
        title: '消息',
        width: 200,
        content: '确定重置吗？',
        ok: function () {
            $.ajax({
                //请求方式
                type: "POST",
                //请求的媒体类型
                contentType: "application/json;charset=UTF-8",
                //请求地址
                url: "/com-example/tasks/reset",
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

function taskDataDetail(row) {
    try {
        Utils.get("taskData").val(JSON.stringify(JSON.parse(row.taskData), null, 4));
    } catch (e) {
        Utils.get("taskData").val(row.taskData);
    }
    showTaskDataInfo();
}

function taskStatusFormatter(value) {
    switch (value) {
        case 0:
            return "待处理";
        case 1:
            return "处理中";
        case 2:
            return "已处理";
        case 3:
            return "处理失败";
        case 99:
            return "处理异常";
        default:
            return "-";
    }
}

function operateFormatter(value, row, index) {
    switch (value) {
        case 1:
            return [
                "<div class=’form-inline‘>",
                "<button type='button' class='btn btn-primary' onclick='taskDataDetail(" + JSON.stringify(row) + ")'>报文</button>",
                "<button style='visibility: hidden'/>",
                "<button type='button' class='btn btn-primary' onclick='reset(" + row.id + ")'>重置</button>",
                "</div>"
            ].join('');
        case 3:
            return [
                "<div class=’form-inline‘>",
                "<button type='button' class='btn btn-primary' onclick='taskDataDetail(" + JSON.stringify(row) + ")'>报文</button>",
                "<button style='visibility: hidden'/>",
                "<button type='button' class='btn btn-primary' onclick='reset(" + row.id + ")'>重置</button>",
                "</div>"
            ].join('');
        case 99:
            return [
                "<div class=’form-inline‘>",
                "<button type='button' class='btn btn-primary' onclick='taskDataDetail(" + JSON.stringify(row) + ")'>报文</button>",
                "<button style='visibility: hidden'/>",
                "<button type='button' class='btn btn-primary' onclick='reset(" + row.id + ")'>重置</button>",
                "</div>"
            ].join('');
        default:
            return [
                "<div class=’form-inline‘>",
                "<button type='button' class='btn btn-primary' onclick='taskDataDetail(" + JSON.stringify(row) + ")'>报文</button>",
                "</div>"
            ].join('');
    }
}

//展示
function showTaskDataInfo() {
    Utils.get('taskDataInfo').modal({backdrop: 'static', keyboard: false});
}

//隐藏
function hideTaskDataInfo() {
    Utils.get('taskDataInfo').modal('hide');
}