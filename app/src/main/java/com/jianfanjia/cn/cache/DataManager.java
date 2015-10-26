package com.jianfanjia.cn.cache;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.SharedPrefer;

import java.util.Calendar;
import java.util.List;

public class DataManager {
    private static final String TAG = DataManager.class.getName();
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    private static DataManager instance;
    private Context context;
    private SharedPrefer sharedPreferdata = null;
    private SharedPrefer sharedPreferuser = null;
    private List<Process> processLists;
    private List<RequirementInfo> requirementInfoList;
    private ProcessInfo currentProcessInfo;// 当前工地信息p
    private String currentUploadImageId;// 当前上传的imageId;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private DataManager() {
        context = MyApplication.getInstance();
        sharedPreferdata = new SharedPrefer(context, Constant.SHARED_DATA);
        sharedPreferuser = new SharedPrefer(context, Constant.SHARED_USER);
    }

    public void saveRequirementInfoList(String jsonRequirementInfoList){
        sharedPreferdata.setValue(Constant.REQUIREMENTINFO_LIST,jsonRequirementInfoList);
    }

    public void setRequirementInfoList(List<RequirementInfo> requirementInfoList){
        this.requirementInfoList = requirementInfoList;
    }

    public List<RequirementInfo> getRequirementInfoList(){
        return  requirementInfoList;
    }

    public List<RequirementInfo> getRequirementInfoListByCache(){
        String jsonRequirementList = sharedPreferdata.getValue(
                Constant.REQUIREMENTINFO_LIST, null);
        if (jsonRequirementList != null) {
            requirementInfoList = JsonParser.jsonToList(jsonRequirementList,
                    new TypeToken<List<RequirementInfo>>() {
                    }.getType());
        }
        return requirementInfoList;
    }

    public String getCurrentUploadImageId() {
        return currentUploadImageId;
    }

    public void setCurrentUploadImageId(String currentUploadImageId) {
        this.currentUploadImageId = currentUploadImageId;
    }

    public void setCurrentProcessInfo(ProcessInfo currentProcessInfo) {
        this.currentProcessInfo = currentProcessInfo;
    }

    public ProcessInfo getDefaultProcessInfo() {
        return currentProcessInfo;
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
         return sharedPreferuser.getValue(Constant.DEFAULT_PROCESS, 0);// 默认的工地为0
    }

    public void setDefaultPro(int defaultPro) {
        sharedPreferuser.setValue(Constant.DEFAULT_PROCESS, defaultPro);
    }

    /**
     * 拿工地信息
     *
     * @param processId
     * @return
     */
    public ProcessInfo getProcessInfoById(String processId) {
        return (ProcessInfo) sharedPreferdata.getValue(processId);
    }

    public void saveProcessInfo(ProcessInfo processInfo) {
        sharedPreferdata.setValue(processInfo.get_id(), processInfo);
    }

    public boolean isLogin() {
        return sharedPreferdata.getValue(Constant.USER_IS_LOGIN, false);// 默认是没登录
    }

    public void setLogin(boolean isLogin) {
        sharedPreferdata.setValue(Constant.USER_IS_LOGIN, isLogin);
    }

    public void savaLastLoginTime(long loginTime) {
        sharedPreferuser.setValue(Constant.LAST_LOGIN_TIME, loginTime);
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

    public int getCurrentList() {
        return sharedPreferuser.getValue(Constant.CURRENT_LIST, 0);
    }

    public void setCurrentList(int currentList) {
        sharedPreferuser.setValue(Constant.CURRENT_LIST, currentList);
    }

    public boolean isPushOpen() {
        return sharedPreferuser.getValue(Constant.ISOPEN, true);
    }

    public void setPushOpen(boolean isOpen) {
        sharedPreferuser.setValue(Constant.ISOPEN, isOpen);
    }

    public boolean isFirst() {
        return sharedPreferdata.getValue(Constant.ISFIRST, true);
    }

    public void setFisrt(boolean isFirst) {
        sharedPreferdata.setValue(Constant.ISFIRST, isFirst);
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
        currentProcessInfo = null;
        // sharedPreferdata.clear();
        sharedPreferuser.clear();
    }

}