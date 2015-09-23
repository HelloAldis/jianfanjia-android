package com.jianfanjia.cn.activity;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.jianfanjia.cn.adapter.DesignerSiteInfoAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.LoadClientHelper;
import com.jianfanjia.cn.http.request.ProcessListRequest;
import com.jianfanjia.cn.interf.LoadDataListener;
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
		OnClickListener, OnItemClickListener, LoadDataListener {
	private static final String TAG = DesignerSiteActivity.class.getName();
	private MainHeadView mainHeadView = null;
	private ListView siteListView = null;
	private List<Process> siteList;
	private TextView errorText;
	private DesignerSiteInfoAdapter designerSiteInfoAdapter = null;

	@Override
	public void initView() {
		initMainHeadView();
		siteListView = (ListView) findViewById(R.id.designer_site_listview);
		siteList = dataManager.getProcessLists();
		if (siteList == null) {
			LoadClientHelper.requestProcessList(this, new ProcessListRequest(
					this), this);
		}
		designerSiteInfoAdapter = new DesignerSiteInfoAdapter(
				DesignerSiteActivity.this, siteList);
		siteListView.setAdapter(designerSiteInfoAdapter);
		setEmptyView();
	}

	private void setEmptyView() {
		ViewStub mViewStub = (ViewStub) findViewById(R.id.empty);
		errorText = (TextView) mViewStub.inflate().findViewById(R.id.tv_error);
		errorText.setText("暂无工地数据");
		siteListView.setEmptyView(mViewStub);
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
		siteList = dataManager.getProcessLists();
		designerSiteInfoAdapter.setList(siteList);
		designerSiteInfoAdapter.notifyDataSetChanged();
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
