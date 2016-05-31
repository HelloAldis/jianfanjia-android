package com.jianfanjia.cn.designer.business;

import android.content.Context;
import android.text.TextUtils;

import java.util.Calendar;

import com.jianfanjia.api.ApiClient;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.GeTuiManager;
import com.jianfanjia.cn.designer.tools.SharedPrefer;
import com.jianfanjia.common.tool.LogTool;

public class DataManagerNew {
    private static final String TAG = DataManagerNew.class.getName();
    private static DataManagerNew instance;
    private Context context;
    private SharedPrefer sharedPreferdata = null;
    private SharedPrefer sharedPreferuser = null;
    private Requirement requirementInfo;// 需求信息
    private Designer mDesigner;

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

    public Designer getDesigner() {
        if (mDesigner == null) {
            return (Designer) sharedPreferuser.getValue(Global.DESIGNER_INFO);
        }
        return mDesigner;
    }

    public void setDesigner(Designer designer) {
        mDesigner = designer;
        sharedPreferuser.setValue(Global.DESIGNER_INFO, designer);
    }

    public String getPicPath() {
        return sharedPreferdata.getValue(Constant.TEMP_IMG, null);
    }

    public void setPicPath(String picPath) {
        this.sharedPreferdata.setValue(Constant.TEMP_IMG, picPath);
    }

    public Requirement getRequirementInfo() {
        return requirementInfo;
    }

    public void setRequirementInfo(Requirement requirementInfo) {
        this.requirementInfo = requirementInfo;
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

    public String getOldAccount() {
        return sharedPreferuser.getValue(Constant.ACCOUNT, null);
    }

    public String getOldUserId() {
        return sharedPreferuser.getValue(Constant.USER_ID, null);
    }

    public String getOldUserName() {
        String userName = sharedPreferuser.getValue(Constant.USERNAME, null);
        if (TextUtils.isEmpty(userName)) {
            return context.getString(R.string.ower);
        }
        return userName;
    }

    public String getOldUserImagePath() {
        String userImagePath;
        String imageId = sharedPreferuser.getValue(Constant.USERIMAGE_ID, null);
        if (imageId == null) {
            userImagePath = Constant.DEFALUT_OWNER_PIC;
        } else {
            userImagePath = imageId;
        }
        return userImagePath;
    }

    public boolean isFirst() {
        return sharedPreferdata.getValue(Constant.ISFIRST, true);
    }

    public void setFisrt(boolean isFirst) {
        sharedPreferdata.setValue(Constant.ISFIRST, isFirst);
    }

    public String getAccount() {
        if(getDesigner() != null){
            return getDesigner().getPhone();
        }
        return getOldAccount();
    }

    public String getUserId() {
        if(getDesigner() != null){
            return getDesigner().get_id();
        }
        return getOldUserId();
    }

    public String getUserName() {
        if(getDesigner() != null){
            return getDesigner().getUsername();
        }
        return getOldUserName();
    }

    public String getUserImagePath() {
        if(getDesigner() != null){
            String userImagePath;
            String imageId = getDesigner().getImageid();
            if (imageId == null) {
                userImagePath = Constant.DEFALUT_OWNER_PIC;
            } else {
                userImagePath = imageId;
            }
            return userImagePath;
        }
        return getOldUserImagePath();
    }

    public void cleanData() {
        sharedPreferuser.clear();
        requirementInfo = null;
    }

    /**
     * 登录成功所做的基本操作
     *
     * @param designer
     */
    public static void loginSuccess(Designer designer) {
        getInstance().savaLastLoginTime(Calendar.getInstance()
                .getTimeInMillis());
        getInstance().setDesigner(designer);
        GeTuiManager.bindGeTui(MyApplication.getInstance(), getInstance().getUserId());
    }

    /**
     * 登出所做的操作
     */
    public static void loginOut() {
        GeTuiManager.cancelBind(MyApplication.getInstance(), getInstance().getUserId());
        getInstance().cleanData();
        ApiClient.clearCookie();
    }


}
