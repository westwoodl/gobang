package com.xrc.gb.repository;

import com.xrc.gb.repository.domain.go.RoomDO;
import org.junit.Test;

/**
 * @author xu rongchao
 * @date 2020/3/29 10:55
 */
public class TestLombook {


    @Test
    public void test() {
        RoomDO roomDO1 = new RoomDO();
        roomDO1.setId(1);
        RoomDO roomDO2 = new RoomDO();
        roomDO2.setId(1);

        System.out.println(roomDO1.equals(roomDO2));
    }
}
