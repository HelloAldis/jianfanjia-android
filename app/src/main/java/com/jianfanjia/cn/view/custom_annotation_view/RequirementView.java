package com.jianfanjia.cn.view.custom_annotation_view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.OrderDesignerInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.fragment.XuQiuFragment;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.tools.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Description: com.jianfanjia.cn.view.custom_annotation_view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-19 17:00
 */
@EViewGroup(R.layout.list_item_req)
public class RequirementView extends BaseAnnotationView {

    public RequirementView(Context context) {
        super(context);
    }

    @ViewById
    protected TextView ltm_req_cell;
    @ViewById
    protected TextView ltm_req_starttime_cont;
    @ViewById
    protected TextView ltm_req_updatetime_cont;
    @ViewById
    protected ImageButton ltm_req_edit;
    @ViewById
    protected TextView ltm_req_status;
    @ViewById
    protected ImageView ltm_req_owner_head;
    @ViewById
    protected TextView ltm_req_gotopro;
    @ViewById
    protected ImageView ltm_req_designer_head0;
    @ViewById
    protected TextView ltm_req_designer_name0;
    @ViewById
    protected TextView ltm_req_designer_status0;
    @ViewById
    protected ImageView ltm_req_designer_head1;
    @ViewById
    protected TextView ltm_req_designer_name1;
    @ViewById
    protected TextView ltm_req_designer_status1;
    @ViewById
    protected ImageView ltm_req_designer_head2;
    @ViewById
    protected TextView ltm_req_designer_name2;
    @ViewById
    protected TextView ltm_req_designer_status2;

    public void bind(RequirementInfo requirementInfo, final ClickCallBack clickCallBack, final int position) {
        ltm_req_cell.setText(requirementInfo.getCell());
        ltm_req_starttime_cont.setText(StringUtils.covertLongToString(requirementInfo.getCreate_at()));
        ltm_req_updatetime_cont.setText(StringUtils.covertLongToString(requirementInfo.getLast_status_update_time()));
        ltm_req_status.setText(getResources().getStringArray(R.array.requirement_status)[Integer.parseInt(requirementInfo.getStatus())]);
        imageLoader.displayImage(dataManagerNew.getUserImagePath(), ltm_req_owner_head,options);
        if (requirementInfo.getStatus().equals(Global.PLAN_STATUS5)) {
            ltm_req_gotopro.setEnabled(true);
        } else {
            ltm_req_gotopro.setEnabled(false);
            ltm_req_gotopro.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCallBack.click(position, XuQiuFragment.ITEM_GOTOPRO);
                }
            });
        }
        if (requirementInfo.getStatus().equals(Global.PLAN_STATUS0)) {
            ltm_req_edit.setVisibility(View.VISIBLE);
            ltm_req_edit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCallBack.click(position, XuQiuFragment.ITEM_EDIT);
                }
            });
        } else {
            ltm_req_edit.setVisibility(View.GONE);
        }

        List<OrderDesignerInfo> recDesignerInfos = requirementInfo.getRec_designers();
        List<OrderDesignerInfo> orderDesignerInfos = requirementInfo.getOrder_designers();
        if (recDesignerInfos != null) {
            for (int i = 0; i < recDesignerInfos.size(); i++) {
                RelativeLayout designerLayout = (RelativeLayout) getRootView().findViewById(getResources().getIdentifier("ltm_req_designer_layout" + i, "id", getContext().getPackageName()));
                ImageView headView = (ImageView) getRootView().findViewById(getResources().getIdentifier("ltm_req_designer_head" + i, "id", getContext().getPackageName()));
                TextView nameView = (TextView) getRootView().findViewById(getResources().getIdentifier("ltm_req_designer_name" + i, "id", getContext().getPackageName()));
                TextView statusView = (TextView) getRootView().findViewById(getResources().getIdentifier("ltm_req_designer_status" + i, "id", getContext().getPackageName()));
                if (!TextUtils.isEmpty(recDesignerInfos.get(i).getUsername())) {
                    nameView.setText(recDesignerInfos.get(i).getUsername());
                } else {
                    nameView.setText(getResources().getString(R.string.designer));
                }
                if (!TextUtils.isEmpty(recDesignerInfos.get(i).getImageid())) {
                    ImageLoader.getInstance().displayImage(Url_New.GET_IMAGE + recDesignerInfos.get(i).getImageid(), headView,options);
                } else {
                    ImageLoader.getInstance().displayImage(Constant.DEFALUT_DESIGNER_PIC, headView,options);
                }
                statusView.setText(getResources().getString(R.string.str_not_order));
                designerLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickCallBack.click(position, XuQiuFragment.ITEM_GOTOODERDESI);
                    }
                });
            }
        } else if (orderDesignerInfos != null) {
            int size = orderDesignerInfos.size();
            for (int i = 0; i < Constant.REC_DESIGNER_TOTAL; i++) {
                RelativeLayout designerLayout = (RelativeLayout) getRootView().findViewById(getResources().getIdentifier("ltm_req_designer_layout" + i, "id", getContext().getPackageName()));
                ImageView headView = (ImageView) getRootView().findViewById(getResources().getIdentifier("ltm_req_designer_head" + i, "id", getContext().getPackageName()));
                TextView nameView = (TextView) getRootView().findViewById(getResources().getIdentifier("ltm_req_designer_name" + i, "id", getContext().getPackageName()));
                TextView statusView = (TextView) getRootView().findViewById(getResources().getIdentifier("ltm_req_designer_status" + i, "id", getContext().getPackageName()));
                if (i < size) {
                    if (!TextUtils.isEmpty(orderDesignerInfos.get(i).getUsername())) {
                        nameView.setText(orderDesignerInfos.get(i).getUsername());
                    } else {
                        nameView.setText(getResources().getString(R.string.designer));
                    }
                    if (!TextUtils.isEmpty(orderDesignerInfos.get(i).getImageid())) {
                        ImageLoader.getInstance().displayImage(Url_New.GET_IMAGE + orderDesignerInfos.get(i).getImageid(), headView,options);
                    } else {
                        ImageLoader.getInstance().displayImage(Constant.DEFALUT_DESIGNER_PIC, headView,options);
                    }
                    statusView.setText(getResources().getStringArray(R.array.plan_status)[Integer.parseInt(orderDesignerInfos.get(i).getPlan().getStatus())]);
                    designerLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickCallBack.click(position, XuQiuFragment.ITEM_GOTOMYDESI);
                        }
                    });
                } else {
                    nameView.setText("");
                    ImageLoader.getInstance().displayImage(Constant.DEFALUT_DESIGNER_PIC, headView,options);
                    statusView.setText(getResources().getString(R.string.str_not_order));
                    designerLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickCallBack.click(position, XuQiuFragment.ITEM_GOTOODERDESI);
                        }
                    });
                }
            }


        }
    }

}
