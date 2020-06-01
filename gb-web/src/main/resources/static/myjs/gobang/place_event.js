var game_mode = 2; // 1.五子棋打谱 2.围棋打谱 3. 五子棋对弈 4.围棋对弈
var me_of_2 = true;

// 最后红点标识
var end_i = -1;
var end_j = -1;

/**
 * 在棋盘上下棋
 * role 1黑棋 2白棋
 */
function placeOneChess(i, j, role) {
    if (i > -1 && j > -1 && i < chessBord.length && j < chessBord.length && chessBord[i][j] === 0) {
        if (game_mode === 1) {
            // todo
            return;
        }
        //围棋打谱
        if (game_mode === 2) {
            if (!goStepOne(i, j, me_of_2 ? 1 : 2, chessBord, placeRecord)) {
                return;
            }
            place(i, j, me_of_2 ? 1 : 2);
            me_of_2 = !me_of_2;
            end_i = i;
            end_j = j;
            reflash();
            initSlider(placeRecord.length);
            playSound();
            return;
        }
        // 围棋落子
        if (game_mode === 4) {
            if (role === my_role) {
                is_me = false;
            }
            if (!goStepOne(i, j, role, chessBord, placeRecord)) {
                if (role === my_role) {
                    is_me = true;
                }
                return;
            }
        }
        // 五子棋落子
        if (game_mode === 3) {
            if (role === my_role) {
                is_me = false;
            }
            chessBord[i][j] = role; //我，已占位置
        }
        placeRecord[placeRecord.length] = {value: role, x: i, y: j, dead: false, num: placeRecord.length}
        end_i = i;
        end_j = j;
        reflash();
        playSound();
        return true;
    }
}
// 我，下棋
$(function () {
    chess.onclick = function (e) {
        let i = getArrIndex(e.offsetX);
        let j = getArrIndex(e.offsetY);

        if (game_mode === 3 || game_mode === 4) {
            if (go_game_is_end || !is_me) {
                // alertLayer(go_vue.goStatus);
                return;
            }
            if(placeOneChess(i, j, my_role)) {
                placeChessRequest(i, j);
                return;
            }
        }
        placeOneChess(i, j);
    };
//悬停提示，防止ij不动时re flash
    var las_move_i = -1;
    var las_move_j = -1;
    chess.onmousemove = function (e) {
        if (game_mode === 3 || game_mode === 4) {
            if (go_game_is_end === undefined || go_game_is_end || !is_me) {
                return;
            }
        }

        let i = getArrIndex(e.offsetX);
        let j = getArrIndex(e.offsetY);
        if (i < 0 || j < 0 || i >= chessBord.length || j >= chessBord.length) {
            return;
        }
        if (chessBord[i][j] === 0) {
            if (las_move_j === j && las_move_i === i) {
                return;
            }
            if (las_move_i >= 0 && las_move_j >= 0 && chessBord[las_move_i][las_move_j] === 0) {
                reflash()
            }
            las_move_i = i;
            las_move_j = j;
            if (game_mode === 3 || game_mode === 4) {
                oneStep(i, j, my_role === 2, bSize, true);
            } else {
                oneStep(i, j, !me_of_2, bSize, true);
            }
        }
    };

    function getArrIndex(offset) {
        offset = offset * ratio;
        return Math.floor((offset - bSize / 2) / bSize);
    }

    chess.onmouseout = function (e) {
        if (las_move_j > -1 || las_move_i > -1) {
            las_move_j = -1;
            las_move_i = -1;
            reflash();
        }
    };
});
