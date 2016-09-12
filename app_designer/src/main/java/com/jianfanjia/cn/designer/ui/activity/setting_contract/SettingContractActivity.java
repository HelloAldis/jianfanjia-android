package com.jianfanjia.cn.designer.ui.activity.setting_contract;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.bean.ConfigContractInfo;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.BusinessCovertUtil;
import com.jianfanjia.cn.designer.view.SwipeBackLayout;
import com.jianfanjia.common.tool.DateFormatTool;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.StringUtils;

/**
 * Description: com.jianfanjia.cn.designer.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-21 14:43
 */
public class SettingContractActivity extends BaseSwipeBackActivity {

    @Bind(R.id.datePicker)
    protected DatePicker datePicker;

    @Bind(R.id.workTypeContent)
    protected TextView decTypeContent;

    @Bind(R.id.totalPriceContent)
    protected TextView totalPriceContent;

    @Bind(R.id.durationContent)
    protected TextView durationContent;

    @Bind(R.id.startTimeContent)
    protected TextView startTimeContent;

    @Bind(R.id.endTimeContent)
    protected TextView endTimeContent;

    @Bind(R.id.timeTitle)
    protected TextView titleTimeView;

    @Bind(R.id.contractInfoLayout)
    protected LinearLayout contractInfoLayout;

    @Bind(R.id.chooseDateLayout)
    protected LinearLayout chooseDateLayout;

    @Bind(R.id.head_center_title)
    protected TextView titleHeadView;

    @Bind(R.id.tv_next)
    protected TextView nextView;

    private Requirement requirementInfo;
    private Plan plan;
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
        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.TOP);
        this.getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            requirementInfo = (Requirement) bundle.getSerializable(Global.REQUIREMENT_INFO);
            plan = (Plan) bundle.getSerializable(Global.PLAN_DETAIL);
            if (requirementInfo != null) {
                requirementid = requirementInfo.get_id();
                totalDuration = plan.getDuration();
                totalBudget = plan.getTotal_price();
                workType = requirementInfo.getWork_type();
                LogTool.d(" requirementid = " + requirementid +
                        " totalDuration =" + totalDuration + " totalBudget=" + totalBudget
                        + " workType=" + workType);
            }
        }
    }

    protected void initView() {
        decTypeContent.setText(String.format(getString(R.string.process_workType_cont),
                BusinessCovertUtil.convertWorktypeToShow(workType)));
        totalPriceContent.setText(String.format(getString(R.string.process_totalprice_cont),
                StringUtils.digitUppercase(totalBudget), (int) totalBudget));
        durationContent.setText(String.format(getString(R.string.process_duration_cont),
                totalDuration));

        if (requirementInfo.getStatus().equals(Global.REQUIREMENT_STATUS4)) {//设置开工日期
            titleHeadView.setText(getString(R.string.str_setting_startdate));

            startTimeContent.setText(String.format(getString(R.string.process_startTime_cont), "__", "__", "__"));
            endTimeContent.setText(String.format(getString(R.string.process_endTime_cont), "__", "__", "__"));
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);//开工日期必须从第二天开始算起
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

            nextView.setVisibility(View.VISIBLE);
        } else {
            nextView.setVisibility(View.GONE);

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
        titleTimeView.setText(DateFormatTool.covertLongToStringHasChinese(calendar.getTimeInMillis()));
    }

    @OnClick({R.id.head_back_layout, R.id.tv_next})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                overridePendingTransition(0, R.anim.slide_out_to_bottom);
                break;
            case R.id.tv_next:
                navegationToNext();
                break;
        }
    }

    private void navegationToNext() {
        long startTime = chooseCalendar.getTimeInMillis();
        LogTool.d(DateFormatTool.longToStringHasMini(startTime));
        ConfigContractInfo configContractInfo = new ConfigContractInfo();
        configContractInfo.setRequirementid(requirementid);
        configContractInfo.setStartAt(startTime);

        Bundle bundle = new Bundle();
        bundle.putSerializable(ChooseTeamActivity.CONFIG_CONTRACT_INFO, configContractInfo);
        startActivity(ChooseTeamActivity.class, bundle);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_config_contract;
    }
}
