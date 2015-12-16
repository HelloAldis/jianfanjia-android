package com.jianfanjia.cn.designer.base;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.Toast;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.cache.DataManagerNew;
import com.jianfanjia.cn.designer.dao.impl.NotifyMessageDao;
import com.jianfanjia.cn.designer.inter.manager.ListenerManeger;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.interf.NetStateListener;
import com.jianfanjia.cn.designer.receiver.NetStateReceiver;
import com.jianfanjia.cn.designer.tools.ActivityManager;
import com.jianfanjia.cn.designer.tools.DaoManager;
import com.jianfanjia.cn.designer.tools.ImageShow;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.AddPhotoPopWindow;
import com.jianfanjia.cn.designer.view.dialog.DialogControl;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;
import com.jianfanjia.cn.designer.view.dialog.WaitDialog;

/**
 * @author fengliang
 * @ClassName: BaseActivity
 * @Description: activity基类
 * @date 2015年7月24日 上午11:46:40
 */
public abstract class BaseActivity extends FragmentActivity implements
        DialogControl, NetStateListener, ApiUiUpdateListener {
    protected ActivityManager activityManager = null;
    protected DownloadManager downloadManager = null;
    protected NotifyMessageDao notifyMessageDao = null;
    protected LayoutInflater inflater = null;
    protected FragmentManager fragmentManager = null;
    protected NotificationManager nManager = null;
    protected ImageShow imageShow;
    protected ListenerManeger listenerManeger = null;
    protected NetStateReceiver netStateReceiver = null;
    protected AddPhotoPopWindow popupWindow = null;
    protected DataManagerNew dataManager;
    protected String userIdentity = null;
    protected boolean isOpen = false;
    private boolean _isVisible;
    private WaitDialog _waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.d(this.getClass().getName(), "onCreate()");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getLayoutId() != -1) {
            setContentView(getLayoutId());
        }
        init();
        initDao();
        initParams();
        initView();
        setListener();
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void setListener();

    private void init() {
        activityManager = ActivityManager.getInstance();
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        notifyMessageDao = DaoManager.getNotifyMessageDao(this);
        listenerManeger = ListenerManeger.getListenerManeger();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        dataManager = DataManagerNew.getInstance();
        fragmentManager = this.getSupportFragmentManager();
        imageShow = ImageShow.getImageShow();
        netStateReceiver = new NetStateReceiver(this);
        _isVisible = true;
        activityManager.addActivity(this);
    }

    private void initParams() {
        userIdentity = dataManager.getUserType();
        LogTool.d(this.getClass().getName(), "userIdentity=" + userIdentity);
    }

    private void initDao() {

    }

    @Override
    public void onConnect() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisConnect() {
        // TODO Auto-generated method stub

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
        isOpen = dataManager.isPushOpen();
        registerNetReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogTool.d(this.getClass().getName(), "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogTool.d(this.getClass().getName(), "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogTool.d(this.getClass().getName(), "onDestroy()");
        unregisterNetReceiver();
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

    @Override
    public void loadSuccess(Object data) {
        hideWaitDialog();
    }

    @Override
    public void preLoad() {
        showWaitDialog();
    }

    @Override
    public void loadFailture(String errorMsg) {
        hideWaitDialog();
        setErrorView();
        makeTextLong(errorMsg);
    }

    // 设置错误视图
    protected void setErrorView() {
        // TODO Auto-generated method stub
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
    protected void registerNetReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netStateReceiver, intentFilter);
    }

    // 取消网络监听广播
    protected void unregisterNetReceiver() {
        unregisterReceiver(netStateReceiver);
    }

}
