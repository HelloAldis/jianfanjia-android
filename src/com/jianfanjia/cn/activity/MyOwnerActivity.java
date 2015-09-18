package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.adapter.MyOwerInfoAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: MyOwnerActivity
 * @Description:我的业主
 * @author fengliang
 * @date 2015-9-16 上午9:31:59
 * 
 */
public class MyOwnerActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener {
	private static final String TAG = MyOwnerActivity.class.getName();
	private ListView ownerListView = null;
	private List<Process> ownerList = new ArrayList<Process>();
	private MyOwerInfoAdapter myOwerInfoAdapter = null;
	private MainHeadView mainHeadView = null;

	@Override
	public void initView() {
		initMainHeadView();
		ownerListView = (ListView) findViewById(R.id.my_ower_listview);
		get_Designer_Owner();
	}

	private void initMainHeadView() {
		mainHeadView = (MainHeadView) findViewById(R.id.my_ower_head_layout);
		mainHeadView.setBackListener(this);
		mainHeadView.setMianTitle(getResources().getString(R.string.my_ower));
		mainHeadView.setLayoutBackground(R.color.head_layout_bg);
		mainHeadView.setDividerVisable(View.VISIBLE);
	}

	@Override
	public void setListener() {
		ownerListView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_back_layout:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		Process info = ownerList.get(position);
		String ownerId = info.get_id();
		LogTool.d(TAG, "ownerId=" + ownerId);
		Intent intent = new Intent(MyOwnerActivity.this,
				OwnerInfoActivity.class);
		intent.putExtra("owner", info.getUser());
		startActivity(intent);
	}

	private void get_Designer_Owner() {
		JianFanJiaApiClient.get_Designer_Owner(MyOwnerActivity.this,
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
								ownerList = JsonParser.jsonToList(
										response.get(Constant.DATA).toString(),
										new TypeToken<List<Process>>() {
										}.getType());
								LogTool.d(TAG, "ownerList:" + ownerList);
								myOwerInfoAdapter = new MyOwerInfoAdapter(
										MyOwnerActivity.this, ownerList);
								ownerListView.setAdapter(myOwerInfoAdapter);
							} else if (response.has(Constant.ERROR_MSG)) {
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							makeTextLong(getString(R.string.load_failure));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						hideWaitDialog();
						makeTextLong(getString(R.string.tip_no_internet));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						hideWaitDialog();
						makeTextLong(getString(R.string.tip_no_internet));
					};
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTool.d(TAG, "---onResume()");
		listenerManeger.addPushMsgReceiveListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTool.d(TAG, "---onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		LogTool.d(TAG, "---onStop()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogTool.d(TAG, "---onDestroy()");
		listenerManeger.removePushMsgReceiveListener(this);
	}

	@Override
	public void onReceiveMsg(NotifyMessage message) {
		LogTool.d(TAG, "message=" + message);
		// sendNotifycation(message);
		showNotify(message);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_my_owner;
	}

}
