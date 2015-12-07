package com.jianfanjia.cn.activity;

import android.view.View;
import android.view.View.OnClickListener;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description:我的收藏
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class CollectActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = CollectActivity.class.getName();
    private MainHeadView mainHeadView = null;

    @Override
    public void initView() {
        initMainHeadView();
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_collect_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.my_favorite));
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
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_collect;
    }
}
