package com.jianfanjia.cn.designer.base;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import butterknife.ButterKnife;
import com.jianfanjia.api.ApiClient;
import com.jianfanjia.cn.designer.AppManager;
import com.jianfanjia.cn.designer.business.DataManagerNew;
import com.jianfanjia.cn.designer.tools.ImageShow;
import com.jianfanjia.cn.designer.view.AddPhotoDialog;
import com.jianfanjia.common.tool.LogTool;
import com.umeng.analytics.MobclickAgent;

/**
 * Description:activity基类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public abstract class BaseActivity extends AppCompatActivity{
    protected DownloadManager downloadManager = null;
    protected LayoutInflater inflater = null;
    protected FragmentManager fragmentManager = null;
    protected NotificationManager nManager = null;
    protected AddPhotoDialog popupWindow = null;
    protected DataManagerNew dataManager = null;
    protected ImageShow imageShow = null;
    protected AppManager appManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.jianfanjia.common.tool.LogTool.d("onCreate()");
        setContentView(getLayoutId());
        init(savedInstanceState);
        ButterKnife.bind(this);
    }

    protected void init(Bundle savedInstanceState) {
        appManager = AppManager.getAppManager();
        appManager.addActivity(this);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        dataManager = DataManagerNew.getInstance();
        fragmentManager = this.getSupportFragmentManager();
        imageShow = ImageShow.getImageShow();
    }

    public abstract int getLayoutId();


    protected Context getUiContext() {
        if (this != null) {
            return this;
        }
        return getApplicationContext();
    }

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
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    // 通过Class跳转界面
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    // 含有Bundle通过Class跳转界面
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    // 通过Action跳转界面
    protected void startActivity(String action) {
        startActivity(action, null);
    }

    // 含有Bundle通过Action跳转界面
    protected void startActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


}
