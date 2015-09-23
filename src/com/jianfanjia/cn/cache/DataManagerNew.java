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
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
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

	private SharedPrefer sharedPreferdata = null;
	private SharedPrefer sharedPreferuser = null;
	private List<Process> processLists;
	private Map<String, ProcessInfo> processMap = new HashMap<String, ProcessInfo>();
	private OwnerInfo ownerInfo;// ҵ���ĸ�����Ϣ
	private DesignerInfo designerInfo;// ���ʦ�ĸ�����Ϣ
	private String totalDuration;// �ܹ���
	private RequirementInfo requirementInfo;// ������Ϣ
	private ProcessInfo currentProcessInfo;// ��ǰ������Ϣp
	private String currentUploadImageId;// ��ǰ�ϴ���imageId;

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

	public String getCurrentUploadImageId() {
		return currentUploadImageId;
	}

	public void setCurrentUploadImageId(String currentUploadImageId) {
		this.currentUploadImageId = currentUploadImageId;
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

	public OwnerInfo getOwnerInfoById(String ownerId) {
		return (OwnerInfo) sharedPreferdata.getValue(ownerId);
	}

	public void setDesignerInfo(DesignerInfo designerInfo) {
		sharedPreferdata.setValue(designerInfo.get_id(), designerInfo);
	}

	public DesignerInfo getDesignerInfoById(String designerId) {
		return (DesignerInfo) sharedPreferdata.getValue(designerId);
	}

	public void setOwnerInfo(OwnerInfo ownerInfo) {
		sharedPreferdata.setValue(ownerInfo.get_id(), ownerInfo);
	}

	public Object getOwnerOrDesignerByIdAndType(String userType, String _id) {
		if (userType.equals(Constant.IDENTITY_DESIGNER)) {
			return getDesignerInfoById(_id);
		} else if (userType.equals(Constant.IDENTITY_OWNER)) {
			return getOwnerInfoById(_id);
		}
		return null;
	}

	// ���ʦ�û���ȡ��������
	public DesignerInfo getDesignerInfo() {
		return getDesignerInfoById(getUserId());
		/*
		 * if (designerInfo == null && !NetTool.isNetworkAvailable(context)) {
		 * designerInfo = getDesignerInfoById(getUserId()); } return
		 * designerInfo;
		 */
	}

	// ҵ���û���ȡ��������
	public OwnerInfo getOwnerInfo() {
		return getOwnerInfoById(getUserId());
		/*
		 * if (ownerInfo == null && !NetTool.isNetworkAvailable(context)) {
		 * ownerInfo = getOwnerInfoById(getUserId()); } return ownerInfo;
		 */
	}

	public void setCurrentProcessInfo(ProcessInfo currentProcessInfo) {
		this.currentProcessInfo = currentProcessInfo;
	}

	public ProcessInfo getDefaultProcessInfo() {
		if (currentProcessInfo == null) {
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
			return sharedPreferdata.getValue(Constant.DEFAULT_PROCESS, 0);// Ĭ�ϵĹ���Ϊ0
		} else if (getUserType().equals(Constant.IDENTITY_OWNER)) {
			return 0;
		}
		return 0;
	}

	public void setDefaultPro(int defaultPro) {
		sharedPreferdata.setValue(Constant.DEFAULT_PROCESS, defaultPro);
	}

	/**
	 * �ù�����Ϣ
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

	public boolean isLogin() {
		return sharedPreferdata.getValue(Constant.USER_IS_LOGIN, false);// Ĭ����û��¼
	}

	public void setLogin(boolean isLogin) {
		sharedPreferdata.setValue(Constant.USER_IS_LOGIN, isLogin);
	}

	public void savaLastLoginTime(long loginTime) {
		sharedPreferdata.setValue(Constant.LAST_LOGIN_TIME, loginTime);
	}

	// �Ƿ��¼��Ϣ�ѹ���
	public boolean isLoginExpire() {
		long currentTime = Calendar.getInstance().getTimeInMillis();// ��ǰʱ��
		long loginLoginTime = sharedPreferdata.getValue(
				Constant.LAST_LOGIN_TIME, currentTime);
		if (currentTime - loginLoginTime > Constant.LOGIN_EXPIRE) {
			return true;
		}
		return false;
	}

	public boolean isConfigPro() {
		return sharedPreferdata.getValue(Constant.ISCONFIG_PROCESS, false);
	}

	public void setConfigPro(boolean isConfig) {
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
		sharedPreferuser.setValue(Constant.USERIMAGE_ID, imgId);
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
		ownerInfo = null;
		designerInfo = null;
		requirementInfo = null;
		totalDuration = null;
		currentProcessInfo = null;
		processMap.clear();
		sharedPreferdata.clear();
	}

}
