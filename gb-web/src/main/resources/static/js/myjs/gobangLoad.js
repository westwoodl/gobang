var game_mode = 1; // 1.五子棋打谱 2.围棋打谱 3. 五子棋对弈 4.围棋对弈
var over = false;
var me = true; //我
var resultTxt = document.getElementById('result-wrap');

var broad_size = 700; //****** 棋盘的大小
var line_num = 19; // *****线的数量
var bSize = broad_size / (line_num + 1);
var font_size = 13;

var chressBord = [];//棋盘
for (var i = 0; i < line_num; i++) {
    chressBord[i] = [];
    for (var j = 0; j < line_num; j++) {
        chressBord[i][j] = 0;
    }
}
var handlNum = [];

var chess = document.getElementById("chess");
chess.width = broad_size;
chess.height = broad_size;

var context = chess.getContext('2d');

context.strokeStyle = '#000000'; //边框颜色
// 图片
var img = new Image();
img.src = "../static/image/go/board.jpg";

//棋子
var b1_img = new Image();
b1_img.src = "../static/image/go/b1.png";
b1_img.width = bSize - 1;
b1_img.height = bSize - 1;

var b2_img = new Image();
b2_img.src = "../static/image/go/b2.png";
b2_img.width = bSize - 1;
b2_img.height = bSize - 1;

var w1_img = new Image();
w1_img.src = "../static/image/go/w1.png";
w1_img.width = bSize - 1;
w1_img.height = bSize - 1;

var w2_img = new Image();
w2_img.src = "../static/image/go/w2.png";
w2_img.width = bSize - 1;
w2_img.height = bSize - 1;


window.onload = function () {
    //珍珑
    // chressBord = [
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
    //     [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]
    // ];
    reflash();
    // drawChessBoard(img, line_num, broad_size, font_size); // 画棋盘
};
$(function () {

})
// 我，下棋
var end_i = -1;
var end_j = -1;
chess.onclick = function (e) {
    if (over) {
        return;
    }
    var x = e.offsetX;
    var y = e.offsetY;
    var i = Math.floor((x - bSize / 2) / bSize);
    var j = Math.floor((y - bSize / 2) / bSize);
    _nowi = i;
    _nowj = j;
    if (chressBord[i][j] === 0) {
        end_i = i;
        end_j = j;
        // chressBordAdd(i, j, !me); //我，已占位置
        if (!goStepOne(i, j, me ? 1 : 2, chressBord)) {
            return;
        }
        reflash();
        me = !me;
        playSound();
    }
};
//悬停提示
var las_move_i;
var las_move_j;
chess.onmousemove = function (e) {
    if (over) {
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
        oneStep(i, j, me, bSize, true);
    }
};


function chressBordAdd(i, j, isWhite) {
    if (isWhite) {
        chressBord[i][j] = 2;
    } else {
        chressBord[i][j] = 1;
    }
}

function reflash() {
    drawChessBoard(img, line_num, broad_size, font_size);
    for (let k = 0; k < line_num; k++) {
        for (let l = 0; l < line_num; l++) {
            if (chressBord[k][l] === 1) {
                oneStep(k, l, true, bSize)
            }
            if (chressBord[k][l] === 2) {
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