package com.jianfanjia.cn.http;

import org.apache.http.HttpEntity;
import android.content.Context;
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

	static {
		client.setConnectTimeout(10);
	}
	
	public static AsyncHttpClient getHttpClient(){
		return client;
	}

	// -----------------------------get-------------------------------------
	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
	}

	public static void get(Context context, String url,
			AsyncHttpResponseHandler responseHandler) {
		client.get(context, url, responseHandler);
	}

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
	}

	public static void get(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(context, url, params, responseHandler);
	}

	// -----------------------------post-------------------------------------
	public static void post(String url, AsyncHttpResponseHandler responseHandler) {
		client.post(url, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}

	public static void post(Context context, String url, HttpEntity entity,
			String contentType, AsyncHttpResponseHandler responseHandler) {
		client.post(context, url, entity, contentType, responseHandler);
	}

	public static void post(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(context, url, params, responseHandler);
	}

	public static void put(Context context, String url, HttpEntity entity,
			String contentType, AsyncHttpResponseHandler responseHandler) {
		client.put(context, url, entity, contentType, responseHandler);
	}

	public static void put(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.put(context, url, params, responseHandler);
	}

	public static void setCookie(String cookie) {
		client.addHeader("Cookie", cookie);
	}

}
