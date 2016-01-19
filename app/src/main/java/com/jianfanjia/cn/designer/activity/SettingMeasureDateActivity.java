package com.jianfanjia.cn.designer.activity;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseAnnotationActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;

/**
 * Description: com.jianfanjia.cn.designer.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-19 17:48
 */
@EActivity(R.layout.dialog_choose_date)
public class SettingMeasureDateActivity extends BaseAnnotationActivity{

    @ViewById(R.id.datePicker)
    protected DatePicker datePicker;

    @ViewById(R.id.timePicker)
    protected TimePicker timePicker;

    @AfterViews
    protected void initAnnotationView(){
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        long time = cal.getTimeInMillis();
        datePicker.setMinDate(time);
        timePicker.setIs24HourView(true);
    }

    @Click({R.id.head_back_layout,R.id.head_choose_layout,R.id.btn_phone_layout})
    protected void click(View view){
        switch (view.getId()){
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.head_choose_layout:
                break;
            case R.id.phone_layout:
                break;
        }
    }

}
