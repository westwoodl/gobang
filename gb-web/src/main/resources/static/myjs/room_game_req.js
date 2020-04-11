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

var init_room_id;
// vue数据初始化
$(function () {
    join_room_data_vue = new Vue({
        el: "#chess_config_card_div",
        data: {
            isSuccess: false,
            room: null,
            currentUserId:null
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
                join_room_data_vue.currentUserId = user_id;
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
    connectWebSocket();
    return false;
}

/*
    先加入房间 -> 首次加载数据 -> 持续更新房间数据
 */
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
                init_room_id = roomId;
            } else {
                locationLeave();
            }
        },
        error: function () {
            alert("系统繁忙");
        }
    });
}

/*
    web socket 请求获取房间数据
 */
var gobang_socket;

function connectWebSocket() {
    if (typeof (WebSocket) == "undefined") {
        alertLayer("您的浏览器不支持，建议使用谷歌浏览器");
        return;
    }
    let socketUrl = base_gobang_url + "/socket/go/" + user_id;
    socketUrl = socketUrl.replace("https", "ws").replace("http", "ws");
    gobang_socket = new WebSocket(socketUrl);
    //打开事件
    gobang_socket.onopen = function () {
        is_user_join_room = true;
        alertLayer("进入连接");
    };
    //获得消息事件
    gobang_socket.onmessage = function (msg) {
        console.log("来消息了：" + msg.data.roomDO + msg.data.goQueryResp);
        let data = JSON.parse(msg.data);
        if (data.success) {
            if (data.data.roomDO == null) {
                fetchGoData(data.data.goQueryResp);
                return;
            }
            fetchRoomData(data.data.roomDO);

            if (data.data.roomDO.roomStatus === 2) {
                if (!is_game_start_for_room) {
                    is_game_start_for_room = true;
                    alertLayer("开始游戏");
                }
                fetchGoData(data.data.goQueryResp);
            }
            if (data.data.roomDO.roomStatus === 3) {
                is_room_end = true;
            }
        } else {
            alertLayer(data.msg);
        }
    };
    //关闭事件
    gobang_socket.onclose = function () {
        alertLayer("会话已关闭");
        // leaveRoomRequest();
    };

    //发生了错误事件
    gobang_socket.onerror = function () {
        alertLayer("websocket发生了错误");
    }
}

function fetchRoomData(roomData) {
    join_room_data_vue.isSuccess = true;
    join_room_data_vue.room = roomData;
}

function startGameRequest(gameMode) {
    if (gameMode == null) {
        gameMode = 2;
    }
    $.ajax({
        async: true,
        type: "PUT",
        url: base_gobang_url + "/room/start",
        xhrFields: {withCredentials: true},	//前端适配：允许session跨域
        crossDomain: true,
        data: {
            roomId: join_room_data_vue.room.id,
            gameMode: gameMode
        },
        dataType: "json",
        success: function (data) {
            if (!data.success) {
                alertLayer(data.msg);
            } else {
                is_game_start_for_room = true;
                alertLayer("游戏开始");
            }
        },
        error: function () {
            alert("系统繁忙");
        }
    });
}


function leaveRoomRequest() {
    if (join_room_data_vue.room.createUser == user_id) {

        layer.confirm('您是房主，离开后房间就找不到了哦', {
            btn: ['确定', '取消'] //按钮
        }, function () {
            leaveRoomRequestFun();
        }, function () {
        });
    } else {
        leaveRoomRequestFun();
    }
}

function leaveRoomRequestFun() {
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
                gobang_socket.close();
                locationLeave();
                is_room_end = true;
            } else {
                alertLayer(data.msg);
                if (data.msg == "房间不存在") {
                    locationLeave();
                }
            }
        },
        error: function () {
            alert("系统繁忙");
        }
    });
}
