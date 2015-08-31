package com.jianfanjia.cn.fragment;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;

/**
 * 
 * @ClassName: OwnerMenuFragment
 * @Description: 业主侧滑栏菜单
 * @author fengliang
 * @date 2015-8-26 上午9:51:44
 * 
 */
public class OwnerMenuFragment extends BaseFragment implements
		OnCheckedChangeListener {
	private static final String TAG = OwnerMenuFragment.class.getName();
	private static final int HOME = 0;
	private static final int NOTIFY = 1;
	private static final int MYDESIGNER = 2;
	private static final int MYSITE = 3;
	private static final int SETTING = 4;
	private static final int HELP = 5;
	private RadioGroup mTabRg = null;
	private OwnerSiteManageFragment siteManageFragment = null;
	private NotifyFragment notifyFragment = null;
	private DesignerFragment designerFragment = null;
	private OwnerSiteFragment ownerSiteFragment = null;
	private SettingFragment settingFragment = null;
	private HelpFrgment helpFragment = null;

	@Override
	public void initView(View view) {
		mTabRg = (RadioGroup) view.findViewById(R.id.tab_rg_menu);
	}

	@Override
	public void setListener() {
		mTabRg.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		((MainActivity) getActivity()).getSlidingPaneLayout().closePane();
		switch (checkedId) {
		case R.id.tab_rb_1:
			setTabSelection(HOME);
			break;
		case R.id.tab_rb_2:
			setTabSelection(NOTIFY);
			break;
		case R.id.tab_rb_3:
			setTabSelection(MYDESIGNER);
			break;
		case R.id.tab_rb_4:
			setTabSelection(MYSITE);
			break;
		case R.id.tab_rb_5:
			setTabSelection(SETTING);
			break;
		case R.id.tab_rb_6:
			setTabSelection(HELP);
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
		case HOME:
			if (siteManageFragment != null) {
				transaction.show(siteManageFragment);
			} else {
				siteManageFragment = new OwnerSiteManageFragment();
				transaction.add(R.id.slidingpane_content, siteManageFragment);
			}
			break;
		case NOTIFY:
			if (notifyFragment != null) {
				transaction.show(notifyFragment);
			} else {
				notifyFragment = new NotifyFragment();
				transaction.add(R.id.slidingpane_content, notifyFragment);
			}
			break;
		case MYDESIGNER:
			if (designerFragment != null) {
				transaction.show(designerFragment);
			} else {
				designerFragment = new DesignerFragment();
				transaction.add(R.id.slidingpane_content, designerFragment);
			}
			break;
		case MYSITE:
			if (ownerSiteFragment != null) {
				transaction.show(ownerSiteFragment);
			} else {
				ownerSiteFragment = new OwnerSiteFragment();
				transaction.add(R.id.slidingpane_content, ownerSiteFragment);
			}
			break;
		case SETTING:
			if (settingFragment != null) {
				transaction.show(settingFragment);
			} else {
				settingFragment = new SettingFragment();
				transaction.add(R.id.slidingpane_content, settingFragment);
			}
			break;
		case HELP:
			if (helpFragment != null) {
				transaction.show(helpFragment);
			} else {
				helpFragment = new HelpFrgment();
				transaction.add(R.id.slidingpane_content, helpFragment);
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
		if (helpFragment != null) {
			ft.hide(helpFragment);
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_owner_menu;
	}
}
