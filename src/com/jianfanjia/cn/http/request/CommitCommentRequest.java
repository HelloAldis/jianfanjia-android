package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.CommitCommentInfo;

public class CommitCommentRequest extends BaseRequest {
	
	private CommitCommentInfo commitCommentInfo;
	
	public CommitCommentRequest(Context context,CommitCommentInfo commitCommentInfo) {
		super(context);
		this.commitCommentInfo = commitCommentInfo;
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
	public void onSuccess(BaseResponse baseResponse) {
		if(baseResponse.getMsg() != null){
			makeLongTextToast("ÆÀÂÛ³É¹¦");
		}
	}

	public CommitCommentInfo getCommitCommentInfo() {
		return commitCommentInfo;
	}


	public void setCommitCommentInfo(CommitCommentInfo commitCommentInfo) {
		this.commitCommentInfo = commitCommentInfo;
	}
	

}
