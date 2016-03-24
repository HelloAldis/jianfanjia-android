package com.jianfanjia.cn.api.request;

import com.jianfanjia.cn.api.ApiCallback;
import com.jianfanjia.cn.api.ApiResponse;

/**
 * Created by Aldis.Zhan on 16/2/24.
 */


/**
 * 一些和特定API的通用逻辑到他们的子类里面去重写方法,不要把方法加到这个这里来,考虑加到BaseApiCallBackImpl里面
 */
public class BaseRequest implements ApiCallback<ApiResponse<Object>> {
    @Override
    public void onPreLoad() {
        //不要把方法加到这个这里来,考虑加到BaseApiCallBackImpl里面
    }

    @Override
    public void onHttpDone() {
        //不要把方法加到这个这里来,考虑加到BaseApiCallBackImpl里面
    }

    @Override
    public void onSuccess(ApiResponse<Object> apiResponse) {
        //不要把方法加到这个这里来,考虑加到BaseApiCallBackImpl里面
    }

    @Override
    public void onFailed(ApiResponse<Object> apiResponse) {
        //不要把方法加到这个这里来,考虑加到BaseApiCallBackImpl里面
    }

    @Override
    public void onNetworkError(int code) {
        //不要把方法加到这个这里来,考虑加到BaseApiCallBackImpl里面
    }
}
