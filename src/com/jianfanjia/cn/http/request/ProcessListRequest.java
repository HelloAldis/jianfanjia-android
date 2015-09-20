package com.jianfanjia.cn.http.request;

import java.util.List;
import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.tools.JsonParser;

public class ProcessListRequest extends BaseRequest {
	
	private List<Process> processLists;

	public ProcessListRequest(Context context) {
		super(context);
	}
	
	@Override
	public void onSuccess(BaseResponse baseResponse) {
		// TODO Auto-generated method stub
		super.onSuccess(baseResponse);
		String data = baseResponse.getData().toString();
		if(data != null){
			processLists = JsonParser.jsonToList(data,
					new TypeToken<List<Process>>() {
					}.getType());
			dataManager.setProcessLists(processLists);
			dataManager.saveProcessLists(data);
		}
	}
	
	

}
