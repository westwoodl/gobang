/*
    进入房间后-游戏开始前的的数据获取

 */

// 游戏是否开始标识
var is_game_start_for_room = false;
// 用户是否进入房间标识
var is_user_join_room = false;
// 房间数据呈现
var join_room_data_vue;

// vue数据初始化
$(function () {
    join_room_data_vue = new Vue({
        el: "#chess_config_card_div",
        data: {
            isSuccess: false,
            room: null
        }
    });
});


//房间进入事件
function joinRoom() {
    let url = document.location.toString();
    let arrUrl = url.split("?");
    let roomId;
    if (arrUrl.length > 1 && arrUrl[1] != null && arrUrl[1].split("=").length > 1
        && arrUrl[1].split("=")[0] === "roomId"
        && arrUrl[1].split("=")[1] !== ''
        && arrUrl[1].split("=")[1] != null) {
        roomId = arrUrl[1].split("=")[1];
    } else {
        alertLayer("房间不存在");
        window.location.href="/";
        return;
    }
    joinRoomRequest(roomId);
    return false;
}


/*
    先加入房间 -> 首次加载数据 -> 持续更新房间数据
 */

// 加入房间
function joinRoomRequest(roomId) {
    $.ajax({
        type: "PUT",
        url: base_gobang_url + "/room/join",
        xhrFields: {withCredentials: true},	//前端适配：允许session跨域
        crossDomain: true,
        data: {
            roomId: roomId,
        },
        dataType: "json",
        success: function (data) {
            alertLayer(data.msg);
            if (data.success) {
                firstLoadRoomRequest(roomId, data.data);
            } else {
                window.location.href="/";
            }
        },
        error: function () {
            alert("系统繁忙");
        }
    });
}

// 进入房间加载数据 加载成功后 循环获取数据
function firstLoadRoomRequest(roomId, expectTime) {
    displayWhat('chess_div');
    $("body").css("background", "#2B2B2B");
    $("#chess_config_card_div").show();
    // $("#chess_config_div").css("background", "#333D49");

    $.ajax({
        type: "GET",
        url: base_gobang_url + "/room/" + roomId,
        xhrFields: {withCredentials: true},	//前端适配：允许session跨域
        crossDomain: true,
        data: {},
        dataType: "json",
        success: function (res) {
            join_room_data_vue.isSuccess = res.success;
            join_room_data_vue.room = res.data;
            if (!res.success) {
                alertLayer(res.msg);
            } else {
                is_user_join_room = true;
                queryRoomBlockRequest(roomId, expectTime);
            }
        },
        error: function () {
            alert("系统繁忙");
        }
    });

}


// 查询数据 20s超时
function queryRoomBlockRequest(roomId, room_expect_time) {
    $.ajax({
        type: "get",
        url: base_gobang_url + "/room/queryBlock",
        xhrFields: {withCredentials: true},	//前端适配：允许session跨域
        crossDomain: true,
        data: {
            roomId: roomId,
            expectTime: room_expect_time
        },
        timeout: 25000,
        dataType: "json",
        success: function (data) {
            if (data.success) {
                join_room_data_vue.isSuccess = data.success;
                join_room_data_vue.room = data.data;
                if (!is_game_start_for_room && is_user_join_room) {
                    console.log("游戏未开始：" + roomId + ";" + new Date(join_room_data_vue.room.modifyTime).getTime());
                    queryRoomBlockRequest(roomId, new Date(join_room_data_vue.room.modifyTime).getTime());
                }
            } else {
                if (!is_game_start_for_room && is_user_join_room) {
                    queryRoomBlockRequest(roomId, room_expect_time);
                }
            }
        },
        complete: function (XMLHttpRequest, textStatus) {
            if (textStatus === 'timeout' && !is_game_start_for_room && is_user_join_room) {
                console.log("请求房间超时，正在重试");
                queryRoomBlockRequest(roomId, room_expect_time);
            }
        },
        error: function () {
            alert("系统繁忙");
        }
    });
}

function startGameRequest() {

    $.ajax({
        async: false,
        type: "GET",
        url: base_gobang_url + "/user/friend",
        xhrFields: {withCredentials: true},	//前端适配：允许session跨域
        crossDomain: true,
        data: {},
        dataType: "json",
        success: function (data) {
            user_my_friend_vue.isSuccess = data.success;
            user_my_friend_vue.dataList = data.data;
            if (data.data.length < 1) {
                alertLayer("您还没有好友哦");
            }
            if (!data.success) {
                alertLayer(data.msg);
            } else {
                layui.use('element', function () {
                    var element = layui.element;

                    //…
                });
            }
        },
        error: function () {
            alert("系统繁忙");
        }
    });


}
