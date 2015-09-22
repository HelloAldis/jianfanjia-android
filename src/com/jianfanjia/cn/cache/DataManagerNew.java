package com.jianfanjia.cn.cache;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.bean.MyDesignerInfo;
import com.jianfanjia.cn.bean.MyOwnerInfo;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.bean.SectionInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.jianfanjia.cn.tools.SharedPrefer;

public class DataManagerNew {
	private static final String TAG = DataManagerNew.class.getName();
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	private static DataManagerNew instance;
	private Context context;

	private SharedPrefer sharedPreferdata = null;
	private SharedPrefer sharedPreferuser = null;
	private List<Process> processLists;
	private Map<String, ProcessInfo> processMap = new HashMap<String, ProcessInfo>();
	private MyOwnerInfo myOwnerInfo;// 我的业主信息
	private MyDesignerInfo myDesignerInfo;// 我的设计师信息
	private OwnerInfo ownerInfo;// 业主的个人信息
	private DesignerInfo designerInfo;// 设计师的个人信息
	private String totalDuration;// 总工期
	private RequirementInfo requirementInfo;// 需求信息
	private ProcessInfo currentProcessInfo;//当前工地信息
	
	public static DataManagerNew getInstance() {
		if (instance == null) {
			instance = new DataManagerNew();
		}
		return instance;
	}

	private DataManagerNew() {
		context = MyApplication.getInstance();
		sharedPreferdata = new SharedPrefer(context, Constant.SHARED_DATA);
		sharedPreferuser = new SharedPrefer(context, Constant.SHARED_USER);
	}

	public RequirementInfo getRequirementInfo() {
		return requirementInfo;
	}

	public void setRequirementInfo(RequirementInfo requirementInfo) {
		this.requirementInfo = requirementInfo;
	}

	public String getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(String totalDuration) {
		this.totalDuration = totalDuration;
	}

	public void setOwnerInfo(OwnerInfo ownerInfo) {
		this.ownerInfo = ownerInfo;
		sharedPreferdata.setValue(Constant.OWNER_INFO, ownerInfo);
	}

	public void setDesignerInfo(DesignerInfo designerInfo) {
		this.designerInfo = designerInfo;
		sharedPreferdata.setValue(Constant.DESIGNER_INFO, designerInfo);
	}

	// 设计师用户获取个人资料
	public DesignerInfo getDesignerInfo() {
		if (designerInfo == null && !NetTool.isNetworkAvailable(context)) {
			designerInfo = (DesignerInfo) sharedPreferdata
					.getValue(Constant.DESIGNER_INFO);
		}
		return designerInfo;
	}

	// 业主用户获取个人资料
	public OwnerInfo getOwnerInfo() {
		if (ownerInfo == null && !NetTool.isNetworkAvailable(context)) {
			ownerInfo = (OwnerInfo) sharedPreferdata
					.getValue(Constant.OWNER_INFO);
		}
		return ownerInfo;
	}
	
	public void setCurrentProcessInfo(ProcessInfo currentProcessInfo) {
		this.currentProcessInfo = currentProcessInfo;
	}

	public ProcessInfo getDefaultProcessInfo() {
		if(currentProcessInfo == null){
			String processId = getDefaultProcessId();
			if (processId != null) {
				return getProcessInfoById(processId);
			} else {
				return null;
			}
		}
		return currentProcessInfo;
	}

	public SectionInfo getDefaultSectionInfoByPosition(int position) {
		ProcessInfo processInfo = getDefaultProcessInfo();
		if (processInfo != null) {
			return processInfo.getSections().get(position);
		}
		return null;
	}

	public String getDefaultDesignerId() {
		if (processLists == null || processLists.size() == 0) {
			processLists = getProcessListsByCache();
			if (processLists != null && processLists.size() != 0) {
				return processLists.get(getDefaultPro()).getFinal_designerid();
			} else {
				return null;
			}
		} else {
			return processLists.get(getDefaultPro()).getFinal_designerid();
		}
	}

	public String getDefaultOwnerId() {
		if (processLists == null || processLists.size() == 0) {
			processLists = getProcessListsByCache();
			if (processLists != null && processLists.size() != 0) {
				return processLists.get(getDefaultPro()).getUserid();
			} else {
				return null;
			}
		} else {
			return processLists.get(getDefaultPro()).getUserid();
		}
	}

	public String getDefaultProcessId() {
		if (processLists == null || processLists.size() == 0) {
			processLists = getProcessListsByCache();
			if (processLists != null && processLists.size() != 0) {
				return processLists.get(getDefaultPro()).get_id();
			} else {
				return null;
			}
		} else {
			return processLists.get(getDefaultPro()).get_id();
		}
	}

	public List<Process> getProcessLists() {
		return processLists;
	}

	public void setProcessLists(List<Process> processLists) {
		this.processLists = processLists;
	}

	public void saveProcessLists(String jsonProcessLists) {
		sharedPreferdata.setValue(Constant.DESIGNER_PROCESS_LIST,
				jsonProcessLists);
	}

	public List<Process> getProcessListsByCache() {
		String jsonProcessList = sharedPreferdata.getValue(
				Constant.DESIGNER_PROCESS_LIST, null);
		if (jsonProcessList != null) {
			processLists = JsonParser.jsonToList(jsonProcessList,
					new TypeToken<List<Process>>() {
					}.getType());
		}
		return processLists;
	}

	public int getDefaultPro() {
		if (getUserType().equals(Constant.IDENTITY_DESIGNER)) {
			return sharedPreferdata.getValue(Constant.DEFAULT_PROCESS, 0);// 默认的工地为0
		} else if (getUserType().equals(Constant.IDENTITY_OWNER)) {
			return 0;
		}
		return 0;
	}

	public void setDefaultPro(int defaultPro) {
		sharedPreferdata.setValue(Constant.DEFAULT_PROCESS, defaultPro);
	}

	/**
	 * 拿工地信息
	 * 
	 * @param processId
	 * @return
	 */
	public ProcessInfo getProcessInfoById(String processId) {
		ProcessInfo processInfo = processMap.get(processId);
		if (!NetTool.isNetworkAvailable(context) && processInfo == null) {
			return (ProcessInfo) sharedPreferdata.getValue(processId);
		}
		return processInfo;
	}

	public void saveProcessInfo(ProcessInfo processInfo) {
		processMap.put(processInfo.get_id(), processInfo);
		sharedPreferdata.setValue(processInfo.get_id(), processInfo);
	}

	public MyOwnerInfo getMyOwnerInfoById(String ownerId) {
		return (MyOwnerInfo) sharedPreferdata.getValue(ownerId);
	}

	public MyOwnerInfo getMyOwnerInfo() {
		return myOwnerInfo;
	}

	public void setMyOwnerInfo(MyOwnerInfo myOwnerInfo) {
		this.myOwnerInfo = myOwnerInfo;
		sharedPreferdata.setValue(myOwnerInfo.get_id(), myOwnerInfo);
	}

	public MyDesignerInfo getMyDesignerInfo() {
		return myDesignerInfo;
	}

	public void setMyDesignerInfo(MyDesignerInfo myDesignerInfo) {
		this.myDesignerInfo = myDesignerInfo;
		sharedPreferdata.setValue(myDesignerInfo.get_id(), myDesignerInfo);
	}

	public MyDesignerInfo getMyDesignerInfoById(String designerId) {
		return (MyDesignerInfo) sharedPreferdata.getValue(designerId);
	}

	public boolean isLogin() {
		return sharedPreferdata.getValue(Constant.USER_IS_LOGIN, false);// 默认是没登录
	}

	public void setLogin(boolean isLogin) {
		sharedPreferdata.setValue(Constant.USER_IS_LOGIN, isLogin);
	}

	public void savaLastLoginTime(long loginTime) {
		sharedPreferdata.setValue(Constant.LAST_LOGIN_TIME, loginTime);
	}

	// 是否登录信息已过期
	public boolean isLoginExpire() {
		long currentTime = Calendar.getInstance().getTimeInMillis();// 当前时间
		long loginLoginTime = sharedPreferdata.getValue(
				Constant.LAST_LOGIN_TIME, currentTime);
		if (currentTime - loginLoginTime > Constant.LOGIN_EXPIRE) {
			return true;
		}
		return false;
	}
	
	public boolean isConfigPro(){
		return sharedPreferdata.getValue(Constant.ISCONFIG_PROCESS, false);
	}
	
	public void setConfigPro(boolean isConfig){
		sharedPreferdata.setValue(Constant.ISCONFIG_PROCESS, isConfig);
	}

	public boolean isPushOpen() {
		return sharedPreferuser.getValue(Constant.ISOPEN, true);
	}

	public void setPushOpen(boolean isOpen) {
		sharedPreferuser.setValue(Constant.ISOPEN, isOpen);
	}

	public boolean isFirst() {
		return sharedPreferuser.getValue(Constant.ISFIRST, false);
	}

	public void setFisrt(boolean isFirst) {
		sharedPreferuser.setValue(Constant.ISFIRST, isFirst);
	}

	public void setUserImagePath(String imgId) {
		sharedPreferuser.setValue(Constant.USERIMAGE_ID, Url.GET_IMAGE + imgId);
	}

	public void saveLoginUserInfo(LoginUserBean userBean) {
		sharedPreferuser.setValue(Constant.ACCOUNT, userBean.getPhone());
		sharedPreferuser.setValue(Constant.USERTYPE, userBean.getUsertype());
		sharedPreferuser.setValue(Constant.USERNAME, userBean.getUsername());
		sharedPreferuser.setValue(Constant.USERIMAGE_ID, userBean.getImageid());
		sharedPreferuser.setValue(Constant.USER_ID, userBean.get_id());
		sharedPreferuser.setValue(Constant.PASSWORD, userBean.getPass());
	}

	public void setUserName(String userName) {
		sharedPreferuser.setValue(Constant.USERNAME, userName);
	}

	public String getPassword() {
		return sharedPreferuser.getValue(Constant.PASSWORD, null);
	}

	public String getAccount() {
		return sharedPreferuser.getValue(Constant.ACCOUNT, null);
	}

	public String getUserType() {
		String userType = sharedPreferuser.getValue(Constant.USERTYPE, "1");
		return userType;
	}

	public String getUserId() {
		return sharedPreferuser.getValue(Constant.USER_ID, null);
	}

	public String getUserName() {
		String userName = sharedPreferuser.getValue(Constant.USERNAME, null);
		if (userName == null) {
			if (getUserType().equals(Constant.IDENTITY_OWNER)) {
				return context.getString(R.string.ower);
			} else if (getUserType().equals(Constant.IDENTITY_DESIGNER)) {
				return context.getString(R.string.designer);
			}
		}
		return userName;
	}

	public String getUserImagePath() {
		String userImagePath = null;
		String imageId = sharedPreferuser.getValue(Constant.USERIMAGE_ID, null);
		LogTool.d(TAG, "imageId:" + imageId);
		if (imageId == null) {
			if (getUserType().equals(Constant.IDENTITY_OWNER)) {
				userImagePath = Constant.DEFALUT_OWNER_PIC;
			} else if (getUserType().equals(Constant.IDENTITY_DESIGNER)) {
				userImagePath = Constant.DEFALUT_DESIGNER_PIC;
			}
		} else {
			userImagePath = Url.GET_IMAGE + imageId;
		}
		LogTool.d(TAG, "userImagePath:" + userImagePath);
		return userImagePath;
	}

	public void cleanData() {
		processLists = null;
		myOwnerInfo = null;
		myDesignerInfo = null;
		ownerInfo = null;
		designerInfo = null;
		requirementInfo = null;
		totalDuration = null;
		currentProcessInfo = null;
		processMap.clear();
		sharedPreferdata.clear();
	}

}
