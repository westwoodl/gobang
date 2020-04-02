//点击房间进入
function joinRoom(data) {
    if (!isLogin()) {
        return;
    }
    if (data == null) {
        return;
    }
    if (data.roomPassword == null || data.roomPassword === "") {
        alertLayer(data.roomName);
        gameRoomShow(data.id);
        return;
    }

    var join_room_html =
        '<input type="text" id="join_room_input" name="title" required lay-verify="required" placeholder="请输入房间密码" autocomplete="off" class="layui-input">' +
        '<span id="span"></span>';
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.open({
            title: '进入房间'
            , resize: false
            , content: join_room_html
            , yes: function (index, layero) {
                var $dom = $(layero);
                var user_name = $dom.find("#join_room_input").val();

                if (user_name.length < 1) {
                    $dom.find("#span").html("请输密码");
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                    return;
                }

                if (user_name.length > 10) {
                    $dom.find("#span").html("密码长度不会大于10");
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                    return;
                }
                if ($dom.find("#join_room_input").val() === data.roomPassword) {
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                    alertLayer(data.roomName);
                    if (data.id != null && data.id !== "") {
                        gameRoomShow(data.id);
                    }
                } else {
                    $dom.find("#span").html("密码错误");
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                }
            }
        });
    });
    return false;
}

var join_room_data_vue;
$(function () {
    join_room_data_vue = new Vue({
        el: "#chess_config_card_div",
        data: {
            isSuccess: false,
            room: null
        }
    });
});

//进入房间数据加载
function gameRoomShow(go_id) {
    displayWhat('chess_div');
    $("body").css("background", "#2B2B2B");
    // $("#chess_config_div").css("background", "#333D49");

    $.ajax({
        async: true,
        type: "GET",
        url: base_gobang_url + "/room/" + go_id,
        xhrFields: {withCredentials: true},	//前端适配：允许session跨域
        crossDomain: true,
        data: {},
        dataType: "json",
        success: function (res) {
            join_room_data_vue.isSuccess = res.success;
            join_room_data_vue.room = res.data;
            if (!res.success) {
                alertLayer(res.msg);
            }
            if (res.data == null || res.data === "") {
                alertLayer("房间不存在");
                return;
            }
        },
        error: function () {
            alert("系统繁忙");
        }
    });


}