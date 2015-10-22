package com.jianfanjia.cn.view.custom_annotation_view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.OrderDesignerInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.ClickCallBack;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.view.baseview
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-22 10:46
 */
@EViewGroup(R.layout.list_item_my_designer_type1)
public class MyDesignerViewType1 extends RelativeLayout {

    @ViewById(R.id.ltm_my_designer_head)
    protected ImageView headView;

    @ViewById(R.id.ltm_my_designer_name)
    protected TextView nameView;

    @ViewById(R.id.ltm_my_designer_status)
    protected TextView statusView;

    @ViewById(R.id.ltm_my_designer_content)
    protected RelativeLayout contentLayout;

    @ViewById(R.id.ltm_my_designer_middle_layout)
    protected RelativeLayout middleLayout;

    @ViewById(R.id.merger_button1)
    protected Button button1;

    @ViewById(R.id.merger_button2)
    protected Button button2;


    public MyDesignerViewType1(Context context) {
        super(context);
    }

    public void bind(OrderDesignerInfo designerInfo,ClickCallBack clickCallBack,int position) {
        String status = designerInfo.getPlan().getStatus();
        switch (status){
            case Global.PLAN_STATUS1:
                button1.setVisibility(View.GONE);
                button2.setText(getResources().getString(R.string.str_change_designer));
                statusView.setText(getResources().getString(R.string.already_refuse));

                break;
            case Global.PLAN_STATUS3:
                button1.setText(getResources().getString(R.string.str_comment));
                button2.setText(getResources().getString(R.string.str_view_plan));
                statusView.setText(getResources().getString(R.string.already_commit));
                break;
            case Global.PLAN_STATUS4:
                button1.setVisibility(View.GONE);
                button2.setText(getResources().getString(R.string.str_view_plan));
                statusView.setText(getResources().getString(R.string.not_choose));
                break;
            case Global.PLAN_STATUS5:
                button1.setText(getResources().getString(R.string.str_view_plan));
                button2.setText(getResources().getString(R.string.str_view_contract));
                statusView.setText(getResources().getString(R.string.already_choose));
                break;
            case Global.PLAN_STATUS6:
                button1.setText(getResources().getString(R.string.str_comment));
                button2.setText(getResources().getString(R.string.str_view_plan));
                button2.setEnabled(false);
                statusView.setText(getResources().getString(R.string.already_measure));
                break;
            case Global.PLAN_STATUS7:
                button1.setVisibility(View.GONE);
                button2.setText(getResources().getString(R.string.str_change_designer));
                statusView.setText(getResources().getString(R.string.not_response));
                break;
            case Global.PLAN_STATUS8:
                button1.setText(getResources().getString(R.string.str_comment));
                button2.setText(getResources().getString(R.string.str_view_plan));
                button2.setEnabled(false);
                statusView.setText(getResources().getString(R.string.not_commit));
                break;

        }

    }
}
