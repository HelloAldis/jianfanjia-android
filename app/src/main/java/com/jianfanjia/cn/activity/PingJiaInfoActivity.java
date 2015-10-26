package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.Evaluation;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description:查看评价信息
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PingJiaInfoActivity extends BaseActivity implements
        OnClickListener {
    private static final String TAG = PingJiaInfoActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private ImageView designer_head_img = null;
    private TextView designerName = null;
    private RatingBar bar = null;
    private RatingBar speedBar = null;
    private RatingBar attudeBar = null;

    private Evaluation evaluation = null;

    @Override
    public void initView() {
        initMainHeadView();
        designer_head_img = (ImageView) findViewById(R.id.designer_head_img);
        designerName = (TextView) findViewById(R.id.designerName);
        bar = (RatingBar) findViewById(R.id.ratingBar);
        speedBar = (RatingBar) findViewById(R.id.speedBar);
        attudeBar = (RatingBar) findViewById(R.id.attudeBar);
        Intent intent = this.getIntent();
        Bundle viewBundle = intent.getExtras();
        evaluation = (Evaluation) viewBundle.getSerializable(Global.EVALUATION);
        LogTool.d(TAG, "evaluation:" + evaluation);
        if (null != evaluation) {
            imageLoader.displayImage(Url_New.GET_IMAGE + evaluation.get_id(), designer_head_img, options);
            designerName.setText(evaluation.getUserid());

        }
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_pingjia_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.pingjiaText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_ping_jia_info;
    }

}
