package com.jianfanjia.cn.api;

import com.jianfanjia.cn.tools.LogTool;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jyz on 16/2/24.
 */
public class LoggingInterceptor implements Interceptor {
    private static final String TAG = LoggingInterceptor.class.getName();


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LogTool.d(TAG, String.format("%s request %s  on connection %s -- headers %s -- body",
                request.method(), request.url(), chain.connection(), request.headers(), request.body()));

        Response response = chain.proceed(request);

        LogTool.d(TAG, String.format("Receive code %s, headers %s -- body %s", response.code(), response.headers(), response.body()));

        return response;
    }
}
