package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

public class CommitCommentRequest extends BaseRequest {

    public CommitCommentRequest(Context context) {
        super(context);
        url = url_new.POST_PROCESS_COMMENT;
    }


    @Override
    public void all() {
        // TODO Auto-generated method stub
        super.all();

    }

    @Override
    public void pre() {
        // TODO Auto-generated method stub
        super.pre();
    }

    @Override
    public void onSuccess(Object data) {
        if (data != null) {
        }
    }


}