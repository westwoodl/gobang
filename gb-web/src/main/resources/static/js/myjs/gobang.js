/**
 * 画棋盘
 * @param img
 * @param line_num 线的数量 如 19条线
 * @param broad_size 正方形画布边长
 */
function drawChessBoard(img, line_num, broad_size, font_size) {
    context.drawImage(img,0,0,broad_size,broad_size);
    for(var i = 0; i < line_num ; i++){
        if (i===0|| i === line_num - 1) { //画粗线
            context.beginPath();
            context.lineWidth = 2.5;
        } else { //画细线
            context.beginPath();
            context.lineWidth = 1;
        }

        var bSize = broad_size/(line_num + 1);
        context.moveTo(bSize + i * bSize , bSize);
        context.lineTo(bSize + i * bSize , broad_size - bSize);
        context.stroke();
        context.moveTo(bSize , bSize + i * bSize);
        context.lineTo(broad_size - bSize , bSize + i * bSize);
        context.stroke();
        context.font=font_size + "px Arial";

        if (i+1>9) {
            context.fillText(i + 1, 10, bSize + 3 + i * bSize); // shu
            context.fillText(i + 1, bSize - 10 + i * bSize, 28); //heng
        } else {
            context.fillText(i + 1, bSize/2 - 10, bSize + 3 + i * bSize);
            context.fillText(i + 1, bSize - 3 + i * bSize, 28);
        }
        context.fillText( String.fromCharCode(i + 'A'.charCodeAt()), broad_size - 30, bSize + 7 + i * bSize);
        context.fillText( String.fromCharCode(i + 'A'.charCodeAt()), bSize - 5 + i * bSize, broad_size - 17);
        if (line_num!== 19) {
            continue;
        }
        //画点
        if (i === 3||i===15) {
            context.beginPath();
            context.arc(bSize + i * bSize, bSize + i * bSize, bSize/14, bSize/5, 0, 2 * Math.PI);// 画圆
            context.arc(broad_size - bSize - i * bSize, bSize + i * bSize, bSize/14, bSize/5, 0, 2 * Math.PI);// 画圆
            context.closePath();
            context.fill();
        }
        if (i===3||i===15) {
            context.beginPath();
            context.arc(bSize + i * bSize, bSize + 9 * bSize, bSize/14, bSize/5, 0, 2 * Math.PI);// 画圆
            context.arc(bSize + 9 * bSize, bSize + i * bSize, bSize/14, bSize/5, 0, 2 * Math.PI);// 画圆
            context.closePath();
            context.fill();
        }
    }
}
/**
 *
 * @param i
 * @param j
 * @param me
 * @param bSize 格子大小
 */
function oneStep(i,j,me,bSize,xuanting) {
    if(xuanting) {
        context.beginPath();
        context.arc(bSize + i * bSize, bSize + j * bSize, bSize/5, bSize/5, 0, 2 * Math.PI);// 画圆
        context.closePath();
        context.stroke();
        return;
    }
    if(me){
        var random_img = i > 5 ? b1_img : b2_img;
        context.drawImage(random_img,bSize/2 + i * bSize, bSize/2 + j * bSize, bSize-0.5, bSize-0.5);
    }else{
        var random_img = j > 8 ? w1_img : w2_img;
        context.drawImage(random_img,bSize/2 + i * bSize, bSize/2 + j * bSize, bSize-0.5, bSize-0.5);
    }

}


