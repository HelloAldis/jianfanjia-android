package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.jianfanjia.cn.R;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.fragment.ForgetPswInputPhoneFragment;
import com.jianfanjia.cn.fragment.FrgPswInputVerificationFragment;
import com.jianfanjia.cn.interf.FragmentListener;

/**
 * 
 * @ClassName: ForgetPswActivity
 * @Description: 忘记密码
 * @author fengliang
 * @date 2015-8-25 下午5:29:34
 * 
 */
public class ForgetPswActivity extends BaseActivity implements FragmentListener {
	private static final String TAG = ForgetPswActivity.class.getClass()
			.getName();
	private ForgetPswInputPhoneFragment forgetPswInputPhoneFragment = null;
	private FrgPswInputVerificationFragment frgPswInputVerificationFragment = null;
	private List<Fragment> fragments = new ArrayList<Fragment>();

	private int currentPage = 0;// 所在的Fragment页面的位置

	@Override
	public void initView() {
		forgetPswInputPhoneFragment = new ForgetPswInputPhoneFragment();
		frgPswInputVerificationFragment = new FrgPswInputVerificationFragment();
		fragments.add(forgetPswInputPhoneFragment);
		fragments.add(frgPswInputVerificationFragment);
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.forget_psw_content,
				forgetPswInputPhoneFragment);
		fragmentTransaction.commit();
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBack() {
		fragmentManager.popBackStack();
		if (currentPage >= 0) {
			currentPage--;
		} else {
			finish();
		}
	}

	@Override
	public void onNext() {
		if (currentPage < 1) {
			Log.i(TAG, "next");
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.setCustomAnimations(
					R.anim.fragment_slide_right_enter,
					R.anim.fragment_slide_left_exit,
					R.anim.fragment_slide_left_enter,
					R.anim.fragment_slide_right_exit);
			fragmentTransaction.replace(R.id.forget_psw_content,
					fragments.get(++currentPage));
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_forget_psw;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (currentPage >= 0) {
			currentPage--;
		} else {
			finish();
		}
	}

}
