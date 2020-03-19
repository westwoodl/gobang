package com.xrc.gb.repository;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xu rongchao
 * @date 2020/3/4 13:53
 */
@SpringBootApplication(scanBasePackages = {
        "com.xrc.gb.repository.dao"
})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
