package com.jianfanjia.api.request.guest;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Created by jyz on 16/3/1.
 */
public class DownloadRequest extends BaseRequest {
    private String url;
    private String destFileName;
    private String destDirName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDestFileName() {
        return destFileName;
    }

    public void setDestFileName(String destFileName) {
        this.destFileName = destFileName;
    }

    public String getDestDirName() {
        return destDirName;
    }

    public void setDestDirName(String destDirName) {
        this.destDirName = destDirName;
    }
}
