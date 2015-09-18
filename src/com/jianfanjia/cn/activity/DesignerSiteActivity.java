package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * 
 * @ClassName: DesignerSiteActivity
 * @Description: ���ʦ����
 * @author fengliang
 * @date 2015-9-11 ����10:02:42
 * 
 */
public class DesignerSiteActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener, LoadDataListener {
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
			dataManager.requestProcessList(this);
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
		hideWaitDialog();
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
		hideWaitDialog();
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
		return R.layout.activity_designer_site;
	}

}
