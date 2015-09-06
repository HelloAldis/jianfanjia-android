package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONObject;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DesignerSiteInfoAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerSiteInfo;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.dialog.CustomProgressDialog;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: DesignerSiteFragment
 * @Description: 我的工地（设计师）
 * @author zhanghao
 * @date 2015-8-26 下午7:07:52
 * 
 */
public class DesignerSiteFragment extends BaseFragment implements
		OnItemClickListener {
	private static final String TAG = DesignerSiteFragment.class.getName();
	private CustomProgressDialog progressDialog = null;
	private ImageView headView;
	private ListView siteListView;
	private List<DesignerSiteInfo> caigouList = new ArrayList<DesignerSiteInfo>();
	private DesignerSiteInfoAdapter designerSiteInfoAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progressDialog = new CustomProgressDialog(getActivity(), "正在加载中",
				R.style.dialog);
	}

	@Override
	public void initView(View view) {
		headView = (ImageView) view.findViewById(R.id.designer_site_head);
		siteListView = (ListView) view
				.findViewById(R.id.designer_site_listview);
		getMySiteList();
	}

	@Override
	public void setListener() {
		headView.setOnClickListener(this);
		siteListView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.my_ower_head:

			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {

	}

	// 获取装修工地
	private void getMySiteList() {
		JianFanJiaApiClient.get_Designer_Process_List(getActivity(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						progressDialog.show();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						progressDialog.dismiss();
						caigouList = JsonParser.getDesignerSiteList(response
								.toString());
						designerSiteInfoAdapter = new DesignerSiteInfoAdapter(
								getActivity(), caigouList);
						siteListView.setAdapter(designerSiteInfoAdapter);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						progressDialog.dismiss();
						makeTextLong(getString(R.string.tip_login_error_for_network));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						progressDialog.dismiss();
						makeTextLong(getString(R.string.tip_login_error_for_network));
					};
				});
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_designer_site;
	}

}
