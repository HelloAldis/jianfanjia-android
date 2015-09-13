package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONObject;
import android.view.View;
import android.widget.ListView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DelayNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyDelayInfo;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
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
public class YanQiNotifyFragment extends BaseFragment {
	private static final String TAG = YanQiNotifyFragment.class.getName();
	private ListView listView = null;
	private List<NotifyDelayInfo> caigouList = new ArrayList<NotifyDelayInfo>();
	private NotifyDelayInfo caiGouInfo = null;
	private DelayNotifyAdapter caiGouAdapter = null;

	@Override
	public void initView(View view) {
		listView = (ListView) view.findViewById(R.id.tip_delay__listview);
		for (int i = 0; i < 3; i++) {
			caiGouInfo = new NotifyDelayInfo();
			caiGouInfo.setContent("���ϰ�" + i);
			caiGouInfo.setTitle("���Ѳɹ�����" + i);
			caiGouInfo.setTime("2015-8-26");
			caiGouInfo.setStage("��ľ�׶�>ľ���׶�");
			caiGouInfo.setIsagree(i % 2);
			caigouList.add(caiGouInfo);
		}
		caiGouAdapter = new DelayNotifyAdapter(getActivity(), caigouList);
		listView.setAdapter(caiGouAdapter);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment�ɼ�ʱ��������
			LogTool.d(this.getClass().getName(), "1111111111111111");
			getRescheduleAll();
		} else {
			// ���ɼ�ʱ��ִ�в���
			LogTool.d(this.getClass().getName(), "222222222222222");
		}
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

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

}
