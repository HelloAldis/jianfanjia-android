package com.jianfanjia.cn.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;

/**
 * 
 * @ClassName: UserByOwnerInfoActivity
 * @Description:用户个人信息(业主)
 * @author fengliang
 * @date 2015-8-18 下午12:11:49
 * 
 */
public class UserByOwnerInfoActivity extends BaseActivity implements
		OnClickListener {
	private TextView ownerinfo_back = null;
	private Button btn_confirm = null;

	@Override
	public void initView() {
		ownerinfo_back = (TextView) this.findViewById(R.id.ownerinfo_back);
		btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
	}

	@Override
	public void setListener() {
		ownerinfo_back.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ownerinfo_back:
			finish();
			break;
		case R.id.btn_confirm:
			break;
		default:
			break;
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_owner_info;
	}

}
