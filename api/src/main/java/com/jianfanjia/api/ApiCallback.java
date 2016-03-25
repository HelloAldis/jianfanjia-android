package com.jianfanjia.api;

/**
 * Created by Aldis.Zhan on 16/2/24.
 */

/**
 * Api的基本回调接口
 * @param <T> ApiResponse 的实体类
 */
public interface ApiCallback<T> {
    /**
     * 最先被调用的,可以用来显示网络指示器
     */
    public void onPreLoad();

    /**
     * http调用结束了,不管成功和失败总是会被调用的方法
     */
    public void onHttpDone();

    /**
     * Server返回http 200 且没有返回err_msg信息, Server正确处理了请求,回调这个
     * @param apiResponse Api server 返回的对象
     */
    public void onSuccess(T apiResponse);

    /**
     * Server返回http 200 且返回有err_msg信息, Server判断有业务错误
     * @param apiResponse Api server 返回的对象
     */
    public void onFailed(T apiResponse);

    /**
     * Server没有返回 200 如404, 403, 500等
     * @param code 错误码 -1,代表没有网络, 其它的是http状态吗
     */
    public void onNetworkError(int code);
}
