package com.xrc.gb.work.gobang;

import com.xrc.gb.manager.go.dto.GoPieces;
import com.xrc.gb.work.BaseGoGameRunner;

/**
 * @author xu rongchao
 * @date 2020/3/23 16:26
 */
public class GoBangGameRunner extends BaseGoGameRunner {
    private static int[][] checkerboard;

    @Override
    public void place(GoPieces goPieces) {


    }


    public static void main(String[] args) {
        for (int[] ss : checkerboard){
            for (int s : ss) {
                System.out.println(s);
            }
        }
    }
}
