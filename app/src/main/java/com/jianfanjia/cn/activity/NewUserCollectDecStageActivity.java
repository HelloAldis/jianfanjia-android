package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.view.View;

import com.jianfanjia.cn.base.BaseAnnotationActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-14 09:35
 */
@EActivity(R.layout.activity_register_collect_decstage)
public class NewUserCollectDecStageActivity extends BaseAnnotationActivity {

    @Click({R.id.dec_stage0,R.id.dec_stage1,R.id.dec_stage2})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.dec_stage0:
                intentToCollectReq();
                break;
            case R.id.dec_stage1:
                intentToCollectReq();
                break;
            case R.id.dec_stage2:
                intentToCollectReq();
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    protected void intentToCollectReq(){
        Intent intent = new Intent(this,NewUserCollectLoveStyleActivity_.class);
        startActivity(intent);
    }
}
