package com.jianfanjia.cn.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.jianfanjia.cn.AppConfig;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.UpdateVersion;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
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
    private Handler handler = new Handler();
    private boolean first;// 用于判断导航界面是否显示
    private boolean isLoginExpire;// 是否登录过去
    private boolean isLogin;// 是否登录过
    private UpdateVersion updateVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        first = dataManager.isFirst();
        isLogin = dataManager.isLogin();
        isLoginExpire = AppConfig.getInstance(this).isLoginExpire();
        LogTool.d(this.getClass().getName(), "first=" + first);
        checkVersion();
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
                                    showNewVersionDialog(WelcomeActivity.this,
                                            String.format(getString(R.string.new_version_message),
                                                    updateVersion.getVersion_name()),
                                            updateVersion);
                                } else {
//                                    makeTextLong(getString(R.string.no_new_version));
                                    handler.postDelayed(runnable, 2000);
                                }
                            }
                        }
                    }

                    @Override
                    public void loadFailture(String error_msg) {

                    }
                }
        );
    }

    /**
     * 显示新版本对话框
     *
     * @param activity
     * @param message
     * @param updateVersion
     */
    public void showNewVersionDialog(final Activity activity, String message, final UpdateVersion updateVersion) {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(activity);
        dialog.setTitle("版本更新");
        dialog.setMessage(message);
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UiHelper.startUpdateService(activity, updateVersion.getDownload_url());
                        if(updateVersion.getUpdatetype().equals(Global.REC_UPDATE)){
                            handler.postDelayed(runnable, 2000);
                        }
                    }

                });
        dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(updateVersion.getUpdatetype().equals(Global.REC_UPDATE)){
                    handler.postDelayed(runnable, 2000);
                }else{
                    WelcomeActivity.this.finish();
                }
            }

        });
        dialog.show();
    }



    @Override
    public void initView() {

    }

    @Override
    public void setListener() {
        // TODO Auto-generated method stub
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void loadFailture(String error_msg) {
        startActivity(LoginNewActivity_.class);
        finish();
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (!first) {
                if (!isLogin) {
                    Log.i(this.getClass().getName(), "没有登录");
                    startActivity(LoginNewActivity_.class);
                    finish();
                } else {
                    if (!isLoginExpire) {// 登录未过期，添加cookies到httpclient记录身份
                        Log.i(this.getClass().getName(), "未过期");
                        startActivity(MainActivity.class);
                        finish();
                    } else {
                        Log.i(this.getClass().getName(), "已经过期");
                        MyApplication.getInstance().clearCookie();
                        JianFanJiaClient.login(WelcomeActivity.this, dataManager.getAccount(), dataManager.getPassword(), WelcomeActivity.this, WelcomeActivity.this);
                    }
                }
            } else {
                startActivity(NavigateActivity.class);
                finish();
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
