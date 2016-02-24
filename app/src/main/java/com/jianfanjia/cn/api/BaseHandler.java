package com.jianfanjia.cn.api;

/**
 * Created by jyz on 16/2/24.
 */
public interface BaseHandler<T> {
    public void onPreLoad();
    public void onSuccess(ApiResponse<T> apiResponse);
    public void onFailed();
    public void onNetworkError();
}
