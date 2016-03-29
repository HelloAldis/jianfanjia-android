package com.jianfanjia.api;

import java.io.IOException;

import com.jianfanjia.common.tool.LogTool;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Aldis.Zhan on 16/2/24.
 */
public class LoggingInterceptor implements Interceptor {
    private static final String TAG = LoggingInterceptor.class.getName();


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LogTool.d(TAG, String.format("%s request %s -- headers %s", request.method(), request.url(), request.headers
                ().toString()));

        Response response = chain.proceed(request);

        LogTool.d(TAG, String.format("Receive %s %s, headers %s", response.request().url(), response.code(), response
                .headers()));

        return response;
    }
}
