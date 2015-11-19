package com.jianfanjia.cn.view.custom_annotation_view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.MyDesignerActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.OrderDesignerInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.view.baseview.BaseAnnotationView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.view.baseview
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-22 10:46
 */
@EViewGroup(R.layout.list_item_my_designer_type3)
public class MyDesignerViewType0 extends BaseAnnotationView {

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

    @ViewById(R.id.ltm_my_designer_textview3)
    protected TextView textView3;

    @ViewById(R.id.designerinfo_auth)
    ImageView authView;

    public MyDesignerViewType0(Context context) {
        super(context);
    }

    public void bind(OrderDesignerInfo designerInfo,final ClickCallBack clickCallBack,final int position) {
        String status = designerInfo.getPlan().getStatus();
        String imageid = designerInfo.getImageid();
        String username = designerInfo.getUsername();
        headView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position, MyDesignerActivity.VIEW_DESIGNER);
            }
        });
        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(context,imageid,headView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, headView);
        }
        /*if (!TextUtils.isEmpty(imageid)) {
            ImageLoader.getInstance().displayImage(Url_New.GET_THUMBNAIL_IMAGE + imageid, headView, options);
        } else {
            ImageLoader.getInstance().displayImage(Constant.DEFALUT_OWNER_PIC, headView, options);
        }*/
        if (designerInfo.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
            authView.setVisibility(View.VISIBLE);
        } else {
            authView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(username)) {
            nameView.setText(username);
        } else {
            nameView.setText(getResources().getString(R.string.designer));
        }
        switch (status) {
            case Global.PLAN_STATUS0:
                textView3.setText(getResources().getString(R.string.wait_response));
                statusView.setText(getResources().getString(R.string.already_order));
                statusView.setTextColor(getResources().getColor(R.color.green_color));
                break;
        }

    }
}
