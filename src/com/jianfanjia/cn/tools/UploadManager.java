package com.jianfanjia.cn.tools;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.GetImageListener;
import com.jianfanjia.cn.interf.UploadImageListener;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UploadManager {
	private static final String TAG = UploadManager.class.getName();
	private Context context;
	private static UploadManager uploadManager;

	public static UploadManager getUploadManager(Context context) {
		if (null == uploadManager) {
			uploadManager = new UploadManager(context);
		}
		return uploadManager;
	}

	public UploadManager(Context context) {
		this.context = context;
	}

	/**
	 * 用户上传图片
	 * 
	 * @param imagePath
	 */
	public void getImageIdByUpload(String imagePath,
			final GetImageListener getImageListener) {
		JianFanJiaApiClient.uploadImage(context, imagePath,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						try {
							if (response.has(Constant.DATA)) {
								JSONObject obj = new JSONObject(response
										.toString());
								String imageid = obj.getString("data");
								LogTool.d(TAG, "imageid:" + imageid);
								getImageListener.getImageId(imageid);
							} else if (response.has(Constant.ERROR_MSG)) {

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG, "Throwable throwable:" + throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "statusCode:" + statusCode
								+ " throwable:" + throwable);
					};
				});
	}

	/**
	 * 用户上传图片到装修流程
	 * 
	 * @param siteId
	 * @param processId
	 * @param processInfoId
	 * @param imageid
	 */
	public void submitImgToProgress(String siteId, String processId,
			String processInfoId, String imageid,
			final UploadImageListener uploadImageListener) {
		LogTool.d(TAG, "siteId:" + siteId + " processId:" + processId
				+ " processInfoId:" + processInfoId + " imageid:" + imageid);
		JianFanJiaApiClient.submitImageToProcess(context, siteId, processId,
				processInfoId, imageid, new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						try {
							if (response.has(Constant.DATA)) {
								JSONObject obj = new JSONObject(response
										.toString());
								String msg = obj.getString(Constant.DATA);
								LogTool.d(TAG, "msg:" + msg);
								uploadImageListener.onSuccess(msg);
							} else if (response.has(Constant.ERROR_MSG)) {

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG, "Throwable throwable:" + throwable);
						uploadImageListener.onFailure();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "statusCode:" + statusCode
								+ " throwable:" + throwable);
						uploadImageListener.onFailure();
					};
				});
	}

	/**
	 * 设计师提交验收图片
	 * 
	 * @param siteId
	 * @param processId
	 * @param key
	 * @param imageid
	 */
	public void submitCheckedImg(String siteId, String processId, String key,
			String imageId, final UploadImageListener uploadImageListener) {
		JianFanJiaApiClient.submitYanShouImage(context, siteId, processId, key,
				imageId, new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						try {
							if (response.has(Constant.DATA)) {
								JSONObject obj = new JSONObject(response
										.toString());
								String msg = obj.getString(Constant.DATA);
								LogTool.d(TAG, "msg:" + msg);
								uploadImageListener.onSuccess(msg);
							} else if (response.has(Constant.ERROR_MSG)) {

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG, "Throwable throwable:" + throwable);
						uploadImageListener.onFailure();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "statusCode:" + statusCode
								+ " throwable:" + throwable);
						uploadImageListener.onFailure();
					};
				});
	}

}
