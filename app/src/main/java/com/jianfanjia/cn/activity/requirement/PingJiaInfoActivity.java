package com.jianfanjia.cn.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.Evaluation;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
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
    private TextView commentText = null;

    private String imageid = null;
    private String designer_name = null;
    private Evaluation evaluation = null;

    @Override
    public void initView() {
        initMainHeadView();
        designer_head_img = (ImageView) findViewById(R.id.designer_head_img);
        designerName = (TextView) findViewById(R.id.designerName);
        bar = (RatingBar) findViewById(R.id.ratingBar);
        speedBar = (RatingBar) findViewById(R.id.speedBar);
        attudeBar = (RatingBar) findViewById(R.id.attudeBar);
        commentText = (TextView) findViewById(R.id.commentText);
        Intent intent = this.getIntent();
        Bundle viewBundle = intent.getExtras();
        imageid = viewBundle.getString(Global.IMAGE_ID);
        designer_name = viewBundle.getString(Global.DESIGNER_NAME);
        evaluation = (Evaluation) viewBundle.getSerializable(Global.EVALUATION);
        LogTool.d(TAG, "imageid:" + imageid + " designer_name:" + designer_name + " evaluation:" + evaluation);
        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(this, imageid, designer_head_img);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, designer_head_img);
        }
        designerName.setText(designer_name);
        if (null != evaluation) {
            float speed = evaluation.getRespond_speed();
            float attitude = evaluation.getService_attitude();
            bar.setRating((int) (speed + attitude) / 2);
            speedBar.setRating((int) speed);
            attudeBar.setRating((int) attitude);
            if (!TextUtils.isEmpty(evaluation.getComment())) {
                commentText.setText(evaluation.getComment());
            } else {
                commentText.setText("暂无内容");
            }
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
                appManager.finishActivity(this);
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
