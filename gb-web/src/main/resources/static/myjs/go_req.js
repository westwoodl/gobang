/*
    游戏开始后的数据获取

 */
var go_vue;
var go_game_is_end = false;
var is_me = false; // var user_id;
var my_role = 0; // 1.黑棋 2.白棋 0.观战者

$(function () {
    go_vue = new Vue({
        el: "#go_status_div",
        data: {
            isSuccess: false,
            goId: null,
            goStatus: null,
            go: null,
            my_role: my_role
        }
    });
});

function fetchGoData(goData) {
    loadBroadArray(goData);
    loadGoDO(goData);
}

/*
    将godo的值赋给js变量
 */
function loadGoDO(goQueryResp) {
    // 获取我的角色
    if (user_id === goQueryResp.blackUserId) {
        my_role = 1;
    } else if (user_id === goQueryResp.whiteUserId) {
        my_role = 2;
    } else {
        my_role = 0;
    }
    // 构建对局状态 是否是我下棋
    go_vue.goId = goQueryResp.id;
    go_vue.isSuccess = true;
    if (goQueryResp.goStatus === 1) {
        go_vue.goStatus = "等待黑方行棋";
        if (my_role === 1) {
            is_me = true;
            go_vue.goStatus = "等待您行棋";
        }
    }
    if (goQueryResp.goStatus === 2) {
        go_vue.goStatus = "等待白方行棋";
        if (my_role === 2) {
            is_me = true;
            go_vue.goStatus = "等待您行棋";
        }
    }
    if (goQueryResp.goStatus === 0) {
        go_vue.goStatus = "对局结束";
        go_game_is_end = true;
    }
    go_vue.goStatus = go_vue.goStatus + " 第" + (goQueryResp.goContext.placeArrays.length + 1) + "手";
    go_vue.go = goQueryResp;
    if (goQueryResp.goType == 1) {
        game_mode = 4;
    }
    if (goQueryResp.goType == 2) {
        game_mode = 3;
    }
    alertLayer(go_vue.goStatus);
    setColor(goQueryResp.goStatus);
}

function loadBroadArray(goQueryResp) {
    var placeArrays = goQueryResp.goContext.placeArrays;
    if (placeArrays.length - 1 === placeRecord.length) {
        let goPieces = placeArrays[placeArrays.length - 1];
        placeOneChess(goPieces.x, goPieces.y, goPieces.pieceType);
    } else {
        initChessArry();
        for (let i = 0; i < placeArrays.length; i++) {
            let goPieces = placeArrays[i];
            // 死亡棋子
            if (goPieces.dead != null && goPieces.dead === true) {
                chessBord[goPieces.x][goPieces.y] = 0;
                continue;
            }
            chessBord[goPieces.x][goPieces.y] = goPieces.pieceType;
            if (i === placeArrays.length - 1) {
                end_i = goPieces.x;
                end_j = goPieces.y;
            }
            placeRecord[placeRecord.length] = {value: goPieces.pieceType, x: goPieces.x, y: goPieces.y, dead: goPieces.dead, num: placeRecord.length}
        }
    }

    reflash();
}

/**
 * 弃子投降
 */
function giveUp() {
    $.ajax({
        type: "PUT",
        url: base_gobang_url + "/gobang/defeat",
        xhrFields: {withCredentials: true},	//前端适配：允许session跨域
        crossDomain: true,
        data: {
            id: go_vue.goId
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {

            } else {
                alertLayer(data.msg);
            }
        },
        error: function () {
            alert("系统繁忙");
        }
    });

}

function placeChessRequest(x, y) {
    $.ajax({
        async: true,
        type: "PUT",
        url: base_gobang_url + "/gobang/place",
        xhrFields: {withCredentials: true},	//前端适配：允许session跨域
        crossDomain: true,
        data: {
            id: go_vue.goId,
            x: x,
            y: y
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                setColor();
                if (data.msg !== '落子成功') {
                    alertLayer(data.msg);
                    go_game_is_end = true;
                } else {
                    go_vue.goStatus = data.msg + " 等待对方行棋";
                    is_me = false;
                }
            } else {
                alertLayer(data.msg);
            }
        },
        error: function () {
            alert("系统繁忙");
        }
    });

}

// 1.黑棋 2.白棋 0.观战者
function setColor(status) {
    if (status == null) {
        if (my_role === 1) {
            $("#white_div").css({"background": "rgb(78,84,101)", "color": "rgb(255,239,209)"});
            $("#black_div").css({"background": "", "color": ""});
            return;
        }
        if (my_role === 2) {
            $("#black_div").css({"background": "rgb(78,84,101)", "color": "rgb(255,239,209)"});
            $("#white_div").css({"background": "", "color": ""});
            return;
        }
        return;
    }
    if (status === 1) {
        $("#black_div").css({"background": "rgb(78,84,101)", "color": "rgb(255,239,209)"});
        $("#white_div").css({"background": "", "color": ""});
        return;
    }
    if (status === 2) {
        $("#white_div").css({"background": "rgb(78,84,101)", "color": "rgb(255,239,209)"});
        $("#black_div").css({"background": "", "color": ""});
        return;
    }
    if (status === 0) {
        $("#black_div").css({"background": "", "color": ""});
        $("#white_div").css({"background": "", "color": ""});
    }
}