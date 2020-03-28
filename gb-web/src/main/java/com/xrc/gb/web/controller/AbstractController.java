package com.xrc.gb.web.controller;

import com.xrc.gb.consts.CommonConst;
import com.xrc.gb.util.ExceptionHelper;
import com.xrc.gb.util.PageQueryReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
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

    protected void hasError(@NonNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw ExceptionHelper.newBusinessException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
    }

    protected Integer getUserId() {
        HttpSession session = getRequest().getSession();
        return (Integer) session.getAttribute("user");
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
