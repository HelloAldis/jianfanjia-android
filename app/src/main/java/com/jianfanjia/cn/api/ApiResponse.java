package com.jianfanjia.cn.api;

/**
 * Created by jyz on 16/2/24.
 */
public class ApiResponse<T> {
    private T data;
    private String msg;
    private String err_msg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }
}
