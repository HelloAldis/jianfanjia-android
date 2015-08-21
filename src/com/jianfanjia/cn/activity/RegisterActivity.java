package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.fragment.RegChooseRoleFragment;
import com.jianfanjia.cn.fragment.RegInputPhoneFragment;
import com.jianfanjia.cn.fragment.ReginputVerificationFragment;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: RegisterActivity
 * @Description: 注册界面
 * @author fengliang
 * @date 2015-8-18 下午12:11:35
 * 
 */
public class RegisterActivity extends BaseActivity {
	
	public static final String RegChooseRoleFragment = "regChooseRoleFragment";
	public static final String RegInputPhoneFragment = "regInputPhoneFragment";
	public static final String ReginputVerificationFragment = "reginputVerificationFragment";
	
	private RegisterInfo registerInfo = new RegisterInfo();//注册所需要的信息实体类

	private RegChooseRoleFragment regChooseRoleFragment;
	private RegInputPhoneFragment regInputPhoneFragment;
	private ReginputVerificationFragment reginputVerificationFragment;
	
	private List<Fragment> fragments = new ArrayList<Fragment>(); 
	
	private int currentPage = 0;//所在的Fragment页面的位置

	@Override
	public void initView() {
		regChooseRoleFragment = new RegChooseRoleFragment(this);
		regInputPhoneFragment = new RegInputPhoneFragment(this);
		reginputVerificationFragment = new ReginputVerificationFragment(this);
		fragments.add(regChooseRoleFragment);
		fragments.add(regInputPhoneFragment);
		fragments.add(reginputVerificationFragment);
		
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.reg_content, regChooseRoleFragment);
		fragmentTransaction.commit();
	}
	
	//点击下一步的动作
	public void next(){
		if(currentPage < 2){
			Log.i(this.getClass().getName(),"next");
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_right_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_left_enter,R.anim.fragment_slide_right_exit);
			fragmentTransaction.replace(R.id.reg_content,
					fragments.get(++currentPage));
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}
	}
	
	//点击返回的动作
	public void back(){
		fragmentManager.popBackStack();
		if(currentPage >= 1){
			currentPage--;
		}
	}

	@Override
	public void setListener() {

	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_register;
	}

	public RegisterInfo getRegisterInfo() {
		return registerInfo;
	}
	
}
