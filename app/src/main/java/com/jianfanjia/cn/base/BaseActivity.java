package com.jianfanjia.cn.base;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.jianfanjia.cn.AppManager;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.cache.DataManagerNew;
import com.jianfanjia.cn.dao.impl.NotifyMessageDao;
import com.jianfanjia.cn.http.OkHttpClientManager;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.NetStateListener;
import com.jianfanjia.cn.receiver.NetStateReceiver;
import com.jianfanjia.cn.tools.DaoManager;
import com.jianfanjia.cn.tools.ImageShow;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.dialog.DialogControl;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.cn.view.dialog.WaitDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Description:activity基类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public abstract class BaseActivity extends AppCompatActivity implements
        DialogControl, ApiUiUpdateListener {
//    protected DownloadManager downloadManager = null;
    protected NotifyMessageDao notifyMessageDao = null;
    protected LayoutInflater inflater = null;
    protected FragmentManager fragmentManager = null;
    protected NotificationManager nManager = null;
//    protected NetStateReceiver netStateReceiver = null;
    private boolean _isVisible;
    private WaitDialog _waitDialog;
    protected DataManagerNew dataManager;
    protected ImageShow imageShow;
    protected AppManager appManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.d(this.getClass().getName(), "onCreate()");
        setContentView(getLayoutId());
        init(savedInstanceState);
        ButterKnife.bind(this);
//        initView();
//        setListener();
    }

    protected void init(Bundle savedInstanceState) {
        appManager = AppManager.getAppManager();
        appManager.addActivity(this);
//        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        netStateReceiver = new NetStateReceiver(this);
        notifyMessageDao = DaoManager.getNotifyMessageDao(MyApplication.getInstance());
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        dataManager = DataManagerNew.getInstance();
        fragmentManager = this.getSupportFragmentManager();
        imageShow = ImageShow.getImageShow();
        _isVisible = true;
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public void setListener() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogTool.d(this.getClass().getName(), "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogTool.d(this.getClass().getName(), "onResume()");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogTool.d(this.getClass().getName(), "onPause()");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogTool.d(this.getClass().getName(), "onStop()");
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        appManager.finishActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpClientManager.cancelTag(this);
        LogTool.d(this.getClass().getName(), "onDestroy()");
    }

    protected void makeTextShort(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected void makeTextLong(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
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

    @Override
    public void preLoad() {
        if (_waitDialog == null) {
            showWaitDialog();
        }
    }

    @Override
    public void loadSuccess(Object data) {
        hideWaitDialog();
    }

    @Override
    public void loadFailture(String error_msg) {
        hideWaitDialog();
        makeTextShort(error_msg);
    }

    @Override
    public WaitDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    @Override
    public WaitDialog showWaitDialog(int resid) {
        return showWaitDialog(getString(resid));
    }

    @Override
    public WaitDialog showWaitDialog(String message) {
        if (_isVisible) {
            if (_waitDialog == null) {
                _waitDialog = DialogHelper.getWaitDialog(this, message);
            }
            if (_waitDialog != null) {
                _waitDialog.setMessage(message);
                _waitDialog.show();
            }
            return _waitDialog;
        }
        return null;
    }

    @Override
    public void hideWaitDialog() {
        if (_isVisible && _waitDialog != null) {
            try {
                _waitDialog.dismiss();
                _waitDialog = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // 注册网络监听广播
//    protected void registerNetReceiver() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(netStateReceiver, intentFilter);
//    }

    // 取消网络监听广播
//    protected void unregisterNetReceiver() {
//        unregisterReceiver(netStateReceiver);
//    }
}
