package com.jianfanjia.cn.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description:评价设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PingjiaActivity extends BaseActivity implements
        OnRatingBarChangeListener, OnClickListener, ApiUiUpdateListener {
    private static final String TAG = PingjiaActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private RatingBar bar = null;
    private RatingBar speedBar = null;
    private RatingBar attudeBar = null;
    private Button btn_commit = null;


    @Override
    public void initView() {
        initMainHeadView();
        bar = (RatingBar) findViewById(R.id.ratingBar);
        speedBar = (RatingBar) findViewById(R.id.speedBar);
        attudeBar = (RatingBar) findViewById(R.id.attudeBar);
        btn_commit = (Button) findViewById(R.id.btn_commit);
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
        speedBar.setOnRatingBarChangeListener(this);
        btn_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            case R.id.btn_commit:
                break;
            default:
                break;
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating,
                                boolean fromUser) {
        int num = (int) rating;
        String result = null;  //保存文字信息
        switch (num) {
            case 5:
                result = "非常满意";
                break;
            case 4:
                result = "满意";
                break;
            case 3:
                result = "还可以";
                break;
            case 2:
                result = "不满意";
                break;
            case 1:
                result = "非常不满意";
                break;
            default:
                break;
        }
        LogTool.d(TAG, " result:" + result);
        makeTextLong(" result:" + result);
    }

    //评价设计师
    private void evaluateDesignerByUser(String requirementid, String designerid, int service_attitude, int respond_speed, String comment, String is_anonymous) {
        JianFanJiaClient.evaluateDesignerByUser(PingjiaActivity.this, requirementid, designerid, service_attitude, respond_speed, comment, is_anonymous, this, this);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        LogTool.d(TAG, "data:" + data);
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