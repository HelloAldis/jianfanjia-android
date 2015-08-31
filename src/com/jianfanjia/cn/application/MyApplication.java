package com.jianfanjia.cn.application;

import java.util.Arrays;
import java.util.List;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseApplication;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.bean.SiteInfo;
import com.jianfanjia.cn.config.Constant;

/**
 * @version 1.0
 * @Description 此类是我的应用程序类
 * @author Administrator
 * @date 2015-8-20 下午1:45
 * 
 */
public class MyApplication extends BaseApplication {
	private static MyApplication instance;
	private boolean isLogin;// 判断用户是否登录
	private String userType;// 判断用户类型
	private RegisterInfo registerInfo = new RegisterInfo();// 注册实体信息
	private ProcessInfo processInfo;//工地信息
	private List<String> site_data;//静态的工序列表

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
	
	public ProcessInfo getProcessInfo() {
		return processInfo;
	}

	public void setProcessInfo(ProcessInfo processInfo) {
		this.processInfo = processInfo;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	/**
	 * @description 根据英文的name,拿到中文的name
	 * @param string
	 * @return
	 */
	public String getStringById(String name){
		int StringId = getResources().getIdentifier(name, "string", getPackageName());
		return getResources().getString(StringId);
	}
	
	/**
	 * @description 根据name,拿到当前的进行工序的位置
	 * @param name
	 * @return 默认返回0，当前工序为第一个工序
	 */
	public int getPositionByItemName(String name){
		String[] items = getResources().getStringArray(R.array.site_data);
		for(int i= 0;i<items.length;i++){
			if(items[i].equals(name)){
				return i;
			}
		}
		return 0;
	}
}
