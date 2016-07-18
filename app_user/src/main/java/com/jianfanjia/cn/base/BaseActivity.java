package com.jianfanjia.cn.base;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import butterknife.ButterKnife;
import com.jianfanjia.api.ApiClient;
import com.jianfanjia.cn.AppManager;
import com.jianfanjia.cn.business.DataManagerNew;
import com.jianfanjia.cn.tools.ImageShow;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.common.tool.LogTool;
import com.umeng.analytics.MobclickAgent;

/**
 * Description:activity基类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected LayoutInflater inflater = null;
    protected FragmentManager fragmentManager = null;
    protected NotificationManager nManager = null;
    protected DataManagerNew dataManager;
    protected ImageShow imageShow;
    protected AppManager appManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.d("onCreate()");
        setContentView(getLayoutId());
        init(savedInstanceState);
        ButterKnife.bind(this);
    }

    protected void init(Bundle savedInstanceState) {
        appManager = AppManager.getAppManager();
        appManager.addActivity(this);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        dataManager = DataManagerNew.getInstance();
        fragmentManager = this.getSupportFragmentManager();
        imageShow = ImageShow.getImageShow();
    }

    protected Context getUiContext() {
        if (this != null) {
            return this;
        }
        return getApplicationContext();
    }

    public abstract int getLayoutId();

    @Override
    protected void onStart() {
        super.onStart();
        LogTool.d("onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogTool.d("onResume()");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogTool.d("onPause()");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogTool.d("onStop()");
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        appManager.finishActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApiClient.cancelTag(this);
        LogTool.d("onDestroy()");
    }

    protected void makeTextShort(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    // 通过Class跳转界面
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    // 含有Bundle通过Class跳转界面
    protected void startActivity(Class<?> cls, Bundle bundle) {
        startActivity(cls, bundle, -1);
    }

    protected void startActivity(Class<?> cls, Bundle bundle, int flag) {
        IntentUtil.startActivityHasFlag(this, cls, bundle, flag);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode) {
        IntentUtil.startActivityForResult(this, cls, null, requestCode);
    }

    protected void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        IntentUtil.startActivityForResult(this, cls, bundle, requestCode);
    }

}
