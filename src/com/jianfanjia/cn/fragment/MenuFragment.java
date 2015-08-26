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
 * @ClassName: MenuFragment
 * @Description: 侧滑栏菜单
 * @author fengliang
 * @date 2015-8-26 上午9:51:44
 * 
 */
public class MenuFragment extends BaseFragment implements
		OnCheckedChangeListener {
	private static final String TAG = MenuFragment.class.getClass().getName();
	private static final int HOME = 0;
	private static final int NOTIFY = 1;
	private static final int ROLE = 2;
	private static final int SITE = 3;
	private static final int SETTING = 4;
	private static final int HELP = 5;
	private RadioGroup mTabRg = null;
	private SiteManageFragment siteManageFragment = null;
	private NotifyFragment notifyFragment = null;
	private DesignerFragment designerFragment = null;
	private SiteFragment siteFragment = null;
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
			setTabSelection(ROLE);
			break;
		case R.id.tab_rb_4:
			setTabSelection(SITE);
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
		switch (index) {
		case HOME:
			siteManageFragment = new SiteManageFragment();
			transaction.replace(R.id.slidingpane_content, siteManageFragment);
			break;
		case NOTIFY:
			notifyFragment = new NotifyFragment();
			transaction.replace(R.id.slidingpane_content, notifyFragment);
			break;
		case ROLE:
			designerFragment = new DesignerFragment();
			transaction.replace(R.id.slidingpane_content, designerFragment);
			break;
		case SITE:
			siteFragment = new SiteFragment();
			transaction.replace(R.id.slidingpane_content, siteFragment);
			break;
		case SETTING:
			settingFragment = new SettingFragment();
			transaction.replace(R.id.slidingpane_content, settingFragment);
			break;
		case HELP:
			helpFragment = new HelpFrgment();
			transaction.replace(R.id.slidingpane_content, helpFragment);
			break;
		default:
			break;
		}
		transaction.commit();
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_menu;
	}
}
