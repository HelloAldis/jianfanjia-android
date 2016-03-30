package com.jianfanjia.cn.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.model.User;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.cn.tools.TDevice;
import com.jianfanjia.cn.tools.UiHelper;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-14 09:35
 */
public class NewUserCollectDecStageActivity extends BaseActivity {

    @Bind(R.id.dec_stage0)
    TextView dec_stage_0;

    @Bind(R.id.dec_stage1)
    TextView dec_stage_1;

    @Bind(R.id.dec_stage2)
    TextView dec_stage_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();
    }

    @OnClick({R.id.dec_stage0, R.id.dec_stage1, R.id.dec_stage2})
    protected void click(final View view) {
        UiHelper.imageButtonAnim(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                switch (view.getId()) {
                    case R.id.dec_stage0:
                        intentToCollectReq(Global.DEC_PROGRESS0);
                        break;
                    case R.id.dec_stage1:
                        intentToCollectReq(Global.DEC_PROGRESS1);
                        break;
                    case R.id.dec_stage2:
                        intentToCollectReq(Global.DEC_PROGRESS2);
                        break;
                }
            }
        });
    }

    public void initView() {
//        dec_stage_1.setTranslationY(TDevice.getScreenHeight() / 2);
        LogTool.d(this.getClass().getName(), "TDevice.getScreenHeight() =" + TDevice.getScreenHeight());
        dec_stage_1.setTranslationY(TDevice.getScreenHeight());
        dec_stage_1.animate().translationY(0).setInterpolator(new OvershootInterpolator(1.0f)).setStartDelay(200)
                .setDuration(700).start();
        dec_stage_2.setScaleX(0.1f);
        dec_stage_2.setScaleY(0.1f);
        dec_stage_2.setAlpha(0.f);
        dec_stage_2.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(300).setStartDelay(700).start();
        dec_stage_0.setScaleX(0.1f);
        dec_stage_0.setScaleY(0.1f);
        dec_stage_0.setAlpha(0.f);
        dec_stage_0.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(300).setStartDelay(700).start();
    }

    protected void intentToCollectReq(String stage) {
        User ownerInfo = new User();
        ownerInfo.setDec_progress(stage);
        Bundle ownerBundle = new Bundle();
        ownerBundle.putSerializable(Global.OWNERINFO, ownerInfo);
        startActivity(NewUserCollectLoveStyleActivity.class, ownerBundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_collect_decstage;
    }
}
