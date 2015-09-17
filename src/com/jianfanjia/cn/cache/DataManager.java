package com.jianfanjia.cn.cache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.AppConfig;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.bean.MyDesignerInfo;
import com.jianfanjia.cn.bean.MyOwnerInfo;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.ProcessReflect;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.jianfanjia.cn.tools.SharedPrefer;
import com.loopj.android.http.JsonHttpResponseHandler;

public class DataManager {
	private static final String TAG = DataManager.class.getName();
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	private static DataManager instance;
	private Context context;

	private boolean isLogin;// 是否登录
	public SharedPrefer sharedPrefer = null;
	private Map<String, ProcessInfo> processInfos = new HashMap<String, ProcessInfo>();
	private List<ProcessReflect> processReflects = new ArrayList<ProcessReflect>();
	private List<Process> designerProcessLists;
	private MyOwnerInfo myOwnerInfo;// 我的业主信息
	private MyDesignerInfo myDesignerInfo;// 我的设计师信息

	private OwnerInfo ownerInfo;// 业主的个人信息
	private DesignerInfo designerInfo;// 设计师的个人信息

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

	@SuppressWarnings("unchecked")
	public List<Process> getDesignerProcessLists() {
		if (!NetTool.isNetworkAvailable(context)) {
			designerProcessLists = (List<Process>) sharedPrefer
					.getValue(Constant.DESIGNER_PROCESS_LIST);
		}
		return designerProcessLists;
	}

	// 通过业主id拿到工地信息
	public ProcessInfo getProcessInfoByOwnerId(String ownerId) {
		if (processReflects.size() == 0) {
			@SuppressWarnings("unchecked")
			List<ProcessReflect> reflects = (List<ProcessReflect>) sharedPrefer
					.getValue(Constant.PROCESSINFO_REFLECT);
			if (reflects != null) {
				processReflects = reflects;
			}
		}
		String processInfoId = null;
		for (int i = 0; i < processReflects.size(); i++) {
			processInfoId = processReflects.get(i).getProcessId();
			if (ownerId.equals(processReflects.get(i).getOwnerId())) {
				break;
			}
		}
		if (processInfoId != null) {
			return getProcessInfo(processInfoId);
		}
		return null;
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

	// 设计师用户获取个人资料
	public DesignerInfo getDesignerInfo() {
		if (designerInfo == null && !NetTool.isNetworkAvailable(context)) {
			designerInfo = (DesignerInfo) sharedPrefer
					.getValue(Constant.DESIGNER_INFO);
		}
		return designerInfo;
	}

	// 业主用户获取个人资料
	public OwnerInfo getOwnerInfo() {
		if (ownerInfo == null && !NetTool.isNetworkAvailable(context)) {
			ownerInfo = (OwnerInfo) sharedPrefer.getValue(Constant.OWNER_INFO);
		}
		return ownerInfo;
	}

	public void setOwnerInfo(OwnerInfo ownerInfo) {
		sharedPrefer.setValue(Constant.OWNER_INFO, ownerInfo);
		this.ownerInfo = ownerInfo;
	}

	public void setDesignerInfo(DesignerInfo designerInfo) {
		sharedPrefer.setValue(Constant.DESIGNER_INFO, designerInfo);
		this.designerInfo = designerInfo;
	}

	public MyOwnerInfo getOwnerInfo(String ownerId) {
		if (ownerId == null)
			return null;
		if (myOwnerInfo == null) {
			if (!NetTool.isNetworkAvailable(context)) {
				myOwnerInfo = (MyOwnerInfo) sharedPrefer.getValue(ownerId);
			}
		}
		return myOwnerInfo;
	}

	public MyDesignerInfo getDesignerInfo(String designerId) {
		if (designerId == null)
			return null;
		if (myDesignerInfo == null) {
			if (!NetTool.isNetworkAvailable(context)) {
				myDesignerInfo = (MyDesignerInfo) sharedPrefer
						.getValue(designerId);
			}
		}
		return myDesignerInfo;
	}

	public void getOwnerInfoById(String ownerId, final LoadDataListener listener) {
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
								myOwnerInfo = JsonParser.jsonToBean(response
										.get(Constant.DATA).toString(),
										MyOwnerInfo.class);
								if (myOwnerInfo != null) {
									sharedPrefer.setValue(myOwnerInfo.get_id(),
											myOwnerInfo);
								}
								listener.loadSuccess();
							} else if (response.has(Constant.ERROR_MSG)) {
								listener.loadFailture();
								MyApplication.getInstance().makeTextLong(
										response.get(Constant.ERROR_MSG)
												.toString());
							}
						} catch (JSONException e) {
							listener.loadFailture();
							e.printStackTrace();
							MyApplication.getInstance().makeTextLong(
									context.getString(R.string.load_failure));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						listener.loadFailture();
						MyApplication.getInstance().makeTextLong(
								context.getString(R.string.tip_no_internet));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						listener.loadFailture();
						MyApplication.getInstance().makeTextLong(
								context.getString(R.string.tip_no_internet));
					};
				});
	}

	public void getDesignerInfoById(String designerId,
			final LoadDataListener listener) {
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
								myDesignerInfo = JsonParser.jsonToBean(response
										.get(Constant.DATA).toString(),
										MyDesignerInfo.class);
								if (myDesignerInfo != null) {
									sharedPrefer.setValue(
											myDesignerInfo.get_id(),
											myDesignerInfo);
								}
								listener.loadSuccess();
							} else if (response.has(Constant.ERROR_MSG)) {
								listener.loadFailture();
								MyApplication.getInstance().makeTextLong(
										response.get(Constant.ERROR_MSG)
												.toString());
							}
						} catch (JSONException e) {
							listener.loadFailture();
							e.printStackTrace();
							MyApplication.getInstance().makeTextLong(
									context.getString(R.string.load_failure));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						listener.loadFailture();
						MyApplication.getInstance().makeTextLong(
								context.getString(R.string.tip_no_internet));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						listener.loadFailture();
						MyApplication.getInstance().makeTextLong(
								context.getString(R.string.tip_no_internet));
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

	public void requestProcessInfoById(String processId,
			final LoadDataListener listener) {
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
								listener.loadSuccess();
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								listener.loadFailture();
								MyApplication.getInstance().makeTextLong(
										response.get(Constant.ERROR_MSG)
												.toString());
							}
						} catch (JSONException e) {
							// 通知页面刷新
							listener.loadFailture();
							e.printStackTrace();
							MyApplication.getInstance().makeTextLong(
									context.getString(R.string.load_failure));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						// 通知页面刷新
						listener.loadFailture();
						MyApplication.getInstance().makeTextLong(
								context.getString(R.string.tip_no_internet));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						// 通知页面刷新
						listener.loadFailture();
						MyApplication.getInstance().makeTextLong(
								context.getString(R.string.tip_no_internet));
					}
				});
	}

	/**
	 * 加载工地列表
	 */
	public void requestProcessList(final LoadDataListener listener) {
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
									Log.i(TAG, "designerProcessLists size ="
											+ designerProcessLists.size());
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
									if (getDefaultPro() > processReflects
											.size() - 1) {
										setDefaultPro(0);
									}
									// 如果有工地，加载默认的工地
									if (processReflects.size() > 0) {
										requestProcessInfoById(processReflects
												.get(getDefaultPro())
												.getProcessId(), listener);
									} else {
										listener.loadSuccess();
									}
								}
								// 保存工地流程
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								listener.loadFailture();
								MyApplication.getInstance().makeTextLong(
										response.get(Constant.ERROR_MSG)
												.toString());
							}
						} catch (JSONException e) {
							// 通知页面刷新
							listener.loadFailture();
							e.printStackTrace();
							MyApplication.getInstance().makeTextLong(
									context.getString(R.string.load_failure));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						// 通知页面刷新
						listener.loadFailture();
						MyApplication.getInstance().makeTextLong(
								context.getString(R.string.tip_no_internet));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						// 通知页面刷新
						listener.loadFailture();
						MyApplication.getInstance().makeTextLong(
								context.getString(R.string.tip_no_internet));
					}
				});

	}

	public void login(String name, String password,
			final LoadDataListener listener) {
		JianFanJiaApiClient.login(context, name, password,
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
								AppConfig.getInstance(context)
										.savaLastLoginTime(
												Calendar.getInstance()
														.getTimeInMillis());
								LoginUserBean loginUserBean = JsonParser
										.jsonToBean(response.get(Constant.DATA)
												.toString(),
												LoginUserBean.class);
								LogTool.d(TAG, "loginUserBean:" + loginUserBean);
								saveLoginUserInfo(loginUserBean);
								setLogin(true);
								listener.loadSuccess();
							} else if (response.has(Constant.ERROR_MSG)) {
								listener.loadFailture();
								MyApplication.getInstance().makeTextLong(
										response.get(Constant.ERROR_MSG)
												.toString());
							}
						} catch (JSONException e) {
							e.printStackTrace();
							listener.loadFailture();
							MyApplication.getInstance().makeTextLong(
									context.getString(R.string.load_failure));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						listener.loadFailture();
						MyApplication.getInstance().makeTextLong(
								context.getString(R.string.tip_no_internet));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						listener.loadFailture();
						MyApplication.getInstance().makeTextLong(
								context.getString(R.string.tip_no_internet));
					};
				});
	}

	public boolean isLogin() {
		return sharedPrefer.getValue(Constant.USER_IS_LOGIN, false);// 默认是没登录
	}

	public void setLogin(boolean isLogin) {
		sharedPrefer.setValue(Constant.USER_IS_LOGIN, isLogin);
	}

	public void saveLoginUserInfo(LoginUserBean userBean) {
		sharedPrefer.setValue(Constant.ACCOUNT, userBean.getPhone());
		sharedPrefer.setValue(Constant.USERTYPE, userBean.getUsertype());
		sharedPrefer.setValue(Constant.USERNAME, userBean.getUsername());
		sharedPrefer.setValue(Constant.USERIMAGE_ID, userBean.getImageid());
		sharedPrefer.setValue(Constant.USER_ID, userBean.get_id());
	}

	public void savePassword(String pass) {
		sharedPrefer.setValue(Constant.PASSWORD, pass);
	}

	public String getPassword() {
		return sharedPrefer.getValue(Constant.PASSWORD, null);
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

	public void cleanData() {
		processInfos.clear();
		processReflects.clear();
		designerProcessLists = null;
		myOwnerInfo = null;
		myDesignerInfo = null;
	}

}
