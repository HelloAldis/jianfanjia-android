package com.jianfanjia.cn.designer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jianfanjia.cn.designer.Event.UpdateEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseAnnotationActivity;
import com.jianfanjia.cn.designer.bean.PlanInfo;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.cache.BusinessManager;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.StringUtils;

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
 * Date:2016-01-21 14:43
 */
@EActivity(R.layout.activity_config_contract)
public class SettingContractActivity extends BaseAnnotationActivity {

    @ViewById(R.id.datePicker)
    protected DatePicker datePicker;

    @ViewById(R.id.workTypeContent)
    protected TextView decTypeContent;

    @ViewById(R.id.totalPriceContent)
    protected TextView totalPriceContent;

    @ViewById(R.id.durationContent)
    protected TextView durationContent;

    @ViewById(R.id.startTimeContent)
    protected TextView startTimeContent;

    @ViewById(R.id.endTimeContent)
    protected TextView endTimeContent;

    @ViewById(R.id.timeTitle)
    protected TextView titleTimeView;

    @ViewById(R.id.contractInfoLayout)
    protected LinearLayout contractInfoLayout;

    @ViewById(R.id.chooseDateLayout)
    protected LinearLayout chooseDateLayout;

    @ViewById(R.id.head_center_title)
    protected TextView titleHeadView;

    private RequirementInfo requirementInfo;
    private PlanInfo planInfo;
    private String requirementid;
    private int totalDuration;
    private float totalBudget;
    private String workType;
    private Calendar startCalendar = Calendar.getInstance();//开工日期
    private Calendar endCalendar = Calendar.getInstance();//竣工日期
    private Calendar chooseCalendar = Calendar.getInstance();//选择日期

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            requirementInfo = (RequirementInfo) bundle.getSerializable(Global.REQUIREMENT_INFO);
            planInfo = (PlanInfo) bundle.getSerializable(Global.PLAN);
            if (requirementInfo != null) {
                requirementid = requirementInfo.get_id();
                totalDuration = planInfo.getDuration();
                totalBudget = planInfo.getTotal_price();
                workType = requirementInfo.getWork_type();
                LogTool.d(this.getClass().getName(), " requirementid = " + requirementid +
                        " totalDuration =" + totalDuration + " totalBudget=" + totalBudget
                        + " workType=" + workType);
            }
        }
    }

    @AfterViews
    protected void initAnnotationView() {

        decTypeContent.setText(String.format(getString(R.string.process_workType_cont),
                BusinessManager.convertWorktypeToShow(workType)));
        totalPriceContent.setText(String.format(getString(R.string.process_totalprice_cont),
                StringUtils.digitUppercase(totalBudget), (int) totalBudget));
        durationContent.setText(String.format(getString(R.string.process_duration_cont),
                totalDuration));

        if (requirementInfo.getStatus().equals(Global.REQUIREMENT_STATUS4)) {//设置开工日期
            titleHeadView.setText(getString(R.string.str_setting_startdate));

            startTimeContent.setText(String.format(getString(R.string.process_startTime_cont), "__", "__", "__"));
            endTimeContent.setText(String.format(getString(R.string.process_endTime_cont), "__", "__", "__"));
            startCalendar.add(Calendar.DAY_OF_MONTH,1);//开工日期必须从第二天开始算起
            datePicker.setMinDate(startCalendar.getTimeInMillis());
            datePicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            datePicker.init(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                    startCalendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            chooseCalendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                            updateTitle(chooseCalendar);
                        }
                    });
            updateTitle(startCalendar);
        } else {
            titleHeadView.setText(getString(R.string.contract_profile));

            //已经设置了开工时间，就只展示合同
            chooseDateLayout.setVisibility(View.GONE);
            startCalendar.setTimeInMillis(requirementInfo.getStart_at());
            endCalendar.setTimeInMillis(startCalendar.getTimeInMillis());
            endCalendar.add(Calendar.DAY_OF_MONTH, totalDuration);

            startTimeContent.setText(String.format(getString(R.string.process_startTime_cont),
                    startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH) + 1,
                    startCalendar.get(Calendar.DAY_OF_MONTH)));
            endTimeContent.setText(String.format(getString(R.string.process_endTime_cont),
                    endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH) + 1,
                    endCalendar.get(Calendar.DAY_OF_MONTH)));
        }
    }

    protected void updateTitle(Calendar calendar) {
        titleTimeView.setText(StringUtils.covertLongToStringHasChinese(calendar.getTimeInMillis()));
    }

    @Click({R.id.head_back_layout, R.id.btn_confirm})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.btn_confirm:
                long startTime = chooseCalendar.getTimeInMillis();
                LogTool.d(this.getClass().getName(), StringUtils.covertLongToStringHasMini(startTime));
                configStartTime(requirementid, startTime);
                break;
        }
    }

    private void configStartTime(String requirementid, long statrAt) {
        JianFanJiaClient.configContract(this, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                hideWaitDialog();
                appManager.finishActivity(SettingContractActivity_.class);
                EventBus.getDefault().post(new UpdateEvent(null));
            }

            @Override
            public void loadFailture(String error_msg) {

            }
        }, requirementid, statrAt, this);
    }

}