package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;

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
public class PreviewDecorationActivity extends BaseActivity {
    private static final String TAG = PreviewDecorationActivity.class.getName();
    private BeautyImgInfo beautyImgInfo = null;

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        Bundle decorationBundle = intent.getExtras();
        beautyImgInfo = (BeautyImgInfo) decorationBundle.getSerializable(Global.DECORATION);
        LogTool.d(TAG, "beautyImgInfo=" + beautyImgInfo);
    }

    @Override
    public void setListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_decoration;
    }
}
