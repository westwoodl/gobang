package com.xrc.gb.work.gobang;

import com.xrc.gb.enums.PieceTypeEnum;
import com.xrc.gb.enums.PlaceResultTypeEnum;
import com.xrc.gb.manager.go.dto.GoContext;
import com.xrc.gb.manager.go.dto.GoPieces;
import com.xrc.gb.manager.go.dto.GoQueryResp;
import com.xrc.gb.util.CheckParameter;
import com.xrc.gb.work.AbstractGoGameRunner;
import com.xrc.gb.work.exception.PlaceNotAllowException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * x  y  y  y  y  y
 * x
 * x
 * x
 * x
 * x
 *
 * @author xu rongchao
 * @date 2020/3/23 16:26
 */
@Component
public class GoBangGameRunner extends AbstractGoGameRunner {
    private static int[][] checkerboard;

    private static final int GAME_PIECES_CONTINUITY_MAX_NUM = 3;

    @Override
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

    @Override
    public int[][] buildArrays(@NonNull GoContext goContext) {
        int size = goContext.getCheckerBoardSize();
        int[][] goArrays = new int[size][size];
        for (GoPieces goPieces : goContext.getPlaceArrays()) {
            if (goArrays[goPieces.getX()][goPieces.getY()] != 0) {
                throw new PlaceNotAllowException("Current location is placed");
            }
            goArrays[goPieces.getX()][goPieces.getY()] = goPieces.getPieceType();
        }
        return goArrays;
    }



    /**
     *                  up
     *
     *
     * --------<-left--------right->
     *
     *
     *                 down
     * 判断最后一个棋子是不是结束了对局
     * 写这个代码的时候，吧 x 和 y 搞反了，但该算法遍历了所有情况，所以并不影响结果。。
     * x理应为 第几行
     * y为第几列
     */
    @Override
    public final boolean judgeLast(int[][] goArrays, int lastX, int lastY) {
        int upNum = 0, rightNum = 0, leftNum = 0, downNum = 0;
        int currentPieces = goArrays[lastX][lastY];
        if (currentPieces == 0) {
            throw new IllegalArgumentException("lastX and lastY is illegal parameters");
        }
        int size = goArrays.length;
        while (true) {
            if (lastY - upNum - 1 >= 0 && goArrays[lastX][lastY - upNum - 1] == currentPieces) {
                if (++upNum > GAME_PIECES_CONTINUITY_MAX_NUM) return true;
            } else break;
        }
        while (true) {
            if (lastX + rightNum + 1 < size && goArrays[lastX + rightNum + 1][lastY] == currentPieces) {
                if (++rightNum > GAME_PIECES_CONTINUITY_MAX_NUM) return true;
            } else break;
        }
        while (true) {
            if (lastX + leftNum + 1 >= 0 && goArrays[lastX - leftNum - 1][lastY] == currentPieces) {
                if (++leftNum > GAME_PIECES_CONTINUITY_MAX_NUM) return true;
            } else break;
        }
        while (true) {
            if (lastY + downNum + 1 < size && goArrays[lastX][lastY + downNum + 1] == currentPieces) {
                if (++downNum > GAME_PIECES_CONTINUITY_MAX_NUM) return true;
            } else break;
        }

        int right_upNum = 0, right_downNum = 0, left_upNum = 0, left_downNum = 0;
        /*
         *                  up
         *     left_upNum   *    right_upNum
         *                  *
         *        <-left--------right->
         *                  *
         *   left_downNum   *    right_downNum
         *                 down
         */
        while (true) {
            if (lastY - right_upNum - 1 >= 0 && lastX + right_upNum + 1 < size
                    && goArrays[lastX + right_upNum + 1][lastY - right_upNum - 1] == currentPieces) {
                if (++right_upNum > GAME_PIECES_CONTINUITY_MAX_NUM) return true;
            } else break;
        }
        while (true) {
            if (lastX - left_upNum - 1 >= 0 && lastY - left_upNum - 1 >= 0
                    && goArrays[lastX - left_upNum - 1][lastY - left_upNum - 1] == currentPieces) {
                if (++left_upNum > GAME_PIECES_CONTINUITY_MAX_NUM) return true;
            } else break;
        }
        while (true) {
            if (lastX - left_downNum - 1 >= 0 && lastY + left_downNum + 1 < size
                    && goArrays[lastX - left_downNum - 1][lastY + left_downNum + 1] == currentPieces) {
                if (++left_downNum > GAME_PIECES_CONTINUITY_MAX_NUM) return true;
            } else break;
        }
        while (true) {
            if (lastX + right_downNum + 1 < size && lastY + right_downNum + 1 < size
                    && goArrays[lastX + right_downNum + 1][lastY + right_downNum + 1] == currentPieces) {
                if (++right_downNum > GAME_PIECES_CONTINUITY_MAX_NUM) return true;
            } else break;
        }

        return upNum + downNum > GAME_PIECES_CONTINUITY_MAX_NUM
                || rightNum + leftNum > GAME_PIECES_CONTINUITY_MAX_NUM
                || right_downNum + left_upNum > GAME_PIECES_CONTINUITY_MAX_NUM
                || right_upNum + left_downNum > GAME_PIECES_CONTINUITY_MAX_NUM;
    }


    @Override
    public boolean judgeAll(int[][] goArrays) {
        for (int i = 0; i < goArrays.length; i++) {
            for (int j = 0; j < goArrays[0].length; j++) {
                if (goArrays[i][j] != 0 && judgeLast(goArrays, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }


}
