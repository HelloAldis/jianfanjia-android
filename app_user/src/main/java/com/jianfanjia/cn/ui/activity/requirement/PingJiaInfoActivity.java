package com.jianfanjia.cn.ui.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.model.Evaluation;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description:查看评价信息
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PingJiaInfoActivity extends BaseSwipeBackActivity {
    private static final String TAG = PingJiaInfoActivity.class.getName();

    @Bind(R.id.my_pingjia_head_layout)
    protected MainHeadView mainHeadView;
    @Bind(R.id.designer_head_img)
    protected ImageView designer_head_img;
    @Bind(R.id.designerName)
    protected TextView designerName;
    @Bind(R.id.ratingBar)
    protected RatingBar bar;
    @Bind(R.id.speedBar)
    protected RatingBar speedBar;
    @Bind(R.id.attudeBar)
    protected RatingBar attudeBar;
    @Bind(R.id.commentText)
    protected TextView commentText;

    private String imageid = null;
    private String designer_name = null;
    private float respond_speed;
    private float service_attitude;
    private Evaluation evaluation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle viewBundle = intent.getExtras();
        imageid = viewBundle.getString(IntentConstant.IMAGE_ID);
        designer_name = viewBundle.getString(IntentConstant.DESIGNER_NAME);
        respond_speed = viewBundle.getFloat(IntentConstant.RESPOND_SPEED);
        service_attitude = viewBundle.getFloat(IntentConstant.SERVICE_ATTITUDE);
        evaluation = (Evaluation) viewBundle.getSerializable(IntentConstant.EVALUATION);
        LogTool.d("imageid:" + imageid + " designer_name:" + designer_name + " respond_speed:" + respond_speed +
                " service_attitude:" + service_attitude + " evaluation:"
                + evaluation);
    }

    public void initView() {
        initMainHeadView();

        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(this, imageid, designer_head_img);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, designer_head_img);
        }
        designerName.setText(designer_name);
        bar.setRating((respond_speed + service_attitude) / 2);
        if (null != evaluation) {
            float speed = evaluation.getRespond_speed();
            float attitude = evaluation.getService_attitude();
            speedBar.setRating((int) speed);
            attudeBar.setRating((int) attitude);
            if (!TextUtils.isEmpty(evaluation.getComment())) {
                commentText.setText(evaluation.getComment());
            } else {
                commentText.setText("");
            }
        }
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.pingjiaDetailText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @OnClick(R.id.head_back_layout)
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
