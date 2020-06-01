var broad_size = 875; //****** 棋盘的大小
var line_num = 19; // *****线的数量
var bSize = broad_size / (line_num + 1);
var font_size = 13;

// 图片
var img = new Image();
img.src = "../../static/image/go/board1.jpg";
img.height = broad_size;
img.width = broad_size;

//棋子
var b1_img = new Image();
b1_img.src = "../../static/image/go/b1.png";
b1_img.width = bSize - 1;
b1_img.height = bSize - 1;

var b2_img = new Image();
b2_img.src = "../../static/image/go/b2.png";
b2_img.width = bSize - 1;
b2_img.height = bSize - 1;

var b3_img = new Image();
b3_img.src = "../../static/image/go/b3.png";
b3_img.width = bSize - 1;
b3_img.height = bSize - 1;

var w1_img = new Image();
w1_img.src = "../../static/image/go/w1.png";
w1_img.width = bSize - 1;
w1_img.height = bSize - 1;

var w2_img = new Image();
w2_img.src = "../../static/image/go/w2.png";
w2_img.width = bSize - 1;
w2_img.height = bSize - 1;

var w3_img = new Image();
w3_img.src = "../../static/image/go/w3.png";
w3_img.width = bSize - 1;
w3_img.height = bSize - 1;


var placeRecord = []; // x,y,value,num,dead
var show_hand_num = false;

var chessBord = [];//棋盘 19*19
initChessArry();

// 下棋
function place(i, j, one_or_two) {
    placeRecord[placeRecord.length] = {value: one_or_two, x: i, y: j, dead: false, num: placeRecord.length}
}

// 杀棋
function killPlaceRecord(arr, placeRecord) {
    for (let i = 0; i < placeRecord.length; i++) {
        if (arr[placeRecord[i].x][placeRecord[i].y] === 0) {
            placeRecord[i].dead = true;
        }
    }
}

var chess = document.getElementById("chess");
chess.width = broad_size;
chess.height = broad_size;

var context = chess.getContext('2d');
context.strokeStyle = '#000000'; //边框颜色

var getPixelRatio = function (context) {
    var backingStore = context.backingStorePixelRatio ||
        context.webkitBackingStorePixelRatio ||
        context.mozBackingStorePixelRatio ||
        context.msBackingStorePixelRatio ||
        context.oBackingStorePixelRatio ||
        context.backingStorePixelRatio || 1;
    return (window.devicePixelRatio || 1) / backingStore;
};
var ratio = getPixelRatio(context);

chess.style.width = chess.width * ratio;
chess.style.height = chess.height * ratio;

window.onload = function () {
    // drawChessBoard(img, line_num, broad_size, font_size); // 画棋盘
    reflash();
};

/**
 * 画棋盘
 * @param img
 * @param line_num 线的数量 如 19条线
 * @param broad_size 正方形画布边长
 */
function drawChessBoard(img, line_num, broad_size, font_size) {
    context.drawImage(img, 0, 0, broad_size, broad_size);
    context.strokeStyle = 'rgb(0,0,0)';
    for (var i = 0; i < line_num; i++) {
        if (i === 0 || i === line_num - 1) { //画粗线
            context.beginPath();
            context.lineWidth = 2;
        } else { //画细线
            context.beginPath();
            context.lineWidth = 1.1;
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
function oneStep(i, j, isWhite, bSize, xuanting, hand_num) {
    if (xuanting) {
        if (!isWhite) {
            context.fillStyle = 'rgba(0,0,0,0.3)';
        } else {
            context.fillStyle = 'rgba(255,255,255,0.3)';
        }
        context.beginPath();
        context.arc(bSize + i * bSize, bSize + j * bSize, bSize / 4, bSize / 4, 0, 2 * Math.PI);// 画圆
        context.fill();
        context.closePath();
        return;
    }
    context.save();
    context.shadowOffsetX = 1; // 阴影Y轴偏移
    context.shadowOffsetY = 5; // 阴影X轴偏移
    context.shadowBlur = 3; // 模糊尺寸
    context.shadowColor = 'rgba(0,0,0, 0.3)';
    var random_img;
    if (!isWhite) {
        if (i < 6) {
            random_img = b1_img;
        } else if (i > 6 && i < 13) {
            random_img = b2_img;
        } else {
            random_img = b3_img;
        }
        context.drawImage(random_img, bSize / 2 + i * bSize, bSize / 2 + j * bSize, random_img.height, random_img.width);
        //random_img.height,random_img.width,null,null, bSize - 0.5, bSize - 0.5);
    } else {
        if (j < 6) {
            random_img = w1_img;
        } else if (j > 6 && j < 13) {
            random_img = w2_img;
        } else {
            random_img = w3_img;
        }
        context.drawImage(random_img, bSize / 2 + i * bSize, bSize / 2 + j * bSize, random_img.height, random_img.width);
    }
    context.restore();

    if (show_hand_num) {
        if (end_j > -1 && end_i > -1 && chessBord[end_i][end_j] !== 0 && i === end_i && j === end_j) {
            context.fillStyle = "#FF0000";
        } else if (isWhite) {
            context.fillStyle = '#000000';
        } else {
            context.fillStyle = '#ffffff';
        }
        context.font = 20 + "px Arial";
        hand_num = hand_num + 1;
        if (hand_num < 10) {
            context.fillText(hand_num, bSize + i * bSize - 6, bSize + j * bSize + 6);
        } else if (hand_num >= 10 && hand_num < 100) {
            context.fillText(hand_num, bSize + i * bSize - 10, bSize + j * bSize + 6);
        } else if (hand_num >= 100) {
            context.fillText(hand_num, bSize + i * bSize - 15, bSize + j * bSize + 6);
        }
    }
    // 最后落子提示
    if (!show_hand_num && end_j > -1 && end_i > -1 && chessBord[end_i][end_j] !== 0 && i === end_i && j === end_j) {
        context.fillStyle = "#FF0000";
        context.beginPath();
        context.arc(bSize + i * bSize, bSize + j * bSize, 5, 0, 2 * Math.PI);
        context.fill();
        context.closePath();
    }
}


function showHandNum(obj) {
    if (!show_hand_num) {
        $(obj).html("取消手数");
        show_hand_num = true;
    } else {
        $(obj).html("显示手数");
        show_hand_num = false;
    }
    reflash();
}

function initChessArry() {
    for (let i = 0; i < line_num; i++) {
        chessBord[i] = [];
        for (let j = 0; j < line_num; j++) {
            chessBord[i][j] = 0;
        }
    }
    placeRecord = [];
}

function cleanChess() {
    initChessArry();
    reflash();
}

/**
 * 回退棋盘
 */
let chessBord2;
let placeRecord2;

function backChessToThis() {
    if (chessBord2 != null && placeRecord2 != null) {
        placeRecord = placeRecord2;
        chessBord = chessBord2;
        me_of_2 = placeRecord[placeRecord.length - 1].value === 2;
        reflash();
        chessBord2 = null;
        placeRecord2 = null;
        initSlider(placeRecord.length);
    }
}


function reflash(end) {
    drawChessBoard(img, line_num, broad_size, font_size);
    if (end == null || end === placeRecord.length) {
        for (let i = 0; i < placeRecord.length; i++) {
            if (!placeRecord[i].dead || i === end - 1)
                oneStep(placeRecord[i].x, placeRecord[i].y, placeRecord[i].value === 2, bSize, false, placeRecord[i].num);
        }
        return;
    }
    // 回退棋盘
    chessBord2 = [];
    placeRecord2 = [];
    for (let i = 0; i < chessBord.length; i++) {
        chessBord2[i] = [];
        for (let j = 0; j < chessBord.length; j++) {
            chessBord2[i][j] = 0;
        }
    }
    for (let i = 0; i < end; i++) {
        goStepOne(placeRecord[i].x, placeRecord[i].y, placeRecord[i].value, chessBord2, placeRecord2, false);
        placeRecord2[placeRecord2.length] = {
            value: placeRecord[i].value,
            x: placeRecord[i].x,
            y: placeRecord[i].y,
            dead: false,
            num: placeRecord2.length
        }
    }
    for (let i = 0; i < end; i++) {
        if (!placeRecord2[i].dead || i === end - 1)
            oneStep(placeRecord2[i].x, placeRecord2[i].y, placeRecord2[i].value === 2, bSize, false, placeRecord2[i].num);
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
    let borswer = window.navigator.userAgent.toLowerCase();
    if (borswer.indexOf("ie") >= 0) {
        //IE内核浏览器
        let strEmbed = '<embed id="' + id + '" name="embedPlay" src="' + src + '" autostart="true" hidden="true" loop="false"/>';
        if ($("#" + id).length === 0)
            $("body").append(strEmbed);
        let embed = document.embedPlay;

        //浏览器不支持 audion，则使用 embed 播放
        embed.volume = 100;
        //embed.play();这个不需要
    } else {
        // //非IE内核浏览器
        // var strAudio = "<audio id='" + id + "' src=\"" + src + "\" hidden='true'>";
        // if ($("#" + id).length === 0)
        //     $("body").append(strAudio);
        let audio = document.getElementById(id);
        audio.play();
    }
}
