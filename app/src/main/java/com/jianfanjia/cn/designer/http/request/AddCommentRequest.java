package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

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
        url = url_new.ADD_COMMENT;
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
    public void onSuccess(Object data) {
        super.onSuccess(data);
    }

}
