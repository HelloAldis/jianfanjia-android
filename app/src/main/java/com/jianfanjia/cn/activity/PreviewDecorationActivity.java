package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.BeautyImgInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.tools.LogTool;

/**
 * Description:预览装修美图
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PreviewDecorationActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = PreviewDecorationActivity.class.getName();
    private Toolbar toolbar = null;
    private ImageButton toolbar_add = null;
    private ViewPager viewPager = null;
    private BeautyImgInfo beautyImgInfo = null;

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_add = (ImageButton) findViewById(R.id.toolbar_add);
        toolbar.setNavigationIcon(R.mipmap.icon_register_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.showpicPager);
        Intent intent = this.getIntent();
        Bundle decorationBundle = intent.getExtras();
        beautyImgInfo = (BeautyImgInfo) decorationBundle.getSerializable(Global.DECORATION);
        LogTool.d(TAG, "beautyImgInfo=" + beautyImgInfo);

    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_add:

                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_decoration;
    }
}
