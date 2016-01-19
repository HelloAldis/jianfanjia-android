package com.jianfanjia.cn.designer.view.custom_annotation_view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.designer.activity.requirement.MyDesignerActivity;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.bean.OrderDesignerInfo;
import com.jianfanjia.cn.designer.config.Constant;
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
@EViewGroup(R.layout.list_item_my_designer_type1)
public class MyDesignerViewType6 extends BaseAnnotationView {

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
    protected TextView button1;

    @ViewById(R.id.merger_button2)
    protected TextView button2;

    @ViewById(R.id.designerinfo_auth)
    ImageView authView;

    public MyDesignerViewType6(Context context) {
        super(context);
    }

    public void bind(OrderDesignerInfo designerInfo, final ClickCallBack clickCallBack, final int position) {
        String imageid = designerInfo.getImageid();
        String username = designerInfo.getUsername();
        headView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position,MyDesignerActivity.VIEW_DESIGNER);
            }
        });
        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(context, imageid, headView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, headView);
        }
        if (!TextUtils.isEmpty(username)) {
            nameView.setText(username);
        } else {
            nameView.setText(getResources().getString(R.string.designer));
        }
        if (designerInfo.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
            authView.setVisibility(View.VISIBLE);
        } else {
            authView.setVisibility(View.GONE);
        }

        if (designerInfo.getEvaluation() == null) {
            button1.setText(getResources().getString(R.string.str_comment));
            button1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCallBack.click(position, MyDesignerActivity.COMMENT);
                }
            });
        } else {
            button1.setText(getResources().getString(R.string.str_already_comment));
            button1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCallBack.click(position, MyDesignerActivity.VIEW_COMMENT);
                }
            });
        }
        button2.setText(getResources().getString(R.string.str_view_plan));
        button2.setEnabled(false);
        button2.setTextColor(getResources().getColor(R.color.grey_color));
        statusView.setText(getResources().getString(R.string.already_measure));
        statusView.setTextColor(getResources().getColor(R.color.grey_color));


    }
}