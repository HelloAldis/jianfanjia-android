package com.jianfanjia.api;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.api.request.BaseRequest;
import com.jianfanjia.common.base.application.BaseApplication;
import com.jianfanjia.common.tool.JsonParser;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.NetTool;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Aldis.Zhan on 16/2/23.
 */

/**
 * 各个接口执行顺序一般为下面三类
 * <p/>
 * 1. 成功
 * BaseApiCallbackImpl.onPreLoad() -> apiCallback.onPreLoad() -> http请求 -> http返回
 * -> BaseApiCallbackImpl.onHttpDone() -> apiCallback.onHttpDone()
 * -> BaseApiCallbackImpl.onSuccess() -> apiCallback.onSuccess()
 * <p/>
 * 2. 业务失败
 * BaseApiCallbackImpl.onPreLoad() -> apiCallback.onPreLoad() -> http请求 -> http返回
 * -> BaseApiCallbackImpl.onHttpDone() -> apiCallback.onHttpDone()
 * -> BaseApiCallbackImpl.onFailed() -> apiCallback.onFailed()
 * <p/>
 * 3. 网络失败
 * BaseApiCallbackImpl.onPreLoad() -> apiCallback.onPreLoad() -> http请求 -> http返回
 * -> BaseApiCallbackImpl.onHttpDone() -> apiCallback.onHttpDone()
 * -> BaseApiCallbackImpl.onNetworkError() -> apiCallback.onNetworkError()
 */
public class ApiClient {

    private static final String TAG = ApiClient.class.getName();
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType IMAGE_MEDIA_TYPE = MediaType.parse("image/jpeg");

    private static ApiCallback BASE_API_CALLBACK = null;
    private static CookieStore COOKIE_STORE = null;
    private static OkHttpClient CLIENT = null;
    private static String USER_AGENT = null;

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
                    LogTool.d("Network e: " + e.toString());
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
                    String json = StringUtils.unEscapeHtmp(response.body().string());
                    response.body().close();
                    logResponseBody(json);
                    ApiResponse apiResponse = getApiResponse(json, apiCallback);

                    if (TextUtils.isEmpty(apiResponse.getErr_msg())) {
                        success(baseRequest, apiCallback, apiResponse);
                    } else {
                        failed(baseRequest, apiCallback, apiResponse);
                    }
                } else {
                    response.body().close();
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
                if (apiCallback != null) {
                    apiCallback.onHttpDone();
                }
            }
        });
    }

    private static void success(final BaseRequest baseRequest, final ApiCallback apiCallback, final ApiResponse
            apiResponse) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (BASE_API_CALLBACK != null) {
                    BASE_API_CALLBACK.onSuccess(apiResponse);
                }
                if (apiCallback != null) {
                    apiCallback.onSuccess(apiResponse);
                }
            }
        });
    }

    private static void failed(final BaseRequest baseRequest, final ApiCallback apiCallback, final ApiResponse
            apiResponse) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (BASE_API_CALLBACK != null) {
                    BASE_API_CALLBACK.onFailed(apiResponse);
                }
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
        LogTool.d("request body: " + string);
    }

    private static void logResponseBody(String string) {
        LogTool.d("response body: " + string);
    }


    /**
     * Get 方法请求通用
     *
     * @param url         api url
     * @param baseRequest request对象
     * @param apiCallback
     */
    public static void okGet(String url, BaseRequest baseRequest, ApiCallback apiCallback, Object tag) {
        Request request = new Request.Builder().url(url).tag(tag).header("User-Agent", ApiClient.USER_AGENT).build();
        api(request, baseRequest, apiCallback);
    }


    /**
     * POST 方法请求通用
     *  @param url         api url
     * @param baseRequest request对象
     * @param apiCallback 回调
     * @param tag
     */
    public static void okPost(String url, BaseRequest baseRequest, ApiCallback apiCallback, Object tag) {
        String json = JsonParser.beanToJson(baseRequest);
        logRequestBody(json);
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, json);
        Request request = new Request.Builder().url(url).tag(tag).header("User-Agent", ApiClient.USER_AGENT).post(body).build();
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
    public static void okUpload(String url, BaseRequest baseRequest, byte[] bytes, ApiCallback apiCallback, Object tag) {
        RequestBody body = RequestBody.create(IMAGE_MEDIA_TYPE, bytes);
        Request request = new Request.Builder().url(url).tag(tag).header("User-Agent", ApiClient.USER_AGENT).post(body).build();
        api(request, baseRequest, apiCallback);
    }

    /**
     * 清空Cookie
     */
    public static void clearCookie() {
        LogTool.d("clearCookie");
        ApiClient.COOKIE_STORE.removeAll();
    }

    public static void init(CookieStore store, ApiCallback apiCallback, String userAgent) {
        ApiClient.COOKIE_STORE = store;
        ApiClient.BASE_API_CALLBACK = apiCallback;
        ApiClient.USER_AGENT = userAgent;
        ApiClient.CLIENT = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(new CookieManager(ApiClient.COOKIE_STORE, CookiePolicy.ACCEPT_ALL)))
                .addInterceptor(new LoggingInterceptor()).build();
    }

    public static void cancelTag(Object tag)
    {
        for (Call call : CLIENT.dispatcher().queuedCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
                LogTool.d("cancel queued call tag =" + tag);
            }
        }
        for (Call call : CLIENT.dispatcher().runningCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
                LogTool.d("cancel running call tag =" + tag);
            }
        }
    }

}
