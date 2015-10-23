package com.jianfanjia.cn.view.custom_annotation_view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.MyDesignerActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.OrderDesignerInfo;
import com.jianfanjia.cn.bean.PlanInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;

/**
 * Description: com.jianfanjia.cn.view.baseview
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-22 10:46
 */
@EViewGroup(R.layout.list_item_my_designer_type2)
public class MyDesignerViewType2 extends RelativeLayout {

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
    protected Button button3;

    public MyDesignerViewType2(Context context) {
        super(context);
    }

    public void bind(OrderDesignerInfo designerInfo, final ClickCallBack clickCallBack, final int position) {
        String status = designerInfo.getPlan().getStatus();
        String imageid = designerInfo.getImageid();
        String username = designerInfo.getUsername();
        PlanInfo planInfo = designerInfo.getPlan();
        long housechecktime = planInfo.getHouse_check_time();
        if (!TextUtils.isEmpty(imageid)) {
            ImageLoader.getInstance().displayImage(Url_New.GET_IMAGE + imageid, headView);
        } else {
            ImageLoader.getInstance().displayImage(Constant.DEFALUT_DESIGNER_PIC, headView);
        }
        if (!TextUtils.isEmpty(username)) {
            nameView.setText(username);
        } else {
            nameView.setText(getResources().getString(R.string.designer));
        }
        statusView.setText(getResources().getString(R.string.already_repsonse));

        switch (status) {
            case Global.PLAN_STATUS2:
                if (Calendar.getInstance().getTimeInMillis() - housechecktime > Constant.CONFIRM_HOUSE_EXPIRE) {
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
                }
                break;
        }

    }
}