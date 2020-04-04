/*
    进入房间后-游戏开始前的的数据获取

 */
var user_id;
// 游戏是否开始标识
var is_game_start_for_room = false;

var is_room_end = false;
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

    $.ajax({
        async: true,
        type: "GET",
        url: base_gobang_url + "/user/load",
        xhrFields: {withCredentials: true},	//前端适配：允许session跨域
        crossDomain: true,
        data: {},
        dataType: "json",
        success: function (data) {
            if (data.success) {
                user_id = data.data.id;
                joinRoom();
            } else {
                alertLayer(data.msg);
                locationLeave();

            }
        },
        error: function () {
            alert("系统繁忙");
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
        locationLeave();
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
                locationLeave();
            }
        },
        error: function () {
            alert("系统繁忙");
        }
    });
}

// 进入房间加载数据 加载成功后 循环获取数据
function firstLoadRoomRequest(roomId, expectTime) {
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


// 查询数据 25s超时
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
                if (!is_game_start_for_room && is_user_join_room && !is_room_end) {
                    console.log("游戏未开始：" + roomId + ";" + new Date(join_room_data_vue.room.modifyTime).getTime());
                    queryRoomBlockRequest(roomId, new Date(join_room_data_vue.room.modifyTime).getTime());
                }
            } else {
                if (data.msg === '对局不存在') {
                    alertLayer(data.msg);
                }
                if (!is_game_start_for_room && is_user_join_room && !is_room_end) {
                    queryRoomBlockRequest(roomId, room_expect_time);
                }
            }
        },
        complete: function (XMLHttpRequest, textStatus) {
            if (textStatus === 'timeout' && !is_game_start_for_room && is_user_join_room && !is_room_end) {
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
        async: true,
        type: "PUT",
        url: base_gobang_url + "/room/start",
        xhrFields: {withCredentials: true},	//前端适配：允许session跨域
        crossDomain: true,
        data: {
            roomId: join_room_data_vue.room.id
        },
        dataType: "json",
        success: function (data) {
            if (!data.success) {
                alertLayer(data.msg);
            } else {
                is_game_start_for_room = true;
                alertLayer("游戏开始");
                loadGoRequest(data.data);
            }
        },
        error: function () {
            alert("系统繁忙");
        }
    });
}

function leaveRoomRequest() {
    if (join_room_data_vue.room.createUser == user_id) {
        layer.msg('您是房主，离开后房间就找不到了哦', {
            time: 20000, //20s后自动关闭
            btn: ['明白了', '取消']
        }, function () {
            $.ajax({
                async: true,
                type: "PUT",
                url: base_gobang_url + "/room/leave",
                xhrFields: {withCredentials: true},	//前端适配：允许session跨域
                crossDomain: true,
                data: {
                    roomId: join_room_data_vue.room.id
                },
                dataType: "json",
                success: function (data) {
                    alertLayer(data.msg);
                    if (data.success) {
                        locationLeave();
                        is_room_end = true;
                    } else {
                        alertLayer(data.msg);
                    }
                },
                error: function () {
                    alert("系统繁忙");
                }
            });
        }, function () {
        });
    }
}
