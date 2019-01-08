package cn.hankchan.utils.model;

import java.util.HashMap;

/**
 * @author hankchan
 * @since 2018/8/30
 */
public class ApiResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 362498820763181265L;

    private ApiResult() { }

    public static ApiResult prepare() {
        return new ApiResult();
    }

    public ApiResult success(Object result, String requestId) {
        this.put("result", result);
        this.put("is_success", true);
        this.put("code", "200");
        this.put("msg", "");
        return this;
    }

    public ApiResult error(String msg, String code, String requestId) {
        this.put("result", null);
        this.put("is_success", false);
        this.put("code", code);
        this.put("msg", msg);
        return this;
    }
}
