package com.jianfanjia.cn.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseApplication;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.bean.SiteInfo;
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
	private boolean isLogin;// �ж��û��Ƿ��¼
	private String userType;// �ж��û�����
	private RegisterInfo registerInfo = new RegisterInfo();// ע��ʵ����Ϣ
	private SiteInfo siteInfo;
	private List<String> site_data;//��̬�Ĺ����б�

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		site_data = Arrays.asList(getResources().getStringArray(R.array.site_data));
	}
	
	public List<String> getSite_data() {
		return site_data;
	}

	public void setSite_data(List<String> site_data) {
		this.site_data = site_data;
	}



	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public static MyApplication getInstance() {
		return instance;
	}

	public RegisterInfo getRegisterInfo() {
		return registerInfo;
	}

	public void saveLoginUserInfo(LoginUserBean userBean) {
		sharedPrefer.setValue(Constant.ACCOUNT, userBean.getPhone());
		sharedPrefer.setValue(Constant.USERTYPE, userBean.getUsertype());
		sharedPrefer.setValue(Constant.PASSWORD, userBean.getPass());
		sharedPrefer.setValue(Constant.USERIMAGE_ID, userBean.getImageId());
	}
	
	public SiteInfo getSiteInfo() {
		return siteInfo;
	}

	public void setSiteInfo(SiteInfo siteInfo) {
		this.siteInfo = siteInfo;
	}


	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
}
