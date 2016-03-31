package com.jianfanjia.cn.view.custom_annotation_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.requirement.MyDesignerActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.tools.ImageShow;

/**
 * Description: com.jianfanjia.cn.view.baseview
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-22 10:46
 */
public class MyDesignerViewType1 extends RecyclerView.ViewHolder{

    @Bind(R.id.ltm_my_designer_head)
    protected ImageView headView;

    @Bind(R.id.ltm_my_designer_name)
    protected TextView nameView;

    @Bind(R.id.ltm_my_designer_status)
    protected TextView statusView;

    @Bind(R.id.ltm_my_designer_content)
    protected RelativeLayout contentLayout;

    @Bind(R.id.ltm_my_designer_middle_layout)
    protected RelativeLayout middleLayout;

    @Bind(R.id.merger_button1)
    protected TextView button1;

    @Bind(R.id.merger_button2)
    protected TextView button2;

    @Bind(R.id.designerinfo_auth)
    ImageView authView;

    private Context context;

    public MyDesignerViewType1(View view, Context context) {
        super(view);
        ButterKnife.bind(this, view);
        this.context = context;
    }

    public static MyDesignerViewType1 build(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_my_designer_type1, null);
        return new MyDesignerViewType1(view, context);
    }

    public void bind(Designer designerInfo, final ClickCallBack clickCallBack, final int position) {
        String imageid = designerInfo.getImageid();
        String username = designerInfo.getUsername();
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position, MyDesignerActivity.VIEW_DESIGNER);
            }
        });
        if (!TextUtils.isEmpty(imageid)) {
            ImageShow.getImageShow().displayImageHeadWidthThumnailImage(context, imageid, headView);
        } else {
            headView.setImageResource(R.mipmap.icon_default_head);
        }
        if (!TextUtils.isEmpty(username)) {
            nameView.setText(username);
        } else {
            nameView.setText(context.getResources().getString(R.string.designer));
        }
        if (designerInfo.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
            authView.setVisibility(View.VISIBLE);
        } else {
            authView.setVisibility(View.GONE);
        }

        button1.setVisibility(View.GONE);
        button2.setText(context.getResources().getString(R.string.str_change_designer));
        statusView.setText(context.getResources().getString(R.string.already_refuse));
        statusView.setTextColor(context.getResources().getColor(R.color.grey_color));
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position, MyDesignerActivity.CHANGE_DESIGNER);
            }
        });

        Requirement requirementInfo = designerInfo.getRequirement();
        String requirementStatus = requirementInfo.getStatus();
        if (requirementStatus.equals(Global.REQUIREMENT_STATUS4) || requirementStatus.equals(Global.REQUIREMENT_STATUS5)
                || requirementStatus.equals(Global.REQUIREMENT_STATUS7) || requirementStatus.equals(Global.REQUIREMENT_STATUS8)){
            button2.setEnabled(false);
            button2.setTextColor(context.getResources().getColor(R.color.grey_color));
        } else {
            button2.setEnabled(true);
            button2.setTextColor(context.getResources().getColor(R.color.font_white));
        }

    }
}