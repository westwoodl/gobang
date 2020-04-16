/*
    type: 1.b  2.w
 */
function goStepOne(i, j, type, arr, placeRecord, isPlay) {
    if (isPlay == null) {
        isPlay = true;
    }
    if (isOut(i, j, arr)) {
        return false;
    }
    if (arr[i][j]!==0) {
        return false;
    }

    arr[i][j] = type;
    if(killOpp(i, j, arr, isPlay)) {
        killPlaceRecord(arr, placeRecord);
        return true;
    }
    if (countQi(i, j, arr) === 0) {
        alertLayer("这里没气哦");
        arr[i][j] = 0;
        return false;
    }
    return true;

}

/**
 * 让身边的气为零的敌棋变为死棋，变不了返回false
 *能否杀死对手
 */
function killOpp(x, y, arr, isPlay) {
    // 可能会导致多边杀
    let isKill = false;
    let curType = arr[x][y];
    if (isOpp(x - 1, y, arr, curType)) {
        if (!haveQi(x - 1, y, arr)) {
            killNow(x - 1, y, arr, arr[x-1][y]);
            isKill = true;
        }
    }
    if (isOpp(x + 1, y, arr, curType)) {
        if (!haveQi(x + 1, y, arr)) {
            killNow(x + 1, y, arr, arr[x+1][y]);
            isKill = true;
        }
    }
    if (isOpp(x, y - 1, arr, curType)) {
        if (!haveQi(x, y - 1, arr)) {
            killNow(x, y - 1, arr, arr[x][y-1]);
            isKill = true;
        }
    }
    if (isOpp(x, y + 1, arr, curType)) {
        if (!haveQi(x, y + 1, arr)) {
            killNow(x, y + 1, arr, arr[x][y+1]);
            isKill = true;
        }
    }
    if (isKill && isPlay ) {
        if (deadNum > 4) {
            playSound("../static/media/remove0.wav", "remove0");
        } else if(deadNum > 3) {
            playSound("../static/media/remove0.wav", "remove4");
        } else if (deadNum > 1) {
            playSound("../static/media/remove2.wav", "remove2");
        } else{
            playSound("../static/media/remove1.wav", "remove1");
        }
    }
    deadNum = 0;
    return isKill;
}

/**
 * i和k为死棋，置为零
 */
var deadNum = 0;
function killNow(i, j, arr, curType) {
    if (isOut(i, j, arr)) {
        return;
    }
    if (arr[i][j] === 0) {
        return;
    }
    if (arr[i][j] !== curType) {
        return;
    }
    if (arr[i][j] === curType) {
        arr[i][j] = 0;
        deadNum++;
        killNow(i - 1, j, arr, curType);
        killNow(i + 1, j, arr, curType);
        killNow(i, j - 1, arr, curType);
        killNow(i, j + 1, arr, curType);
    }
}

function haveQi(i, j, arr) {
    let count = have_qi(i, j, arr[i][j], arr);
    right_chess_broad(arr);
    return count;
}

function have_qi(i, j, type, arrs) {
    if (isOut(i, j, arrs)) {
        return false;
    }
    if (arrs[i][j] === 0) {
        return true;
    }
    if (arrs[i][j] !== type) {
        return false;
    } else {
        arrs[i][j] = -type;
        return have_qi(i - 1, j, type, arrs)
            || have_qi(i + 1, j, type, arrs)
            || have_qi(i, j - 1, type, arrs)
            || have_qi(i, j + 1, type, arrs);
    }
}

/**
 * 计算当前 i, j的气
 */
function countQi(i, j, arr) {
    let count = count_qi(i, j, arr[i][j], arr);
    right_chess_broad(arr);
    return count;
}


// 是否是对手
function isOpp(i, j, arr, type) {
    if (isNotOut(i, j, arr)) {
        return arr[i][j] !== 0 && arr[i][j] !== type;
    }
    return false;
}

// 是否越界
function isNotOut(i, j, arr) {
    return i >= 0 && j >= 0 && i <= arr.length - 1 && j <= arr.length - 1;
}

function isOut(i, j, arr) {
    return !isNotOut(i, j, arr);
}

// type 1. black 2. white
function count_qi(i, j, type, arrs) {
    if (i < 0 || j < 0 || i > arrs.length - 1 || j > arrs.length - 1) {
        return 0;
    }
    if (arrs[i][j] === 0) {
        arrs[i][j] = 3;
        return 1;
    }
    if (arrs[i][j] !== type) {
        return 0;
    } else {
        arrs[i][j] = -type;
        return count_qi(i - 1, j, type, arrs)
            + count_qi(i + 1, j, type, arrs)
            + count_qi(i, j - 1, type, arrs)
            + count_qi(i, j + 1, type, arrs);
    }
}


function right_chess_broad(error_arr) {
    for (let i = 0; i < error_arr.length; i++) {
        for (let j = 0; j < error_arr[0].length; j++) {
            if (error_arr[i][j] === 0) {
                continue;
            }
            if (error_arr[i][j] < 0) {
                error_arr[i][j] = -error_arr[i][j]
                continue;
            }
            if (error_arr[i][j] === 3) {
                error_arr[i][j] = 0;
            }
        }
    }
}

function mathAbs(val) {
    return val < 0 ? -val : val;
}