var game_mode = 3; // 1.五子棋打谱 2.围棋打谱 3. 五子棋对弈 4.围棋对弈
// var  = false;
// var me = true; //我
// var my_role; //1黑棋  2白棋

// 最后红点标识
var end_i = -1;
var end_j = -1;
// 我，下棋
chess.onclick = function (e) {
    if (go_game_is_end || !is_me) {
        return;
    }
    if (!is_me) {
        alertLayer(go_vue.goStatus);
    }
    var x = e.offsetX;
    var y = e.offsetY;
    var i = Math.floor((x - bSize / 2) / bSize);
    var j = Math.floor((y - bSize / 2) / bSize);
    if (chressBord[i][j] === 0) {
        // 围棋落子
        if (game_mode === 4) {
            if (!goStepOne(i, j, my_role, chressBord)) {
                return;
            }
            is_me = false;
            end_i = i;
            end_j = j;
            // chressBordAdd(i, j, !me); //我，已占位置

            reflash();
            playSound();
            placeChessRequest(i, j);
            return;
        }
        // 五子棋落子
        if (game_mode === 3) {
            is_me = false;
            chressBord[i][j] = my_role; //我，已占位置
            end_i = i;
            end_j = j;
            reflash();
            playSound();
            placeChessRequest(i, j);
        }
    }
};
//悬停提示
var las_move_i;
var las_move_j;
chess.onmousemove = function (e) {
    if (go_game_is_end || !is_me) {
        return;
    }
    var x = e.offsetX;
    var y = e.offsetY;

    var i = Math.floor((x - bSize / 2) / bSize);
    var j = Math.floor((y - bSize / 2) / bSize);
    if (i < 0 || j < 0 || i >= chressBord.length || j >= chressBord.length) {
        return;
    }
    if (chressBord[i][j] === 0) {
        if (las_move_j === j && las_move_i === i) {
            return;
        }
        if (las_move_i >= 0 && las_move_j >= 0 && chressBord[las_move_i][las_move_j] === 0) {
            reflash()
        }
        las_move_i = i;
        las_move_j = j;
        oneStep(i, j, my_role === 2, bSize, true);
    }
};


function reflash() {
    drawChessBoard(img, line_num, broad_size, font_size);
    for (let k = 0; k < line_num; k++) {
        for (let l = 0; l < line_num; l++) {
            if (chressBord[k][l] === 2) {
                oneStep(k, l, true, bSize)
            }
            if (chressBord[k][l] === 1) {
                oneStep(k, l, false, bSize)
            }
        }
    }
}

chess.onmouseout = function (e) {
    if (las_move_j > 0 && las_move_j > 0) {
        reflash();
    }
};