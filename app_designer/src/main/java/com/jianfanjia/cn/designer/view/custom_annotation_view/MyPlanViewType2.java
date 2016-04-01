package com.jianfanjia.cn.designer.view.custom_annotation_view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.cache.BusinessManager;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.fragment.RecycleViewFragment;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.tools.StringUtils;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description: com.jianfanjia.cn.view.baseview
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-22 10:46
 */
public class MyPlanViewType2 extends MyPlanViewTypeBase {

    @Bind(R.id.ltm_req_owner_head)
    protected ImageView headView;

    @Bind(R.id.ltm_req_username)
    protected TextView nameView;

    @Bind(R.id.ltm_req_status)
    protected TextView statusView;

    @Bind(R.id.ltm_req_cell)
    protected TextView cellView;

    @Bind(R.id.ltm_req_time)
    protected TextView createTimeView;

    @Bind(R.id.ltm_req_info)
    protected TextView desciptionView;

    @Bind(R.id.ltm_req_info_layout)
    protected RelativeLayout contentLayout;

    @Bind(R.id.ltm_req_sex)
    protected ImageView sexView;

    @Bind(R.id.phoneLayout)
    protected RelativeLayout phoneLayout;

    @Bind(R.id.measureLayout)
    protected RelativeLayout measureLayout;

    @Bind(R.id.notifyLayout)
    protected RelativeLayout notifyLayout;

    @Bind(R.id.measure_time_content)
    protected TextView measureTimeView;

    public MyPlanViewType2(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public static MyPlanViewType2 build(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_plan_type2, null);
        return new MyPlanViewType2(view);
    }

    public void bind(Requirement requirementInfo, final ClickCallBack clickCallBack, final int position) {
        cellView.setText(requirementInfo.getBasic_address());
        statusView.setText(getResources().getStringArray(R.array.plan_status)[Integer.parseInt(requirementInfo
                .getPlan().getStatus())]);
        statusView.setTextColor(getResources().getColor(R.color.orange_color));
        long lastUpdateTime = requirementInfo.getPlan().getLast_status_update_time();
        if (lastUpdateTime != 0l) {
            createTimeView.setText(StringUtils.covertLongToStringHasMini(lastUpdateTime));
        }
        long measureTime = requirementInfo.getPlan().getHouse_check_time();
        if (Calendar.getInstance().getTimeInMillis() > measureTime) {
            measureLayout.setVisibility(View.GONE);
            notifyLayout.setVisibility(View.VISIBLE);
        } else {
            measureLayout.setVisibility(View.VISIBLE);
            notifyLayout.setVisibility(View.GONE);
            if (measureTime != 0l) {
                measureTimeView.setText(StringUtils.covertLongToStringHasMini(measureTime));
            }
        }
        String imageId = requirementInfo.getUser().getImageid();
        if (!TextUtils.isEmpty(imageId)) {
            imageShow.displayImageHeadWidthThumnailImage(context, imageId, headView);
        } else {
            headView.setImageResource(R.mipmap.icon_default_head);
        }
        String username = requirementInfo.getUser().getUsername();
        if (!TextUtils.isEmpty(username)) {
            nameView.setText(username);
        } else {
            nameView.setText(getResources().getString(R.string.ower));
        }
        String sex = requirementInfo.getUser().getSex();
        if (!TextUtils.isEmpty(sex)) {
            sexView.setVisibility(View.VISIBLE);
            switch (sex) {
                case Constant.SEX_MAN:
                    sexView.setImageResource(R.mipmap.icon_designer_user_man);
                    break;
                case Constant.SEX_WOMEN:
                    sexView.setImageResource(R.mipmap.icon_designer_user_woman);
                    break;
            }
        } else {
            sexView.setVisibility(View.GONE);
        }
        String des = BusinessManager.getDesc(requirementInfo.getHouse_type(), requirementInfo.getHouse_area(),
                requirementInfo.getDec_style(), requirementInfo.getTotal_price());
        if (!TextUtils.isEmpty(des)) {
            desciptionView.setText(des);
        }
        contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position, RecycleViewFragment.PRIVIEW_REQUIREMENT_TYPE);
            }
        });
        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position, RecycleViewFragment.PHONE_TYPE);
            }
        });
        notifyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position, RecycleViewFragment.NOTIFY_MEASURE_HOUSE_TYPE);
            }
        });
    }
}
