package com.jianfanjia.cn.http;

import java.util.Locale;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 
 * @ClassName: HttpRestClient
 * @Description: http«Î«Û¿‡
 * @author fengliang
 * @date 2015-8-18 œ¬ŒÁ1:18:08
 * 
 */
public class HttpRestClient {
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	static{
		client.addHeader("Content-Type", "application/json;charset=utf-8");
		client.addHeader("Accept-Language", Locale.getDefault().toString());
	}
	
	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
	}

	public static void get(Context context, String url,
			AsyncHttpResponseHandler responseHandler) {
		client.get(context, url, responseHandler);
	}

	public static void post(String url, AsyncHttpResponseHandler responseHandler) {
		client.post(url, responseHandler);
	}

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
	}

	public static void get(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(context, url, params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
		LogTool.d("HttpRestClient", new StringBuilder("Post" ).append(url).append("&")
                .append(params).toString());
	}
	
	public static void post(Context context,String url,HttpEntity entity,String contentType,AsyncHttpResponseHandler responseHandler){
		client.post(context, url, entity, contentType, responseHandler);
		LogTool.d("HttpRestClient", new StringBuilder("Post" ).append(url).append("&")
                .append(entity.toString()).toString());
	}

	
	public static void post(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(context, url, params, responseHandler);
	}
}
