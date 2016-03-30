package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.bean.Process;
import com.jianfanjia.cn.designer.tools.JsonParser;

import java.util.List;

public class GetProcessListRequest extends BaseRequest {
	
	private List<Process> processLists;

	public GetProcessListRequest(Context context) {
		super(context);
		url = url_new.GET_PROCESS_LIST;
	}
	
	@Override
	public void pre() {
		super.pre();
	}
	
	@Override
	public void onSuccess(Object data) {
		super.onSuccess(data);
		if(data != null){
			processLists = JsonParser.jsonToList((String)data,
					new TypeToken<List<Process>>() {
					}.getType());

			if(processLists != null){
				dataManager.setProcessLists(processLists);
				dataManager.saveProcessLists((String)data);
			}
		}
	}
	

}
