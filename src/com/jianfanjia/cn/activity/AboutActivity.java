package com.jianfanjia.cn.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.tools.LogTool;

/**
 * @class AboutActivity
 * @Description ��������
 * @author zhanghao
 * @date 2015-8-27 ����8:22
 * 
 */
public class AboutActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = AboutActivity.class.getName();
	private TextView backView;// ������ͼ
	private TextView currentVersion;// ��ǰ�汾

	@Override
	public void initView() {
		backView = (TextView) findViewById(R.id.about_back);
		currentVersion = (TextView) findViewById(R.id.about_version);
		currentVersion.setText(MyApplication.getInstance().getVersionName());
	}

	@Override
	public void setListener() {
		backView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTool.d(TAG, "---onResume()");
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
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_about;
	}

}
