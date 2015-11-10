package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.config.Url;

/**
 * Name: AddCommentRequest
 * User: fengliang
 * Date: 2015-10-23
 * Time: 19:33
 */
public class AddCommentRequest extends BaseRequest {
    private String topicid;
    private String topictype;
    private String content;
    private String to;

    public AddCommentRequest(Context context, String topicid, String topictype, String content, String to) {
        super(context);
        this.topicid = topicid;
        this.topictype = topictype;
        this.content = content;
        this.to = to;
        url = Url.ADD_COMMENT;
    }

    @Override
    public void pre() {
        super.pre();
    }

    @Override
    public void all() {
        super.all();
    }

    @Override
    public void onSuccess(BaseResponse data) {
        super.onSuccess(data);
    }

}