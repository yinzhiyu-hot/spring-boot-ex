$(function () {
    $('ul.sidebar-menu li').click(function () {
        var li = $('ul.sidebar-menu li.active');
        li.removeClass('active');
        $(this).addClass('active');
    });

    $('.myLeftMenu').click(function (e) {

        var url = $(this).attr('data');
        //  console.log(url);
        $('#container').load(url);
    });
});

function go(value) {
    Utils.get('rightContent').attr('src', value);
}