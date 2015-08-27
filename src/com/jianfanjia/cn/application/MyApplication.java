package com.jianfanjia.cn.application;

import com.jianfanjia.cn.base.BaseApplication;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.config.Constant;

/**
 * @version 1.0
 * @Description �������ҵ�Ӧ�ó�����
 * @author Administrator
 * @date 2015-8-20 ����1:45
 *
 */
public class MyApplication extends BaseApplication {
	private static MyApplication instance;
	private boolean isLogin;//�ж��û��Ƿ��¼
	private int userType = -1;//�ж��û�����
	private RegisterInfo registerInfo = new RegisterInfo();//ע��ʵ����Ϣ
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public static MyApplication getInstance(){
		return instance;
	}

	public RegisterInfo getRegisterInfo() {
		return registerInfo;
	}
	
	public void saveLoginUserInfo(LoginUserBean userBean){
		sharedPrefer.setValue(Constant.ACCOUNT, userBean.getPhone());
		sharedPrefer.setValue(Constant.USERTYPE, userBean.getUsertype());
		sharedPrefer.setValue(Constant.PASSWORD, userBean.getPass());
		sharedPrefer.setValue(Constant.USERIMAGE_ID, userBean.getImageId());
	}
}
