package com.jianfanjia.cn.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jianfanjia.cn.activity.DesignerSiteActivity;
import com.jianfanjia.cn.activity.MyOwnerActivity;
import com.jianfanjia.cn.activity.NotifyActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SettingActivity;
import com.jianfanjia.cn.activity.UserByDesignerInfoActivity;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;

/**
 * 
 * @ClassName: DesignMenuFragment
 * @Description: 设计师侧滑栏菜单
 * @author fengliang
 * @date 2015-8-28 上午10:40:10
 * 
 */
public class DesignerMenuFragment extends BaseFragment {
	private static final String TAG = DesignerMenuFragment.class.getName();
	private RelativeLayout notifyLayout = null;
	private ImageView img_head = null;
	private ImageView notifyIcon = null;
	private TextView nameText = null;
	private TextView phoneText = null;
	private TextView tab_rb_1 = null;
	private TextView tab_rb_2 = null;
	private TextView tab_rb_3 = null;
	private TextView tab_rb_4 = null;
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constant.INTENT_ACTION_USERINFO_CHANGE)) {
				mUserName = dataManager.getUserName();
				if (!TextUtils.isEmpty(mUserName)) {
					nameText.setText(mUserName);
				} else {
					nameText.setText("业主");
				}
			}else if(action.equals(Constant.INTENT_ACTION_USER_IMAGE_CHANGE)){
				
			}
		}
	};

	@Override
	public void initView(View view) {
		notifyLayout = (RelativeLayout) view.findViewById(R.id.notifyLayout);
		notifyIcon = (ImageView) view.findViewById(R.id.notifyIcon);
		img_head = (ImageView) view.findViewById(R.id.img_head);
		nameText = (TextView) view.findViewById(R.id.name_text);
		phoneText = (TextView) view.findViewById(R.id.phone_text);
		tab_rb_1 = (TextView) view.findViewById(R.id.tab_rb_1);
		tab_rb_2 = (TextView) view.findViewById(R.id.tab_rb_2);
		tab_rb_3 = (TextView) view.findViewById(R.id.tab_rb_3);
		tab_rb_4 = (TextView) view.findViewById(R.id.tab_rb_4);
		if (!TextUtils.isEmpty(mUserName)) {
			nameText.setText(mUserName);
		} else {
			nameText.setText("设计师");
		}
		if (!TextUtils.isEmpty(mAccount)) {
			phoneText.setText("账号:" + mAccount);
		}
		imageLoader.displayImage(mUserImageId, img_head, options);
	}

	@Override
	public void setListener() {
		img_head.setOnClickListener(this);
		notifyLayout.setOnClickListener(this);
		tab_rb_2.setOnClickListener(this);
		tab_rb_3.setOnClickListener(this);
		tab_rb_4.setOnClickListener(this);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		IntentFilter filter = new IntentFilter(
				Constant.INTENT_ACTION_USERINFO_CHANGE);
		filter.addAction(Constant.INTENT_ACTION_USER_IMAGE_CHANGE);
		getActivity().registerReceiver(mReceiver, filter);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(mReceiver);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_head:
			startActivity(UserByDesignerInfoActivity.class);
			break;
		case R.id.notifyLayout:
			startActivity(NotifyActivity.class);
			break;
		case R.id.tab_rb_2:
			startActivity(MyOwnerActivity.class);
			break;
		case R.id.tab_rb_3:
			startActivity(DesignerSiteActivity.class);
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
		return R.layout.fragment_designer_menu;
	}

}
