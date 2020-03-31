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
    drawChessBoard(img, line_num, broad_size, font_size); // 画棋盘
};
// 我，下棋
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
        oneStep(i, j, me, bSize);
        chressBord[i][j] = me ? 1 : 2; //我，已占位置
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
        if (las_move_j ===j && las_move_i === i) {
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