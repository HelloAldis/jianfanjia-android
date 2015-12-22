package com.jianfanjia.cn.tools;

import com.jianfanjia.cn.http.OkHttpClientManager;
import com.jianfanjia.cn.http.coreprogress.listener.impl.UIProgressListener;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;

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

    public void download(String url, final String filePath, String fileName, ApiUiUpdateListener listener, UIProgressListener progressListener) {
        OkHttpClientManager.getDownloadDelegate().downloadAsyn(url, fileName, filePath, listener, progressListener, this);
    }
}  
