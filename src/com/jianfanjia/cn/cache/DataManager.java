package com.jianfanjia.cn.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

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
import com.jianfanjia.cn.bean.UserByDesignerInfo;
import com.jianfanjia.cn.bean.UserByOwnerInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.jianfanjia.cn.tools.SharedPrefer;
import com.loopj.android.http.JsonHttpResponseHandler;

public class DataManager extends Observable {
	private static final String TAG = "DataManeger";
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	
	private boolean isLogin;// �Ƿ��¼
	public SharedPrefer sharedPrefer = null;
	private static DataManager instance;
	private Context context;
	private Map<String, ProcessInfo> processInfos = new HashMap<String, ProcessInfo>();
	private List<ProcessInfo> processLists;
	private int defaultPro = 0;//Ĭ�ϵĹ�����0
	private UserByOwnerInfo ownerInfo;//��ǰҵ��
	private UserByDesignerInfo designerInfo;//��ǰ���ʦ
	
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
	
	public int getDefaultPro() {
		return defaultPro;
	}

	public void setDefaultPro(int defaultPro) {
		this.defaultPro = defaultPro;
	}

	public UserByOwnerInfo getOwnerInfo(String ownerId) {
		if(ownerInfo == null){
			if(!NetTool.isNetworkAvailable(context)){
				ownerInfo = (UserByOwnerInfo)sharedPrefer.getValue(Constant.OWNER_INFO);
			}else{
				if(ownerId != null){
					getOwnerInfoById(ownerId);
				}
			}
		}
		return ownerInfo;
	}

	public UserByDesignerInfo getDesignerInfo(String designerId) {
		if(designerInfo == null){
			if(!NetTool.isNetworkAvailable(context)){
				designerInfo = (UserByDesignerInfo)sharedPrefer.getValue(Constant.DESIGNER_INFO);
			}else{
				if(designerId != null){
					getDesignerInfoById(designerId);
				}
			}
		}
		return designerInfo;
	}

	private void getOwnerInfoById(String ownerId){
		JianFanJiaApiClient.getOwnerInfoById(context, ownerId, new JsonHttpResponseHandler() {
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
								ownerInfo = JsonParser.jsonToBean(
										response.get(Constant.DATA).toString(),
										UserByOwnerInfo.class);
								
								sharedPrefer.setValue(Constant.OWNER_INFO,ownerInfo);
								
								setChanged();
								notifyObservers(SUCCESS);
							} else if (response.has(Constant.ERROR_MSG)) {
								MyApplication.getInstance().makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							e.printStackTrace();
							MyApplication.getInstance().makeTextLong(context.getString(R.string.tip_login_error_for_network));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						MyApplication.getInstance().makeTextLong(context.getString(R.string.tip_login_error_for_network));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						MyApplication.getInstance().makeTextLong(context.getString(R.string.tip_login_error_for_network));
					};
				});
	}
	
	private void getDesignerInfoById(String designerId){
		JianFanJiaApiClient.getOwnerInfoById(context, designerId, new JsonHttpResponseHandler() {
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
						designerInfo = JsonParser.jsonToBean(
								response.get(Constant.DATA).toString(),
								UserByDesignerInfo.class);
						
						sharedPrefer.setValue(Constant.DESIGNER_INFO,designerInfo);
						setChanged();
						notifyObservers(SUCCESS);
					} else if (response.has(Constant.ERROR_MSG)) {
						MyApplication.getInstance().makeTextLong(response.get(Constant.ERROR_MSG)
								.toString());
					}
				} catch (JSONException e) {
					e.printStackTrace();
					MyApplication.getInstance().makeTextLong(context.getString(R.string.tip_login_error_for_network));
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LogTool.d(TAG,
						"Throwable throwable:" + throwable.toString());
				MyApplication.getInstance().makeTextLong(context.getString(R.string.tip_login_error_for_network));
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				LogTool.d(TAG, "throwable:" + throwable);
				MyApplication.getInstance().makeTextLong(context.getString(R.string.tip_login_error_for_network));
			};
		});
	}
	

	// ����id�õ��������̣�ͨ�õ�
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

	public void requestOwnerProcessInfo() {
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
								// ���湤�����̵�����
								sharedPrefer.setValue(processInfo.get_id(),
										processInfo);
								// ���湤���������ڴ���
								processInfos.put(processInfo.get_id(),
										processInfo);
								// ���湤��id�ڱ���
								sharedPrefer.setValue(Constant.PROCESSINFO_ID,
										processInfo.get_id());
								// ����ҵ�������ʦid
								sharedPrefer.setValue(
										Constant.FINAL_DESIGNER_ID,
										processInfo.getFinal_designerid());
								sharedPrefer.setValue(Constant.FINAL_OWNER_ID,processInfo.getUserid());
								// ֪ͨҳ��ˢ��
								setChanged();
								notifyObservers(SUCCESS);
							} else if (response.has(Constant.ERROR_MSG)) {
								// ֪ͨҳ��ˢ��
								setChanged();
								notifyObservers(FAILURE);
								MyApplication.getInstance().makeTextLong(
										response.get(Constant.ERROR_MSG)
												.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							// ֪ͨҳ��ˢ��
							setChanged();
							notifyObservers(FAILURE);
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
						// ֪ͨҳ��ˢ��
						setChanged();
						notifyObservers(FAILURE);
						MyApplication
								.getInstance()
								.makeTextLong(
										context.getString(R.string.tip_login_error_for_network));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						// ֪ͨҳ��ˢ��
						setChanged();
						notifyObservers(FAILURE);
						MyApplication
								.getInstance()
								.makeTextLong(
										context.getString(R.string.tip_login_error_for_network));
					}
				});
	}

	public void getDesignerProcessInfo() {
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
									// ���湤���������ڴ���
									processInfos.put(processInfo.get_id(),
											processInfo);
									// ���湤�������ڱ���
									processInfos.put(processInfo.get_id(),
											processInfo);
									sharedPrefer.setValue(processInfo.get_id(),
											processInfo);
								}
								// �����������������ڱ���
								sharedPrefer
										.setValue(Constant.PROCESSINFO_LIST,
												processLists);
								//���浱ǰ���ʦ��ҵ����id
								sharedPrefer.setValue(Constant.FINAL_OWNER_ID,processLists.get(defaultPro).getUserid());
								sharedPrefer.setValue(Constant.FINAL_DESIGNER_ID,processLists.get(defaultPro).getFinal_designerid());
								// ֪ͨҳ��ˢ��
								setChanged();
								notifyObservers(SUCCESS);
								// ���湤������
							} else if (response.has(Constant.ERROR_MSG)) {
								// ֪ͨҳ��ˢ��
								setChanged();
								notifyObservers(FAILURE);
								MyApplication.getInstance().makeTextLong(
										response.get(Constant.ERROR_MSG)
												.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							// ֪ͨҳ��ˢ��
							setChanged();
							notifyObservers(FAILURE);
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
						// ֪ͨҳ��ˢ��
						setChanged();
						notifyObservers(FAILURE);
						MyApplication
								.getInstance()
								.makeTextLong(
										context.getString(R.string.tip_login_error_for_network));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						// ֪ͨҳ��ˢ��
						setChanged();
						notifyObservers(FAILURE);
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
