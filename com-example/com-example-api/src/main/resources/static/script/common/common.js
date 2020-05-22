var Utils = {
    get: function (id) {
        return $('#' + id);
    }
};

function paramsMatter(value, row, index) {
    if (value == null || value === '') {
        return "";
    }
    var span = document.createElement('span');
    span.setAttribute('title', value);
    span.innerHTML = value;
    return span.outerHTML;
}

function dataFormatter(value, row, index) {
    if (value == null || value === '') {
        return "";
    }
    var dt = new Date(value);
    return dateFormat("YYYY-mm-dd HH:MM:SS", dt);
}

function dateFormat(fmt, date) {
    var ret;
    var opt = {
        "Y+": date.getFullYear().toString(),        // 年
        "m+": (date.getMonth() + 1).toString(),     // 月
        "d+": date.getDate().toString(),            // 日
        "H+": date.getHours().toString(),           // 时
        "M+": date.getMinutes().toString(),         // 分
        "S+": date.getSeconds().toString()          // 秒
        // 有其他格式化字符需求可以继续添加，必须转化成字符串
    };
    for (var k in opt) {
        ret = new RegExp("(" + k + ")").exec(fmt);
        if (ret) {
            fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
        }
    }
    return fmt;
}