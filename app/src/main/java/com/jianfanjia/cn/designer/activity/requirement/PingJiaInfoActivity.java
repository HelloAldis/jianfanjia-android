package com.jianfanjia.cn.designer.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.bean.Designer;
import com.jianfanjia.cn.designer.bean.Evaluation;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.MainHeadView;

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
    private Designer designer = null;
    private float totalAttidude;

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
        designer = (Designer) viewBundle.getSerializable(Global.DESIGNER_INFO);
        imageid = designer.getImageid();
        designer_name = designer.getUsername();
        totalAttidude = (designer.getRespond_speed() + designer.getService_attitude()) / 2;
        evaluation = (Evaluation) viewBundle.getSerializable(Global.EVALUATION);
        bar.setRating(totalAttidude);
        designerName.setText(designer_name);
        LogTool.d(TAG, "imageid:" + imageid + " designer_name:" + designer_name + " evaluation:" + evaluation + " totalAttitude =" + totalAttidude);
        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(this, imageid, designer_head_img);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, designer_head_img);
        }
        if (null != evaluation) {
            float speed = evaluation.getRespond_speed();
            float attitude = evaluation.getService_attitude();
            speedBar.setRating((int) speed);
            attudeBar.setRating((int) attitude);
            if (!TextUtils.isEmpty(evaluation.getComment())) {
                commentText.setText(evaluation.getComment());
            }
        } else {
            commentText.setText(getString(R.string.not_comment));
        }
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_pingjia_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setMianTitle(getResources().getString(R.string.pingjiaDetailText));
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
