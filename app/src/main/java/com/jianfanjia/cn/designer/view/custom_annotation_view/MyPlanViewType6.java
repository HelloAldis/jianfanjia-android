package com.jianfanjia.cn.designer.view.custom_annotation_view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.cache.BusinessManager;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.fragment.RecycleViewFragment;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.tools.StringUtils;
import com.jianfanjia.cn.designer.view.baseview.BaseAnnotationView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.view.baseview
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-22 10:46
 */
@EViewGroup(R.layout.list_item_plan_type6)
public class MyPlanViewType6 extends BaseAnnotationView {

    @ViewById(R.id.ltm_req_owner_head)
    protected ImageView headView;

    @ViewById(R.id.ltm_req_username)
    protected TextView nameView;

    @ViewById(R.id.ltm_req_status)
    protected TextView statusView;

    @ViewById(R.id.ltm_req_cell)
    protected TextView cellView;

    @ViewById(R.id.ltm_req_time)
    protected TextView createTimeView;

    @ViewById(R.id.ltm_req_info)
    protected TextView desciptionView;

    @ViewById(R.id.ltm_req_info_layout)
    protected RelativeLayout contentLayout;

    @ViewById(R.id.ltm_req_sex)
    protected ImageView sexView;

    @ViewById(R.id.phoneLayout)
    protected RelativeLayout phoneLayout;

    @ViewById(R.id.commentLayout)
    protected RelativeLayout commentLayout;

    public MyPlanViewType6(Context context) {
        super(context);
    }

    public void bind(RequirementInfo requirementInfo, final ClickCallBack clickCallBack, final int position) {
        cellView.setText(requirementInfo.getCell());
        long lastUpdateTime = requirementInfo.getPlan().getLast_status_update_time();
        if (lastUpdateTime != 0l) {
            createTimeView.setText(StringUtils.covertLongToStringHasMini(lastUpdateTime));
        }
        statusView.setText(getResources().getStringArray(R.array.plan_status)[Integer.parseInt(requirementInfo.getPlan().getStatus())]);
        statusView.setTextColor(getResources().getColor(R.color.orange_color));
        String imageId = requirementInfo.getUser().getImageid();
        if (!TextUtils.isEmpty(imageId)) {
            imageShow.displayImageHeadWidthThumnailImage(context, imageId, headView);
        }else{
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
        contentLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position, RecycleViewFragment.PRIVIEW_REQUIREMENT_TYPE);
            }
        });
        phoneLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position, RecycleViewFragment.PHONE_TYPE);
            }
        });
        if(requirementInfo.getEvaluation() != null){
            commentLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCallBack.click(position, RecycleViewFragment.RREVIEW_COMMENT_TYPE);
                }
            });
            commentLayout.setEnabled(true);
        }else{
            commentLayout.setEnabled(false);
        }
    }
}
