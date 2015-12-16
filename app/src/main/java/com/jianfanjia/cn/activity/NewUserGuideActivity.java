package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.config.Global;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-14 11:08
 */
@EActivity(R.layout.activity_register_guide)
public class NewUserGuideActivity extends BaseAnnotationActivity {

    @ViewById(R.id.success_layout)
    LinearLayout successLayout;

    @Click({R.id.btn_scan, R.id.btn_publish_requirement})
    void Click(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                startActivity(MainActivity.class);
                finish();
                break;
            case R.id.btn_publish_requirement:
                Bundle bundle = new Bundle();
                bundle.putBoolean(Global.IS_PUBLISHREQUIREMENT,true);
                startActivity(MainActivity.class,bundle);
                finish();
                break;
            default:
                break;
        }

    }

}
