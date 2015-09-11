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
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.ProcessReflect;
import com.jianfanjia.cn.bean.UserByDesignerInfo;
import com.jianfanjia.cn.bean.UserByOwnerInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
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

	private boolean isLogin;// 是否登录
	public SharedPrefer sharedPrefer = null;
	private static DataManager instance;
	private Context context;
	private Map<String, ProcessInfo> processInfos = new HashMap<String, ProcessInfo>();
	private List<ProcessReflect> processReflects = new ArrayList<ProcessReflect>();
	private List<Process> designerProcessLists = new ArrayList<Process>();
	private UserByOwnerInfo ownerInfo;// 当前业主
	private UserByDesignerInfo designerInfo;// 当前设计师

	public static DataManager getInstance() {
		if (instance == null) {
			instance = new DataManager();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public List<Process> getDesignerProcessLists() {
		if (designerProcessLists == null) {
			return (List<Process>) sharedPrefer
					.getValue(Constant.DESIGNER_PROCESS_LIST);
		}
		return designerProcessLists;
	}

	private DataManager() {
		context = MyApplication.getInstance();
		sharedPrefer = new SharedPrefer(context, Constant.SHARED_MAIN);
	}

	public int getDefaultPro() {
		if (getUserType().equals(Constant.IDENTITY_DESIGNER)) {
			return sharedPrefer.getValue(Constant.DEFAULT_PROCESS, 0);// 默认的工地为0
		} else if (getUserType().equals(Constant.IDENTITY_OWNER)) {
			return 0;
		}
		return 0;
	}

	public void setDefaultPro(int defaultPro) {
		sharedPrefer.setValue(Constant.DEFAULT_PROCESS, defaultPro);
	}

	public UserByOwnerInfo getOwnerInfo(String ownerId) {
		if (ownerInfo == null) {
			if (!NetTool.isNetworkAvailable(context)) {
				ownerInfo = (UserByOwnerInfo) sharedPrefer
						.getValue(Constant.OWNER_INFO);
			} else {
				if (ownerId != null) {
					getOwnerInfoById(ownerId);
				}
			}
		}
		return ownerInfo;
	}

	public void requestProcessInfo() {
		requestProcessList();
	}

	public UserByDesignerInfo getDesignerInfo(String designerId) {
		if (designerInfo == null) {
			if (!NetTool.isNetworkAvailable(context)) {
				designerInfo = (UserByDesignerInfo) sharedPrefer
						.getValue(Constant.DESIGNER_INFO);
			} else {
				if (designerId != null) {
					getDesignerInfoById(designerId);
				}
			}
		}
		return designerInfo;
	}

	private void getOwnerInfoById(String ownerId) {
		JianFanJiaApiClient.getOwnerInfoById(context, ownerId,
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
								ownerInfo = JsonParser.jsonToBean(
										response.get(Constant.DATA).toString(),
										UserByOwnerInfo.class);
								sharedPrefer.setValue(Constant.OWNER_INFO,
										ownerInfo);
								setChanged();
								notifyObservers(SUCCESS);
							} else if (response.has(Constant.ERROR_MSG)) {
								MyApplication.getInstance().makeTextLong(
										response.get(Constant.ERROR_MSG)
												.toString());
							}
						} catch (JSONException e) {
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
					};
				});
	}

	private void getDesignerInfoById(String designerId) {
		JianFanJiaApiClient.getDesignerInfoById(context, designerId,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d("getDesignerInfoById", "JSONObject response:"
								+ response);
						try {
							if (response.has(Constant.DATA)) {
								designerInfo = JsonParser.jsonToBean(response
										.get(Constant.DATA).toString(),
										UserByDesignerInfo.class);

								sharedPrefer.setValue(Constant.DESIGNER_INFO,
										designerInfo);
								setChanged();
								notifyObservers(SUCCESS);
							} else if (response.has(Constant.ERROR_MSG)) {
								MyApplication.getInstance().makeTextLong(
										response.get(Constant.ERROR_MSG)
												.toString());
							}
						} catch (JSONException e) {
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
					};
				});
	}

	// 根据id拿到工地流程，通用的
	private ProcessInfo getProcessInfo(String id) {
		if (!processInfos.containsKey(id))
			return null;
		ProcessInfo processInfo = processInfos.get(id);
		if (processInfo == null && !NetTool.isNetworkAvailable(context)) {
			processInfo = (ProcessInfo) sharedPrefer.getValue(id);
		}
		return processInfo;
	}

	public ProcessInfo getDefaultProcessInfo() {
		return getProcessInfo(getDefaultProcessId());
	}

	public String getDefaultDesignerId() {
		if (processReflects.size() == 0) {
			@SuppressWarnings("unchecked")
			List<ProcessReflect> reflects = (List<ProcessReflect>) sharedPrefer
					.getValue(Constant.PROCESSINFO_REFLECT);
			if (reflects != null) {
				processReflects = reflects;
			}
		} else {
			return processReflects.get(getDefaultPro()).getDesignerId();
		}
		return null;
	}

	public String getDefaultOwnerId() {
		if (processReflects.size() == 0) {
			@SuppressWarnings("unchecked")
			List<ProcessReflect> reflects = (List<ProcessReflect>) sharedPrefer
					.getValue(Constant.PROCESSINFO_REFLECT);
			if (reflects != null) {
				processReflects = reflects;
			}
		} else {
			return processReflects.get(getDefaultPro()).getOwnerId();
		}
		return null;
	}

	public String getDefaultProcessId() {
		if (processReflects.size() == 0) {
			@SuppressWarnings("unchecked")
			List<ProcessReflect> reflects = (List<ProcessReflect>) sharedPrefer
					.getValue(Constant.PROCESSINFO_REFLECT);
			if (reflects != null) {
				processReflects = reflects;
			}
		} else {
			return processReflects.get(getDefaultPro()).getProcessId();
		}
		return null;

	}

	public void requestProcessInfoById(String processId) {
		JianFanJiaApiClient.get_ProcessInfo_By_Id(context, processId,
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
								if (null != processInfo) {
									// 把之前的数据清空
									sharedPrefer.setValue(processInfo.get_id(),
											processInfo);// 保存工地流程到本地
									processInfos.put(processInfo.get_id(),
											processInfo);// 保存工地流程在内存中
								}
								setChanged();
								notifyObservers(SUCCESS);
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								setChanged();
								notifyObservers(FAILURE);
								MyApplication.getInstance().makeTextLong(
										response.get(Constant.ERROR_MSG)
												.toString());
							}
						} catch (JSONException e) {
							// 通知页面刷新
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
						// 通知页面刷新
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
						// 通知页面刷新
						setChanged();
						notifyObservers(FAILURE);
						MyApplication
								.getInstance()
								.makeTextLong(
										context.getString(R.string.tip_login_error_for_network));
					}
				});
	}

	/**
	 * 加载工地列表
	 */
	public void requestProcessList() {
		JianFanJiaApiClient.get_Designer_Process_List(context,
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
								designerProcessLists = JsonParser.jsonToList(
										response.get(Constant.DATA).toString(),
										new TypeToken<List<Process>>() {
										}.getType());
								if (designerProcessLists != null) {
									processReflects.clear();
									// 把之前的数据清空
									ProcessReflect processReflect = null;
									for (Process process : designerProcessLists) {
										// 保存工地流程在内存中
										processReflect = new ProcessReflect(
												process.get_id(), process
														.getUserid(), process
														.getFinal_designerid());

										// 重新添加工地列表
										processReflects.add(processReflect);
									}
									// 保存整个工地流程映射在本地
									sharedPrefer.setValue(
											Constant.DESIGNER_PROCESS_LIST,
											designerProcessLists);
									sharedPrefer.setValue(
											Constant.PROCESSINFO_REFLECT,
											processReflects);

									// 默认的工地大于当前获取的工地数，重设工地
									if (getUserType().equals(
											Constant.IDENTITY_OWNER)) {
										setDefaultPro(0);
									} else if (getUserType().equals(
											Constant.IDENTITY_OWNER)) {
										if (getDefaultPro() > processReflects
												.size() - 1) {
											setDefaultPro(0);
										}
									}
									// 如果有工地，加载默认的工地
									if (processReflects.size() > 0) {
										requestProcessInfoById(processReflects
												.get(getDefaultPro())
												.getProcessId());
									} else {
										setChanged();
										notifyObservers(SUCCESS);
									}

								}

								// 保存工地流程
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								setChanged();
								notifyObservers(FAILURE);
								MyApplication.getInstance().makeTextLong(
										response.get(Constant.ERROR_MSG)
												.toString());
							}
						} catch (JSONException e) {
							// 通知页面刷新
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
						// 通知页面刷新
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
						// 通知页面刷新
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
		sharedPrefer.setValue(Constant.USER_ID, userBean.get_id());
	}

	public String getAccount() {
		return sharedPrefer.getValue(Constant.ACCOUNT, null);
	}

	public String getUserType() {
		String userType = sharedPrefer.getValue(Constant.USERTYPE, "1");
		return userType;
	}

	public String getUserId() {
		return sharedPrefer.getValue(Constant.USER_ID, null);
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

	public String getUserImagePath() {
		String userImagePath = null;
		String imageId = sharedPrefer.getValue(Constant.USERIMAGE_ID, null);
		if (imageId == null) {
			if (getUserType().equals(Constant.IDENTITY_OWNER)) {
				userImagePath = Constant.DEFALUT_OWNER_PIC;
			} else if (getUserType().equals(Constant.IDENTITY_DESIGNER)) {
				userImagePath = Constant.DEFALUT_DESIGNER_PIC;
			}
		} else {
			userImagePath = Url.GET_IMAGE + imageId;
		}
		return userImagePath;
	}

}
