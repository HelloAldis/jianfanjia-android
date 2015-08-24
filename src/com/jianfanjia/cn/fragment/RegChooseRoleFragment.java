package com.jianfanjia.cn.fragment;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.RegisterActivity;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.UserInfo;

/**
 * @version 1.0
 * @Description 注册选择角色Fragment
 * @author zhanghao
 * @date 2015-8-21 上午9:15
 *
 */
public class RegChooseRoleFragment extends BaseFragment implements OnTouchListener{
	
	private ImageView designerView;//设计师身份
	private ImageView owerView;//业主身份
	private Button nextView;//下一步
	private TextView backView;//返回
	
	private ImageView indicatorView;//指示器
	private TextView proTipView;//提示操作
	
	private RegisterActivity registerActivity;//注册Activty
	
	public RegChooseRoleFragment(RegisterActivity registerActivity){
		this.registerActivity = registerActivity;
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_register_choose_role;
	}

	@Override
	public void initView(View view) {
		designerView = (ImageView)view.findViewById(R.id.choose_designer);
		owerView = (ImageView)view.findViewById(R.id.choose_ower);
		nextView = (Button)view.findViewById(R.id.next);
		backView = (TextView)view.findViewById(R.id.goback);
		
		indicatorView = (ImageView)view.findViewById(R.id.indicator);
		indicatorView.setImageResource(R.drawable.rounded_enrollment1);
		
		proTipView = (TextView)view.findViewById(R.id.register_pro);
		proTipView.setText(getString(R.string.choose_role));
		
		nextView.setEnabled(false);
	}
	
	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		int viewId = arg0.getId();
		switch (viewId) {
		case R.id.next:
			registerActivity.next();
			break;
		case R.id.goback:
			registerActivity.finish();
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
		if(action == MotionEvent.ACTION_DOWN){
			switch (viewId) {
				case R.id.choose_designer:
					owerView.setImageResource(R.drawable.btn_enrollment_user1_normal);
					designerView.setImageResource(R.drawable.btn_enrollment_designer2_pressed);
					registerActivity.getRegisterInfo().setUserType(String.valueOf(UserInfo.IDENTITY_DESIGNER));
					nextView.setEnabled(true);
					break;
				case R.id.choose_ower:
					designerView.setImageResource(R.drawable.btn_enrollment_designer2_normal);
					owerView.setImageResource(R.drawable.btn_enrollment_user1_pressed);
					registerActivity.getRegisterInfo().setUserType(String.valueOf(UserInfo.IDENTITY_COMMON_USER));
					nextView.setEnabled(true);
					break;
				default:
					break;
			}
		}
		return false;
	}

}
