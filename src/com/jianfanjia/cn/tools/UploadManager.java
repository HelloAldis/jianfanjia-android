package com.jianfanjia.cn.tools;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.text.TextUtils;

import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.UploadImageListener;
import com.jianfanjia.cn.interf.UploadPortraitListener;
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
	 * 用户上传图片到装修流程
	 * 
	 * @param imagePath
	 * @param siteId
	 * @param processId
	 * @param processInfoId
	 * @param uploadImageListener
	 */
	public void uploadProcedureImage(String imagePath, final String siteId,
			final String processId, final String processInfoId,
			final UploadImageListener uploadImageListener) {
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
								if (null != imageid) {
									submitImgToProgress(siteId, processId,
											processInfoId, imageid,
											uploadImageListener);
								}
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
	 * 设计师提交验收图片
	 * 
	 * @param imagePath
	 * @param siteId
	 * @param processId
	 * @param key
	 * @param uploadImageListener
	 */
	public void uploadCheckImage(String imagePath, final String siteId,
			final String processId, final String key,
			final UploadImageListener uploadImageListener) {
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
								if (null != imageid) {
									submitCheckedImg(siteId, processId, key,
											imageid, uploadImageListener);
								}
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
							if (response.has(Constant.SUCCESS_MSG)) {
								JSONObject obj = new JSONObject(response
										.toString());
								String msg = obj
										.getString(Constant.SUCCESS_MSG);
								LogTool.d(TAG, "msg:" + msg);
								uploadImageListener.onSuccess(msg);
							} else if (response.has(Constant.ERROR_MSG)) {
								uploadImageListener.onSuccess(response
										.getString(Constant.ERROR_MSG));
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
							if (response.has(Constant.SUCCESS_MSG)) {
								JSONObject obj = new JSONObject(response
										.toString());
								String msg = obj
										.getString(Constant.SUCCESS_MSG);
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
	 * 上传个人头像
	 * 
	 * @param imgPath
	 */
	public void uploadPortrait(String imgPath,
			final UploadPortraitListener listener) {
		JianFanJiaApiClient.uploadImage(context, imgPath,
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
								if (!TextUtils.isEmpty(imageid)) {
									listener.getImageId(imageid);
								}
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
}
