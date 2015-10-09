package com.jianfanjia.cn.http;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Looper;
import com.loopj.android.http.JsonHttpResponseHandler;

public class OperationJsonHttpResponseHandler extends JsonHttpResponseHandler {

	public OperationJsonHttpResponseHandler(Looper looper) {

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			Throwable throwable, JSONArray errorResponse) {
		// TODO Auto-generated method stub
		super.onFailure(statusCode, headers, throwable, errorResponse);
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			Throwable throwable, JSONObject errorResponse) {
		// TODO Auto-generated method stub
		super.onFailure(statusCode, headers, throwable, errorResponse);
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
		// TODO Auto-generated method stub
		super.onSuccess(statusCode, headers, response);
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		// TODO Auto-generated method stub
		super.onSuccess(statusCode, headers, response);
	}

}
