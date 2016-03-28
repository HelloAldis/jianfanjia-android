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
}
