// 图片
var img = new Image();
img.src = "../../static/image/go/board.jpg";

var broad_size = 700; //****** 棋盘的大小
var line_num = 19; // *****线的数量
var bSize = broad_size / (line_num + 1);
var font_size = 13;

//棋子
var b1_img = new Image();
b1_img.src = "../../static/image/go/b1.png";
b1_img.width = bSize - 1;
b1_img.height = bSize - 1;

var b2_img = new Image();
b2_img.src = "../../static/image/go/b2.png";
b2_img.width = bSize - 1;
b2_img.height = bSize - 1;

var w1_img = new Image();
w1_img.src = "../../static/image/go/w1.png";
w1_img.width = bSize - 1;
w1_img.height = bSize - 1;

var w2_img = new Image();
w2_img.src = "../../static/image/go/w2.png";
w2_img.width = bSize - 1;
w2_img.height = bSize - 1;



var chressBord = [];//棋盘 19*19
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


window.onload = function () {
    drawChessBoard(img, line_num, broad_size, font_size); // 画棋盘

};

/**
 * 画棋盘
 * @param img
 * @param line_num 线的数量 如 19条线
 * @param broad_size 正方形画布边长
 */
function drawChessBoard(img, line_num, broad_size, font_size) {
    // context.drawImage(img, 0, 0, broad_size, broad_size);
    context.drawImage(img, 0, 0);
    context.strokeStyle = '#000000';
    for (var i = 0; i < line_num; i++) {
        if (i === 0 || i === line_num - 1) { //画粗线
            context.beginPath();
            context.lineWidth = 2;
        } else { //画细线
            context.beginPath();
            context.lineWidth = 1;
        }

        var bSize = broad_size / (line_num + 1);
        context.moveTo(bSize + i * bSize, bSize);
        context.lineTo(bSize + i * bSize, broad_size - bSize);
        context.stroke();
        context.moveTo(bSize, bSize + i * bSize);
        context.lineTo(broad_size - bSize, bSize + i * bSize);
        context.stroke();
        context.font = font_size + "px Arial";

        context.fillStyle = "#000000";
        if (i + 1 > 9) {
            context.fillText(i + 1, 10, bSize + 3 + i * bSize); // shu
            context.fillText(i + 1, bSize - 10 + i * bSize, 28); //heng
        } else {
            context.fillText(i + 1, bSize / 2 - 10, bSize + 3 + i * bSize);
            context.fillText(i + 1, bSize - 3 + i * bSize, 28);
        }
        context.fillText(String.fromCharCode(i + 'A'.charCodeAt()), broad_size - 30, bSize + 7 + i * bSize);
        context.fillText(String.fromCharCode(i + 'A'.charCodeAt()), bSize - 5 + i * bSize, broad_size - 17);
        if (line_num !== 19) {
            continue;
        }
        //画点
        if (i === 3 || i === 15) {
            context.beginPath();
            context.arc(bSize + i * bSize, bSize + i * bSize, bSize / 14, bSize / 5, 0, 2 * Math.PI);// 画圆
            context.arc(broad_size - bSize - i * bSize, bSize + i * bSize, bSize / 14, bSize / 5, 0, 2 * Math.PI);// 画圆
            context.closePath();
            context.fill();
        }
        if (i === 3 || i === 15 || i === 9) {
            context.beginPath();
            context.arc(bSize + i * bSize, bSize + 9 * bSize, bSize / 14, bSize / 5, 0, 2 * Math.PI);// 画圆
            context.arc(bSize + 9 * bSize, bSize + i * bSize, bSize / 14, bSize / 5, 0, 2 * Math.PI);// 画圆
            context.closePath();
            context.fill();
        }
    }
}

/**
 *
 * @param i
 * @param j
 * @param isWhite
 * @param bSize 格子大小
 * @param xuanting 是否悬停
 */
function oneStep(i, j, isWhite, bSize, xuanting) {
    if (xuanting) {
        if (!isWhite) {
            context.strokeStyle = "#000000";
        } else {
            context.strokeStyle = "#ffffff";
        }
        context.beginPath();
        context.arc(bSize + i * bSize, bSize + j * bSize, bSize / 5, bSize / 5, 0, 2 * Math.PI);// 画圆
        context.closePath();
        context.stroke();
        return;
    }
    if (!isWhite) {
        var random_img = i > 5 ? b1_img : b2_img;
        context.drawImage(random_img, bSize / 2 + i * bSize, bSize / 2 + j * bSize, random_img.height, random_img.width);
        //random_img.height,random_img.width,null,null, bSize - 0.5, bSize - 0.5);
    } else {
        var random_img = j > 8 ? w1_img : w2_img;
        context.drawImage(random_img, bSize / 2 + i * bSize, bSize / 2 + j * bSize, random_img.height, random_img.width);
    }
// 最后落子提示
    if (end_j > -1 && end_i > -1 && chressBord[end_i][end_j] !== 0 && i === end_i && j === end_j) {
        context.fillStyle = "#FF0000";
        context.beginPath();
        context.arc(bSize + i * bSize, bSize + j * bSize, 5, 0, 2 * Math.PI);
        context.fill();
        context.closePath();
    }
}

/**
 * 播放落子音
 * src 可以为任意声音来源
 */
function playSound(src, id) {
    if (src == null || src == "" || src === undefined) {
        src = "../static/media/play.wav";
    }
    if (id == null) {
        id = "play";
    }
    var borswer = window.navigator.userAgent.toLowerCase();
    if (borswer.indexOf("ie") >= 0) {
        //IE内核浏览器
        var strEmbed = '<embed id="' + id + '" name="embedPlay" src="' + src + '" autostart="true" hidden="true" loop="false"/>';
        if ($("#" + id).length === 0)
            $("body").append(strEmbed);
        var embed = document.embedPlay;

        //浏览器不支持 audion，则使用 embed 播放
        embed.volume = 100;
        //embed.play();这个不需要
    } else {
        //非IE内核浏览器
        var strAudio = "<audio id='" + id + "' src=\"" + src + "\" hidden='true'>";
        if ($("#" + id).length === 0)
            $("body").append(strAudio);
        var audio = document.getElementById(id);
        //浏览器支持 audion
        audio.play();
    }
}