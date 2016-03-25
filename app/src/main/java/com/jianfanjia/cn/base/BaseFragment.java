package com.jianfanjia.cn.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.cache.DataManagerNew;
import com.jianfanjia.cn.dao.impl.NotifyMessageDao;
import com.jianfanjia.cn.http.OkHttpClientManager;
import com.jianfanjia.cn.tools.DaoManager;
import com.jianfanjia.cn.tools.ImageShow;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.dialog.DialogControl;
import com.jianfanjia.cn.view.dialog.WaitDialog;

/**
 * Description:Fragment基类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 15:42
 */
public abstract class BaseFragment extends Fragment {
    protected FragmentManager fragmentManager = null;
    protected NotifyMessageDao notifyMessageDao = null;
    protected DataManagerNew dataManager = null;
    protected LayoutInflater inflater = null;
    protected ImageShow imageShow = null;
    protected String mUserName = null;// 用户名
    protected String mAccount = null;// 账号
    protected String mUserImageId = null;// 头像
    protected String mUserType = null;// 用户类型
    protected String mImageId = null;
    private View view = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.d(this.getClass().getName(), "onCreate");
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogTool.d(this.getClass().getName(), "onCreateView");
        this.inflater = inflater;
        if (getLayoutId() > 0) {
            view = inflateView(getLayoutId());
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogTool.d(this.getClass().getName(), "onActivityCreated");
        initUserInfo();
//        initView(view);
//        setListener();
    }

    private void init() {
        dataManager = DataManagerNew.getInstance();
        notifyMessageDao = DaoManager.getNotifyMessageDao(MyApplication.getInstance());
        fragmentManager = getFragmentManager();
        imageShow = ImageShow.getImageShow();
    }

    private void initUserInfo() {
        mUserName = dataManager.getUserName();
        mAccount = dataManager.getAccount();
        mUserImageId = dataManager.getUserImagePath();
        mUserType = dataManager.getUserType();
        LogTool.d(this.getClass().getName(), "mUserName:" + mUserName
                + " mAccount:" + mAccount + " userImageId:" + mUserImageId);
    }

    public abstract int getLayoutId();

    public abstract void initView(View view);

    public abstract void setListener();

    protected View inflateView(int resId) {
        return this.inflater.inflate(resId, null);
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
        OkHttpClientManager.cancelTag(this);
        LogTool.d(this.getClass().getName(), "onDestroy");
    }

    protected void makeTextShort(String text) {
        if (getContext() == null) return;
        if(TextUtils.isEmpty(text)) return;
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected void makeTextLong(String text) {
        if (getContext() == null) return;
        if(TextUtils.isEmpty(text)) return;
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    // 通过Class跳转界面
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    // 含有Bundle通过Class跳转界面
    protected void startActivity(Class<?> cls, Bundle bundle) {
        IntentUtil.startActivity(getContext(), cls, bundle);
    }

    protected void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        IntentUtil.startActivityForResult(this, cls, bundle, requestCode);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    protected void startActivityForResultByHost(Class<?> cls, Bundle bundle, int requestCode) {
        IntentUtil.startActivityForResult(getActivity(), cls, bundle, requestCode);
    }

    protected void startActivityForResultByHost(Class<?> cls, int requestCode) {
        startActivityForResultByHost(cls, null, requestCode);
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
