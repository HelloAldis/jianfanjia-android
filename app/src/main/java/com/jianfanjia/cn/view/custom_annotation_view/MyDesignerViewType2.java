package com.jianfanjia.cn.view.custom_annotation_view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.requirement.MyDesignerActivity;
import com.jianfanjia.cn.bean.OrderDesignerInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.StringUtils;
import com.jianfanjia.cn.view.baseview.BaseAnnotationView;

/**
 * Description: com.jianfanjia.cn.view.baseview
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-22 10:46
 */
@EViewGroup(R.layout.list_item_my_designer_type2)
public class MyDesignerViewType2 extends BaseAnnotationView {

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

    @ViewById(R.id.ltm_my_designer_textview1)
    protected TextView textView1;

    @ViewById(R.id.ltm_my_designer_textview2)
    protected TextView textView2;

    @ViewById(R.id.ltm_my_designer_button3)
    protected TextView button3;

    @ViewById(R.id.designerinfo_auth)
    ImageView authView;

    public MyDesignerViewType2(Context context) {
        super(context);
    }

    public void bind(OrderDesignerInfo designerInfo, final ClickCallBack clickCallBack, final int position) {
        String imageid = designerInfo.getImageid();
        String username = designerInfo.getUsername();
        headView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position, MyDesignerActivity.VIEW_DESIGNER);
            }
        });
        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(context, imageid, headView);
        } else {
            headView.setImageResource(R.mipmap.icon_default_head);
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

        statusView.setTextColor(getResources().getColor(R.color.blue_color));
        statusView.setText(getResources().getString(R.string.already_repsonse));
        LogTool.d(this.getClass().getName(),"当前时间 ="+ Calendar.getInstance().getTimeInMillis()+ "  量房时间 =" + designerInfo.getPlan().getHouse_check_time());
        if (Calendar.getInstance().getTimeInMillis() > designerInfo.getPlan().getHouse_check_time()) {
            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            button3.setVisibility(View.VISIBLE);
            button3.setText(getResources().getString(R.string.confirm_measure_house));
            button3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCallBack.click(position, MyDesignerActivity.CONFIRM_MEASURE_HOUSE);
                }
            });
        } else {
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.GONE);
            textView1.setText(getResources().getString(R.string.measure_house_time));
            textView2.setText(StringUtils.covertLongToStringHasMini(designerInfo.getPlan().getHouse_check_time()));
        }

        RequirementInfo requirementInfo = designerInfo.getRequirement();
        String requirementStatus = requirementInfo.getStatus();
        if (requirementStatus.equals(Global.REQUIREMENT_STATUS4) || requirementStatus.equals(Global.REQUIREMENT_STATUS5)
                || requirementStatus.equals(Global.REQUIREMENT_STATUS7) || requirementStatus.equals(Global.REQUIREMENT_STATUS8) ) {
            button3.setEnabled(false);
            button3.setTextColor(getResources().getColor(R.color.grey_color));
        } else {
            button3.setEnabled(true);
            button3.setTextColor(getResources().getColor(R.color.orange_color));
        }

    }
}
