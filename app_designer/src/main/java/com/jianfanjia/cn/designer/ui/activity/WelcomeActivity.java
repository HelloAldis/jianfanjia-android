package com.jianfanjia.cn.designer.ui.activity;

import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.UpdateVersion;
import com.jianfanjia.api.request.common.CheckVersionRequest;
import com.jianfanjia.api.request.common.RefreshSessionRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.GeTuiManager;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.ui.activity.login_and_register.LoginNewActivity;
import com.jianfanjia.cn.designer.view.dialog.CommonDialog;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;
import com.jianfanjia.common.tool.LogTool;

/**
 * @author fengliang
 * @ClassName: WelcomeActivity
 * @Description: 欢迎
 * @date 2015-8-29 上午9:30:21
 */
public class WelcomeActivity extends BaseActivity{
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

        initView();
        logGeTuiAPPKey();
        first = dataManager.isFirst();
        LogTool.d("first=" + first);
        checkVersion();
    }

    private void logGeTuiAPPKey() {
        try {
            ApplicationInfo var4 = getPackageManager().getApplicationInfo(getPackageName(), 128);
            if (var4 != null && var4.metaData != null) {
                String var5 = var4.metaData.getString("PUSH_APPID");
                String var6 = var4.metaData.getString("PUSH_APPSECRET");
                String var7 = var4.metaData.get("PUSH_APPKEY") != null ? var4.metaData.get("PUSH_APPKEY").toString()
                        : null;
                if (var5 != null) {
                    var5 = var5.trim();
                }
                if (var6 != null) {
                    var6 = var6.trim();
                }
                if (var7 != null) {
                    var7 = var7.trim();
                }
                LogTool.d("PUSH_APPID :" + var5 + "--" + "PUSH_APPSECRET :" + var6 + "--" + "PUSH_APPKEY:" + var7);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 检查版本
    private void checkVersion() {
        Api.checkVesion(new CheckVersionRequest(), new ApiCallback<ApiResponse<UpdateVersion>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<UpdateVersion> apiResponse) {
                updateVersion = apiResponse.getData();
                if (updateVersion != null) {
                    if (Integer.parseInt(updateVersion
                            .getVersion_code()) > MyApplication
                            .getInstance().getVersionCode()) {
                        showNewVersionDialog(
                                String.format(getString(R.string.new_version_message),
                                        updateVersion.getVersion_name()),
                                updateVersion);
                    } else {
                        handler.postDelayed(runnable, 1500);
                    }
                }
            }

            @Override
            public void onFailed(ApiResponse<UpdateVersion> apiResponse) {
                handler.postDelayed(runnable, 1500);
            }

            @Override
            public void onNetworkError(int code) {
                handler.postDelayed(runnable, 1500);
            }
        },this);

    }

    /**
     * 显示新版本对话框
     *
     * @param message
     * @param updateVersion
     */
    public void showNewVersionDialog(String message, final UpdateVersion updateVersion) {
        dialog.setTitle(getString(R.string.version_update));
        dialog.setMessage(message);
        dialog.setPositiveButton(R.string.update,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UiHelper.startUpdateService(WelcomeActivity.this, updateVersion.getDownload_url());
                        if (updateVersion.getUpdatetype().equals(Global.REC_UPDATE)) {
                            handler.postDelayed(runnable, 1000);
                        }
                    }

                });
        dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (updateVersion.getUpdatetype().equals(Global.REC_UPDATE)) {
                    handler.postDelayed(runnable, 1000);
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

    public void initView() {
        LogTool.d("initView");
        dialog = DialogHelper
                .getPinterestDialog(this);
        GeTuiManager.initGeTui(getApplicationContext());
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            if (!first) {
                isLogin = dataManager.isLogin();
                isLoginExpire = dataManager.isLoginExpire();
                LogTool.d("not first");
                if (!isLogin) {
                    LogTool.d("not login");
                    startActivity(LoginNewActivity.class);
                    appManager.finishActivity(WelcomeActivity.this);
                } else {
                    if (!isLoginExpire) {// 登录未过期，添加cookies到httpclient记录身份
                        LogTool.d("not expire");
                        GeTuiManager.bindGeTui(getApplicationContext(), dataManager.getUserId());
                        startActivity(MainActivity.class);
                        appManager.finishActivity(WelcomeActivity.this);
                    } else {
                        LogTool.d("expire");
//                        ApiClient.clearCookie();
                        refreshSession();
                    }
                }
//            } else {
//                LogTool.d(TAG, "启动导航");
//                startActivity(NavigateActivity.class);
//                appManager.finishActivity(WelcomeActivity.this);
//            }
        }
    };

    private void refreshSession() {
        RefreshSessionRequest refreshSessionRequest = new RefreshSessionRequest();
        refreshSessionRequest.set_id(dataManager.getUserId());
        Api.refreshSession(refreshSessionRequest, new ApiCallback<ApiResponse<Designer>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<Designer> apiResponse) {
                startActivity(MainActivity.class);
                appManager.finishActivity(WelcomeActivity.this);
            }

            @Override
            public void onFailed(ApiResponse<Designer> apiResponse) {
                startActivity(LoginNewActivity.class);
                appManager.finishActivity(WelcomeActivity.this);
            }

            @Override
            public void onNetworkError(int code) {
                startActivity(LoginNewActivity.class);
                appManager.finishActivity(WelcomeActivity.this);
            }
        },this);
    }

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
