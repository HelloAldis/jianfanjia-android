package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.tools.JsonParser;

import java.util.List;

public class ProcessListRequest extends BaseRequest {
	
	private List<Process> processLists;

	public ProcessListRequest(Context context) {
		super(context);
		url = Url_New.GET_PROCESS_LIST;
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
