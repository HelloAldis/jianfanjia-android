package com.jianfanjia.cn.http;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.request.AddPicToCheckRequest;
import com.jianfanjia.cn.http.request.AddPicToSectionItemRequest;
import com.jianfanjia.cn.http.request.CommitCommentRequest;
import com.jianfanjia.cn.http.request.DeletePicRequest;
import com.jianfanjia.cn.http.request.DesignerInfoRequest;
import com.jianfanjia.cn.http.request.GetRequirementRequest;
import com.jianfanjia.cn.http.request.LoginRequest;
import com.jianfanjia.cn.http.request.LogoutRequest;
import com.jianfanjia.cn.http.request.OwnerInfoRequest;
import com.jianfanjia.cn.http.request.PostRequirementRequest;
import com.jianfanjia.cn.http.request.ProcessInfoRequest;
import com.jianfanjia.cn.http.request.ProcessListRequest;
import com.jianfanjia.cn.http.request.TotalDurationRequest;
import com.jianfanjia.cn.http.request.UploadPicRequest;
import com.jianfanjia.cn.http.request.UploadPicRequestNew;
import com.jianfanjia.cn.http.request.UserByDesignerInfoRequest;
import com.jianfanjia.cn.http.request.UserByDesignerInfoUpdateRequest;
import com.jianfanjia.cn.http.request.UserByOwnerInfoRequest;
import com.jianfanjia.cn.http.request.UserByOwnerInfoUpdateRequest;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;

public class LoadClientHelper {
	private static final String TAG = LoadClientHelper.class.getName();

	/**
	 * 用户登录
	 * 
	 * @param context
	 * @param loginRequest
	 * @param listener
	 */
	public static void login(final Context context,
			final LoginRequest loginRequest, final LoadDataListener listener) {
		JianFanJiaApiClient.login(context, loginRequest.getUsername(),
				loginRequest.getPassword(), new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						loginRequest.pre();
						if (listener != null) {
							listener.preLoad();
						}
						loginRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						loginRequest.all();
						LogTool.d(TAG, "JSONObject response:" + response);
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.DATA)
									&& response.get(Constant.DATA) != null) {
								LoginUserBean loginUserBean = JsonParser
										.jsonToBean(response.get(Constant.DATA)
												.toString(),
												LoginUserBean.class);
								LogTool.d(TAG, "loginUserBean:" + loginUserBean);
								baseResponse.setData(loginUserBean);
								loginRequest.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								loginRequest.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						if (listener != null) {
							listener.loadFailture();
						}
					}

				});
	}

	/**
	 * 用户登出
	 * 
	 * @param context
	 * @param logoutRequest
	 * @param listener
	 */
	public static void logout(final Context context,
			final LogoutRequest logoutRequest, final LoadDataListener listener) {
		JianFanJiaApiClient.logout(context, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				LogTool.d(TAG, "onStart()");
				if (listener != null) {
					listener.preLoad();
				}
				logoutRequest.pre();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				LogTool.d(TAG, "response:" + response.toString());
				BaseResponse baseResponse = new BaseResponse();
				try {
					if (response.has(Constant.SUCCESS_MSG)) {
						baseResponse.setMsg(response.get(Constant.SUCCESS_MSG)
								.toString());
						logoutRequest.onSuccess(baseResponse);
						if (listener != null) {
							listener.loadSuccess();
						}
					} else if (response.has(Constant.ERROR_MSG)) {
						// 通知页面刷新
						baseResponse.setErr_msg(response
								.get(Constant.ERROR_MSG).toString());
						logoutRequest.onFailure(baseResponse);
						if (listener != null) {
							listener.loadFailture();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if (listener != null) {
						listener.loadFailture();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LogTool.d(TAG, "Throwable throwable:" + throwable.toString());
				if (listener != null) {
					listener.loadFailture();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				LogTool.d(TAG, "throwable:" + throwable);
				if (listener != null) {
					listener.loadFailture();
				}
			};
		});
	}

	/**
	 * 加载工地列表
	 */
	public static void requestProcessList(final Context context,
			final ProcessListRequest proListRequest,
			final LoadDataListener listener) {
		JianFanJiaApiClient.get_Designer_Process_List(context,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						proListRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.DATA)
									&& response.get(Constant.DATA) != null) {
								baseResponse.setData(response
										.get(Constant.DATA).toString());
								proListRequest.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
								// 保存工地流程
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								proListRequest.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							// 通知页面刷新
							if (listener != null) {
								listener.loadFailture();
							}
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						// 通知页面刷新
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						// 通知页面刷新
						if (listener != null) {
							listener.loadFailture();
						}
					}
				});

	}

	/**
	 * 加载某个工地信息
	 * 
	 * @param context
	 * @param processInfoRequest
	 * @param listener
	 */
	public static void requestProcessInfoById(final Context context,
			final ProcessInfoRequest processInfoRequest,
			final LoadDataListener listener) {
		JianFanJiaApiClient.get_ProcessInfo_By_Id(context,
				processInfoRequest.getProcessId(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						processInfoRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.DATA)
									&& response.get(Constant.DATA) != null) {
								baseResponse.setData(response
										.get(Constant.DATA).toString());
								processInfoRequest.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								processInfoRequest.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							// 通知页面刷新
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						// 通知页面刷新
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						// 通知页面刷新
						if (listener != null) {
							listener.loadFailture();
						}
					}
				});
	}

	/**
	 * 加载某个业主信息
	 * 
	 * @param context
	 * @param ownerInfoRequest
	 * @param listener
	 */
	public static void getOwnerInfoById(final Context context,
			final OwnerInfoRequest ownerInfoRequest,
			final LoadDataListener listener) {
		JianFanJiaApiClient.getOwnerInfoById(context,
				ownerInfoRequest.getOwnerId(), new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						ownerInfoRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.DATA)
									&& response.get(Constant.DATA) != null) {
								baseResponse.setData(response
										.get(Constant.DATA).toString());
								ownerInfoRequest.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								ownerInfoRequest.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					};
				});
	}

	/**
	 * 记载某个设计师信息
	 * 
	 * @param context
	 * @param designerInfoRequest
	 * @param listener
	 */
	public static void getDesignerInfoById(final Context context,
			final DesignerInfoRequest designerInfoRequest,
			final LoadDataListener listener) {
		JianFanJiaApiClient.getDesignerInfoById(context,
				designerInfoRequest.getDesignerId(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						designerInfoRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d("getDesignerInfoById", "JSONObject response:"
								+ response);
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.DATA)
									&& response.get(Constant.DATA) != null) {
								baseResponse.setData(response
										.get(Constant.DATA).toString());
								designerInfoRequest.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								designerInfoRequest.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					};
				});
	}

	/**
	 * 提交某个评论
	 * 
	 * @param context
	 * @param commitCommentRequest
	 * @param listener
	 */
	public static void postCommentInfo(final Context context,
			final CommitCommentRequest commitCommentRequest,
			final LoadDataListener listener) {
		JianFanJiaApiClient.comment(context,
				commitCommentRequest.getCommitCommentInfo(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						commitCommentRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.SUCCESS_MSG)) {
								baseResponse.setMsg(response.get(
										Constant.SUCCESS_MSG).toString());
								commitCommentRequest.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								commitCommentRequest.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					};
				});
	}

	/**
	 * 加载设计师用户的用户信息
	 * 
	 * @param context
	 * @param userByDesignerInfoRequest
	 * @param listener
	 */
	public static void getUserInfoByDesigner(final Context context,
			final UserByDesignerInfoRequest userByDesignerInfoRequest,
			final LoadDataListener listener) {
		JianFanJiaApiClient.get_Designer_Info(context,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						userByDesignerInfoRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.DATA)
									&& response.get(Constant.DATA) != null) {
								baseResponse.setData(response
										.get(Constant.DATA).toString());
								userByDesignerInfoRequest
										.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								userByDesignerInfoRequest
										.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					};
				});
	}

	/**
	 * 加载业主用户的用户信息
	 * 
	 * @param context
	 * @param userByOwnerInfoRequest
	 * @param listener
	 */
	public static void getUserInfoByOwner(final Context context,
			final UserByOwnerInfoRequest userByOwnerInfoRequest,
			final LoadDataListener listener) {
		JianFanJiaApiClient.get_Owner_Info(context,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						userByOwnerInfoRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.DATA)
									&& response.get(Constant.DATA) != null) {
								baseResponse.setData(response
										.get(Constant.DATA).toString());
								userByOwnerInfoRequest.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								userByOwnerInfoRequest.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					};
				});
	}

	/**
	 * 更新业主个人信息
	 * 
	 * @param context
	 * @param userByOwnerInfoUpdateRequest
	 * @param listener
	 */
	public static void postOwnerUpdateInfo(final Context context,
			final UserByOwnerInfoUpdateRequest userByOwnerInfoUpdateRequest,
			final LoadDataListener listener) {
		JianFanJiaApiClient.put_OwnerInfo(context,
				userByOwnerInfoUpdateRequest.getOwnerUpdateInfo(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						userByOwnerInfoUpdateRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.SUCCESS_MSG)) {
								baseResponse.setMsg(response.get(
										Constant.SUCCESS_MSG).toString());
								userByOwnerInfoUpdateRequest
										.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								userByOwnerInfoUpdateRequest
										.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					};
				});
	}

	/**
	 * 更新设计师个人信息
	 * 
	 * @param context
	 * @param userByDesignerInfoUpdateRequest
	 * @param listener
	 */
	public static void postDesignerUpdateInfo(
			final Context context,
			final UserByDesignerInfoUpdateRequest userByDesignerInfoUpdateRequest,
			final LoadDataListener listener) {
		JianFanJiaApiClient.put_DesignerInfo(context,
				userByDesignerInfoUpdateRequest.getDesignerUpdateInfo(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						userByDesignerInfoUpdateRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.SUCCESS_MSG)) {
								baseResponse.setMsg(response.get(
										Constant.SUCCESS_MSG).toString());
								userByDesignerInfoUpdateRequest
										.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								userByDesignerInfoUpdateRequest
										.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					};
				});
	}

	/**
	 * 获得方案的总工期
	 * 
	 * @param context
	 * @param totalDurationRequest
	 * @param listener
	 */
	public static void getPlanTotalDuration(final Context context,
			final TotalDurationRequest totalDurationRequest,
			final LoadDataListener listener) {
		JianFanJiaApiClient.get_TotalDuration(context,
				totalDurationRequest.getPlanId(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						totalDurationRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.DATA)
									&& response.get(Constant.DATA) != null) {
								baseResponse.setData(response
										.get(Constant.DATA).toString());
								totalDurationRequest.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								totalDurationRequest.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					};
				});

	}

	/**
	 * 拿到需求
	 * 
	 * @param context
	 * @param requirementRequest
	 * @param listener
	 */
	public static void get_Requirement(final Context context,
			final GetRequirementRequest requirementRequest,
			final LoadDataListener listener) {
		JianFanJiaApiClient.get_Requirement(context,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						requirementRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.DATA)
									&& response.get(Constant.DATA) != null) {
								baseResponse.setData(response
										.get(Constant.DATA).toString());
								requirementRequest.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								requirementRequest.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					};
				});
	}

	/**
	 * 配置需求
	 * 
	 * @param context
	 * @param postRequirementRequest
	 * @param listener
	 */
	public static void post_Requirement(final Context context,
			final PostRequirementRequest postRequirementRequest,
			final LoadDataListener listener) {
		JianFanJiaApiClient.post_Owner_Process(context,
				postRequirementRequest.getRequirementInfo(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						postRequirementRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.DATA)
									&& response.get(Constant.DATA) != null) {
								baseResponse.setData(response
										.get(Constant.DATA).toString());
								postRequirementRequest.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								postRequirementRequest.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					};
				});
	}

	public static void upload_Image(final Context context,
			final UploadPicRequestNew uploadPicRequest,
			final LoadDataListener listener) {
		JianFanJiaApiClient.uploadImage(context,
				uploadPicRequest.getBitmap(), new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						uploadPicRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.DATA)
									&& response.get(Constant.DATA) != null) {
								baseResponse.setData(response
										.get(Constant.DATA).toString());
								uploadPicRequest.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								uploadPicRequest.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG, "Throwable throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "statusCode:" + statusCode
								+ " throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					};
				});
	}
	
	public static void upload_Image(final Context context,
			final UploadPicRequest uploadPicRequest,
			final LoadDataListener listener) {
		JianFanJiaApiClient.uploadImage(context,
				uploadPicRequest.getImagePath(), new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						uploadPicRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.DATA)
									&& response.get(Constant.DATA) != null) {
								baseResponse.setData(response
										.get(Constant.DATA).toString());
								uploadPicRequest.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								uploadPicRequest.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG, "Throwable throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "statusCode:" + statusCode
								+ " throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					};
				});
	}
	
	/**
	 * 删除验收图片
	 * @param context
	 * @param deletePicRequest
	 * @param listener
	 */
	public static void delete_Image(final Context context,
			final DeletePicRequest deletePicRequest,
			final LoadDataListener listener) {
		JianFanJiaApiClient.deleteYanshouImgByDesigner(context,deletePicRequest.getProcessId(), deletePicRequest.getSection(), deletePicRequest.getKey(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						deletePicRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.SUCCESS_MSG)) {
								baseResponse.setMsg(response.get(
										Constant.SUCCESS_MSG).toString());
								deletePicRequest.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								deletePicRequest.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG, "Throwable throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "statusCode:" + statusCode
								+ " throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					};
				});
	}

	public static void submitImgToProgress(final Context context,
			final AddPicToSectionItemRequest addPicToSectionItemRequest,
			final LoadDataListener listener) {
		LogTool.d(TAG, "siteId:" + addPicToSectionItemRequest.getProcessId()
				+ " section:" + addPicToSectionItemRequest.getSection()
				+ " item:" + addPicToSectionItemRequest.getItem() + " imageid:"
				+ addPicToSectionItemRequest.getImageId());
		JianFanJiaApiClient.submitImageToProcess(context,
				addPicToSectionItemRequest.getProcessId(),
				addPicToSectionItemRequest.getSection(),
				addPicToSectionItemRequest.getItem(),
				addPicToSectionItemRequest.getImageId(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						addPicToSectionItemRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.SUCCESS_MSG)) {
								baseResponse.setMsg(response.get(
										Constant.SUCCESS_MSG).toString());
								addPicToSectionItemRequest
										.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								addPicToSectionItemRequest
										.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG, "Throwable throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "statusCode:" + statusCode
								+ " throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					};
				});
	}
	
	public static void submitCheckedImg(final Context context,final AddPicToCheckRequest addPicToCheckRequest, final LoadDataListener listener) {
		JianFanJiaApiClient.submitYanShouImage(context, addPicToCheckRequest.getProcessId(),
				addPicToCheckRequest.getSection(),
				addPicToCheckRequest.getKey(),
				addPicToCheckRequest.getImageId(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						if (listener != null) {
							listener.preLoad();
						}
						addPicToCheckRequest.pre();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						BaseResponse baseResponse = new BaseResponse();
						try {
							if (response.has(Constant.SUCCESS_MSG)) {
								baseResponse.setMsg(response.get(
										Constant.SUCCESS_MSG).toString());
								addPicToCheckRequest
										.onSuccess(baseResponse);
								if (listener != null) {
									listener.loadSuccess();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								// 通知页面刷新
								baseResponse.setErr_msg(response.get(
										Constant.ERROR_MSG).toString());
								addPicToCheckRequest
										.onFailure(baseResponse);
								if (listener != null) {
									listener.loadFailture();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (listener != null) {
								listener.loadFailture();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG, "Throwable throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "statusCode:" + statusCode
								+ " throwable:" + throwable);
						if (listener != null) {
							listener.loadFailture();
						}
					};
				});
	}
}
