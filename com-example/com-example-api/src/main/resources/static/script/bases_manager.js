$(function ($) {
    initTable();
    queryTable();
    Utils.get("queryBtn").bind("click", queryTable);
    Utils.get("addBtn").bind("click", add);

    //清空按钮
    Utils.get('cancelAddBtn').click(function () {
        Utils.get("bizTypeAdd").val("");
        Utils.get("bizKeyAdd").val("");
        Utils.get("bizValueAdd").val("");
        Utils.get("remarkAdd").val("");
        Utils.get('addForm').data('bootstrapValidator').resetForm(true);
        hideAddBaseConfig();
    });

    //基本信息-异步提交表单
    Utils.get('submitAddBtn').click(function () {
        Utils.get('addForm').bootstrapValidator('validate');

        if (!Utils.get('addForm').data("bootstrapValidator").isValid()) {
            return;
        }

        //组装数据
        var data = {
            "bizType": Utils.get("bizTypeAdd").val(),
            "bizKey": Utils.get("bizKeyAdd").val(),
            "bizValue": Utils.get("bizValueAdd").val(),
            "remark": Utils.get("remarkAdd").val()
        };
        submitAdd(data);
    });

    Utils.get('addForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {

            bizTypeAdd: {
                validators: {
                    notEmpty: {
                        message: '业务类型不能为空'
                    }
                }
            },
            bizKeyAdd: {
                validators: {
                    notEmpty: {
                        message: '业务配置键不能为空'
                    }
                }
            },
            bizValueAdd: {
                validators: {
                    notEmpty: {
                        message: '业务配置值不能为空'
                    }
                }
            }
        }
    });

    //清空按钮
    Utils.get('cancelUpdateBtn').click(function () {
        Utils.get("id").val("");
        Utils.get("bizTypeUpdate").val("");
        Utils.get("bizKeyUpdate").val("");
        Utils.get("bizValueUpdate").val("");
        Utils.get("remarkUpdate").val("");
        Utils.get('updateForm').data('bootstrapValidator').resetForm(true);
        hideUpdateBaseConfig();
    });

    //基本信息-异步提交表单
    Utils.get('submitUpdateBtn').click(function () {
        Utils.get('updateForm').bootstrapValidator('validate');

        if (!Utils.get('updateForm').data("bootstrapValidator").isValid()) {
            return;
        }

        //组装数据
        var data = {
            "id": Utils.get("id").val(),
            "bizType": Utils.get("bizTypeUpdate").val(),
            "bizKey": Utils.get("bizKeyUpdate").val(),
            "bizValue": Utils.get("bizValueUpdate").val(),
            "remark": Utils.get("remarkUpdate").val()
        };
        submitUpdate(data);
    });

    Utils.get('updateForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {

            bizTypeUpdate: {
                validators: {
                    notEmpty: {
                        message: '业务类型不能为空'
                    }
                }
            },
            bizKeyUpdate: {
                validators: {
                    notEmpty: {
                        message: '业务配置键不能为空'
                    }
                }
            },
            bizValueUpdate: {
                validators: {
                    notEmpty: {
                        message: '业务配置值不能为空'
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
                field: 'bizType',
                title: '业务类型',
                align: "left",
                width: 200,
                formatter: paramsMatter
            }, {
                field: 'bizKey',
                title: '业务配置键',
                align: "left",
                width: 250,
                formatter: paramsMatter
            }, {
                field: 'bizValue',
                title: '业务配置值',
                align: "left",
                formatter: paramsMatter
            }, {
                field: 'remark',
                title: '备注',
                align: "left",
                formatter: paramsMatter
            }, {
                field: 'id',
                title: '操作',
                align: "center",
                width: 140,
                formatter: operateFormatter
            }],
        method: 'get',
        contentType: "application/x-www-form-urlencoded",//必须要有！因为bootstap table使用的是ajax方式获取数据，这时会将请求的content type默认设置为 text/plain，这样在服务端直接通过 @RequestParam参数映射是获取不到的。
        url: "/com-example/bases/listPage",//要请求数据的文件路径,加时间戳，防止读取缓存数据
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
            bizType: Utils.get("bizType").val(),
            bizKey: Utils.get("bizKey").val()
        };
    }
}

function operateFormatter(value, row, index) {
    return [
        "<div class=’form-inline‘>",
        "<button type='button' class='btn btn-primary' onclick='update(" + JSON.stringify(row) + ")'>编辑</button>",
        "<button style='visibility: hidden'/>",
        "<button type='button' class='btn btn-primary' onclick='del(" + value + ")'>删除</button>",
        "</div>"
    ].join('');
}

function del(value) {
    var d = dialog({
        title: '消息',
        width: 200,
        content: '确定删除吗？',
        ok: function () {
            $.ajax({
                //请求方式
                type: "POST",
                //请求的媒体类型
                contentType: "application/json;charset=UTF-8",
                //请求地址
                url: "/com-example/bases/delete",
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

function add() {
    showAddBaseConfig();
}

function submitAdd(value) {
    var d = dialog({
        title: '消息',
        width: 200,
        zIndex: 999999999,
        content: '确定提交新增吗？',
        ok: function () {
            $.ajax({
                //请求方式
                type: "POST",
                //请求的媒体类型
                contentType: "application/json;charset=UTF-8",
                //请求地址
                url: "/com-example/bases/add",
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
                                hideAddBaseConfig();
                                queryTable();
                                Utils.get('addForm').data('bootstrapValidator').resetForm(true);
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

//展示
function showAddBaseConfig() {
    Utils.get('addBaseConfig').modal({backdrop: 'static', keyboard: false});
}

//隐藏
function hideAddBaseConfig() {
    Utils.get('addBaseConfig').modal('hide');
}

function update(row) {
    Utils.get("id").val(row.id);
    Utils.get("bizTypeUpdate").val(row.bizType);
    Utils.get("bizKeyUpdate").val(row.bizKey);
    Utils.get("bizValueUpdate").val(row.bizValue);
    Utils.get("remarkUpdate").val(row.remark);
    showUpdateBaseConfig();
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
                url: "/com-example/bases/update",
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
                                hideUpdateBaseConfig();
                                queryTable();
                                Utils.get('updateForm').data('bootstrapValidator').resetForm(true);
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

//展示
function showUpdateBaseConfig() {
    Utils.get('updateBaseConfig').modal({backdrop: 'static', keyboard: false});
}

//隐藏
function hideUpdateBaseConfig() {
    Utils.get('updateBaseConfig').modal('hide');
}

