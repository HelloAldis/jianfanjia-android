package com.jianfanjia.cn.designer.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description:关于
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class AboutActivity extends BaseSwipeBackActivity {
    private static final String TAG = AboutActivity.class.getName();

    @Bind(R.id.about_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.about_version)
    TextView currentVersion;// 当前版本

    private String versionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        initMainHeadView();
        versionInfo = String.format(getString(R.string.about_version), MyApplication.getInstance().getVersionName());
        currentVersion.setText(versionInfo);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.about));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @OnClick(R.id.head_back_layout)
    public void onClick() {
        appManager.finishActivity(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }
}
