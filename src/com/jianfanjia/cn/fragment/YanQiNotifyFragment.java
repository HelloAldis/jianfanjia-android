package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DelayNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyDelayInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.DelayInfoListener;
import com.jianfanjia.cn.interf.SwitchFragmentListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: YanQiNotifyFragment
 * @Description: 延期提醒
 * @author fengliang
 * @date 2015-8-26 下午1:09:52
 * 
 */
public class YanQiNotifyFragment extends BaseFragment implements
		SwitchFragmentListener, OnItemLongClickListener {
	private static final String TAG = YanQiNotifyFragment.class.getName();
	private ListView yanqiListView = null;
	private List<NotifyDelayInfo> delayList = new ArrayList<NotifyDelayInfo>();
	private NotifyDelayInfo notifyDelayInfo = null;
	private DelayNotifyAdapter delayAdapter = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogTool.d(TAG, "dataManager=" + dataManager);
	}

	@Override
	public void initView(View view) {
		yanqiListView = (ListView) view.findViewById(R.id.tip_delay__listview);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment可见时加载数据
			LogTool.d(TAG, "YanQiNotifyFragment 可见");
			getRescheduleAll();
		} else {
			// 不可见时不执行操作
			LogTool.d(TAG, "YanQiNotifyFragment 不可见");
		}
	}

	@Override
	public void setListener() {
		yanqiListView.setOnItemLongClickListener(this);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View v, int position,
			long id) {
		// TODO Auto-generated method stub
		return false;
	}

	// /用户获取我的改期提醒
	private void getRescheduleAll() {
		JianFanJiaApiClient.rescheduleAll(getActivity(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						showWaitDialog();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						hideWaitDialog();
						try {
							if (response.has(Constant.DATA)) {
								delayList = JsonParser.jsonToList(
										response.get(Constant.DATA).toString(),
										new TypeToken<List<NotifyDelayInfo>>() {
										}.getType());
								LogTool.d(TAG, "delayList:" + delayList);
								delayAdapter = new DelayNotifyAdapter(
										getActivity(), delayList,
										new DelayInfoListener() {

											@Override
											public void onAgree() {
												if (null != processInfo) {
													agreeReschedule(processInfo
															.get_id());
												}
											}

											@Override
											public void onRefuse() {
												if (null != processInfo) {
													refuseReschedule(processInfo
															.get_id());
												}
											}

										});
								yanqiListView.setAdapter(delayAdapter);
							} else if (response.has(Constant.ERROR_MSG)) {
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							e.printStackTrace();
							makeTextLong(getString(R.string.tip_login_error_for_network));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						hideWaitDialog();
						makeTextLong(getString(R.string.tip_login_error_for_network));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						hideWaitDialog();
						makeTextLong(getString(R.string.tip_login_error_for_network));
					};
				});
	}

	// 用户同意改期
	private void agreeReschedule(String processid) {
		JianFanJiaApiClient.agreeReschedule(getActivity(), processid,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(this.getClass().getName(), "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(this.getClass().getName(),
								"JSONObject response:" + response);
						try {
							if (response.has(Constant.DATA)) {
								makeTextLong(response.get(Constant.DATA)
										.toString());
							} else if (response.has(Constant.SUCCESS_MSG)) {
								makeTextLong(response.get(Constant.SUCCESS_MSG)
										.toString());
								getRescheduleAll();
							} else if (response.has(Constant.ERROR_MSG)) {
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							makeTextLong(getString(R.string.tip_login_error_for_network));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(this.getClass().getName(),
								"Throwable throwable:" + throwable.toString());
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(this.getClass().getName(), "throwable:"
								+ throwable);
					};
				});
	}

	// 用户拒绝改期
	private void refuseReschedule(String processid) {
		JianFanJiaApiClient.refuseReschedule(getActivity(), processid,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(this.getClass().getName(), "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(this.getClass().getName(),
								"JSONObject response:" + response);
						try {
							if (response.has(Constant.DATA)) {
								makeTextLong(response.get(Constant.DATA)
										.toString());
							} else if (response.has(Constant.SUCCESS_MSG)) {
								makeTextLong(response.get(Constant.SUCCESS_MSG)
										.toString());
								getRescheduleAll();
							} else if (response.has(Constant.ERROR_MSG)) {
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							makeTextLong(getString(R.string.tip_login_error_for_network));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(this.getClass().getName(),
								"Throwable throwable:" + throwable.toString());
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(this.getClass().getName(), "throwable:"
								+ throwable);
					};
				});
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_yanqi_notify;
	}

	@Override
	public void switchTab(int index) {
		// TODO Auto-generated method stub

	}

}
