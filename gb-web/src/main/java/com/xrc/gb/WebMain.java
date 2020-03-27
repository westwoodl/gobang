package com.xrc.gb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author xu rongchao
 * @date 2020/3/3 19:00
 */

@SpringBootApplication
@EnableTransactionManagement
public class WebMain {
    public static void main(String[] args) {
        SpringApplication.run(WebMain.class, args);
    }
}
