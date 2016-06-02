package com.jianfanjia.cn.activity.tools;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.DownloadClient;
import com.jianfanjia.api.progress.UIProgressListener;
import com.jianfanjia.api.request.guest.DownloadRequest;


/**
 * Name: DownLoadManager
 * User: fengliang
 * Date: 2015-12-22
 * Time: 16:16
 */
public class DownLoadManager {
    private static DownLoadManager instance;

    public static DownLoadManager getInstance() {
        if (null == instance) {
            instance = new DownLoadManager();
        }
        return instance;
    }

    public void download(String url, final String filePath, String fileName, ApiCallback<ApiResponse<String>>
            apiCallback, UIProgressListener progressListener) {
        DownloadRequest downloadRequest = new DownloadRequest();
        downloadRequest.setUrl(url);
        downloadRequest.setDestDirName(filePath);
        downloadRequest.setDestFileName(fileName);

        DownloadClient.download(downloadRequest, apiCallback, progressListener);
    }
}  
