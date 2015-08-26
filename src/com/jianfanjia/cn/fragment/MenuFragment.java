package com.jianfanjia.cn.fragment;

import android.view.View;
import android.widget.TextView;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;

/**
 * 
 * @ClassName: MenuFragment
 * @Description: ²à»¬À¸²Ëµ¥
 * @author fengliang
 * @date 2015-8-26 ÉÏÎç9:51:44
 * 
 */
public class MenuFragment extends BaseFragment {
	private static final String TAG = ReginputVerificationFragment.class
			.getClass().getName();
	private TextView mainText = null;
	private TextView notifyText = null;
	private TextView roleText = null;
	private TextView siteText = null;
	private TextView settingText = null;
	private TextView helpText = null;

	@Override
	public void initView(View view) {
		mainText = (TextView) view.findViewById(R.id.mainText);
		notifyText = (TextView) view.findViewById(R.id.notifyText);
		roleText = (TextView) view.findViewById(R.id.roleText);
		siteText = (TextView) view.findViewById(R.id.siteText);
		settingText = (TextView) view.findViewById(R.id.settingText);
		helpText = (TextView) view.findViewById(R.id.helpText);
	}

	@Override
	public void setListener() {
		mainText.setOnClickListener(this);
		notifyText.setOnClickListener(this);
		roleText.setOnClickListener(this);
		siteText.setOnClickListener(this);
		settingText.setOnClickListener(this);
		helpText.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mainText:
			((MainActivity) getActivity()).getSlidingPaneLayout().closePane();
			break;
		case R.id.notifyText:
			((MainActivity) getActivity()).getSlidingPaneLayout().closePane();
			break;
		case R.id.roleText:
			((MainActivity) getActivity()).getSlidingPaneLayout().closePane();
			break;
		case R.id.siteText:
			((MainActivity) getActivity()).getSlidingPaneLayout().closePane();
			break;
		case R.id.settingText:
			((MainActivity) getActivity()).getSlidingPaneLayout().closePane();
			break;
		case R.id.helpText:
			((MainActivity) getActivity()).getSlidingPaneLayout().closePane();
			break;
		default:
			break;
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_menu;
	}
}
