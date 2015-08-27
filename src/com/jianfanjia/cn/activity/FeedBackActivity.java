package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.jianfanjia.cn.adapter.CommentInfoAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.CommentInfo;

/**
 * @class FeedBackActivity
 * @Description 用户反馈
 * @author zhanghao
 * @date 2015-8-27 10:11
 * 
 */
public class FeedBackActivity extends BaseActivity implements OnClickListener {
	private TextView backView;// 返回视图

	@Override
	public void initView() {
		backView = (TextView) findViewById(R.id.comment_back);
	}

	@Override
	public void setListener() {
		backView.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_feedback;
	}

}
