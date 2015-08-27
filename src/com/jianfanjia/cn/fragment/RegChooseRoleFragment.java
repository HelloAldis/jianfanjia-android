package com.jianfanjia.cn.fragment;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.FragmentListener;

/**
 * @version 1.0
 * @Description 注册选择角色Fragment
 * @author zhanghao
 * @date 2015-8-21 上午9:15
 * 
 */
public class RegChooseRoleFragment extends BaseFragment implements
		OnTouchListener {
	private static final String TAG = RegChooseRoleFragment.class.getClass()
			.getName();
	private FragmentListener fragemntListener = null;
	private ImageView designerView = null;// 设计师身份
	private ImageView owerView = null;// 业主身份
	private Button nextView = null;// 下一步
	private TextView backView = null;// 返回
	private ImageView indicatorView = null;// 指示器
	private TextView proTipView = null;// 提示操作

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			fragemntListener = (FragmentListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initView(View view) {
		designerView = (ImageView) view.findViewById(R.id.choose_designer);
		owerView = (ImageView) view.findViewById(R.id.choose_ower);
		nextView = (Button) view.findViewById(R.id.next);
		backView = (TextView) view.findViewById(R.id.goback);
		indicatorView = (ImageView) view.findViewById(R.id.indicator);
		indicatorView.setImageResource(R.drawable.rounded_register1);
		proTipView = (TextView) view.findViewById(R.id.register_pro);
		proTipView.setText(getString(R.string.choose_role));
		nextView.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next:
			fragemntListener.onNext();
			break;
		case R.id.goback:
			fragemntListener.onBack();
			break;
		default:
			break;
		}
	}

	@Override
	public void setListener() {
		designerView.setOnTouchListener(this);
		owerView.setOnTouchListener(this);
		nextView.setOnClickListener(this);
		backView.setOnClickListener(this);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		int viewId = arg0.getId();
		int action = arg1.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			switch (viewId) {
			case R.id.choose_designer:
				owerView.setImageResource(R.drawable.btn_register_user1_normal);
				designerView
						.setImageResource(R.drawable.btn_register_designer2_pressed);
				MyApplication.getInstance().getRegisterInfo().setUserType(Constant.IDENTITY_DESIGNER);
				nextView.setEnabled(true);
				break;
			case R.id.choose_ower:
				designerView
						.setImageResource(R.drawable.btn_register_designer2_normal);
				owerView.setImageResource(R.drawable.btn_register_user1_pressed);
				MyApplication.getInstance().getRegisterInfo().setUserType(Constant.IDENTITY_OWNER);
				nextView.setEnabled(true);
				break;
			default:
				break;
			}
		}
		return false;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_register_choose_role;
	}
}
