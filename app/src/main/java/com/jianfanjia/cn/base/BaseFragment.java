package com.jianfanjia.cn.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.dao.impl.NotifyMessageDao;
import com.jianfanjia.cn.tools.DaoManager;
import com.jianfanjia.cn.tools.LogTool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Description:Fragment基类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 15:42
 */
public abstract class BaseFragment extends Fragment
        implements OnClickListener {
    protected FragmentManager fragmentManager = null;
    protected LayoutInflater inflater = null;
    protected ImageLoader imageLoader = null;
    protected DisplayImageOptions options = null;
    protected NotifyMessageDao notifyMessageDao = null;
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
        view = inflateView(getLayoutId());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(view);
        setListener();
    }

    private void init() {
        notifyMessageDao = DaoManager.getNotifyMessageDao(getActivity());
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pix_default)
                .showImageForEmptyUri(R.mipmap.pix_default)
                .showImageOnFail(R.mipmap.pix_default).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        fragmentManager = getFragmentManager();
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
}
