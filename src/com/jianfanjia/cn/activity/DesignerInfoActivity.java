package com.jianfanjia.cn.activity;

import com.jianfanjia.cn.R;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.Message;
import com.jianfanjia.cn.tools.LogTool;

/**
 * 
 * @ClassName: DesignerInfoActivity
 * @Description: 设计师信息
 * @author fengliang
 * @date 2015-9-5 上午11:28:06
 * 
 */
public class DesignerInfoActivity extends BaseActivity {
	private static final String TAG = DesignerInfoActivity.class.getName();

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_my_designer_detail;
	}

	@Override
	public void onReceiveMsg(Message message) {
		LogTool.d(TAG, "message=" + message);
		showNotify(message);
	}
}
