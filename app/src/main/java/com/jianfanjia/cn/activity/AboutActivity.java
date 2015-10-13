package com.jianfanjia.cn.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jianfanjia.cn.base.BaseActivity;

/**
 * Description:关于
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class AboutActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = AboutActivity.class.getName();
    private TextView backView;// 返回视图
    private TextView currentVersion;// 当前版本


    @Override
    public void initView() {
        backView = (TextView) findViewById(R.id.about_back);
        currentVersion = (TextView) findViewById(R.id.about_version);
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
