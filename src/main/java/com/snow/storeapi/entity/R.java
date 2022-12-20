package com.snow.storeapi.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面响应entity
 */
public class R extends HashMap<String, Object> {
	
	private static final long serialVersionUID = 1L;

    public static final String SUCCESS = "success";

    public static final String FAIL = "fail";
	
	public R() {
		put("result", SUCCESS);
	}
	
	public static R error() {
		return error(FAIL, "未知异常，请联系管理员");
	}
	
	public static R error(String msg) {
		return error(FAIL, msg);
	}
	
	public static R error(Object result, String msg) {
		R r = new R();
		r.put("result", result);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}
	
	public static R ok(Map<String, ?> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}

	public static R ok(List list){
		R r = new R();
		r.put("list",list);
		return r;
	}
	
	public static R ok() {
		return new R();
	}

    public static R error(Map<String, Object> map) {
        R r = new R();
        r.put("result",FAIL);
        r.putAll(map);
        return r;
    }

	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}