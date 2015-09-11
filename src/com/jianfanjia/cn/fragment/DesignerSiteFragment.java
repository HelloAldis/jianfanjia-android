package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DesignerSiteInfoAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerSiteInfo;
import com.jianfanjia.cn.cache.DataManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.SwitchFragmentListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
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
	private ImageView headView;
	private ListView siteListView;
	private List<DesignerSiteInfo> siteList;
	private DesignerSiteInfoAdapter designerSiteInfoAdapter = null;
	private SwitchFragmentListener listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			listener = (SwitchFragmentListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initView(View view) {
		headView = (ImageView) view.findViewById(R.id.designer_site_head);
		siteListView = (ListView) view
				.findViewById(R.id.designer_site_listview);
		siteList = DataManager.getInstance().getDesignerProcessLists();
		if(siteList != null){
			designerSiteInfoAdapter = new DesignerSiteInfoAdapter(
					getActivity(), siteList);
			siteListView
					.setAdapter(designerSiteInfoAdapter);
		}
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
		case R.id.designer_site_head:
			((MainActivity) getActivity()).getSlidingPaneLayout().openPane();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		DesignerSiteInfo siteInfo = siteList.get(position);
		LogTool.d(TAG, "_id=" + siteInfo.get_id());
		DataManager.getInstance().setDefaultPro(position);
		if(listener != null){
			listener.switchFragment(Constant.HOME);
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_designer_site;
	}

}
