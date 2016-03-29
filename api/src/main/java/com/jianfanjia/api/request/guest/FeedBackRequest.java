package com.jianfanjia.api.request.guest;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: FeedBackRequest
 * User: fengliang
 * Date: 2016-03-28
 * Time: 14:35
 */
public class FeedBackRequest extends BaseRequest {
    private String content;

    private String version;

    private String platform;

    public FeedBackRequest(String content, String version, String platform) {
        this.content = content;
        this.version = version;
        this.platform = platform;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
