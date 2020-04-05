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
            go: null
        }
    });
});

/*
    rookStart 房间开始游戏后，执行此函数
 */
function loadGoRequest(goId) {
    $.ajax({
        type: "GET",
        url: base_gobang_url + "/gobang/query",
        xhrFields: {withCredentials: true},	//前端适配：允许session跨域
        crossDomain: true,
        data: {
            goId: goId
        },
        dataType: "json",
        success: function (data) {
            if (!data.success) {
                alertLayer(data.msg);
            } else {
                loadGoDO(data.data);
                loadBroadArray(data.data);
                if (my_role === 1) {
                    $("#black_span").show();
                }
                if (my_role === 2) {
                    $("#white_span").show();
                }
                if (is_me) {
                    //请你click吧
                } else {
                    goBlockRequest(goId, new Date(data.data.modifyTime).getTime());
                }
            }
        },
        error: function () {
            alert("系统繁忙");
        }
    });

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
    alertLayer(go_vue.goStatus);
    setColor(goQueryResp.goStatus);
}

function loadBroadArray(goQueryResp) {
    var placeArrays = goQueryResp.goContext.placeArrays;
    for (let i = 0; i < placeArrays.length; i++) {
        let goPieces = placeArrays[i];
        chressBord[goPieces.x][goPieces.y] = goPieces.pieceType;
    }
    reflash();
}

function goBlockRequest(goId, expectTime) {
    if (go_game_is_end || is_me) {
        return;
    }
    $.ajax({
        type: "get",
        url: base_gobang_url + "/gobang/queryBlock",
        xhrFields: {withCredentials: true},	//前端适配：允许session跨域
        crossDomain: true,
        data: {
            goId: goId,
            expectTime: expectTime
        },
        timeout: 25000,
        dataType: "json",
        success: function (data) {
            if (data.success) {
                loadGoDO(data.data);
                loadBroadArray(data.data);
                if (is_me) {
                    //请click
                } else {
                    goBlockRequest(goId, expectTime);
                }
            } else {
                if (!is_me && !go_game_is_end) {
                    goBlockRequest(goId, expectTime);
                }
            }
        },
        complete: function (XMLHttpRequest, textStatus) {
            if (textStatus === 'timeout' && !is_me && !go_game_is_end) {
                console.log("请求对局超时，正在重试");
                goBlockRequest(goId, expectTime);
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
                let expectTIme = data.data;
                goBlockRequest(go_vue.goId, expectTIme);
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