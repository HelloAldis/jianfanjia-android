package com.jianfanjia.cn.designer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.request.designer.ConfigMeaHouseTimeRequest;
import com.jianfanjia.cn.designer.Event.UpdateEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.cn.designer.tools.StringUtils;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.view.DateTimePicker;
import de.greenrobot.event.EventBus;

/**
 * Description: com.jianfanjia.cn.designer.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-19 17:48
 */
public class SettingMeasureDateActivity extends BaseActivity {

    @Bind(R.id.datePicker)
    protected DateTimePicker datePicker;

    @Bind(R.id.timeTitle)
    protected TextView timeTitleView;

    @Bind(R.id.phone_login)
    protected TextView phoneLogin;

    private String phone;
    private String requirementid;

    private Calendar chooseDate = Calendar.getInstance();
    private Calendar currentDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getDataFromIntent();
        initView();
    }

    private void getDataFromIntent(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            phone = bundle.getString(Global.PHONE);
            requirementid = bundle.getString(Global.REQUIREMENT_ID);
            LogTool.d(this.getClass().getName(), "phone =" + phone + " requirementid = " + requirementid);
        }
    }

    protected void initView() {
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

    @OnClick({R.id.head_back_layout, R.id.btn_confirm, R.id.btn_phone_layout})
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
                    UiHelper.callPhoneIntent(this, phone);
                }
                break;
        }
    }

    private void setHouseTime(String requirementid, long houseCheckTime) {
        ConfigMeaHouseTimeRequest configMeaHouseTimeRequest = new ConfigMeaHouseTimeRequest();
        configMeaHouseTimeRequest.setRequirementid(requirementid);
        configMeaHouseTimeRequest.setHouse_check_time(houseCheckTime);

        Api.configMeaHouse(configMeaHouseTimeRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                hideWaitDialog();
                appManager.finishActivity(SettingMeasureDateActivity.class);
                EventBus.getDefault().post(new UpdateEvent(null));
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_date;
    }
}
