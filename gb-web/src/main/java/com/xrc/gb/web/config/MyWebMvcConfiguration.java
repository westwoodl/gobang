package com.xrc.gb.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

/**
 * @author xu rongchao
 * @date 2020/3/28 10:19
 */
@Configuration
public class MyWebMvcConfiguration implements WebMvcConfigurer {

    @Resource
    LoginHandlerInterceptor loginHandlerInterceptor;

//    /**
//     * 在这里添加 html请求
//     */
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/index").setViewName("/index");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }

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
                .excludePathPatterns("/user")
                .excludePathPatterns("/room").addPathPatterns("/room/create")
                .excludePathPatterns("/gobang/query")
                .excludePathPatterns("/static/**");
    }

    /**
     * 被拦截器拦截的请求不会走这里
     * 允许跨域请求 todo 减少允许范围
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("*");
    }

}
