package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.bean.ProcessInfo;

public class AddPicToSectionItemRequest extends BaseRequest {
	
	private String processId;
	
	private String section;
	
	private String item;
	
	private String imageId;
	
	public AddPicToSectionItemRequest(Context context,String processId,String section,String item,String imageId) {
		super(context);
		this.processId = processId;
		this.section = section;
		this.item = item;
		this.imageId = imageId;
		url = url_new.POST_PROCESS_IMAGE;
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
			ProcessInfo processInfo = dataManager.getDefaultProcessInfo();
//			if(processInfo != null){
//				LogTool.d(this.getClass().getName(), "processInfo != null");
//				processInfo.addImageToItem(section, item, imageId);
//				dataManager.setCurrentProcessInfo(processInfo);
//				dataManager.saveProcessInfo(processInfo);
//			}
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

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}


}
