package com.jianfanjia.cn.designer.view.custom_annotation_view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.cache.BusinessManager;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.fragment.XuQiuFragment;
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
@EViewGroup(R.layout.list_item_req_communicate_type1)
public class MyPlanViewType2 extends BaseAnnotationView {

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

    @ViewById(R.id.button_refuse)
    protected Button refuseButton;

    @ViewById(R.id.button_confirm)
    protected Button confirmButton;

    public MyPlanViewType2(Context context) {
        super(context);
    }

    public void bind(RequirementInfo requirementInfo,final ClickCallBack clickCallBack,final int position) {
        cellView.setText(requirementInfo.getCell());
        createTimeView.setText(StringUtils.covertLongToString(requirementInfo.getLast_status_update_time()));
        statusView.setText(getResources().getStringArray(R.array.plan_status)[Integer.parseInt(requirementInfo.getPlan().getStatus())]);
        String imageId = requirementInfo.getUser().getImageid();
        if(TextUtils.isEmpty(imageId)){
            imageShow.displayLocalImage(dataManagerNew.getUserImagePath(), headView);
        }else{
            imageShow.displayImageHeadWidthThumnailImage(context, imageId, headView);
        }
        String username = requirementInfo.getUser().getUsername();
        if (!TextUtils.isEmpty(username)) {
            nameView.setText(username);
        } else {
            nameView.setText(getResources().getString(R.string.ower));
        }
        String sex = requirementInfo.getUser().getSex();
        if(!TextUtils.isEmpty(sex)){
            sexView.setVisibility(View.VISIBLE);
            switch (sex){
                case Constant.SEX_MAN:
                    sexView.setImageResource(R.mipmap.icon_designer_user_man);
                    break;
                case Constant.SEX_WOMEN:
                    sexView.setImageResource(R.mipmap.icon_designer_user_woman);
                    break;
            }
        }else {
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
                clickCallBack.click(position, XuQiuFragment.ITEM_PRIVIEW);
            }
        });
    }
}
