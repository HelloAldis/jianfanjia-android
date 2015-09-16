package com.jianfanjia.cn.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.activity.MyDesignerActivity;
import com.jianfanjia.cn.activity.NotifyActivity;
import com.jianfanjia.cn.activity.OwnerSiteActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SettingActivity;
import com.jianfanjia.cn.activity.UserByOwnerInfoActivity;
import com.jianfanjia.cn.base.BaseFragment;

/**
 * 
 * @ClassName: OwnerMenuFragment
 * @Description: ҵ���໬���˵�
 * @author fengliang
 * @date 2015-8-26 ����9:51:44
 * 
 */
public class OwnerMenuFragment extends BaseFragment {
	private static final String TAG = OwnerMenuFragment.class.getName();
	private ImageView img_head = null;
	private TextView nameText = null;
	private TextView phoneText = null;
	private TextView tab_rb_1 = null;
	private TextView tab_rb_2 = null;
	private TextView tab_rb_3 = null;
	private TextView tab_rb_4 = null;

	@Override
	public void initView(View view) {
		nameText = (TextView) view.findViewById(R.id.name_text);
		phoneText = (TextView) view.findViewById(R.id.phone_text);
		img_head = (ImageView) view.findViewById(R.id.img_head);
		tab_rb_1 = (TextView) view.findViewById(R.id.tab_rb_1);
		tab_rb_2 = (TextView) view.findViewById(R.id.tab_rb_2);
		tab_rb_3 = (TextView) view.findViewById(R.id.tab_rb_3);
		tab_rb_4 = (TextView) view.findViewById(R.id.tab_rb_4);
		if (!TextUtils.isEmpty(mUserName)) {
			nameText.setText(mUserName);
		} else {
			nameText.setText("ҵ��");
		}
		if (!TextUtils.isEmpty(mAccount)) {
			phoneText.setText("�˺�:" + mAccount);
		}
		imageLoader.displayImage(mUserImageId, img_head, options);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void setListener() {
		img_head.setOnClickListener(this);
		tab_rb_1.setOnClickListener(this);
		tab_rb_2.setOnClickListener(this);
		tab_rb_3.setOnClickListener(this);
		tab_rb_4.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_head:
			startActivity(UserByOwnerInfoActivity.class);
			break;
		case R.id.tab_rb_1:
			startActivity(NotifyActivity.class);
			break;
		case R.id.tab_rb_2:
			startActivity(MyDesignerActivity.class);
			break;
		case R.id.tab_rb_3:
			startActivity(OwnerSiteActivity.class);
			break;
		case R.id.tab_rb_4:
			startActivity(SettingActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_owner_menu;
	}

}
