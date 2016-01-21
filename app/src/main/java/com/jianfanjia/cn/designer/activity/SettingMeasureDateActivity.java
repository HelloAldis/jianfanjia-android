package com.jianfanjia.cn.designer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jianfanjia.cn.designer.Event.UpdateEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseAnnotationActivity;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.StringUtils;
import com.jianfanjia.cn.designer.tools.UiHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

/**
 * Description: com.jianfanjia.cn.designer.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-19 17:48
 */
@EActivity(R.layout.activity_choose_date)
public class SettingMeasureDateActivity extends BaseAnnotationActivity {

    @ViewById(R.id.datePicker)
    protected DatePicker datePicker;

    @ViewById(R.id.timePicker)
    protected TimePicker timePicker;

    @ViewById(R.id.phone_login)
    protected TextView phoneLogin;

    private String phone;
    private String requirementid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            phone = bundle.getString(Global.PHONE);
            requirementid = bundle.getString(Global.REQUIREMENT_ID);
            LogTool.d(this.getClass().getName(), "phone =" + phone + " requirementid = " + requirementid);
        }
    }

    @AfterViews
    protected void initAnnotationView() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        long time = cal.getTimeInMillis();
        datePicker.setMinDate(time);
        timePicker.setIs24HourView(true);

        if (!TextUtils.isEmpty(phone)) {
            phoneLogin.setText(phone);
        }
    }

    @Click({R.id.head_back_layout, R.id.btn_confirm, R.id.btn_phone_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.btn_confirm:
                Calendar cal = Calendar.getInstance();
                cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
                long houseCheckTime = cal.getTimeInMillis();
                LogTool.d(this.getClass().getName(), StringUtils.covertLongToStringHasMini(houseCheckTime));
                setHouseTime(requirementid, houseCheckTime);
                break;
            case R.id.btn_phone_layout:
                if (!TextUtils.isEmpty(phone)) {
                    UiHelper.IntentToPhone(this, phone);
                }
                break;
        }
    }

    private void setHouseTime(String requirementid, long houseCheckTime) {
        JianFanJiaClient.responseRequirement(this, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {
                showWaitDialog();
            }

            @Override
            public void loadSuccess(Object data) {
                hideWaitDialog();
                appManager.finishActivity(SettingMeasureDateActivity_.class);
                EventBus.getDefault().post(new UpdateEvent(null));
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextShort(error_msg);
                hideWaitDialog();
            }
        }, requirementid, houseCheckTime, this);
    }

}
