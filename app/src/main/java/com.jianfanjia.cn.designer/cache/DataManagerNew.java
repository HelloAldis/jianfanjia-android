package com.jianfanjia.cn.designer.cache;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.bean.DesignerInfo;
import com.jianfanjia.cn.designer.bean.LoginUserBean;
import com.jianfanjia.cn.designer.bean.OwnerInfo;
import com.jianfanjia.cn.designer.bean.Process;
import com.jianfanjia.cn.designer.bean.ProcessInfo;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.SharedPrefer;

import java.util.Calendar;
import java.util.List;

public class DataManagerNew {
    private static final String TAG = DataManagerNew.class.getName();
    private static DataManagerNew instance;
    private Context context;

    private SharedPrefer sharedPreferdata = null;
    private SharedPrefer sharedPreferuser = null;
    private List<Process> processLists;
    private DesignerInfo designerInfo;// 设计师的个人信息
    private RequirementInfo requirementInfo;// 需求信息
    private ProcessInfo currentProcessInfo;// 当前工地信息p
    private String currentUploadImageId;// 当前上传的imageId;
    private String picPath = null;

    private DataManagerNew() {
        context = MyApplication.getInstance();
        sharedPreferdata = new SharedPrefer(context, Constant.SHARED_DATA);
        sharedPreferuser = new SharedPrefer(context, Constant.SHARED_USER);
    }

    public static DataManagerNew getInstance() {
        if (instance == null) {
            instance = new DataManagerNew();
        }
        return instance;
    }

    public String getPicPath() {
        return sharedPreferdata.getValue(Constant.TEMP_IMG, null);
    }

    public void setPicPath(String picPath) {
        this.sharedPreferdata.setValue(Constant.TEMP_IMG, picPath);
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

    public OwnerInfo getOwnerInfoById(String ownerId) {
        return (OwnerInfo) sharedPreferdata.getValue(ownerId);
    }

    public DesignerInfo getDesignerInfoById(String designerId) {
        return (DesignerInfo) sharedPreferdata.getValue(designerId);
    }

    // 设计师用户获取个人资料
    public DesignerInfo getDesignerInfo() {
        return getDesignerInfoById(getUserId());
    }

    public void setDesignerInfo(DesignerInfo designerInfo) {
        sharedPreferdata.setValue(designerInfo.get_id(), designerInfo);
    }

    public void setCurrentProcessInfo(ProcessInfo currentProcessInfo) {
        this.currentProcessInfo = currentProcessInfo;
    }

    public ProcessInfo getDefaultProcessInfo() {
        return currentProcessInfo;
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
        return sharedPreferuser.getValue(Constant.USER_IS_LOGIN, false);// 默认是没登录
    }

    public void setLogin(boolean isLogin) {
        sharedPreferuser.setValue(Constant.USER_IS_LOGIN, isLogin);
    }

    public void savaLastLoginTime(long loginTime) {
        sharedPreferdata.setValue(Constant.LAST_LOGIN_TIME, loginTime);
    }

    // 是否登录信息已过期
    public boolean isLoginExpire() {
        Calendar currentDate = Calendar.getInstance();// 当前时间
        long lastLoginTime = sharedPreferdata.getValue(
                Constant.LAST_LOGIN_TIME, currentDate.getTimeInMillis());
        Calendar lastLoginDate = Calendar.getInstance();
        lastLoginDate.setTimeInMillis(lastLoginTime);
        boolean isExipre = !(currentDate.get(Calendar.YEAR) == lastLoginDate.get(Calendar.YEAR)
                && currentDate.get(Calendar.MONTH) == lastLoginDate.get(Calendar.MONTH)
                && currentDate.get(Calendar.DAY_OF_MONTH) == lastLoginDate.get(Calendar.DAY_OF_MONTH));
        LogTool.d(TAG, "currentDate =" + currentDate.get(Calendar.DAY_OF_MONTH));
        LogTool.d(TAG, "isloginExipre =" + isExipre);
        return isExipre;
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
        return sharedPreferuser.getValue(Constant.ISFIRST, true);
    }

    public void setFisrt(boolean isFirst) {
        sharedPreferuser.setValue(Constant.ISFIRST, isFirst);
    }

    public void saveLoginUserInfo(LoginUserBean userBean) {
        sharedPreferuser.setValue(Constant.ACCOUNT, userBean.getPhone());
        sharedPreferuser.setValue(Constant.USERNAME, userBean.getUsername());
        sharedPreferuser.setValue(Constant.USERIMAGE_ID, userBean.getImageid());
        sharedPreferuser.setValue(Constant.USER_ID, userBean.get_id());
        sharedPreferuser.setValue(Constant.PASSWORD, userBean.getPass());
    }

    public String getPassword() {
        return sharedPreferuser.getValue(Constant.PASSWORD, null);
    }

    public String getAccount() {
        return sharedPreferuser.getValue(Constant.ACCOUNT, null);
    }

    public String getUserType() {
        String userType = sharedPreferuser.getValue(Constant.USERTYPE, Constant.IDENTITY_DESIGNER);
        return userType;
    }

    public String getUserId() {
        return sharedPreferuser.getValue(Constant.USER_ID, null);
    }

    public String getUserName() {
        String userName = sharedPreferuser.getValue(Constant.USERNAME, null);
        if (userName == null) {
            return context.getString(R.string.designer);
        }
        return userName;
    }

    public void setUserName(String userName) {
        sharedPreferuser.setValue(Constant.USERNAME, userName);
    }

    public String getUserImagePath() {
        String userImagePath = null;
        String imageId = sharedPreferuser.getValue(Constant.USERIMAGE_ID, null);
        if (imageId == null) {
            userImagePath = Constant.DEFALUT_DESIGNER_PIC;
        } else {
            userImagePath = imageId;
        }
        return userImagePath;
    }

    public void setUserImagePath(String imgId) {
        sharedPreferuser.setValue(Constant.USERIMAGE_ID, imgId);
    }

    public void cleanData() {
        processLists = null;
        designerInfo = null;
        requirementInfo = null;
        currentProcessInfo = null;
        sharedPreferdata.clear();
    }

}
