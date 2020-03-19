package com.xrc.gb.web.common;

import com.alibaba.fastjson.JSONObject;

/**
 * @author xu rongchao
 * @date 2020/3/3 19:16
 */
public class JSONObjectResult extends JSONObject{
    private Object data;

    private JSONObjectResult() {
    }

    public static JSONObjectResult create() {
        return new JSONObjectResult();
    }

    private JSONObjectResult success(String code, String msg) {
        this.put("success", true);
        this.put("code", code);
        this.put("msg", msg);
        return this;
    }

    public JSONObjectResult success(String msg, Object obj) {
        success("200", msg);
        this.put("data", obj);
        return this;
    }

    public JSONObjectResult success(String msg) {
        return success("200", msg);
    }

    public JSONObjectResult success(Object obj) {
        success();
        this.put("data", obj);
        return this;
    }

    public JSONObjectResult success() {
        return success("200", "请求成功");
    }

    public JSONObjectResult fail(String code, String msg) {
        this.put("success", false);
        this.put("code", code);
        this.put("msg", msg);
        return this;
    }

    public JSONObjectResult fail(String msg) {
        return fail("400", msg);
    }

    public JSONObjectResult fail() {
        return fail("400", "请求失败");
    }

}
