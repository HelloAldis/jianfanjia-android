package com.jianfanjia.cn.cache;

import android.content.Context;

import java.util.Calendar;

import com.jianfanjia.api.model.User;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.db.DBHelper;
import com.jianfanjia.cn.tools.GeTuiManager;
import com.jianfanjia.cn.tools.SharedPrefer;
import com.jianfanjia.common.tool.LogTool;

public class DataManagerNew {
    private static final String TAG = DataManagerNew.class.getName();
    private static DataManagerNew instance;
    private Context context;
    private SharedPrefer sharedPreferdata = null;
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
        sharedPreferdata = new SharedPrefer(context, Constant.SHARED_DATA);
        sharedPreferuser = new SharedPrefer(context, Constant.SHARED_USER);
    }

    public static void loginSuccess(User user) {
        getInstance().setLogin(true);
        getInstance().savaLastLoginTime(Calendar.getInstance()
                .getTimeInMillis());
        getInstance().saveLoginUserBean(user);
        GeTuiManager.bindGeTui(MyApplication.getInstance(), getInstance().getUserId());
    }

    public String getPicPath() {
        return sharedPreferdata.getValue(Constant.TEMP_IMG, null);
    }

    public void setPicPath(String picPath) {
        this.sharedPreferdata.setValue(Constant.TEMP_IMG, picPath);
    }

    public void setOwnerInfo(User user) {
        sharedPreferdata.setValue(user.get_id(), user);
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

    public boolean isShowGuide() {
        return sharedPreferdata.getValue(Constant.ISSHOWGUIDE, true);
    }

    public void setShowGuide(boolean isShowGuide) {
        sharedPreferdata.setValue(Constant.ISSHOWGUIDE, isShowGuide);
    }

    public boolean isShowNext() {
        return sharedPreferdata.getValue(Constant.ISSHOWNEXT, true);
    }

    public void setShowNext(boolean isShowNext) {
        sharedPreferdata.setValue(Constant.ISSHOWNEXT, isShowNext);
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
        sharedPreferuser.setValue(Constant.PASSWORD, userBean.getPass());
        sharedPreferuser.setValue(Constant.OPEN_ID, userBean.getWechat_openid());
        sharedPreferuser.setValue(Constant.UNION_ID, userBean.getWechat_unionid());
        sharedPreferdata.setValue(Constant.IS_WEIXIN_FIRST_LOGIN, userBean.is_wechat_first_login());
    }

    public String getWechat_unionid() {
        return sharedPreferuser.getValue(Constant.UNION_ID, null);
    }

    public boolean getWeixinFisrtLogin() {
        return sharedPreferdata.getValue(Constant.IS_WEIXIN_FIRST_LOGIN, false);
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
            return context.getString(R.string.ower);
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
        DataCleanManager.cleanDatabaseByName(context, DBHelper.DBNAME);
    }

}
