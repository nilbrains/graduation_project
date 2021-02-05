package com.nilbrains.graduation.response;

public enum ResponseState {
    SUCCESS(true, 20000, "操作成功"),
    LOGIN_SUCCESS(true, 20000, "登录成功"),
    FAILED(false, 40000, "操作失败"),
    ACCOUNT_NOT_LOGIN(false, 49123, "账号未登录"),
    NOT_FOUND(false, 404, "没有资源"),
    FORBIDDEN(false, 403, "权限不足"),
    GATEWAY_TIMEOUT(false, 504, "没有资源"),
    HTTP_VERSION_NOT_SUPPORTED(false, 505, "不支持"),
    METHOD_NOT_ALLOWED(false, 405, "方法不支持");

    ResponseState(boolean success, int code, String message) {
        this.code = code;
        this.success = success;
        this.message = message;
    }

    private int code;
    private String message;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}
