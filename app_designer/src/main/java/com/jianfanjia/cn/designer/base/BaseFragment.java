package com.jianfanjia.cn.designer.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.cache.DataManagerNew;
import com.jianfanjia.cn.designer.dao.impl.NotifyMessageDao;
import com.jianfanjia.cn.designer.tools.DaoManager;
import com.jianfanjia.cn.designer.tools.ImageShow;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.cn.designer.view.dialog.DialogControl;
import com.jianfanjia.cn.designer.view.dialog.WaitDialog;

/**
 * Description:Fragment基类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 15:42
 */
public abstract class BaseFragment extends Fragment
        implements OnClickListener {
    protected FragmentManager fragmentManager = null;
    protected NotifyMessageDao notifyMessageDao = null;
    protected DataManagerNew dataManager = null;
    protected LayoutInflater inflater = null;
    protected ImageShow imageShow = null;
    protected String mUserName = null;// 用户名
    protected String mAccount = null;// 账号
    protected String mUserImageId = null;// 头像
    private View view = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.jianfanjia.common.tool.LogTool.d(this.getClass().getName(), "onCreate");
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.jianfanjia.common.tool.LogTool.d(this.getClass().getName(), "onCreateView");
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
        com.jianfanjia.common.tool.LogTool.d(this.getClass().getName(), "onActivityCreated");
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
        com.jianfanjia.common.tool.LogTool.d(this.getClass().getName(), "mUserName:" + mUserName
                + " mAccount:" + mAccount + " userImageId:" + mUserImageId);
    }

    public abstract int getLayoutId();

    protected View inflateView(int resId) {
        return this.inflater.inflate(resId, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        com.jianfanjia.common.tool.LogTool.d(this.getClass().getName(), "onResume");
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
        ButterKnife.unbind(this);
        LogTool.d(this.getClass().getName(), "onDestroy");
    }

    @Override
    public void onClick(View v) {

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

    protected void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        startActivityForResult(this, cls, bundle, requestCode);
    }

    protected void startActivityForResult(Activity activity, Class<?> clazz, int requestCode) {
        startActivityForResult(activity, clazz, null, requestCode);
    }

    protected void startActivityForResult(Activity activity, Class<?> clazz, Bundle bundle, int requestCode) {
        if (requestCode < 0) {
            throw new IllegalArgumentException("the requestCode must be biger than 0");
        }
        Intent intent = new Intent(activity.getApplicationContext(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResult(Fragment fragment, Class<?> clazz, int requestCode) {
        startActivityForResult(fragment, clazz, null, requestCode);
    }

    protected void startActivityForResult(Fragment fragment, Class<?> clazz, Bundle bundle, int requestCode) {
        if (requestCode < 0) {
            throw new IllegalArgumentException("the requestCode must be biger than 0");
        }
        Intent intent = new Intent(fragment.getContext(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        fragment.startActivityForResult(intent, requestCode);
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
