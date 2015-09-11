package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.OwnerInfoActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.MyOwerInfoAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: OwerFragment
 * @Description: 我的业主（设计师）
 * @author zhanghao
 * @date 2015-8-26 下午7:07:52
 * 
 */
public class OwnerFragment extends BaseFragment implements OnItemClickListener {
	private static final String TAG = OwnerFragment.class.getName();
	private ListView ownerListView;
	private List<Process> ownerList = new ArrayList<Process>();
	private MyOwerInfoAdapter myOwerInfoAdapter = null;
	
	private MainHeadView mainHeadView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView(View view) {
		ownerListView = (ListView) view.findViewById(R.id.my_ower_listview);
		initMainHead(view);
		get_Designer_Owner();
	}
	
	@SuppressLint("ResourceAsColor") 
	private void initMainHead(View view) {
		mainHeadView = (MainHeadView) view.findViewById(R.id.my_ower_head_layout);
		mainHeadView.setHeadImage(mUserImageId);
		mainHeadView.setBackListener(this);
		mainHeadView.setRightTitleVisable(View.GONE);
		mainHeadView.setMianTitle(getResources().getString(R.string.my_ower));
		mainHeadView.setBackgroundColor(R.color.head_layout_bg);
		mainHeadView.setDividerVisable(View.VISIBLE);
	}

	@Override
	public void setListener() {
		ownerListView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.icon_head:
			((MainActivity) getActivity()).getSlidingPaneLayout().openPane();
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
		Intent intent = new Intent(getActivity(), OwnerInfoActivity.class);
		intent.putExtra("ownerId", ownerId);
		startActivity(intent);
	}

	private void get_Designer_Owner() {
		JianFanJiaApiClient.get_Designer_Owner(getActivity(),
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
								ownerList = JsonParser
										.jsonToList(
												response.get(Constant.DATA)
														.toString(),
												new TypeToken<List<Process>>() {
												}.getType());
								LogTool.d(TAG, "ownerList:" + ownerList);
								myOwerInfoAdapter = new MyOwerInfoAdapter(
										getActivity(), ownerList);
								ownerListView.setAdapter(myOwerInfoAdapter);
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
		return R.layout.fragment_my_ower;
	}
}
