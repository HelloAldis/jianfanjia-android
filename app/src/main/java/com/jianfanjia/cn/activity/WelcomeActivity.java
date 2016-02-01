package com.jianfanjia.cn.activity;

import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.UpdateVersion;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.FileUtil;
import com.jianfanjia.cn.tools.GeTuiManager;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

/**
 * @author fengliang
 * @ClassName: WelcomeActivity
 * @Description: 欢迎
 * @date 2015-8-29 上午9:30:21
 */
public class WelcomeActivity extends BaseActivity implements ApiUiUpdateListener {
    private static final String TAG = WelcomeActivity.class.getName();
    private Handler handler = new Handler();
    private boolean first;// 用于判断导航界面是否显示
    private boolean isLoginExpire;// 是否登录过期
    private boolean isLogin;// 是否登录过
    private UpdateVersion updateVersion;
    private CommonDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        logGeTuiAPPKey();
        first = dataManager.isFirst();
        LogTool.d(TAG, "first=" + first);
        checkVersion();
        LogTool.d(TAG, "sd root =" + FileUtil.getSDRoot());
        LogTool.d(TAG, "sd ex root =" + FileUtil.getExternalSDRoot());
//        LogTool.d(TAG, "sd root =" + FileUtil.getAppCache(this, "jianfan"));
    }

    private void logGeTuiAPPKey() {
        try {
            ApplicationInfo var4 = getPackageManager().getApplicationInfo(getPackageName(), 128);
            if (var4 != null && var4.metaData != null)

            {
                String var5 = var4.metaData.getString("PUSH_APPID");
                String var6 = var4.metaData.getString("PUSH_APPSECRET");
                String var7 = var4.metaData.get("PUSH_APPKEY") != null ? var4.metaData.get("PUSH_APPKEY").toString() : null;
                if (var5 != null) {
                    var5 = var5.trim();
                }

                if (var6 != null) {
                    var6 = var6.trim();
                }

                if (var7 != null) {
                    var7 = var7.trim();
                }

                LogTool.d(TAG, "PUSH_APPID :" + var5 + "--" + "PUSH_APPSECRET :" + var6 + "--" + "PUSH_APPKEY:" + var7);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 检查版本
    private void checkVersion() {
        UiHelper.checkNewVersion(this, new ApiUiUpdateListener() {
                    @Override
                    public void preLoad() {
                    }

                    @Override
                    public void loadSuccess(Object data) {
                        if (data != null) {
                            updateVersion = JsonParser
                                    .jsonToBean(data.toString(),
                                            UpdateVersion.class);
                            if (updateVersion != null) {
                                if (Integer.parseInt(updateVersion
                                        .getVersion_code()) > MyApplication
                                        .getInstance().getVersionCode()) {
                                    showNewVersionDialog(
                                            String.format(getString(R.string.new_version_message),
                                                    updateVersion.getVersion_name()),
                                            updateVersion);
                                } else {
                                    handler.postDelayed(runnable, 2000);
                                }
                            }
                        }
                    }

                    @Override
                    public void loadFailture(String error_msg) {
                        handler.postDelayed(runnable, 2000);
                    }
                }
        );
    }

    /**
     * 显示新版本对话框
     *
     * @param message
     * @param updateVersion
     */
    public void showNewVersionDialog(String message, final UpdateVersion updateVersion) {
        dialog.setTitle("版本更新");
        dialog.setMessage(message);
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UiHelper.startUpdateService(WelcomeActivity.this, updateVersion.getDownload_url());
                        if (updateVersion.getUpdatetype().equals(Global.REC_UPDATE)) {
                            handler.postDelayed(runnable, 1500);
                        }
                    }

                });
        dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (updateVersion.getUpdatetype().equals(Global.REC_UPDATE)) {
                    handler.postDelayed(runnable, 1500);
                } else {
                    appManager.finishActivity(WelcomeActivity.this);
                }
            }

        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        dialog.dismiss();
                        appManager.AppExit();
                        return true;
                }
                return false;
            }
        });
        dialog.show();
    }

    @Override
    public void initView() {
        LogTool.d(TAG, "initView");
        dialog = DialogHelper
                .getPinterestDialog(this);
        GeTuiManager.initGeTui(getApplicationContext());
    }

    @Override
    public void setListener() {
        // TODO Auto-generated method stub
    }

    @Override
    public void loadSuccess(Object data) {
        startActivity(MainActivity.class);
        appManager.finishActivity(WelcomeActivity.this);
//        PushManager.getInstance().initialize(getApplicationContext());//初始化个推
//        PushManager.getInstance().bindAlias(getApplicationContext(), dataManager.getUserId());
    }

    @Override
    public void loadFailture(String error_msg) {
        startActivity(LoginNewActivity_.class);
        appManager.finishActivity(WelcomeActivity.this);
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (!first) {
                isLogin = dataManager.isLogin();
                isLoginExpire = dataManager.isLoginExpire();
                LogTool.d(TAG, "not first");
                if (!isLogin) {
                    LogTool.d(TAG, "not login");
                    startActivity(LoginNewActivity_.class);
                    appManager.finishActivity(WelcomeActivity.this);
                } else {
                    if (!isLoginExpire) {// 登录未过期，添加cookies到httpclient记录身份
                        LogTool.d(TAG, "not expire");
                        GeTuiManager.bindGeTui(getApplicationContext(), dataManager.getUserId());
                        startActivity(MainActivity.class);
                        appManager.finishActivity(WelcomeActivity.this);
                    } else {
                        LogTool.d(TAG, "expire");
                        MyApplication.getInstance().clearCookie();
                        JianFanJiaClient.refreshSession(WelcomeActivity.this, dataManager.getUserId(), WelcomeActivity.this, WelcomeActivity.this);
//                        JianFanJiaClient.login(WelcomeActivity.this, dataManager.getAccount(), dataManager.getPassword(), WelcomeActivity.this, WelcomeActivity.this);
                    }
                }
            } else {
                LogTool.d(TAG, "启动导航");
                startActivity(NavigateActivity.class);
                appManager.finishActivity(WelcomeActivity.this);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

}
