package com.thisarattr.grocery.exception;

public class APIException extends Exception  {

    private int code;

    public APIException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
