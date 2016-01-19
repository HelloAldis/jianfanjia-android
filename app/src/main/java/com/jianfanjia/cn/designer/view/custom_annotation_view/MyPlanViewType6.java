package com.jianfanjia.cn.designer.view.custom_annotation_view;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.view.baseview.BaseAnnotationView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.view.baseview
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-22 10:46
 */
@EViewGroup(R.layout.list_item_req_communicate_type3)
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

    @ViewById(R.id.ltm_req_button0)
    protected Button button0;

    @ViewById(R.id.ltm_req_phone_goto)
    ImageView phone_goto;

    public MyPlanViewType6(Context context) {
        super(context);
    }

    public void bind(RequirementInfo requirementInfo,final ClickCallBack clickCallBack,final int position) {

    }
}
