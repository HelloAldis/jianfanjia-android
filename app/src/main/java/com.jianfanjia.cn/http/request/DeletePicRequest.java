package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.tools.LogTool;

public class DeletePicRequest extends BaseRequest{
	
	private String processId;
	
	private String section;
	
	private String key;
	
	

	public DeletePicRequest(Context context,String processId,String section,String key) {
		super(context);
		this.processId = processId;
		this.section = section;
		this.key = key;
		url = Url_New.DELETE_YANSHOU_IMG_BY_DESIGNER;
	}
	
	@Override
	public void pre() {
		// TODO Auto-generated method stub
		super.pre();
	}
	
	@Override
	public void onSuccess(Object data) {
		// TODO Auto-generated method stub
		if(data.toString() != null){
			ProcessInfo processInfo = dataManager.getDefaultProcessInfo();
			if(processInfo != null){
				LogTool.d(this.getClass().getName(), "processInfo != null");
				boolean flag = processInfo.deleteCheckImage(section, key);
				if(flag){
					dataManager.saveProcessInfo(processInfo);
					dataManager.setCurrentProcessInfo(processInfo);
				}
			}
		}
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	

}
