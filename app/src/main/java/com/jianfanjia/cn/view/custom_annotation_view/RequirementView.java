package com.jianfanjia.cn.view.custom_annotation_view;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.RequirementInfo;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.view.custom_annotation_view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-19 17:00
 */
@EViewGroup(R.layout.list_item_req)
public class RequirementView extends FrameLayout {

    @RootContext
    Context context;

    public RequirementView(Context context) {
        super(context);
    }

    @ViewById
    protected TextView ltm_req_cell;
    @ViewById
    protected TextView ltm_req_publish_time;
    @ViewById
    protected TextView ltm_req_update_time;
    @ViewById
    protected ImageButton ltm_req_edit;
    @ViewById
    protected ImageView ltm_req_owner_head;
    @ViewById
    protected TextView ltm_req_gotopro;
    @ViewById
    protected ImageView ltm_req_designer_head0;
    @ViewById
    protected TextView ltm_req_designer_name0;
    @ViewById
    protected TextView ltm_req_designer_status0;
    @ViewById
    protected ImageView ltm_req_designer_head1;
    @ViewById
    protected TextView ltm_req_designer_name1;
    @ViewById
    protected TextView ltm_req_designer_status1;
    @ViewById
    protected ImageView ltm_req_designer_head2;
    @ViewById
    protected TextView ltm_req_designer_name2;
    @ViewById
    protected TextView ltm_req_designer_status2;

    public void bind(RequirementInfo requirementInfo) {

    }

}
