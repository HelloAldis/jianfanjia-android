package com.jianfanjia.cn.fragment;

import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.LogTool;

/**
 * 
 * @ClassName: DesignMenuFragment
 * @Description: 设计师侧滑栏菜单
 * @author fengliang
 * @date 2015-8-28 上午10:40:10
 * 
 */
public class DesignerMenuFragment extends BaseFragment implements
		OnCheckedChangeListener {
	private static final String TAG = DesignerMenuFragment.class.getName();
	private static final int HOME = 0;
	private static final int NOTIFY = 1;
	private static final int MYOWNER = 2;
	private static final int MYSITE = 3;
	private static final int SETTING = 4;
	private static final int HELP = 5;
	private RadioGroup mTabRg = null;
	private DesignerSiteManageFragment designerSiteManageFragment = null;
	private NotifyFragment notifyFragment = null;
	private OwnerFragment owerFragment = null;
	private DesignerSiteFragment designerSiteFragment = null;
	private SettingFragment settingFragment = null;
	private HelpFrgment helpFragment = null;
	private String mUserName;// 用户名
	private TextView nameText = null;
	private TextView phoneText = null;

	@Override
	public void initView(View view) {
		mUserName = shared.getValue(Constant.ACCOUNT, null);
		LogTool.d(TAG, "mUserName=" + mUserName);
		nameText = (TextView) view.findViewById(R.id.name_text);
		phoneText = (TextView) view.findViewById(R.id.phone_text);
		mTabRg = (RadioGroup) view.findViewById(R.id.tab_rg_menu);
		if (!TextUtils.isEmpty(mUserName)) {
			phoneText.setText("账号:" + mUserName);
		}
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
			setTabSelection(MYOWNER);
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
			if (designerSiteManageFragment != null) {
				transaction.show(designerSiteManageFragment);
			} else {
				designerSiteManageFragment = new DesignerSiteManageFragment();
				transaction.add(R.id.slidingpane_content,
						designerSiteManageFragment);
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
		case MYOWNER:
			if (owerFragment != null) {
				transaction.show(owerFragment);
			} else {
				owerFragment = new OwnerFragment();
				transaction.add(R.id.slidingpane_content, owerFragment);
			}
			break;
		case MYSITE:
			if (designerSiteFragment != null) {
				transaction.show(designerSiteFragment);
			} else {
				designerSiteFragment = new DesignerSiteFragment();
				transaction.add(R.id.slidingpane_content, designerSiteFragment);
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
		if (designerSiteManageFragment != null) {
			ft.hide(designerSiteManageFragment);
		}
		if (notifyFragment != null) {
			ft.hide(notifyFragment);
		}
		if (owerFragment != null) {
			ft.hide(owerFragment);
		}
		if (designerSiteFragment != null) {
			ft.hide(designerSiteFragment);
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
		return R.layout.fragment_designer_menu;
	}
}
