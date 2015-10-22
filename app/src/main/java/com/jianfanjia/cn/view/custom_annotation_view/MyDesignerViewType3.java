package com.jianfanjia.cn.view.custom_annotation_view;

import android.content.Context;
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
@EViewGroup(R.layout.list_item_my_designer_type3)
public class MyDesignerViewType3 extends RelativeLayout {

    public static final int PLAN_TYPE1 = 0;
    public static final int PLAN_TYPE2 = 1;
    public static final int PLAN_TYPE3 = 2;
    public static final int PLAN_TYPE4 = 3;
    public static final int PLAN_TYPE5 = 4;
    public static final int PLAN_TYPE6 = 5;
    public static final int PLAN_TYPE7 = 6;
    public static final int PLAN_TYPE8 = 7;

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

    public MyDesignerViewType3(Context context) {
        super(context);
    }

    public void bind(OrderDesignerInfo designerInfo,ClickCallBack clickCallBack,int position) {
        String status = designerInfo.getPlan().getStatus();
        switch (status){
            case Global.PLAN_STATUS0:
                textView3.setText(getResources().getString(R.string.wait_response));
                statusView.setText(getResources().getString(R.string.already_order));
                break;
        }

    }
}
