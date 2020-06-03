$(function ($) {
    initTable();
    Utils.get("queryBtn").bind("click", queryTable);
});

function queryTable() {
    Utils.get("table").bootstrapTable("removeAll");
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
                field: 'message',
                title: 'Message',
                align: "left",
                formatter: paramsMatter
            }, {
                field: 'recordDate',
                title: '日志时间',
                width: 160,
                align: "center",
                formatter: dataFormatter
            }],
        method: 'get',
        contentType: "application/x-www-form-urlencoded",//必须要有！因为bootstap table使用的是ajax方式获取数据，这时会将请求的content type默认设置为 text/plain，这样在服务端直接通过 @RequestParam参数映射是获取不到的。
        url: "/com-example/logs/listPage",//要请求数据的文件路径,加时间戳，防止读取缓存数据
        pagination: true,//是否开启分页（*）启动分页，必须设为true
        queryParamsType: '',//注意:查询参数组织方式，默认值为 'limit',在默认情况下 传给服务端的参数为：offset,limit,sort 。 设置为 '' 在这种情况下传给服务器的参数为：pageSize,pageNumber
        queryParams: queryParams,//请求服务器时所传的参数
        pageNumber: 1,//初始化加载第一页，默认第一页
        pageSize: 10,//每页的记录行数（*）
        pageList: [10, 15, 20, 25, 30],//可供选择的每页的行数（*）
        sidePagination: "client" //分页方式：client客户端分页，server服务端分页（*）
    });

    //得到查询的参数
    function queryParams(params) {
        return {  //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.pageSize,  //页面大小
            pageNumber: params.pageNumber, //页码
            hashKey: Utils.get("hashKey").val(),
            queryDate: Utils.get("queryDate").val()
        };
    }
}
