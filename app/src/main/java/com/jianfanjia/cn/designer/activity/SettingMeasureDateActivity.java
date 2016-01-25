package com.jianfanjia.cn.designer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jianfanjia.cn.designer.Event.UpdateEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseAnnotationActivity;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.StringUtils;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.view.DateTimePicker;

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
    protected DateTimePicker datePicker;

    @ViewById(R.id.timeTitle)
    protected TextView timeTitleView;

    @ViewById(R.id.phone_login)
    protected TextView phoneLogin;

    private String phone;
    private String requirementid;

    private Calendar chooseDate = Calendar.getInstance();
    private Calendar currentDate = Calendar.getInstance();

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
//        updateTitle(chooseDate);
        datePicker.setOnDateTimeChangedListener(new DateTimePicker.OnDateTimeChangedListener() {
            @Override
            public void onDateTimeChanged(DateTimePicker view,
                                          int year, int month, int day, int hour, int minute) {
                chooseDate.set(year, month, day, hour, minute, 0);

                LogTool.d(this.getClass().getName(),"month =" + month + " day =" + day + " hour =" + hour +" minite =" + minute);

                /**
                 * 更新日期
                 */
                updateTitle(chooseDate);
            }
        });
        datePicker.setMinDate(currentDate);
        if (!TextUtils.isEmpty(phone)) {
            phoneLogin.setText(phone);
        }
    }

    protected void updateTitle(Calendar calendar) {
        timeTitleView.setText(StringUtils.covertLongToStringHasMiniAndChinese(calendar.getTimeInMillis()));
    }

    @Click({R.id.head_back_layout, R.id.btn_confirm, R.id.btn_phone_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.btn_confirm:
                long houseCheckTime = chooseDate.getTimeInMillis();
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
