package com.jianfanjia.cn.view.custom_annotation_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.requirement.MyDesignerActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.tools.ImageShow;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description: com.jianfanjia.cn.view.baseview
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-22 10:46
 */
public class MyDesignerViewType5 extends RecyclerView.ViewHolder {

    @Bind(R.id.ltm_my_designer_left_layout)
    protected RelativeLayout ltm_my_designer_left_layout;

    @Bind(R.id.ltm_my_designer_head)
    protected ImageView headView;

    @Bind(R.id.ltm_my_designer_name)
    protected TextView nameView;

    @Bind(R.id.ltm_my_identity_auth)
    protected ImageView identityAuthView;

    @Bind(R.id.ltm_my_info_auth)
    protected ImageView infoAuthView;

    @Bind(R.id.ltm_my_ratingBar)
    protected RatingBar ratingBarView;

    @Bind(R.id.ltm_my_designer_status)
    protected TextView statusView;

    @Bind(R.id.ltm_my_designer_content)
    protected LinearLayout contentLayout;

    @Bind(R.id.merger_button1_layout)
    protected RelativeLayout merger_button1_layout;

    @Bind(R.id.merger_button2_layout)
    protected RelativeLayout merger_button2_layout;

    @Bind(R.id.merger_button3_layout)
    protected RelativeLayout merger_button3_layout;

    @Bind(R.id.ltm_my_designer_textview1)
    protected TextView button1;

    @Bind(R.id.ltm_my_designer_textview2)
    protected TextView button2;

    @Bind(R.id.ltm_my_designer_textview3)
    protected TextView button3;

    private Context context;

    public MyDesignerViewType5(View view, Context context) {
        super(view);
        ButterKnife.bind(this, view);
        this.context = context;
    }

    public static MyDesignerViewType5 build(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_my_designer_type2, null);
        return new MyDesignerViewType5(view, context);
    }

    public void bind(Designer designerInfo, final ClickCallBack clickCallBack, final int position) {
        String imageid = designerInfo.getImageid();
        String username = designerInfo.getUsername();
        ltm_my_designer_left_layout.setOnClickListener(new View.OnClickListener() {
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
            infoAuthView.setVisibility(View.VISIBLE);
        } else {
            infoAuthView.setVisibility(View.GONE);
        }
        if (designerInfo.getUid_auth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
            identityAuthView.setVisibility(View.VISIBLE);
        } else {
            identityAuthView.setVisibility(View.GONE);
        }
        int respond_speed = (int) designerInfo.getRespond_speed();
        int service_attitude = (int) designerInfo.getService_attitude();
        ratingBarView.setRating((respond_speed + service_attitude) / 2);

        button1.setTextColor(context.getResources().getColor(R.color.light_black_color));
        button1.setText(context.getResources().getString(R.string.str_check_comment));
        merger_button1_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position, MyDesignerActivity.VIEW_COMMENT);
            }
        });

        button2.setText(context.getResources().getString(R.string.str_view_plan));
        merger_button2_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position, MyDesignerActivity.VIEW_PLAN);
            }
        });

        button3.setText(context.getResources().getString(R.string.str_view_contract));
        merger_button3_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position, MyDesignerActivity.VIEW_CONTRACT);
            }
        });

        Requirement requirementInfo = designerInfo.getRequirement();
        String requirementStatus = requirementInfo.getStatus();
        switch (requirementStatus) {
            case Global.REQUIREMENT_STATUS1:
                break;
            case Global.REQUIREMENT_STATUS7:
                merger_button3_layout.setEnabled(true);
                button3.setTextColor(context.getResources().getColor(R.color.orange_color));
                statusView.setTextColor(context.getResources().getColor(R.color.blue_color));
                statusView.setText(context.getResources().getString(R.string
                        .str_wait_confirm_constract));
                break;
            case Global.REQUIREMENT_STATUS2:
                break;
            case Global.REQUIREMENT_STATUS4:
                merger_button3_layout.setEnabled(false);
                button3.setTextColor(context.getResources().getColor(R.color.middle_grey_color));
                statusView.setTextColor(context.getResources().getColor(R.color.blue_color));
                statusView.setText(context.getResources().getString(R.string
                        .str_wait_setting_constract));
                break;
            case Global.REQUIREMENT_STATUS3:
                break;
            case Global.REQUIREMENT_STATUS5:
                merger_button3_layout.setEnabled(true);
                button3.setTextColor(context.getResources().getColor(R.color.orange_color));
                statusView.setTextColor(context.getResources().getColor(R.color.orange_color));
                statusView.setText(context.getResources().getString(R.string
                        .str_working));
                break;
            case Global.REQUIREMENT_STATUS6:
                break;
            case Global.REQUIREMENT_STATUS8:
                merger_button3_layout.setEnabled(true);
                button3.setTextColor(context.getResources().getColor(R.color.orange_color));
                statusView.setTextColor(context.getResources().getColor(R.color.green_color));
                statusView.setText(context.getResources().getString(R.string
                        .str_done));
                break;
            default:
                break;
        }
    }
}
