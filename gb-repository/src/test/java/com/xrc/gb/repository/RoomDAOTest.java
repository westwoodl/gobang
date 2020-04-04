package com.xrc.gb.repository;

import com.xrc.gb.repository.dao.GoDAO;
import com.xrc.gb.repository.dao.RoomDAO;
import com.xrc.gb.repository.domain.go.RoomDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author xu rongchao
 * @date 2020/3/28 15:00
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App.class})
@EnableAutoConfiguration
public class RoomDAOTest {

    @Resource
    private RoomDAO roomDAO;

    @Resource
    private GoDAO goDAO;

    @Test
    public void test_room_query() {
        System.out.println(roomDAO.queryById(1));
        System.out.println(goDAO.queryById(1));
        RoomDO roomDO = new RoomDO();
        System.out.println(roomDAO.queryAllByLimit(roomDO, 0, 5));
        System.out.println(roomDAO.countAll(roomDO));
    }

    @Test
    public void test_insert() {
        RoomDO roomDO = new RoomDO();
        roomDO.setCreateUser(1);
        roomDO.setOpponents(1);
        roomDO.setRoomNumber(1);
        roomDO.setRoomName("粑粑");
        roomDO.setRoomStatus(1);
        roomDO.setWatchUser("3");
        System.out.println(roomDAO.insert(roomDO));
        System.out.println(roomDO.getId());

    }

    @Test
    public void test_update() {
        RoomDO roomDO =new RoomDO();
        roomDO.setId(18);
        roomDO.setRoomNumber(-1);
        roomDO.setOpponents(0);
        System.out.println(roomDAO.update(roomDO));
        System.out.println(roomDO);
    }




}
