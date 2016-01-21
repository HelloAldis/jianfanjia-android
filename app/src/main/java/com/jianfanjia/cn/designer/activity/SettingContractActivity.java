package com.jianfanjia.cn.designer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    @ViewById(R.id.contractInfoLayout)
    protected LinearLayout contractInfoLayout;

    @ViewById(R.id.chooseDateLayout)
    protected LinearLayout chooseDateLayout;

    private RequirementInfo requirementInfo;
    private PlanInfo planInfo;
    private String requirementid;
    private int totalDuration;
    private float totalBudget;
    private String workType;

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
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        long time = cal.getTimeInMillis();
        datePicker.setMinDate(time);

        decTypeContent.setText(String.format(getString(R.string.process_workType_cont), BusinessManager.convertWorktypeToShow(workType)));
        totalPriceContent.setText(String.format(getString(R.string.process_totalprice_cont), StringUtils.digitUppercase(totalBudget), (int) totalBudget));
        durationContent.setText(String.format(getString(R.string.process_duration_cont), totalDuration));

        if(requirementInfo.getStatus().equals(Global.REQUIREMENT_STATUS4)){

        }else{
            //已经设置了开工时间，就只展示合同
            chooseDateLayout.setVisibility(View.GONE);
        }
    }

    @Click({R.id.head_back_layout, R.id.btn_confirm})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.btn_confirm:
                Calendar cal = Calendar.getInstance();
                cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), 8, 0, 0);
                long startTime = cal.getTimeInMillis();
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
