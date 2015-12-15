package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.view.View;

import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.config.Global;

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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    protected void intentToCollectReq(String stage){
        OwnerInfo ownerInfo = new OwnerInfo();
        ownerInfo.setDec_progress(stage);
        Intent intent = new Intent(this,NewUserCollectLoveStyleActivity_.class);
        intent.putExtra(Global.OWNERINFO,ownerInfo);
        startActivity(intent);
    }
}
