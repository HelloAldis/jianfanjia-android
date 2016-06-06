package com.jianfanjia.cn.activity.business;

import android.content.Context;
import android.text.TextUtils;

import java.util.Calendar;

import com.jianfanjia.cn.activity.application.MyApplication;
import com.jianfanjia.cn.activity.tools.SharedPrefer;
import com.jianfanjia.api.ApiClient;
import com.jianfanjia.api.model.User;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.config.Constant;
import com.jianfanjia.cn.activity.tools.GeTuiManager;
import com.jianfanjia.common.tool.LogTool;

public class DataManagerNew {
    private static final String TAG = DataManagerNew.class.getName();
    private static DataManagerNew instance;
    private Context context;
    private SharedPrefer sharedPreferApp = null;
    private SharedPrefer sharedPreferuser = null;

    public static DataManagerNew getInstance() {
        if (instance == null) {
            synchronized (DataManagerNew.class) {
                if (instance == null) {
                    instance = new DataManagerNew();
                }
            }
        }
        return instance;
    }

    private DataManagerNew() {
        context = MyApplication.getInstance();
        sharedPreferApp = new SharedPrefer(context, Constant.SHARED_DATA);
        sharedPreferuser = new SharedPrefer(context, Constant.SHARED_USER);
    }

    public String getPicPath() {
        return sharedPreferApp.getValue(Constant.TEMP_IMG, null);
    }

    public void setPicPath(String picPath) {
        this.sharedPreferApp.setValue(Constant.TEMP_IMG, picPath);
    }

    public void setOwnerInfo(User user) {
        sharedPreferApp.setValue(user.get_id(), user);
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

    public boolean isFirst() {
        return sharedPreferApp.getValue(Constant.ISFIRST, true);
    }

    public void setFisrt(boolean isFirst) {
        sharedPreferApp.setValue(Constant.ISFIRST, isFirst);
    }

    public boolean isShowGuide() {
        return sharedPreferApp.getValue(Constant.ISSHOWGUIDE, true);
    }

    public void setShowGuide(boolean isShowGuide) {
        sharedPreferApp.setValue(Constant.ISSHOWGUIDE, isShowGuide);
    }

    public boolean isShowNext() {
        return sharedPreferApp.getValue(Constant.ISSHOWNEXT, true);
    }

    public void setShowNext(boolean isShowNext) {
        sharedPreferApp.setValue(Constant.ISSHOWNEXT, isShowNext);
    }

    public void setUserImagePath(String imgId) {
        sharedPreferuser.setValue(Constant.USERIMAGE_ID, imgId);
    }

    public void saveLoginUserBean(User userBean) {
        sharedPreferuser.setValue(Constant.ACCOUNT, userBean.getPhone());
        sharedPreferuser.setValue(Constant.USERTYPE, userBean.getUsertype());
        sharedPreferuser.setValue(Constant.USERNAME, userBean.getUsername());
        sharedPreferuser.setValue(Constant.USERIMAGE_ID, userBean.getImageid());
        sharedPreferuser.setValue(Constant.USER_ID, userBean.get_id());
        sharedPreferuser.setValue(Constant.OPEN_ID, userBean.getWechat_openid());
        sharedPreferuser.setValue(Constant.UNION_ID, userBean.getWechat_unionid());
        sharedPreferuser.setValue(Constant.IS_WEIXIN_FIRST_LOGIN, userBean.is_wechat_first_login());
    }

    public String getWechat_unionid() {
        return sharedPreferuser.getValue(Constant.UNION_ID, null);
    }

    public boolean getWeixinFisrtLogin() {
        return sharedPreferuser.getValue(Constant.IS_WEIXIN_FIRST_LOGIN, false);
    }

    public void setUserName(String userName) {
        sharedPreferuser.setValue(Constant.USERNAME, userName);
    }

    public void setAccount(String phone) {
        sharedPreferuser.setValue(Constant.ACCOUNT, phone);
    }

    public String getAccount() {
        return sharedPreferuser.getValue(Constant.ACCOUNT, null);
    }

    public String getUserId() {
        return sharedPreferuser.getValue(Constant.USER_ID, null);
    }

    public String getUserName() {
        String userName = sharedPreferuser.getValue(Constant.USERNAME, null);
        if (TextUtils.isEmpty(userName)) {
            return context.getString(R.string.ower);
        }
        return userName;
    }

    public String getUserImagePath() {
        String userImagePath;
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
    }

    /**
     * 登录成功所做的基本操作
     *
     * @param user
     */
    public static void loginSuccess(User user) {
        getInstance().setLogin(true);
        getInstance().savaLastLoginTime(Calendar.getInstance()
                .getTimeInMillis());
        getInstance().saveLoginUserBean(user);
        GeTuiManager.bindGeTui(MyApplication.getInstance(), user.get_id());
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
