package com.xrc.gb.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author xu rongchao
 * @date 2020/3/28 10:19
 */
@Configuration
public class MyWebMvcConfiguration implements WebMvcConfigurer {

    @Resource
    LoginHandlerInterceptor loginHandlerInterceptor;

    /**
     * 在这里添加 html请求
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("/index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //符合如下url的会运行loginHandlerInterceptor的方法
        InterceptorRegistration loginRegistry = registry.addInterceptor(loginHandlerInterceptor);
        loginRegistry.addPathPatterns("/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/login", "/register")
                .excludePathPatterns("/room", "/room/*").addPathPatterns("/room/create")
                .excludePathPatterns("/gobang/query")
                .excludePathPatterns("/static/**");
    }

}
