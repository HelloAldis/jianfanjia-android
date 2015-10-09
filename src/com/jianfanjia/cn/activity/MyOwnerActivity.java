package com.jianfanjia.cn.activity;

import java.util.List;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.jianfanjia.cn.adapter.MyOwerInfoAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.http.LoadClientHelper;
import com.jianfanjia.cn.http.request.ProcessListRequest;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

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
	public static final String PROCESS = "process";
	private ListView ownerListView = null;
	private List<Process> ownerList;
	private TextView errorText;
	private MyOwerInfoAdapter myOwerInfoAdapter = null;
	private MainHeadView mainHeadView = null;

	@Override
	public void initView() {
		initMainHeadView();
		ownerListView = (ListView) findViewById(R.id.my_ower_listview);
		// get_Designer_Owner();
		ownerList = dataManager.getProcessLists();
		if (ownerList == null) {
			LoadClientHelper.requestProcessList(this, new ProcessListRequest(
					this), this);
		}
		myOwerInfoAdapter = new MyOwerInfoAdapter(MyOwnerActivity.this,
				ownerList);
		ownerListView.setAdapter(myOwerInfoAdapter);
		setEmptyView();
	}

	private void setEmptyView() {
		ViewStub mViewStub = (ViewStub) findViewById(R.id.empty);
		errorText = (TextView) mViewStub.inflate().findViewById(R.id.tv_error);
		errorText.setText("暂无业主数据");
		ownerListView.setEmptyView(mViewStub);
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
	public void loadSuccess() {
		super.loadSuccess();
		ownerList = dataManager.getProcessLists();
		myOwerInfoAdapter.setList(ownerList);
		myOwerInfoAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		Process info = ownerList.get(position);
		String ownerId = info.get_id();
		LogTool.d(TAG, "info=" + ownerId);
		Intent intent = new Intent(MyOwnerActivity.this,
				OwnerInfoActivity.class);
		intent.putExtra(PROCESS, info);
		startActivity(intent);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_my_owner;
	}

}
