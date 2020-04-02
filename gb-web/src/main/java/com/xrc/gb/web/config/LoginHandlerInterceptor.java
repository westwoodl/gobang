package com.xrc.gb.web.config;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.web.common.JSONObjectResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * 用户登录拦截器
 *
 * @author xu rongchao
 * @date 2020/3/28 10:16
 */
@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("user");

        //拦截器跨域请求设置
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,XFILENAME,XFILECATEGORY,XFILESIZE");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

        if (user == null || StringUtils.isBlank(user.toString()))  {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");


            PrintWriter writer = response.getWriter();

            writer.write(JSONObjectResult.create().fail("请登录").toJSONString());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
