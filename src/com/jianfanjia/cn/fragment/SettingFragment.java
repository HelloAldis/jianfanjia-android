package com.jianfanjia.cn.fragment;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.jianfanjia.cn.activity.AboutActivity;
import com.jianfanjia.cn.activity.FeedBackActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;

/**
 * 
 * @ClassName: SettingFragment
 * @Description: …Ë÷√
 * @author fengliang
 * @date 2015-8-26 œ¬ŒÁ3:58:10
 * 
 */
public class SettingFragment extends BaseFragment implements OnCheckedChangeListener{
	private RelativeLayout feedbackFragment = null;
	private RelativeLayout aboutFragment = null;
	private ToggleButton toggleButton;

	@Override
	public void initView(View view) {
		feedbackFragment = (RelativeLayout) view
				.findViewById(R.id.feedback_layout);
		aboutFragment = (RelativeLayout) view.findViewById(R.id.about_layout);
		toggleButton = (ToggleButton)view.findViewById(R.id.mespush_toggle);
	}

	@Override
	public void setListener() {
		feedbackFragment.setOnClickListener(this);
		aboutFragment.setOnClickListener(this);
		toggleButton.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.feedback_layout:
			startActivity(FeedBackActivity.class);
			break;
		case R.id.about_layout:
			startActivity(AboutActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_setting;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean check) {
		
	}

}
