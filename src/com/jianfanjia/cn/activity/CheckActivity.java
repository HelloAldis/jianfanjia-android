package com.jianfanjia.cn.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jianfanjia.cn.base.BaseActivity;

/**
 * 
 * @ClassName: CheckActivity
 * @Description: 验收
 * @author fengliang
 * @date 2015-8-28 下午2:25:36
 * 
 */
public class CheckActivity extends BaseActivity implements OnClickListener {
	private TextView backView;// 返回视图

	@Override
	public void initView() {
		backView = (TextView) findViewById(R.id.check_pic_back);

	}

	@Override
	public void setListener() {
		backView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_check_pic;
	}
}
