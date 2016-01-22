package com.jianfanjia.cn.designer.cache;

import android.content.Context;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.bean.LoginUserBean;
import com.jianfanjia.cn.designer.bean.OwnerInfo;
import com.jianfanjia.cn.designer.bean.Process;
import com.jianfanjia.cn.designer.bean.ProcessInfo;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.config.Constant;
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
    private RequirementInfo requirementInfo;// 需求信息
    private ProcessInfo currentProcessInfo;// 当前工地信息p
    private String currentUploadImageId;// 当前上传的imageId;

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

    public void setOwnerInfo(OwnerInfo ownerInfo) {
        sharedPreferdata.setValue(ownerInfo.get_id(), ownerInfo);
    }

    public void setCurrentProcessInfo(ProcessInfo currentProcessInfo) {
        this.currentProcessInfo = currentProcessInfo;
    }

    public ProcessInfo getDefaultProcessInfo() {
        return currentProcessInfo;
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

    public int getDefaultPro() {
        if (getUserType().equals(Constant.IDENTITY_DESIGNER)) {
            return sharedPreferuser.getValue(Constant.DEFAULT_PROCESS, 0);// 默认的工地为0
        } else if (getUserType().equals(Constant.IDENTITY_OWNER)) {
            return 0;
        }
        return 0;
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
        sharedPreferuser.setValue(Constant.LAST_LOGIN_TIME, loginTime);
    }

    // 是否登录信息已过期
    public boolean isLoginExpire() {
        Calendar currentDate = Calendar.getInstance();// 当前时间
        long lastLoginTime = sharedPreferuser.getValue(
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

    public boolean isFirst() {
        return sharedPreferdata.getValue(Constant.ISFIRST, true);
    }

    public void setFisrt(boolean isFirst) {
        sharedPreferdata.setValue(Constant.ISFIRST, isFirst);
    }

    public void setUserImagePath(String imgId) {
        sharedPreferuser.setValue(Constant.USERIMAGE_ID, imgId);
    }

    public void saveLoginUserBean(LoginUserBean userBean) {
        sharedPreferuser.setValue(Constant.ACCOUNT, userBean.getPhone());
        sharedPreferuser.setValue(Constant.USERTYPE, userBean.getUsertype());
        sharedPreferuser.setValue(Constant.USERNAME, userBean.getUsername());
        sharedPreferuser.setValue(Constant.USERIMAGE_ID, userBean.getImageid());
        sharedPreferuser.setValue(Constant.USER_ID, userBean.get_id());
        sharedPreferuser.setValue(Constant.PASSWORD, userBean.getPass());
        sharedPreferuser.setValue(Constant.OPEN_ID, userBean.getWechat_openid());
        sharedPreferuser.setValue(Constant.UNION_ID, userBean.getWechat_unionid());
        sharedPreferdata.setValue(Constant.IS_WEIXIN_FIRST_LOGIN, userBean.is_wechat_first_login());
    }

    public String getWechat_unionid() {
        return sharedPreferuser.getValue(Constant.UNION_ID, null);
    }

    public void setWechat_unionid(String wechat_unionid) {
        sharedPreferuser.setValue(Constant.UNION_ID, wechat_unionid);
    }

    public void setWeixinFisrtLogin(boolean flag) {
        sharedPreferdata.setValue(Constant.IS_WEIXIN_FIRST_LOGIN, flag);
    }

    public boolean getWeixinFisrtLogin() {
        return sharedPreferdata.getValue(Constant.IS_WEIXIN_FIRST_LOGIN, false);
    }

    public void setUserName(String userName) {
        sharedPreferuser.setValue(Constant.USERNAME, userName);
    }

    public String getPassword() {
        return sharedPreferuser.getValue(Constant.PASSWORD, null);
    }

    public void setAccount(String phone) {
        sharedPreferuser.setValue(Constant.ACCOUNT, phone);
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
            userImagePath = Constant.DEFALUT_OWNER_PIC;
        } else {
            userImagePath = imageId;
        }
        return userImagePath;
    }

    public void cleanData() {
        sharedPreferuser.clear();
        processLists = null;
        requirementInfo = null;
        currentProcessInfo = null;
    }

}
