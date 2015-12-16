package com.jianfanjia.cn.designer.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.R;

/**
 * @author zhanghao
 * @class AboutActivity
 * @Description 关于我们
 * @date 2015-8-27 下午8:22
 */
public class AboutActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = AboutActivity.class.getName();
    private TextView backView;// 返回视图
    private TextView currentVersion;// 当前版本
    private String versionInfo;

    @Override
    public void initView() {
        backView = (TextView) findViewById(R.id.about_back);
        currentVersion = (TextView) findViewById(R.id.about_version);
        versionInfo = String.format(getString(R.string.about_version), MyApplication.getInstance().getVersionName());
        currentVersion.setText(versionInfo);
    }

    @Override
    public void setListener() {
        backView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_back:
                finish();
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
