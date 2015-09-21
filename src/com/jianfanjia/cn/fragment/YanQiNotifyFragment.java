package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DelayNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.SwitchFragmentListener;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: YanQiNotifyFragment
 * @Description: ��������
 * @author fengliang
 * @date 2015-8-26 ����1:09:52
 * 
 */
public class YanQiNotifyFragment extends BaseFragment implements
		SwitchFragmentListener, OnItemLongClickListener {
	private static final String TAG = YanQiNotifyFragment.class.getName();
	private ListView yanqiListView = null;
	private List<NotifyMessage> delayList = new ArrayList<NotifyMessage>();
	private NotifyMessage notifyMessage = null;
	private DelayNotifyAdapter delayAdapter = null;

	@Override
	public void initView(View view) {
		yanqiListView = (ListView) view.findViewById(R.id.tip_delay__listview);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment�ɼ�ʱ��������
			LogTool.d(TAG, "1111111111111111");
			getRescheduleAll();
		} else {
			// ���ɼ�ʱ��ִ�в���
			LogTool.d(TAG, "222222222222222");
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
								// delayList = JsonParser.jsonToList(
								// response.get(Constant.DATA).toString(),
								// new TypeToken<List<NotifyDelayInfo>>() {
								// }.getType());
								// LogTool.d(TAG, "delayList:" + delayList);
								// delayAdapter = new DelayNotifyAdapter(
								// getActivity(), delayList);
								// listView.setAdapter(delayAdapter);
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

	@Override
	public int getLayoutId() {
		return R.layout.fragment_yanqi_notify;
	}

	@Override
	public void switchTab(int index) {
		// TODO Auto-generated method stub

	}

}
