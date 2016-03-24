package com.jianfanjia.cn.view.custom_annotation_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.requirement.MyDesignerActivity;
import com.jianfanjia.cn.bean.OrderDesignerInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.tools.ImageShow;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.StringUtils;

/**
 * Description: com.jianfanjia.cn.view.baseview
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-22 10:46
 */
public class MyDesignerViewType2 extends RecyclerView.ViewHolder {

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

    @Bind(R.id.ltm_my_designer_textview1)
    protected TextView textView1;

    @Bind(R.id.ltm_my_designer_textview2)
    protected TextView textView2;

    @Bind(R.id.ltm_my_designer_button3)
    protected TextView button3;

    @Bind(R.id.designerinfo_auth)
    ImageView authView;

    private Context context;

    public MyDesignerViewType2(View view, Context context) {
        super(view);
        ButterKnife.bind(this, view);
        this.context = context;
    }

    public static MyDesignerViewType2 build(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_my_designer_type2, null);
        return new MyDesignerViewType2(view, context);
    }

    public void bind(OrderDesignerInfo designerInfo, final ClickCallBack clickCallBack, final int position) {
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

        statusView.setTextColor(context.getResources().getColor(R.color.blue_color));
        statusView.setText(context.getResources().getString(R.string.already_repsonse));
        LogTool.d(this.getClass().getName(),"当前时间 ="+ Calendar.getInstance().getTimeInMillis()+ "  量房时间 =" + designerInfo.getPlan().getHouse_check_time());
        if (Calendar.getInstance().getTimeInMillis() > designerInfo.getPlan().getHouse_check_time()) {
            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            button3.setVisibility(View.VISIBLE);
            button3.setText(context.getResources().getString(R.string.confirm_measure_house));
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCallBack.click(position, MyDesignerActivity.CONFIRM_MEASURE_HOUSE);
                }
            });
        } else {
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.GONE);
            textView1.setText(context.getResources().getString(R.string.measure_house_time));
            textView2.setText(StringUtils.covertLongToStringHasMini(designerInfo.getPlan().getHouse_check_time()));
        }

        RequirementInfo requirementInfo = designerInfo.getRequirement();
        String requirementStatus = requirementInfo.getStatus();
        if (requirementStatus.equals(Global.REQUIREMENT_STATUS4) || requirementStatus.equals(Global.REQUIREMENT_STATUS5)
                || requirementStatus.equals(Global.REQUIREMENT_STATUS7) || requirementStatus.equals(Global.REQUIREMENT_STATUS8) ) {
            button3.setEnabled(false);
            button3.setTextColor(context.getResources().getColor(R.color.grey_color));
        } else {
            button3.setEnabled(true);
            button3.setTextColor(context.getResources().getColor(R.color.orange_color));
        }

    }
}
