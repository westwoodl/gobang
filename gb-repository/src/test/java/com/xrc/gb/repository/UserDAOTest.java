package com.xrc.gb.repository;

import com.xrc.gb.repository.dao.UserDAO;
import com.xrc.gb.repository.domain.user.UserDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author xu rongchao
 * @date 2020/3/4 13:23
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {App.class})
//@EnableAutoConfiguration

@RunWith(SpringRunner.class)
//加载主启动类
@SpringBootTest(classes = {App.class})
@EnableAutoConfiguration
public class UserDAOTest {

    @Resource
    private UserDAO userDAO;

    @Test
    public void test_query(){
        UserDO userDO = userDAO.queryById(1);
    }

    @Test
    public void test_insert(){
        UserDO userDO = new UserDO();
        userDO.setUserName("xrc");
        userDO.setAccount("123456");
        userDO.setPassword("123456");
        userDAO.insert(userDO);
    }
}
