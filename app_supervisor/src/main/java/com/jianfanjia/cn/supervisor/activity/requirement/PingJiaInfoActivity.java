package com.jianfanjia.cn.supervisor.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.Evaluation;
import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.supervisor.config.Constant;
import com.jianfanjia.cn.supervisor.config.Global;
import com.jianfanjia.cn.supervisor.view.MainHeadView;
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
    private Evaluation evaluation = null;
    private Designer designer = null;
    private float totalAttidude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle viewBundle = intent.getExtras();
        designer = (Designer) viewBundle.getSerializable(Global.DESIGNER_INFO);
        imageid = designer.getImageid();
        designer_name = designer.getUsername();
        totalAttidude = (designer.getRespond_speed() + designer.getService_attitude()) / 2;
        evaluation = (Evaluation) viewBundle.getSerializable(Global.EVALUATION);
        bar.setRating(totalAttidude);
        designerName.setText(designer_name);
        LogTool.d(TAG, "imageid:" + imageid + " designer_name:" + designer_name + " evaluation:" + evaluation + " " +
                "totalAttitude =" + totalAttidude);
    }

    public void initView() {
        initMainHeadView();

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
        mainHeadView.setMianTitle(getResources().getString(R.string.pingjiaDetailText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @OnClick({R.id.head_back_layout})
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
