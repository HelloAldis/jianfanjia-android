package com.jianfanjia.cn.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.jianfanjia.cn.AppConfig;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.cache.DataManagerNew;
import com.jianfanjia.cn.dao.impl.NotifyMessageDao;
import com.jianfanjia.cn.interf.manager.ListenerManeger;
import com.jianfanjia.cn.tools.DaoManager;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.AddPhotoPopWindow;
import com.jianfanjia.cn.view.dialog.DialogControl;
import com.jianfanjia.cn.view.dialog.WaitDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Description:Fragment基类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 15:42
 */
public class BaseAnnotationFragment extends Fragment {
    protected FragmentManager fragmentManager = null;
    protected NotifyMessageDao notifyMessageDao = null;
    protected DataManagerNew dataManager = null;
    protected AppConfig appConfig = null;
    protected LayoutInflater inflater = null;
    // protected SharedPrefer sharedPrefer = null;
    protected ImageLoader imageLoader = null;
    protected DisplayImageOptions options = null;
    protected ListenerManeger listenerManeger = null;
    protected AddPhotoPopWindow popupWindow = null;
    protected ProcessInfo processInfo = null;
    protected String mUserName = null;// 用户名
    protected String mAccount = null;// 账号
    protected String mUserImageId = null;// 头像
    protected String mUserType = null;// 用户类型
    protected String mImageId = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.d(this.getClass().getName(), "onCreate");
        init();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUserInfo();
    }

    private void init() {
        appConfig = AppConfig.getInstance(getActivity());
        dataManager = DataManagerNew.getInstance();
        notifyMessageDao = DaoManager.getNotifyMessageDao(getActivity());
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pix_default)
                .showImageForEmptyUri(R.mipmap.pix_default)
                .showImageOnFail(R.mipmap.pix_default).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
        // sharedPrefer = dataManager.sharedPreferdata;
        fragmentManager = getFragmentManager();
    }

    private void initUserInfo() {
        mUserName = dataManager.getUserName();
        mAccount = dataManager.getAccount();
        mUserImageId = dataManager.getUserImagePath();
        mUserType = dataManager.getUserType();
        LogTool.d(this.getClass().getName(), "mUserName:" + mUserName
                + " mAccount:" + mAccount + " userImageId:" + mUserImageId);
        processInfo = dataManager.getDefaultProcessInfo();
        LogTool.d(this.getClass().getName(), "processInfo=" + processInfo);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogTool.d(this.getClass().getName(), "onResume");
        initUserInfo();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogTool.d(this.getClass().getName(), "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogTool.d(this.getClass().getName(), "onDestroy");
    }

    protected void makeTextShort(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    protected void makeTextLong(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    // 通过Class跳转界面
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    // 含有Bundle通过Class跳转界面
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void hideWaitDialog() {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            ((DialogControl) activity).hideWaitDialog();
        }
    }

    protected WaitDialog showWaitDialog(int resid) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(resid);
        }
        return null;
    }

    protected WaitDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    protected WaitDialog showWaitDialog(String str) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(str);
        }
        return null;
    }
}