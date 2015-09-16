package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;

/**
 * @class AboutActivity
 * @Description 关于我们
 * @author zhanghao
 * @date 2015-8-27 下午8:22
 * 
 */
public class AboutActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = AboutActivity.class.getName();
	private TextView backView;// 返回视图
	private TextView currentVersion;// 当前版本

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
	public int getLayoutId() {
		return R.layout.activity_about;
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
