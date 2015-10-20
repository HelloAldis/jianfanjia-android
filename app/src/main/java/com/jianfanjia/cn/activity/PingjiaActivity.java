package com.jianfanjia.cn.activity;

import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.tools.LogTool;

/**
 * Description:评价设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PingjiaActivity extends BaseActivity implements
        OnRatingBarChangeListener {
    private static final String TAG = PingjiaActivity.class.getName();
    private RatingBar bar;
    private RatingBar speedBar;
    private RatingBar attudeBar;


    @Override
    public void initView() {
        bar = (RatingBar) findViewById(R.id.ratingBar);
        speedBar = (RatingBar) findViewById(R.id.speedBar);
        attudeBar = (RatingBar) findViewById(R.id.attudeBar);
    }

    @Override
    public void setListener() {
        //设置监听器
        speedBar.setOnRatingBarChangeListener(this);
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_pingjia;
    }
}