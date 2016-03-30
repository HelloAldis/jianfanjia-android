package com.jianfanjia.cn.designer.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description:评价设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PingjiaActivity extends BaseActivity implements
        ApiUiUpdateListener {
    private static final String TAG = PingjiaActivity.class.getName();

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
    @Bind(R.id.contentEdit)
    protected EditText contentEdit;
    @Bind(R.id.btn_commit)
    protected Button btn_commit;

    private String imageid = null;
    private String designer_name = null;
    private String requirementid = null;
    private String designerid = null;
    private float speed = 0.0f;
    private float attitude = 0.0f;

    private int respond_speed = 0;
    private int service_attitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle commentBundle = intent.getExtras();
        imageid = commentBundle.getString(Global.IMAGE_ID);
        designer_name = commentBundle.getString(Global.DESIGNER_NAME);
        requirementid = commentBundle.getString(Global.REQUIREMENT_ID);
        designerid = commentBundle.getString(Global.DESIGNER_ID);
        speed = commentBundle.getFloat(Global.SPEED);
        attitude = commentBundle.getFloat(Global.ATTITUDE);
        LogTool.d(TAG, "imageid:" + imageid + " designer_name:" + designer_name + " requirementid:" + requirementid +
                " designerid:" + designerid + " speed:" + speed + " attitude:" + attitude);
    }

    public void initView() {
        initMainHeadView();

        bar.setRating((int) (speed + attitude) / 2);
        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(this, imageid, designer_head_img);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, designer_head_img);
        }
        designerName.setText(designer_name);

        speedBar.setOnRatingBarChangeListener(speedListener);
        attudeBar.setOnRatingBarChangeListener(attitudeListener);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.pingjiaText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @OnClick({R.id.head_back_layout, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.btn_commit:
                String content = contentEdit.getText().toString().trim();
                evaluateDesignerByUser(requirementid, designerid, service_attitude, respond_speed, content, "0");
                break;
            default:
                break;
        }
    }

    private OnRatingBarChangeListener speedListener = new OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            respond_speed = (int) rating;
            LogTool.d(TAG, "respond_speed:" + respond_speed);
        }
    };

    private OnRatingBarChangeListener attitudeListener = new OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            service_attitude = (int) rating;
            LogTool.d(TAG, "service_attitude:" + service_attitude);
        }
    };

    //评价设计师
    private void evaluateDesignerByUser(String requirementid, String designerid, int service_attitude, int
            respond_speed, String comment, String is_anonymous) {
        JianFanJiaClient.evaluateDesignerByUser(PingjiaActivity.this, requirementid, designerid, service_attitude,
                respond_speed, comment, is_anonymous, this, this);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        LogTool.d(TAG, "data:" + data);
        setResult(RESULT_OK);
        appManager.finishActivity(this);
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextLong(error_msg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pingjia;
    }
}