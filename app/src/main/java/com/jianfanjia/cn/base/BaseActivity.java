package com.jianfanjia.cn.base;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jianfanjia.cn.AppConfig;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.cache.DataManagerNew;
import com.jianfanjia.cn.dao.impl.NotifyMessageDao;
import com.jianfanjia.cn.http.OkHttpClientManager;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.NetStateListener;
import com.jianfanjia.cn.interf.PopWindowCallBack;
import com.jianfanjia.cn.interf.manager.ListenerManeger;
import com.jianfanjia.cn.receiver.NetStateReceiver;
import com.jianfanjia.cn.tools.ActivityManager;
import com.jianfanjia.cn.tools.DaoManager;
import com.jianfanjia.cn.tools.ImageShow;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.ScreenUtil;
import com.jianfanjia.cn.view.AddPhotoPopWindow;
import com.jianfanjia.cn.view.dialog.DialogControl;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.cn.view.dialog.WaitDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * Description:activity基类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public abstract class BaseActivity extends AppCompatActivity implements
        DialogControl, NetStateListener, PopWindowCallBack, ApiUiUpdateListener {
    protected ActivityManager activityManager = null;
    protected DownloadManager downloadManager = null;
    protected NotifyMessageDao notifyMessageDao = null;
    protected LayoutInflater inflater = null;
    protected FragmentManager fragmentManager = null;
    protected NotificationManager nManager = null;
    protected ListenerManeger listenerManeger = null;
    protected NetStateReceiver netStateReceiver = null;
    protected AddPhotoPopWindow popupWindow = null;
    private boolean _isVisible;
    private WaitDialog _waitDialog;
    protected DataManagerNew dataManager;
    protected AppConfig appConfig;
    protected ImageShow imageShow;

    protected String userIdentity = null;
    protected boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.d(this.getClass().getName(), "onCreate()");
        if (Build.VERSION.SDK_INT > 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明导航栏
        }
        setContentView(getLayoutId());
        init();
        initDao();
        initParams();
        initView();
        setListener();
    }

    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
                /*window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int statusBarHeight = ScreenUtil.getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }


    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void setListener();

    private void init() {
        activityManager = ActivityManager.getInstance();
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        notifyMessageDao = DaoManager.getNotifyMessageDao(this);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        dataManager = DataManagerNew.getInstance();
        appConfig = AppConfig.getInstance(this);
        fragmentManager = this.getSupportFragmentManager();
        listenerManeger = ListenerManeger.getListenerManeger();
        netStateReceiver = new NetStateReceiver(this);
        _isVisible = true;
        activityManager.addActivity(this);
        imageShow = ImageShow.getImageShow();
    }

    private void initParams() {

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

    protected void showPopWindow(View view) {
        if (popupWindow == null) {
            popupWindow = new AddPhotoPopWindow(this, this);
        }
        popupWindow.show(view);
    }

    @Override
    public void firstItemClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void secondItemClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void preLoad() {
        showWaitDialog();
    }

    @Override
    public void loadSuccess(Object data) {
        hideWaitDialog();
    }

    @Override
    public void loadFailture(String error_msg) {
        hideWaitDialog();
        makeTextLong(error_msg);
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

    protected String getHouseType(String houseType) {
        String str = null;
        if (houseType.equals("0")) {
            str = "一居";
        } else if (houseType.equals("1")) {
            str = "二居";
        } else if (houseType.equals("2")) {
            str = "三居";
        } else if (houseType.equals("3")) {
            str = "四居";
        } else if (houseType.equals("4")) {
            str = "复式";
        } else if (houseType.equals("5")) {
            str = "别墅";
        } else if (houseType.equals("6")) {
            str = "LOFT";
        } else if (houseType.equals("7")) {
            str = "其他";
        }
        return str;
    }

    protected String getDecStyle(String decStyle) {
        String str = null;
        if (decStyle.equals("0")) {
            str = "欧式";
        } else if (decStyle.equals("1")) {
            str = "中式";
        } else if (decStyle.equals("2")) {
            str = "现代";
        } else if (decStyle.equals("3")) {
            str = "地中海";
        } else if (decStyle.equals("4")) {
            str = "美式";
        } else if (decStyle.equals("5")) {
            str = "东南亚";
        } else if (decStyle.equals("6")) {
            str = "田园";
        }
        return str;
    }

    protected String getWorkType(String workType) {
        String str = null;
        if (workType.equals("0")) {
            str = "半包";
        } else if (workType.equals("1")) {
            str = "全包";
        } else if (workType.equals("2")) {
            str = "纯设计";
        }
        return str;
    }

}
