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
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.view.baseview.BaseAnnotationView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.view.baseview
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-22 10:46
 */
@EViewGroup(R.layout.list_item_my_designer_type1)
public class MyDesignerViewType7 extends BaseAnnotationView {

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

    public MyDesignerViewType7(Context context) {
        super(context);
    }

    public void bind(OrderDesignerInfo designerInfo, final ClickCallBack clickCallBack, final int position) {
        String status = designerInfo.getPlan().getStatus();
        String imageid = designerInfo.getImageid();
        String username = designerInfo.getUsername();
        if (!TextUtils.isEmpty(imageid)) {
            ImageLoader.getInstance().displayImage(Url_New.GET_THUMBNAIL_IMAGE + imageid, headView, options);
        } else {
            ImageLoader.getInstance().displayImage(Constant.DEFALUT_DESIGNER_PIC, headView, options);
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

        button1.setVisibility(View.GONE);
        button2.setText(getResources().getString(R.string.str_change_designer));
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position, MyDesignerActivity.CHANGE_DESIGNER);
            }
        });
        statusView.setText(getResources().getString(R.string.not_response));
        statusView.setTextColor(getResources().getColor(R.color.grey_color));

        RequirementInfo requirementInfo = designerInfo.getRequirement();
        String requirementStatus = requirementInfo.getStatus();
        if (requirementStatus.equals(Global.REQUIREMENT_STATUS4) || requirementStatus.equals(Global.REQUIREMENT_STATUS5) || requirementStatus.equals(Global.REQUIREMENT_STATUS7)) {
            button2.setEnabled(false);
        } else {
            button2.setEnabled(true);
        }


    }
}
