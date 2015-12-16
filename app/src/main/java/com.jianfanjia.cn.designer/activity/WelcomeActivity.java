package com.jianfanjia.cn.designer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.tools.LogTool;

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
                        JianFanJiaClient.login(WelcomeActivity.this, dataManager.getAccount(), dataManager
                                .getPassword(), WelcomeActivity.this, this);
                    }
                }
            } else {
                startActivity(NavigateActivity.class);
                finish();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        first = dataManager.isFirst();
        isLogin = dataManager.isLogin();
        isLoginExpire = dataManager.isLoginExpire();
        LogTool.d(this.getClass().getName(), "first=" + first);
    }

    @Override
    public void initView() {
        handler.postDelayed(runnable, 2000);
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
    public void loadFailture(String errorMsg) {
        startActivity(LoginNewActivity_.class);
        finish();
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
