package com.nilbrains.graduation.response;

public class ResponseResult {
    private boolean success;
    private int code;
    private String message;
    private Object data;

    public ResponseResult(ResponseState responseState) {
        this.success = responseState.isSuccess();
        this.code = responseState.getCode();
        this.message = responseState.getMessage();
    }

    public static ResponseResult SUCCESS() {
        return new ResponseResult(ResponseState.SUCCESS);
    }

    public static ResponseResult SUCCESS(String message) {
        ResponseResult responseResult = new ResponseResult(ResponseState.SUCCESS);
        responseResult.setMessage(message);
        return responseResult;
    }

    public static ResponseResult FAILED() {
        return new ResponseResult(ResponseState.FAILED);
    }

    public static ResponseResult FAILED(String message) {
        ResponseResult responseResult = new ResponseResult(ResponseState.FAILED);
        responseResult.setMessage(message);
        return responseResult;

    }

    public static ResponseResult ERROR_404() {
        return new ResponseResult(ResponseState.NOT_FOUND);
    }

    public static ResponseResult ERROR_403() {
        return new ResponseResult(ResponseState.FORBIDDEN);
    }

    public static ResponseResult ERROR_504() {
        return new ResponseResult(ResponseState.GATEWAY_TIMEOUT);
    }

    public static ResponseResult ERROR_505() {
        return new ResponseResult(ResponseState.HTTP_VERSION_NOT_SUPPORTED);
    }

    public static ResponseResult ERROR_405() {
        return new ResponseResult(ResponseState.METHOD_NOT_ALLOWED);
    }

    public static ResponseResult ACCOUNT_NOT_LOGIN() {
        return new ResponseResult(ResponseState.ACCOUNT_NOT_LOGIN);
    }

    public static ResponseResult LOGIN_SUCCESS() {
        return new ResponseResult(ResponseState.LOGIN_SUCCESS);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

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

    public Object getData() {
        return data;
    }

    public ResponseResult setData(Object data) {
        this.data = data;
        return this;
    }
}
