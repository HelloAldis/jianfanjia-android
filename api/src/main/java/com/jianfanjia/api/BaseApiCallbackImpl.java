package com.jianfanjia.api;

/**
 * Created by Aldis.Zhan on 16/2/24.
 */

/**
 * 基础的Api回调方法, 可以将一些全局的无关具体请求和具体屏的公共逻辑放这里
 */
public class BaseApiCallbackImpl implements ApiCallback<ApiResponse<Object>> {
    @Override
    public void onPreLoad() {

    }

    @Override
    public void onHttpDone() {

    }

    @Override
    public void onSuccess(ApiResponse<Object> apiResponse) {

    }

    @Override
    public void onFailed(ApiResponse<Object> apiResponse) {

    }

    @Override
    public void onNetworkError(int code) {
        if (HttpCode.HTTP_FORBIDDEN_CODE == code) {
//            UiHelper.forbiddenToLogin();
        }
    }
}
