package com.jianfanjia.cn.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jianfanjia.cn.base.BaseActivity;

/**
 * @class AboutActivity
 * @Description 关于我们
 * @author zhanghao
 * @date 2015-8-27 下午8:22
 * 
 */
public class AboutActivity extends BaseActivity implements OnClickListener {
	private TextView backView;// 返回视图

	@Override
	public void initView() {
		backView = (TextView) findViewById(R.id.about_back);
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

}
