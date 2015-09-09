package com.jianfanjia.cn.fragment;

import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.jianfanjia.cn.R;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.UserByDesignerInfoActivity;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.FragmentCallBack;
import com.jianfanjia.cn.tools.LogTool;

/**
 * 
 * @ClassName: DesignMenuFragment
 * @Description: ���ʦ�໬���˵�
 * @author fengliang
 * @date 2015-8-28 ����10:40:10
 * 
 */
public class DesignerMenuFragment extends BaseFragment implements
		OnCheckedChangeListener, FragmentCallBack {
	private static final String TAG = DesignerMenuFragment.class.getName();
	private RadioGroup mTabRg = null;
	private ImageView img_head = null;
	private DesignerSiteManageFragment designerSiteManageFragment = null;
	private NotifyFragment notifyFragment = null;
	private OwnerFragment owerFragment = null;
	private DesignerSiteFragment designerSiteFragment = null;
	private SettingFragment settingFragment = null;
	private TextView nameText = null;
	private TextView phoneText = null;

	@Override
	public void initView(View view) {
		img_head = (ImageView) view.findViewById(R.id.img_head);
		nameText = (TextView) view.findViewById(R.id.name_text);
		phoneText = (TextView) view.findViewById(R.id.phone_text);
		mTabRg = (RadioGroup) view.findViewById(R.id.tab_rg_menu);
		if (!TextUtils.isEmpty(mUserName)) {
			nameText.setText(mUserName);
		} else {
			nameText.setText("���ʦ");
		}
		if (!TextUtils.isEmpty(mAccount)) {
			phoneText.setText("�˺�:" + mAccount);
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
			startActivity(UserByDesignerInfoActivity.class);
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
		// ����һ��Fragment����
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		hideFragments(transaction);
		switch (index) {
		case Constant.HOME:
			if (designerSiteManageFragment != null) {
				transaction.show(designerSiteManageFragment);
			} else {
				designerSiteManageFragment = new DesignerSiteManageFragment();
				transaction.add(R.id.slidingpane_content,
						designerSiteManageFragment);
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
			if (owerFragment != null) {
				transaction.show(owerFragment);
			} else {
				owerFragment = new OwnerFragment();
				transaction.add(R.id.slidingpane_content, owerFragment);
			}
			break;
		case Constant.MYSITE:
			if (designerSiteFragment != null) {
				transaction.show(designerSiteFragment);
			} else {
				designerSiteFragment = new DesignerSiteFragment();
				transaction.add(R.id.slidingpane_content, designerSiteFragment);
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

	// ��fragment�ѱ�ʵ�������൱�ڷ������л�������������
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
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_designer_menu;
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
