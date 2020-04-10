package com.xrc.gb.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.common.consts.CommonConst;
import com.xrc.gb.repository.domain.user.UserDO;
import com.xrc.gb.common.util.ExceptionHelper;
import com.xrc.gb.common.util.PageQueryReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author xu rongchao
 * @date 2020/3/3 19:35
 */
public abstract class AbstractController {

    protected <T> PageQueryReq<T> getPageQueryReq() {
        PageQueryReq<T> pageQueryReq = new PageQueryReq<>();
        pageQueryReq.setPageSize(getPageSize());
        pageQueryReq.setPageIndex(getPageIndex());
        return pageQueryReq;
    }

    /**
     * 一天的session
     */
    protected void addUserSession(UserDO userDO) {
        HttpSession session = getRequest().getSession();
        session.setAttribute("user", JSONObject.toJSONString(userDO));
        session.setMaxInactiveInterval(60 * 60 * 24); //单位为秒
    }

    protected void deleteUserSession() {
        HttpSession session = getRequest().getSession();
        session.removeAttribute("user");
    }

    protected void hasError(@NonNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw ExceptionHelper.newBusinessException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
    }

    @Nullable
    protected Integer getUserId() {
        UserDO userDO = getUserDO();
        if (userDO != null) {
            return userDO.getId();
        }
        return null;
    }

    @Nullable
    protected UserDO getUserDO() {
        HttpSession session = getRequest().getSession();
        String s = String.valueOf(session.getAttribute("user"));
        if (StringUtils.isNotBlank(s)) {
            return JSONObject.parseObject(s, UserDO.class);
        }
        return null;
    }

//    protected UserInfoResp getUserInfo() {
//        return UserInfoHolder.getUserInfo();
//    }

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    protected Integer getPageIndex() {
        String pageIndexStr = getRequest().getParameter("pageIndex");
        if (StringUtils.isNotBlank(pageIndexStr)) {
            int pageIndex = Integer.parseInt(pageIndexStr);
            if (pageIndex < 1) {
                pageIndex = CommonConst.DEFAULT_PAGE_INDEX;
            }
            return pageIndex;
        } else {
            return CommonConst.DEFAULT_PAGE_INDEX;
        }
    }

    protected Integer getPageSize() {
        String pageSizeStr = getRequest().getParameter("pageSize");
        if (StringUtils.isNotBlank(pageSizeStr)) {
            int pageSize = Integer.parseInt(pageSizeStr);
            if (pageSize < 1 || pageSize > CommonConst.MAX_PAGE_SIZE) {
                pageSize = CommonConst.DEFAULT_PAGE_SIZE;
            }
            return pageSize;
        } else {
            return CommonConst.DEFAULT_PAGE_SIZE;
        }
    }


}
