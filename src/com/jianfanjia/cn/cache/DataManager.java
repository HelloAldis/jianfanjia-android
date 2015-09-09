package com.jianfanjia.cn.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.DesignerSiteInfo;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.LoadCallBack;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.jianfanjia.cn.tools.SharedPrefer;
import com.loopj.android.http.JsonHttpResponseHandler;

public class DataManager {

	protected static final String TAG = "DataManeger";
	private boolean isLogin;// �Ƿ��¼
	protected SharedPrefer sharedPrefer = null;
	private static DataManager instance;
	private Context context;
	private Map<String, ProcessInfo> processInfos = new HashMap<String, ProcessInfo>();
	private List<ProcessInfo> processLists;

	public static DataManager getInstance() {
		if (instance == null) {
			instance = new DataManager();
		}
		return instance;
	}

	private DataManager() {
		context = MyApplication.getInstance();
		sharedPrefer = new SharedPrefer(context, Constant.SHARED_MAIN);
	}

	public ProcessInfo getProcessInfo(String id) {
		ProcessInfo processInfo = processInfos.get(id);
		if (processInfo == null && !NetTool.isNetworkAvailable(context)) {
			processInfo = (ProcessInfo) sharedPrefer.getValue(id);
		}
		return processInfo;
	}

	@SuppressWarnings("unchecked")
	public List<ProcessInfo> getProcessInfoList() {
		if (processLists == null) {
			processLists = (ArrayList<ProcessInfo>) sharedPrefer
					.getValue(Constant.PROCESSINFO_LIST);
		}
		return processLists;
	}

	public void requestOwnerProcessInfo(final LoadCallBack loadCallBack) {
		JianFanJiaApiClient.get_Owner_Process(context,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						try {
							if (response.has(Constant.DATA)
									&& response.get(Constant.DATA) != null) {
								ProcessInfo processInfo = JsonParser
										.jsonToBean(response.get(Constant.DATA)
												.toString(), ProcessInfo.class);
								// ���湤������
								sharedPrefer.setValue(processInfo.get_id(),
										processInfo);
								processInfos.put(processInfo.get_id(),
										processInfo);
								sharedPrefer.setValue(Constant.PROCESSINFO_ID,
										processInfo.get_id());
								// ����ҵ�������ʦid
								sharedPrefer.setValue(
										Constant.FINAL_DESIGNER_ID,
										processInfo.getFinal_designerid());
								// ���سɹ��ص�
								loadCallBack.loadSuccess();
							} else if (response.has(Constant.ERROR_MSG)) {
								MyApplication.getInstance().makeTextLong(
										response.get(Constant.ERROR_MSG)
												.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							MyApplication
									.getInstance()
									.makeTextLong(
											context.getString(R.string.tip_login_error_for_network));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						MyApplication
								.getInstance()
								.makeTextLong(
										context.getString(R.string.tip_login_error_for_network));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						MyApplication
								.getInstance()
								.makeTextLong(
										context.getString(R.string.tip_login_error_for_network));
					}
				});
	}

	public void getDesignerProcessInfo(final LoadCallBack loadCallBack) {
		JianFanJiaApiClient.get_Designer_Info(context,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						try {
							if (response.has(Constant.DATA)
									&& response.get(Constant.DATA) != null) {
								processLists = JsonParser
										.jsonToList(
												response.get(Constant.DATA)
														.toString(),
												new TypeToken<List<DesignerSiteInfo>>() {
												}.getType());

								for (ProcessInfo processInfo : processLists) {
									processInfos.put(processInfo.get_id(),
											processInfo);
									sharedPrefer.setValue(processInfo.get_id(),
											processInfo);
								}
								// ���湤������
								sharedPrefer
										.setValue(Constant.PROCESSINFO_LIST,
												processLists);
								// ���سɹ��ص�
								loadCallBack.loadSuccess();
							} else if (response.has(Constant.ERROR_MSG)) {
								MyApplication.getInstance().makeTextLong(
										response.get(Constant.ERROR_MSG)
												.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							MyApplication
									.getInstance()
									.makeTextLong(
											context.getString(R.string.tip_login_error_for_network));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						MyApplication
								.getInstance()
								.makeTextLong(
										context.getString(R.string.tip_login_error_for_network));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						MyApplication
								.getInstance()
								.makeTextLong(
										context.getString(R.string.tip_login_error_for_network));
					}
				});

	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public void saveLoginUserInfo(LoginUserBean userBean) {
		sharedPrefer.setValue(Constant.ACCOUNT, userBean.getPhone());
		sharedPrefer.setValue(Constant.USERTYPE, userBean.getUsertype());
		sharedPrefer.setValue(Constant.USERNAME, userBean.getUsername());
		sharedPrefer.setValue(Constant.USERIMAGE_ID, userBean.getImageId());
	}

	public String getAccount() {
		return sharedPrefer.getValue(Constant.ACCOUNT, null);
	}

	public String getUserType() {
		String userType = sharedPrefer.getValue(Constant.USERTYPE, "1");
		return userType;
	}

	public String getUserName() {
		String userName = sharedPrefer.getValue(Constant.USERNAME, null);
		if (userName == null) {
			if (getUserType().equals(Constant.IDENTITY_OWNER)) {
				return context.getString(R.string.ower);
			} else if (getUserType().equals(Constant.IDENTITY_DESIGNER)) {
				return context.getString(R.string.designer);
			}
		}
		return userName;
	}

	public String getUserImageId() {
		String imageId = sharedPrefer.getValue(Constant.USERIMAGE_ID, null);
		if (imageId == null) {
			if (getUserType().equals(Constant.IDENTITY_OWNER)) {
				return Constant.DEFALUT_OWNER_PIC;
			} else if (getUserType().equals(Constant.IDENTITY_DESIGNER)) {
				return Constant.DEFALUT_DESIGNER_PIC;
			}
		}
		return imageId;
	}

}
