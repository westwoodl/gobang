function recordListShow() {
    displayWhat("record_div");

    $("#record_page_tr").hover(function () {
        $("#record_page_tr").attr({"style": "background:#ffffff"});
    }, function () {
        $("#record_page_tr").attr({"style": "background:#ffffff"});
    });
    $("body").css("background", "#F8F9FA");

    useLayuiPage('record_page_div');

    console.log("record");
    return false;
}

function userDetailShow() {
    displayWhat("user_detail_div");
    $("body").css("background", "#F8F9FA");

    return false;
}

function updateUserDetail(obj) {
    var update_user_html =
        '<input type="text" id="search_user_input" name="title" required lay-verify="required" placeholder="请输入用户名" autocomplete="off" class="layui-input">' +
        '<span id="span"></span>';
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.open({
            title: '查找用户'
            , resize: false
            , content: update_user_html
            , yes: function (index, layero) {
                //do something
                console.log(index);
                var $dom = $(layero);
                // console.log($("#room_create_name_input").val());
                // console.log($("#room_create_pwd_input").val());
                var user_name = $dom.find("#search_user_input").val();

                if (user_name.length < 1) {
                    $dom.find("#span").html("请输入用户名")
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                    return;
                }

                if (user_name.length > 4) {
                    $dom.find("#span").html("用户名长度不会大于4")
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                    return;
                }
                if (queryUserRequest(user_name) === true) {
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                } else {
                    $dom.find("#span").html("系统繁忙，查询失败")
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                }
            }
        });
    });
}

function searchUser() {
    var search_user_html =
        '<input type="text" id="search_user_input" name="title" required lay-verify="required" placeholder="请输入用户名" autocomplete="off" class="layui-input">' +
        '<span id="span"></span>';
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.open({
            title: '查找用户'
            , resize: false
            , content: search_user_html
            , yes: function (index, layero) {
                //do something
                console.log(index);
                var $dom = $(layero);
                // console.log($("#room_create_name_input").val());
                // console.log($("#room_create_pwd_input").val());
                var user_name = $dom.find("#search_user_input").val();

                if (user_name.length < 1) {
                    $dom.find("#span").html("请输入用户名")
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                    return;
                }

                if (user_name.length > 4) {
                    $dom.find("#span").html("用户名长度不会大于4")
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                    return;
                }
                if (queryUserRequest(user_name) === true) {
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                } else {
                    $dom.find("#span").html("系统繁忙，查询失败")
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                }
            }
        });
    });
    return false;
}

function loginOut() {
    //todo
    return false;
}


function loginOrRegister() {
    //todo
    return false;
}

function queryUserRequest(user_name) {
    //todo
    return false;
}


function displayUserDetail() {
    return false;
}