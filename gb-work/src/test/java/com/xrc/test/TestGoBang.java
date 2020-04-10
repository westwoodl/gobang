package com.xrc.test;

import com.xrc.gb.work.game.GoBangGameRunner;
import org.junit.Test;

/**
 * @author xu rongchao
 * @date 2020/3/25 21:57
 */
public class TestGoBang {


    @Test
    public void test_work() {
        int[][] ss = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 2, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 2, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 2, 2, 0, 0, 0, 0},
                {0, 0, 0, 1, 2, 0, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 2, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        System.out.println(ss[7][8]);
        long s = System.currentTimeMillis();
        System.out.println(new GoBangGameRunner().judgeAll(ss));
        System.out.println(new GoBangGameRunner().judgeLast(ss, 2, 3));
        System.out.println(System.currentTimeMillis() - s);
    }
}
