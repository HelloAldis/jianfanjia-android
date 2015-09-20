package com.jianfanjia.cn.cache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

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
import com.jianfanjia.cn.bean.ProcessReflect;
import com.jianfanjia.cn.bean.SectionInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.NetTool;
import com.jianfanjia.cn.tools.SharedPrefer;

public class DataManagerNew {

	private static final String TAG = DataManagerNew.class.getName();
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	private static DataManagerNew instance;
	private Context context;

	public SharedPrefer sharedPrefer = null;
	private List<Process> processLists;
	private MyOwnerInfo myOwnerInfo;// �ҵ�ҵ����Ϣ
	private MyDesignerInfo myDesignerInfo;// �ҵ����ʦ��Ϣ
	private ProcessInfo processInfo;// ��ǰ������Ϣ
	private OwnerInfo ownerInfo;// ҵ���ĸ�����Ϣ
	private DesignerInfo designerInfo;// ���ʦ�ĸ�����Ϣ

	public static DataManagerNew getInstance() {
		if (instance == null) {
			instance = new DataManagerNew();
		}
		return instance;
	}
	
	public void setOwnerInfo(OwnerInfo ownerInfo) {
		this.ownerInfo = ownerInfo;
		sharedPrefer.setValue(Constant.OWNER_INFO, ownerInfo);
	}

	public void setDesignerInfo(DesignerInfo designerInfo) {
		this.designerInfo = designerInfo;
		sharedPrefer.setValue(Constant.DESIGNER_INFO, designerInfo);
	}

	// ���ʦ�û���ȡ��������
	public DesignerInfo getDesignerInfo() {
		if (designerInfo == null && !NetTool.isNetworkAvailable(context)) {
			designerInfo = (DesignerInfo) sharedPrefer
					.getValue(Constant.DESIGNER_INFO);
		}
		return designerInfo;
	}

	// ҵ���û���ȡ��������
	public OwnerInfo getOwnerInfo() {
		if (ownerInfo == null && !NetTool.isNetworkAvailable(context)) {
			ownerInfo = (OwnerInfo) sharedPrefer.getValue(Constant.OWNER_INFO);
		}
		return ownerInfo;
	}
	
	// ͨ��ҵ��id�õ�������Ϣ
	public ProcessInfo getProcessInfoByOwnerId(String ownerId) {
		if (processLists == null || processLists.size() == 0) {
			processLists = getProcessListsByCache();
		}
		String processInfoId = null;
		for (int i = 0; i < processLists.size(); i++) {
			processInfoId = processLists.get(i).get_id();
			if (ownerId.equals(processLists.get(i).getUserid())) {
				break;
			}
		}
		if (processInfoId != null) {
			return getProcessInfoById(processInfoId);
		}
		return null;
	}

	public ProcessInfo getDefaultProcessInfo() {
		if(getProcessInfo() != null){
			return getProcessInfo();
		}else{
			String processId =  getDefaultProcessId();
			if(!NetTool.isNetworkAvailable(context) &&  processId != null){
				return getProcessInfoById(processId);
			}else{
				return null;
			}
		}
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
			if (processLists != null  && processLists.size() != 0) {
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
			if (processLists != null  && processLists.size() != 0) {
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
			if (processLists != null  && processLists.size() != 0) {
				return processLists.get(getDefaultPro()).get_id();
			} else {
				return null;
			}
		} else {
			return processLists.get(getDefaultPro()).get_id();
		}
	}

	private DataManagerNew() {
		context = MyApplication.getInstance();
		sharedPrefer = new SharedPrefer(context, Constant.SHARED_MAIN);
	}

	public List<Process> getProcessLists() {
		return processLists;
	}

	public void setProcessLists(List<Process> processLists) {
		this.processLists = processLists;
	}

	public void saveProcessLists(String jsonProcessLists) {
		sharedPrefer.setValue(Constant.DESIGNER_PROCESS_LIST, jsonProcessLists);
	}

	public List<Process> getProcessListsByCache() {
		String jsonProcessList = sharedPrefer.getValue(
				Constant.DESIGNER_PROCESS_LIST, null);
		if (jsonProcessList != null) {
			processLists = JsonParser.jsonToList(jsonProcessList,
					new TypeToken<List<Process>>() {
					}.getType());
		}
		return processLists;
	}

	public ProcessInfo getProcessInfo() {
		return processInfo;
	}

	public int getDefaultPro() {
		if (getUserType().equals(Constant.IDENTITY_DESIGNER)) {
			return sharedPrefer.getValue(Constant.DEFAULT_PROCESS, 0);// Ĭ�ϵĹ���Ϊ0
		} else if (getUserType().equals(Constant.IDENTITY_OWNER)) {
			return 0;
		}
		return 0;
	}

	public void setDefaultPro(int defaultPro) {
		sharedPrefer.setValue(Constant.DEFAULT_PROCESS, defaultPro);
	}

	/**
	 * �������ù�����Ϣ
	 * 
	 * @param processId
	 * @return
	 */
	public ProcessInfo getProcessInfoById(String processId) {
		return (ProcessInfo) sharedPrefer.getValue(processId);
	}

	public void setProcessInfo(ProcessInfo processInfo) {
		this.processInfo = processInfo;
		sharedPrefer.setValue(processInfo.get_id(), processInfo);
	}

	public MyOwnerInfo getMyOwnerInfoById(String ownerId) {
		return (MyOwnerInfo) sharedPrefer.getValue(ownerId);
	}

	public MyOwnerInfo getMyOwnerInfo() {
		return myOwnerInfo;
	}

	public void setMyOwnerInfo(MyOwnerInfo myOwnerInfo) {
		this.myOwnerInfo = myOwnerInfo;
		sharedPrefer.setValue(myOwnerInfo.get_id(), myOwnerInfo);
	}

	public MyDesignerInfo getMyDesignerInfo() {
		return myDesignerInfo;
	}

	public void setMyDesignerInfo(MyDesignerInfo myDesignerInfo) {
		this.myDesignerInfo = myDesignerInfo;
		sharedPrefer.setValue(myDesignerInfo.get_id(), myDesignerInfo);
	}

	public MyDesignerInfo getMyDesignerInfoById(String designerId) {
		return (MyDesignerInfo) sharedPrefer.getValue(designerId);
	}

	public boolean isLogin() {
		return sharedPrefer.getValue(Constant.USER_IS_LOGIN, false);// Ĭ����û��¼
	}

	public void setLogin(boolean isLogin) {
		sharedPrefer.setValue(Constant.USER_IS_LOGIN, isLogin);
	}

	public void savaLastLoginTime(long loginTime) {
		sharedPrefer.setValue(Constant.LAST_LOGIN_TIME, loginTime);
	}

	// �Ƿ��¼��Ϣ�ѹ���
	public boolean isLoginExpire() {
		long currentTime = Calendar.getInstance().getTimeInMillis();// ��ǰʱ��
		long loginLoginTime = sharedPrefer.getValue(Constant.LAST_LOGIN_TIME,
				currentTime);
		if (currentTime - loginLoginTime > Constant.LOGIN_EXPIRE) {
			return true;
		}
		return false;
	}

	public void saveLoginUserInfo(LoginUserBean userBean) {
		sharedPrefer.setValue(Constant.ACCOUNT, userBean.getPhone());
		sharedPrefer.setValue(Constant.USERTYPE, userBean.getUsertype());
		sharedPrefer.setValue(Constant.USERNAME, userBean.getUsername());
		sharedPrefer.setValue(Constant.USERIMAGE_ID, userBean.getImageid());
		sharedPrefer.setValue(Constant.USER_ID, userBean.get_id());
		sharedPrefer.setValue(Constant.PASSWORD, userBean.getPass());
	}

	public String getPassword() {
		return sharedPrefer.getValue(Constant.PASSWORD, null);
	}

	public String getAccount() {
		return sharedPrefer.getValue(Constant.ACCOUNT, null);
	}

	public String getUserType() {
		String userType = sharedPrefer.getValue(Constant.USERTYPE, "1");
		return userType;
	}

	public String getUserId() {
		return sharedPrefer.getValue(Constant.USER_ID, null);
	}

	public String getUserName() {
		String userName = sharedPrefer.getValue(Constant.USERNAME, null);
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
		String imageId = sharedPrefer.getValue(Constant.USERIMAGE_ID, null);
		if (imageId == null) {
			if (getUserType().equals(Constant.IDENTITY_OWNER)) {
				userImagePath = Constant.DEFALUT_OWNER_PIC;
			} else if (getUserType().equals(Constant.IDENTITY_DESIGNER)) {
				userImagePath = Constant.DEFALUT_DESIGNER_PIC;
			}
		} else {
			userImagePath = Url.GET_IMAGE + imageId;
		}
		return userImagePath;
	}
	
	public void cleanData() {
		processLists = null;
		myOwnerInfo = null;
		myDesignerInfo = null;
	}

}
