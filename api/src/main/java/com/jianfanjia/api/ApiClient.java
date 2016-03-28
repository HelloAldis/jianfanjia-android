package com.jianfanjia.api;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.api.request.BaseRequest;
import com.jianfanjia.common.base.application.BaseApplication;
import com.jianfanjia.common.tool.JsonParser;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.NetTool;
import com.jianfanjia.common.tool.StringUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Aldis.Zhan on 16/2/23.
 */

/**
 * 各个接口执行顺序一般为下面三类
 *
 * 1. 成功
 * BaseApiCallbackImpl.onPreLoad() -> request.onPreLoad() -> apiCallback.onPreLoad() -> http请求 -> http返回
 * -> BaseApiCallbackImpl.onHttpDone() -> request.onHttpDone() -> apiCallback.onHttpDone()
 * -> BaseApiCallbackImpl.onSuccess() -> request.onSuccess() -> apiCallback.onSuccess()
 *
 * 2. 业务失败
 * BaseApiCallbackImpl.onPreLoad() -> request.onPreLoad() -> apiCallback.onPreLoad() -> http请求 -> http返回
 * -> BaseApiCallbackImpl.onHttpDone() -> request.onHttpDone() -> apiCallback.onHttpDone()
 * -> BaseApiCallbackImpl.onFailed() -> request.onFailed() -> apiCallback.onFailed()
 *
 * 3. 网络失败
 * BaseApiCallbackImpl.onPreLoad() -> request.onPreLoad() -> apiCallback.onPreLoad() -> http请求 -> http返回
 * -> BaseApiCallbackImpl.onHttpDone() -> request.onHttpDone() -> apiCallback.onHttpDone()
 * -> BaseApiCallbackImpl.onNetworkError() -> request.onNetworkError() -> apiCallback.onNetworkError()
 *
 */
public class ApiClient {

    private static final String TAG = ApiClient.class.getName();
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType IMAGE_MEDIA_TYPE = MediaType.parse("image/jpeg");
    private static final ApiCallback BASE_API_CALLBACK = null;
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();
    private static Handler mDelivery = new Handler(Looper.getMainLooper());

    private static void api(Request okRequest, final BaseRequest baseRequest, final ApiCallback apiCallback) {
        preLoad(baseRequest, apiCallback);

        if (!NetTool.isNetworkAvailable(BaseApplication.getInstance())) {
            httpDone(baseRequest, apiCallback);
            networkError(baseRequest, apiCallback, HttpCode.NO_NETWORK_ERROR_CODE);
            return;
        }

        CLIENT.newCall(okRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e != null) {
                    LogTool.d(TAG, "Network e: " + e.toString());
                }

                httpDone(baseRequest, apiCallback);
                if (call.isCanceled()) {
                    //什么都不做应为请求被取消
                } else {
                    networkError(baseRequest, apiCallback, HttpCode.NO_NETWORK_ERROR_CODE);
                }

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                httpDone(baseRequest, apiCallback);

                if (response.isSuccessful()) {
                    String json = response.body().string();
                    logResponseBody(json);
                    ApiResponse apiResponse = getApiResponse(json, apiCallback);

                    if (StringUtils.isEmpty(apiResponse.getErr_msg())) {
                        success(baseRequest, apiCallback, apiResponse);
                    } else {
                        failed(baseRequest, apiCallback, apiResponse);
                    }
                } else {
                    networkError(baseRequest, apiCallback, response.code());
                }
            }
        });
    }

    private static void preLoad(final BaseRequest baseRequest, final ApiCallback apiCallback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (BASE_API_CALLBACK != null) {
                    BASE_API_CALLBACK.onPreLoad();
                }
                baseRequest.onPreLoad();
                if (apiCallback != null) {
                    apiCallback.onPreLoad();
                }
            }
        });
    }

    private static void httpDone(final BaseRequest baseRequest, final ApiCallback apiCallback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (BASE_API_CALLBACK != null) {
                    BASE_API_CALLBACK.onHttpDone();
                }
                baseRequest.onHttpDone();
                if (apiCallback != null) {
                    apiCallback.onHttpDone();
                }
            }
        });
    }

    private static void success(final BaseRequest baseRequest, final ApiCallback apiCallback, final ApiResponse apiResponse) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (BASE_API_CALLBACK != null) {
                    BASE_API_CALLBACK.onSuccess(apiResponse);
                }
                baseRequest.onSuccess(apiResponse);
                if (apiCallback != null) {
                    apiCallback.onSuccess(apiResponse);
                }
            }
        });
    }

    private static void failed(final BaseRequest baseRequest, final ApiCallback apiCallback, final ApiResponse apiResponse) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (BASE_API_CALLBACK != null) {
                    BASE_API_CALLBACK.onFailed(apiResponse);
                }
                baseRequest.onFailed(apiResponse);
                if (apiCallback != null) {
                    apiCallback.onFailed(apiResponse);
                }

            }
        });
    }

    private static void networkError(final BaseRequest baseRequest, final ApiCallback apiCallback, final int code) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (BASE_API_CALLBACK != null) {
                    BASE_API_CALLBACK.onNetworkError(code);
                }
                baseRequest.onNetworkError(code);
                if (apiCallback != null) {
                    apiCallback.onNetworkError(code);
                }

            }
        });
    }

    private static ApiResponse getApiResponse(String json, ApiCallback apiCallback) {
        if (apiCallback != null) {
            Type[] types = apiCallback.getClass().getGenericInterfaces();
            ParameterizedType parameterized = (ParameterizedType) types[0];
            return JsonParser.jsonToT(json, parameterized.getActualTypeArguments()[0]);
        } else {
            Type type = new TypeToken<ApiResponse<Object>>() {
            }.getType();
            return JsonParser.jsonToT(json, type);
        }
    }

    private static void logRequestBody(String string) {
        LogTool.d(TAG, "request body: " + string);
    }

    private static void logResponseBody(String string) {
        LogTool.d(TAG, "response body: " + string);
    }


    /**
     * Get 方法请求通用
     *
     * @param url         api url
     * @param baseRequest request对象
     * @param apiCallback
     */
    public static void okGet(String url, BaseRequest baseRequest, ApiCallback apiCallback) {
        Request request = new Request.Builder().url(url).build();
        api(request, baseRequest, apiCallback);
    }


    /**
     * POST 方法请求通用
     *
     * @param url         api url
     * @param baseRequest request对象
     * @param apiCallback 回调
     */
    public static void okPost(String url, BaseRequest baseRequest, ApiCallback apiCallback) {
        String json = JsonParser.beanToJson(baseRequest);
        logRequestBody(json);
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, json);
        Request request = new Request.Builder().url(url).post(body).build();
        api(request, baseRequest, apiCallback);
    }

    /**
     * POST上传数据
     *
     * @param url         api url
     * @param baseRequest request对象
     * @param bytes       数据的byte数组
     * @param apiCallback 回调
     */
    public static void upload(String url, BaseRequest baseRequest, byte[] bytes, ApiCallback apiCallback) {
        RequestBody body = RequestBody.create(IMAGE_MEDIA_TYPE, bytes);
        Request request = new Request.Builder().url(url).post(body).build();
        api(request, baseRequest, apiCallback);
    }
}
