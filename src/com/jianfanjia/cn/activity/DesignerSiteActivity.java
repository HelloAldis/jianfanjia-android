package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.jianfanjia.cn.adapter.DesignerSiteInfoAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * 
 * @ClassName: DesignerSiteActivity
 * @Description: 设计师工地
 * @author fengliang
 * @date 2015-9-11 上午10:02:42
 * 
 */
public class DesignerSiteActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private static final String TAG = DesignerSiteActivity.class.getName();
	private MainHeadView mainHeadView = null;
	private ListView siteListView = null;
	private List<Process> siteList = new ArrayList<Process>();
	private DesignerSiteInfoAdapter designerSiteInfoAdapter = null;

	@Override
	public void initView() {
		initMainHeadView();
		siteListView = (ListView) findViewById(R.id.designer_site_listview);
		siteList = dataManager.getDesignerProcessLists();
		if (siteList != null) {
			designerSiteInfoAdapter = new DesignerSiteInfoAdapter(
					DesignerSiteActivity.this, siteList);
			siteListView.setAdapter(designerSiteInfoAdapter);
		} else {
			dataManager.requestProcessList();
			showWaitDialog();
		}
	}

	@SuppressLint("ResourceAsColor") 
	private void initMainHeadView() {
		mainHeadView = (MainHeadView) findViewById(R.id.designer_site_head_layout);
		mainHeadView.setBackListener(this);
		mainHeadView.setMianTitle(getResources().getString(
				R.string.my_decoration_site));
		mainHeadView.setLayoutBackground(R.color.head_layout_bg);
		mainHeadView.setDividerVisable(View.VISIBLE);
	}

	@Override
	public void setListener() {
		siteListView.setOnItemClickListener(this);
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
		Process siteInfo = siteList.get(position);
		LogTool.d(TAG, "_id=" + siteInfo.get_id());
		dataManager.setDefaultPro(position);
		Intent intent = new Intent();
		intent.putExtra("ProcessId", siteInfo.get_id());
		setResult(Constant.REQUESTCODE_CHANGE_SITE, intent);
		finish();
	}

	@Override
	public void loadSuccess() {
		super.loadSuccess();
		siteList = dataManager.getDesignerProcessLists();
		if (siteList != null) {
			designerSiteInfoAdapter = new DesignerSiteInfoAdapter(
					DesignerSiteActivity.this, siteList);
			siteListView.setAdapter(designerSiteInfoAdapter);
		} else {
			// load empty
		}
	}

	@Override
	public void loadFailture() {
		super.loadFailture();
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_designer_site;
	}

	@Override
	public void processMessage(Message msg) {
		Bundle bundle = msg.getData();
		NotifyMessage message = (NotifyMessage) bundle
				.getSerializable("Notify");
		switch (msg.what) {
		case Constant.SENDBACKNOTICATION:
			sendNotifycation(message);
			break;
		case Constant.SENDNOTICATION:
			showNotify(message);
			break;
		default:
			break;
		}
	}

}
