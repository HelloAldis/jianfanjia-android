package com.jianfanjia.cn.api;

import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.jianfanjia.cn.tools.StringUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Aldis.Zhan on 16/2/23.
 */
public class Api {
    private static final String TAG = Api.class.getName();
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final BaseHandlerImpl BASE_HANDLER = new BaseHandlerImpl();
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();
    private static final int HTTP_FORBIDDEN_CODE = 403;

    private static void okhttp(Request request, final BaseHandler handler) {
        //excute the preload for handler
        BASE_HANDLER.onPreLoad();
        if (handler != null) {
            handler.onPreLoad();
        }

        if (!NetTool.isNetworkAvailable(MyApplication.getInstance())) {
            BASE_HANDLER.onNetworkError();
            return;
        }

        CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogTool.d(TAG, "Network e: " + e.toString());
                BASE_HANDLER.onNetworkError();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.code() >= 200 && response.code() <= 299) {
                    String json = response.body().string();
                    ApiResponse apiResponse = JsonParser.jsonToBean(json, ApiResponse.class);

                    if (StringUtils.isEmpty(apiResponse.err_msg)) {
                        BASE_HANDLER.onSuccess(apiResponse);
                        if (handler != null) {
                            handler.onSuccess(apiResponse);
                        }
                    } else {
                        BASE_HANDLER.onFailed();
                        if (handler != null) {
                            handler.onFailed();
                        }
                    }
                } else {
                    if (response.code() == HTTP_FORBIDDEN_CODE) {
                        //TODO logout
                    } else {
                        BASE_HANDLER.onNetworkError();
                        if (handler != null) {
                            handler.onNetworkError();
                        }
                    }
                }
            }
        });
    }

    private static void okGet(String url, BaseHandler handler) {
        Request request = new Request.Builder().url(url).build();
        okhttp(request, handler);
    }

    private static void okPost(String url, Object o, BaseHandler handler) {
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, JsonParser.beanToJson(o));
        Request request = new Request.Builder().url(url).post(body).build();
        okhttp(request, handler);
    }


}
