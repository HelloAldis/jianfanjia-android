package com.jianfanjia.cn.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;

/**
 * @class FeedBackActivity
 * @Description �û�����
 * @author zhanghao
 * @date 2015-8-27 10:11
 * 
 */
public class FeedBackActivity extends BaseActivity implements OnClickListener {
	private TextView backView;// ������ͼ

	@Override
	public void initView() {
		backView = (TextView) findViewById(R.id.feedback_back);
	}

	@Override
	public void setListener() {
		backView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.feedback_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_feedback;
	}

}
