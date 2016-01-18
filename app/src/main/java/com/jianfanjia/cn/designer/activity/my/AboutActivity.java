package com.jianfanjia.cn.designer.activity.my;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description:关于
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class AboutActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = AboutActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private TextView currentVersion;// 当前版本
    private String versionInfo;

    @Override
    public void initView() {
        initMainHeadView();
        currentVersion = (TextView) findViewById(R.id.about_version);
        versionInfo = String.format(getString(R.string.about_version),MyApplication.getInstance().getVersionName());
        currentVersion.setText(versionInfo);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.
                about_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.about));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }
}
