package com.xrc.gb.work.game;

import com.xrc.gb.common.dto.GoContext;
import com.xrc.gb.common.dto.GoPieces;
import com.xrc.gb.common.enums.GameTypeEnum;
import com.xrc.gb.common.enums.PieceTypeEnum;
import com.xrc.gb.common.enums.PlaceResultTypeEnum;
import com.xrc.gb.common.util.CheckParameter;
import com.xrc.gb.work.AbstractGoGameRunner;
import com.xrc.gb.work.GoGameFactory;
import com.xrc.gb.work.constants.ArrayIdentifiesNums;
import com.xrc.gb.work.exception.PlaceNotAllowException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xu rongchao
 * @date 2020/3/26 8:53
 */
@Component
public class WeiQiGameRunner extends AbstractGoGameRunner implements InitializingBean {


    @Override
    public PlaceResultTypeEnum place(GoContext goContext) {
        List<GoPieces> piecesList = goContext.getPlaceArrays();
        CheckParameter.assertTrue(CollectionUtils.isNotEmpty(piecesList));

        int[][] goArrArrays = buildArrays(goContext);
        GoPieces lastHand = piecesList.get(piecesList.size() - 1);

        if (judgeLast(goArrArrays, lastHand.getX(), lastHand.getY())) {
            for (GoPieces goPieces : goContext.getPlaceArrays()) {
                if (!goPieces.isDead() && goArrArrays[goPieces.getX()][goPieces.getY()] == ArrayIdentifiesNums.DEAD_PIECES) {
                    goPieces.setDead(true);
                }
            }
        }
        return PlaceResultTypeEnum.PLACING_PIECES_SUCCESS;
    }

    /**
     * 判断最后一步能不能杀死棋子
     *
     * @return true, 杀死了 false 没吃子
     */
    @Override
    public boolean judgeLast(int[][] goArrays, int lastX, int lastY) {
        WeiQi weiQi = new WeiQi(lastX, lastY, goArrays);

        if (weiQi.killOpp()) {
            return true;
        }
        if (weiQi.countQi() == 0) {
            throw new PlaceNotAllowException("这里没有气，不能下这里哦");
        }
        return false;
    }

    @Override
    public boolean judgeAll(int[][] goArrays) {
        throw new UnsupportedOperationException();
    }

    /**
     * spring bean的初始化执行顺序：构造方法 --> @PostConstruct注解的方法 --> afterPropertiesSet方法 --> init-method指定的方法。
     * afterPropertiesSet通过接口实现方式调用（效率上高一点），@PostConstruct和init-method都是通过反射机制调用
     */
    @Override
    public void afterPropertiesSet() {
        GoGameFactory.register(GameTypeEnum.WEI_QI, this);
    }


    /**
     * x and y is placed
     * v1.0 done
     */
    public static class WeiQi {
        /**
         * x和y为佯装，下下去后判断能否杀死对手
         * <p>
         * type 当前xy的棋子类型
         * arr  棋盘数组
         */

        private int[][] arr;
        private int x;
        private int y;
        private int type;

        public WeiQi(int x, int y, @NonNull int[][] arr) {
            this.x = x;
            this.y = y;
            this.arr = arr;
            this.type = arr[x][y];
        }

        /**
         * 让身边的气为零的敌棋变为死棋，变不了返回false
         *
         * @return 能否杀死对手
         */
        public boolean killOpp() {
            // 可能会导致多边杀
            boolean isKill = false;
            if (isOpp(x - 1, y)) {
                if (countQi(x - 1, y, arr[x - 1][y]) == 0) {
                    killNow(x - 1, y, arr[x - 1][y]);
                    isKill = true;
                }
            }
            if (isOpp(x + 1, y)) {
                if (countQi(x + 1, y, arr[x + 1][y]) == 0) {
                    killNow(x + 1, y, arr[x + 1][y]);
                    isKill = true;
                }
            }
            if (isOpp(x, y - 1)) {
                if (countQi(x, y - 1, arr[x][y - 1]) == 0) {
                    killNow(x, y - 1, arr[x][y - 1]);
                    isKill = true;
                }
            }
            if (isOpp(x, y + 1)) {
                if (countQi(x, y + 1, arr[x][y + 1]) == 0) {
                    killNow(x, y + 1, arr[x][y + 1]);
                    isKill = true;
                }
            }
            return isKill;
        }

        /**
         * i和k为死棋，置为零
         */
        private void killNow(int i, int j, int curType) {
            if (isOut(i, j)) {
                return;
            }
            if (arr[i][j] == 0) {
                return;
            }
            if (arr[i][j] != curType) {
                return;
            }
            if (arr[i][j] == curType) {
                arr[i][j] = ArrayIdentifiesNums.DEAD_PIECES;
                killNow(i - 1, j, curType);
                killNow(i + 1, j, curType);
                killNow(i, j - 1, curType);
                killNow(i, j + 1, curType);
            }
        }

        public int countQi() {
            return countQi(x, y, type);
        }

        /**
         * 计算当前 i, j的气
         */
        private int countQi(int i, int j, int curType) {
            int count = count_qi(i, j, curType, arr);
            right_chess_broad(arr);
            return count;
        }


        // 是否是对手
        private boolean isOpp(int i, int j) {
            if (isNotOut(i, j)) {
                return arr[i][j] != 0 && arr[i][j] != type;
            }
            return false;
        }

        // 是否越界
        private boolean isNotOut(int i, int j) {
            return i >= 0 && j >= 0 && i <= arr.length - 1 && j <= arr.length - 1;
        }

        private boolean isOut(int i, int j) {
            return !isNotOut(i, j);
        }

        // type 1. black 2. white
        private static int count_qi(int i, int j, int type, int[][] arrs) {
            if (i < 0 || j < 0 || i > arrs.length - 1 || j > arrs.length - 1) {
                return 0;
            }
            if (arrs[i][j] == 0) {
                arrs[i][j] = 3;
                return 1;
            }
            if (arrs[i][j] != type) {
                return 0;
            } else {
                arrs[i][j] = -type;
                return count_qi(i - 1, j, type, arrs)
                        + count_qi(i + 1, j, type, arrs)
                        + count_qi(i, j - 1, type, arrs)
                        + count_qi(i, j + 1, type, arrs);
            }
        }


        private static void right_chess_broad(int[][] error_arr) {

            for (int i = 0; i < error_arr.length; i++) {
                for (int j = 0; j < error_arr[0].length; j++) {
                    if (error_arr[i][j] == 0) {
                        continue;
                    }
                    if (error_arr[i][j] == 3) {
                        error_arr[i][j] = 0;
                    }
                    if (error_arr[i][j] < 0) {
                        error_arr[i][j] = Math.abs(error_arr[i][j]);
                    }
                }
            }
        }
    }
}
