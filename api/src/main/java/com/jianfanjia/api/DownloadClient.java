package com.jianfanjia.api;

import android.os.Handler;
import android.os.Looper;

import com.jianfanjia.api.progress.UIProgressListener;
import com.jianfanjia.api.request.BaseRequest;
import com.jianfanjia.api.request.guest.DownloadRequest;
import com.jianfanjia.common.base.application.BaseApplication;
import com.jianfanjia.common.tool.FileUtil;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.NetTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Aldis.Zhan on 16/3/1.
 */
public class DownloadClient {
    private static final String TAG = DownloadClient.class.getName();
    private static final OkHttpClient CLIENT = new OkHttpClient().newBuilder().build();
    private static Handler mDelivery = new Handler(Looper.getMainLooper());


    private static void okDownload(final Request request, final DownloadRequest downloadRequest, final ApiCallback apiCallback, final UIProgressListener uiProgressListener) {
        preLoad(downloadRequest, apiCallback);

        if (!NetTool.isNetworkAvailable(BaseApplication.getInstance())) {
            httpDone(downloadRequest, apiCallback);
            networkError(downloadRequest, apiCallback, HttpCode.NO_NETWORK_ERROR_CODE);
            return;
        }

        CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e != null) {
                    LogTool.d(TAG, "Network e: " + e.toString());
                }

                httpDone(downloadRequest, apiCallback);
                if (call.isCanceled()) {
                    //什么都不做因为请求被取消
                } else {
                    networkError(downloadRequest, apiCallback,HttpCode.NO_NETWORK_ERROR_CODE);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                httpDone(downloadRequest, apiCallback);

                if (response.isSuccessful()) {
                    byte[] buf = new byte[1024];
                    int len = 0;
                    long total = 0L;
                    File destFile = FileUtil.createFile(downloadRequest.getDestDirName(), downloadRequest.getDestFileName());
                    InputStream is = null;
                    FileOutputStream fos = null;

                    try {
                        is = response.body().byteStream();
                        fos = new FileOutputStream(destFile, false);

                        while ((len = is.read(buf)) != -1) {
                            uiProgressListener.onProgress(total, response.body().contentLength(), false);
                            total += len;
                            fos.write(buf, 0, len);
                        }
                        fos.flush();
                        uiProgressListener.onProgress(total, response.body().contentLength(), true);

                        //如果下载文件成功，传递的数据为文件的绝对路径
                        LogTool.d(TAG, destFile.getAbsolutePath());
                        ApiResponse<String> apiResponse = new ApiResponse<String>();
                        apiResponse.setData(destFile.getAbsolutePath());

                        success(downloadRequest, apiCallback, apiResponse);
                    } catch (IOException e) {
                        networkError(downloadRequest, apiCallback, HttpCode.SAVE_FILE_ERROR_CODE);
                    } finally {
                        FileUtil.closeQuietly(is);
                        FileUtil.closeQuietly(fos);
                    }
                } else {
                    networkError(downloadRequest, apiCallback, response.code());
                }
            }
        });
    }

    private static void preLoad(final BaseRequest baseRequest, final ApiCallback apiCallback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
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
                if (apiCallback != null) {
                    apiCallback.onNetworkError(code);
                }

            }
        });
    }

    public static synchronized void download(DownloadRequest downloadRequest, ApiCallback apiCallback, UIProgressListener uiProgressListener) {
        Request request = new Request.Builder().url(downloadRequest.getUrl()).build();
        okDownload(request, downloadRequest, apiCallback, uiProgressListener);
    }
}
