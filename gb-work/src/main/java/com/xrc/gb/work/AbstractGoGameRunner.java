package com.xrc.gb.work;

import com.xrc.gb.common.dto.go.GoContext;
import com.xrc.gb.common.dto.go.GoPieces;
import com.xrc.gb.common.enums.PieceTypeEnum;
import com.xrc.gb.common.enums.PlaceResultTypeEnum;
import com.xrc.gb.common.util.CheckParameter;
import com.xrc.gb.work.exception.PlaceNotAllowException;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * 下棋基类
 * @author xu rongchao
 * @date 2020/3/23 16:15
 */
public abstract class AbstractGoGameRunner {

    /**
     * 落子
     */
    public PlaceResultTypeEnum place(GoContext goContext) {
        List<GoPieces> piecesList = goContext.getPlaceArrays();
        CheckParameter.assertTrue(CollectionUtils.isNotEmpty(piecesList));

        int[][] goArrArrays = buildArrays(goContext);
        GoPieces lastHand = piecesList.get(piecesList.size() - 1);

        if (judgeLast(goArrArrays, lastHand.getX(), lastHand.getY())) {
            return PieceTypeEnum.WHITE_PIECE.getCode().equals(lastHand.getPieceType()) ?
                    PlaceResultTypeEnum.WHITE_WIN_GAME : PlaceResultTypeEnum.BLACK_WIN_GAME;
        }
        return PlaceResultTypeEnum.PLACING_PIECES_SUCCESS;
    }
    /**
     * 构建 二维数组
     */
    public int[][] buildArrays(GoContext goContext) {
        int size = goContext.getCheckerBoardSize();
        int[][] goArrays = new int[size][size];
        for (GoPieces goPieces : goContext.getPlaceArrays()) {
            if (goArrays[goPieces.getX()][goPieces.getY()] != 0) {
                throw new PlaceNotAllowException("Current location is placed");
            }
            if (!goPieces.isDead()) {
                goArrays[goPieces.getX()][goPieces.getY()] = goPieces.getPieceType();
            }
        }
        return goArrays;
    }
    /**
     * 判断最后一步棋是否结束对局
     *
     */
    public abstract boolean judgeLast(int[][] goArrays, int lastX, int lastY);
    /**
     * 从头到尾判断 对局是否结束
     */
    public abstract boolean judgeAll(int[][] goArrays);
}
