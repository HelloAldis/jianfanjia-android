package com.jianfanjia.cn.fragment;

import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.UserByOwnerInfoActivity;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.FragmentCallBack;
import com.jianfanjia.cn.tools.LogTool;

/**
 * 
 * @ClassName: OwnerMenuFragment
 * @Description: 业主侧滑栏菜单
 * @author fengliang
 * @date 2015-8-26 上午9:51:44
 * 
 */
public class OwnerMenuFragment extends BaseFragment implements
		OnCheckedChangeListener, FragmentCallBack {
	private static final String TAG = OwnerMenuFragment.class.getName();
	private RadioGroup mTabRg = null;
	private ImageView img_head = null;
	private OwnerSiteManageFragment siteManageFragment = null;
	private NotifyFragment notifyFragment = null;
	private DesignerFragment designerFragment = null;
	private OwnerSiteFragment ownerSiteFragment = null;
	private SettingFragment settingFragment = null;
	private TextView nameText = null;
	private TextView phoneText = null;

	@Override
	public void initView(View view) {
		nameText = (TextView) view.findViewById(R.id.name_text);
		phoneText = (TextView) view.findViewById(R.id.phone_text);
		mTabRg = (RadioGroup) view.findViewById(R.id.tab_rg_menu);
		img_head = (ImageView) view.findViewById(R.id.img_head);
		if (!TextUtils.isEmpty(mUserName)) {
			nameText.setText(mUserName);
		} else {
			nameText.setText("业主");
		}
		if (!TextUtils.isEmpty(mAccount)) {
			phoneText.setText("账号:" + mAccount);
		}
	}

	@Override
	public void setListener() {
		mTabRg.setOnCheckedChangeListener(this);
		img_head.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_head:
			startActivity(UserByOwnerInfoActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		((MainActivity) getActivity()).getSlidingPaneLayout().closePane();
		switch (checkedId) {
		case R.id.tab_rb_1:
			setTabSelection(Constant.HOME);
			break;
		case R.id.tab_rb_2:
			setTabSelection(Constant.NOTIFY);
			break;
		case R.id.tab_rb_3:
			setTabSelection(Constant.MY);
			break;
		case R.id.tab_rb_4:
			setTabSelection(Constant.MYSITE);
			break;
		case R.id.tab_rb_5:
			setTabSelection(Constant.SETTING);
			break;
		default:
			break;
		}
	}

	private void setTabSelection(int index) {
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		hideFragments(transaction);
		switch (index) {
		case Constant.HOME:
			if (siteManageFragment != null) {
				transaction.show(siteManageFragment);
			} else {
				siteManageFragment = new OwnerSiteManageFragment();
				transaction.add(R.id.slidingpane_content, siteManageFragment);
			}
			break;
		case Constant.NOTIFY:
			if (notifyFragment != null) {
				transaction.show(notifyFragment);
			} else {
				notifyFragment = new NotifyFragment();
				transaction.add(R.id.slidingpane_content, notifyFragment);
			}
			break;
		case Constant.MY:
			if (designerFragment != null) {
				transaction.show(designerFragment);
			} else {
				designerFragment = new DesignerFragment();
				transaction.add(R.id.slidingpane_content, designerFragment);
			}
			break;
		case Constant.MYSITE:
			if (ownerSiteFragment != null) {
				transaction.show(ownerSiteFragment);
			} else {
				ownerSiteFragment = new OwnerSiteFragment();
				transaction.add(R.id.slidingpane_content, ownerSiteFragment);
			}
			break;
		case Constant.SETTING:
			if (settingFragment != null) {
				transaction.show(settingFragment);
			} else {
				settingFragment = new SettingFragment();
				transaction.add(R.id.slidingpane_content, settingFragment);
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}

	// 当fragment已被实例化，相当于发生过切换，就隐藏起来
	private void hideFragments(FragmentTransaction ft) {
		if (siteManageFragment != null) {
			ft.hide(siteManageFragment);
		}
		if (notifyFragment != null) {
			ft.hide(notifyFragment);
		}
		if (designerFragment != null) {
			ft.hide(designerFragment);
		}
		if (ownerSiteFragment != null) {
			ft.hide(ownerSiteFragment);
		}
		if (settingFragment != null) {
			ft.hide(settingFragment);
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_owner_menu;
	}

	@Override
	public void callBack(int index) {
		View v = mTabRg.getChildAt(index);
		RadioButton rb = (RadioButton) v.findViewById(R.id.tab_rb_4);
		LogTool.d(TAG, "rb:" + rb);
		rb.setChecked(true);
		setTabSelection(index);
	}
}
