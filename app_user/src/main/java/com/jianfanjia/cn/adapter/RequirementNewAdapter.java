package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.RecyclerViewAdapterBase;
import com.jianfanjia.cn.business.DataManagerNew;
import com.jianfanjia.cn.business.RequirementBusiness;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.fragment.XuQiuFragment;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.tools.ImageShow;
import com.jianfanjia.common.tool.DateFormatTool;

/**
 * Description: com.jianfanjia.cn.base.base
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-19 19:15
 */
public class RequirementNewAdapter extends RecyclerViewAdapterBase<Requirement> {
    private static final String TAG = RequirementNewAdapter.class.getName();
    private Context context;
    private ClickCallBack clickCallBack;

    private static final int PACKGET_HIGH_POINT = 2;
    private static final int PACKGET_365 = 1;
    private static final int PACKGET_DEFAULT = 0;

    public RequirementNewAdapter(Context context, ClickCallBack cickCallBack) {
        this.context = context;
        this.clickCallBack = cickCallBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case PACKGET_365:
            case PACKGET_DEFAULT:
                view = LayoutInflater.from(context).inflate(R.layout.list_item_req, null);
                return new RequirementView(view);
            case PACKGET_HIGH_POINT:
                view = LayoutInflater.from(context).inflate(R.layout.list_item_req_high_point, null);
                return new RequirementViewHighPoint(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Requirement requirement = items.get(position);
        return Integer.parseInt(requirement.getPackage_type());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case PACKGET_365:
            case PACKGET_DEFAULT:
                RequirementView requirementView = (RequirementView) holder;
                requirementView.bind(context, items.get(position), clickCallBack, position);
                break;
            case PACKGET_HIGH_POINT:
                RequirementViewHighPoint requirementViewHighPoint = (RequirementViewHighPoint) holder;
                requirementViewHighPoint.bind(context, items.get(position), clickCallBack, position);
                break;
        }
    }

    public static class RequirementViewHighPoint extends RecyclerView.ViewHolder {

        private ImageShow imageShow;

        public RequirementViewHighPoint(View view) {
            super(view);
            ButterKnife.bind(this, view);

            imageShow = ImageShow.getImageShow();
        }

        @Bind(R.id.ltm_req_baseinfo_layout)
        RelativeLayout ltm_req_baseinfo_layout;
        @Bind(R.id.ltm_req_cell)
        protected TextView ltm_req_cell;
        @Bind(R.id.ltm_req_starttime_cont)
        protected TextView ltm_req_starttime_cont;
        @Bind(R.id.ltm_req_updatetime_cont)
        protected TextView ltm_req_updatetime_cont;
        @Bind(R.id.ltm_req_owner_head)
        protected ImageView ltm_req_owner_head;
        @Bind(R.id.ltm_req_gotopro)
        protected TextView ltm_req_gotopro;
        @Bind(R.id.ltm_req_gotopro_layout)
        protected LinearLayout ltm_req_gotopro_layout;

        @Bind(R.id.ltm_req_designer_layout)
        protected RelativeLayout designerLayout;

        @Bind(R.id.ltm_req_designer_head)
        protected ImageView headView;

        @Bind(R.id.ltm_req_designer_name)
        protected TextView nameView;

        @Bind(R.id.ltm_req_designer_status)
        protected TextView statusView;

        @Bind(R.id.designerinfo_auth)
        protected ImageView authView;

        @Bind(R.id.designerinfo_high_point)
        protected ImageView highPointView;

        private Requirement mRequirement;

        public void bind(Context context, Requirement requirementInfo, final ClickCallBack clickCallBack, final int
                position) {
            mRequirement = requirementInfo;
            String cellPhase = requirementInfo.getBasic_address();
            if (!TextUtils.isEmpty(cellPhase)) {
                ltm_req_cell.setText(cellPhase);
            }

            ltm_req_starttime_cont.setText(DateFormatTool.longToString(requirementInfo.getCreate_at()));
            ltm_req_updatetime_cont.setText(DateFormatTool.longToString(requirementInfo.getLast_status_update_time
                    ()));
            DataManagerNew dataManagerNew = DataManagerNew.getInstance();
            if (!dataManagerNew.getUserImagePath().contains(Constant.DEFALUT_PIC_HEAD)) {
                imageShow.displayImageHeadWidthThumnailImage(context, dataManagerNew.getUserImagePath(),
                        ltm_req_owner_head);
            } else {
                ltm_req_owner_head.setImageResource(R.mipmap.icon_default_head);
            }

            String requirementStatus = requirementInfo.getStatus();
            String workType = requirementInfo.getWork_type();

            if (requirementStatus.equals(Global.REQUIREMENT_STATUS0)) {
                ltm_req_baseinfo_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickCallBack.click(position, XuQiuFragment.ITEM_EDIT);
                    }
                });

            } else {
                ltm_req_baseinfo_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickCallBack.click(position, XuQiuFragment.ITEM_PRIVIEW);
                    }
                });
            }

            ltm_req_gotopro_layout.setVisibility(View.VISIBLE);

            setNewActionText(context, requirementInfo.getOrder_designers(), requirementStatus, clickCallBack,
                    position);

            setDesignerItemLayout(context, requirementInfo, clickCallBack, position, requirementStatus, workType);
        }

        private void setNewActionText(Context context, List<Designer> orderDesignerInfos, String requirementStatus,
                                      final ClickCallBack
                                              clickCallBack, final int position) {
            switch (requirementStatus) {
                case Global.REQUIREMENT_STATUS0:
                    ltm_req_gotopro.setTextColor(context.getResources().getColor(R.color.orange_color));
                    ltm_req_gotopro.setText(context.getResources().getString(R.string.str_goto_order_high_point));
                    ltm_req_gotopro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickCallBack.click(position, XuQiuFragment.ITEM_GOTOODERDESI);
                        }
                    });
                    break;
                case Global.REQUIREMENT_STATUS5:
                case Global.REQUIREMENT_STATUS8:
                    ltm_req_gotopro.setTextColor(context.getResources().getColor(R.color.orange_color));
                    ltm_req_gotopro.setText(context.getResources().getString(R.string.str_goto_pro));
                    ltm_req_gotopro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickCallBack.click(position, XuQiuFragment.ITEM_GOTOPRO);
                        }
                    });
                    break;
                case Global.REQUIREMENT_STATUS7:
                case Global.REQUIREMENT_STATUS4:
                    ltm_req_gotopro.setTextColor(context.getResources().getColor(R.color.orange_color));
                    ltm_req_gotopro.setText(context.getResources().getString(R.string.str_view_contract));
                    ltm_req_gotopro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickCallBack.click(position, XuQiuFragment.ITEM_GOTOCONTRACT);
                        }
                    });
                    break;
                default:
                    setHasNewAction(context, orderDesignerInfos, clickCallBack, position);
                    break;

            }
        }

        private void setHasNewAction(Context context, List<Designer> orderDesignerInfos, final ClickCallBack
                clickCallBack, final int position) {
            boolean isHasNewAction = false;
            for (int i = 0; i < orderDesignerInfos.size(); i++) {
                String planStatus = orderDesignerInfos.get(i).getPlan().getStatus();
                if (planStatus.equals(Global.PLAN_STATUS2) || planStatus.equals(Global.PLAN_STATUS3)) {
                    isHasNewAction = true;
                    break;
                }
            }
            if (isHasNewAction) {
                ltm_req_gotopro.setTextColor(context.getResources().getColor(R.color.orange_color));
                ltm_req_gotopro.setText(context.getResources().getString(R.string.str_designer_new_action));
                ltm_req_gotopro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickCallBack.click(position, XuQiuFragment.ITEM_GOTOMYDESI);
                    }
                });
            } else {
                if(mRequirement.getWork_type().equals(RequirementBusiness.WORK_TYPE_PURE_DESIGNER)){
                    ltm_req_gotopro_layout.setVisibility(View.GONE);
                }else{
                    ltm_req_gotopro.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    ltm_req_gotopro.setText(context.getResources().getString(R.string.str_preview_pro));
                    ltm_req_gotopro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickCallBack.click(position, XuQiuFragment.ITEM_GOTOPRO);
                        }
                    });
                }
            }
        }

        private void setDesignerItemLayout(Context context, Requirement requirementInfo, final ClickCallBack
                clickCallBack, final int position, String requirementStatus, String workType) {
            List<Designer> orderDesignerInfos = requirementInfo.getOrder_designers();
            if (orderDesignerInfos != null) {
                int size = orderDesignerInfos.size();
                if (size > 0) {
                    Designer orderDesignerInfo = orderDesignerInfos.get(0);
                    highPointView.setVisibility(View.VISIBLE);
                    if (orderDesignerInfo.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
                        authView.setVisibility(View.VISIBLE);
                    } else {
                        authView.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(orderDesignerInfo.getUsername())) {
                        nameView.setText(orderDesignerInfo.getUsername());
                    } else {
                        nameView.setText(context.getResources().getString(R.string.designer_high_point));
                    }
                    if (!TextUtils.isEmpty(orderDesignerInfo.getImageid())) {
                        imageShow.displayImageHeadWidthThumnailImage(context, orderDesignerInfo
                                .getImageid(), headView);
                    } else {
                        headView.setImageResource(R.mipmap.icon_default_head);
                    }

                    String status = orderDesignerInfo.getPlan().getStatus();
                    switch (status) {
                        case Global.PLAN_STATUS0:
                            statusView.setTextColor(context.getResources().getColor(R.color.blue_color));
                            statusView.setText(context.getResources().getString(R.string.str_wait_respond));
                            break;
                        case Global.PLAN_STATUS1:
                            statusView.setTextColor(
                                    context.getResources().getColor(R.color.middle_grey_color));
                            statusView.setText(context.getResources().getString(R.string.str_refuse));
                            break;
                        case Global.PLAN_STATUS2:
                            statusView.setTextColor(context.getResources().getColor(R.color.blue_color));
                            if (Calendar.getInstance().getTimeInMillis() > orderDesignerInfo.getPlan()
                                    .getHouse_check_time()) {
                                statusView.setText(context.getResources().getString(R.string
                                        .str_wait_confirm_measure_house));
                            } else {
                                statusView.setText(context.getResources().getString(R.string
                                        .str_wait_measure_house));
                            }
                            break;
                        case Global.PLAN_STATUS3:
                            statusView.setTextColor(context.getResources().getColor(R.color.blue_color));
                            statusView.setText(context.getResources().getString(R.string.str_wait_confirm_plan));
                            break;
                        case Global.PLAN_STATUS4:
                            statusView.setTextColor(context.getResources().getColor(R.color.middle_grey_color));
                            statusView.setText(context.getResources().getString(R.string.str_not_choose));
                            break;
                        case Global.PLAN_STATUS5:
                            switch (requirementStatus) {
                                case Global.REQUIREMENT_STATUS1:
                                    break;
                                case Global.REQUIREMENT_STATUS7:
                                    statusView.setTextColor(context.getResources().getColor(R.color.blue_color));
                                    statusView.setText(context.getResources().getString(R.string
                                            .str_wait_confirm_constract));
                                    break;
                                case Global.REQUIREMENT_STATUS2:
                                    break;
                                case Global.REQUIREMENT_STATUS4:
                                    if (workType.equals(Global.PURE_DESIGNER)) {
                                        statusView.setTextColor(context.getResources().getColor(R.color
                                                .green_color));
                                        statusView.setText(context.getResources().getString(R.string
                                                .str_finish));
                                    } else {
                                        statusView.setTextColor(context.getResources().getColor(R.color
                                                .blue_color));
                                        statusView.setText(context.getResources().getString(R.string
                                                .str_wait_setting_constract));
                                    }
                                    break;
                                case Global.REQUIREMENT_STATUS3:
                                    break;
                                case Global.REQUIREMENT_STATUS5:
                                    statusView.setTextColor(context.getResources().getColor(R.color.orange_color));
                                    statusView.setText(context.getResources().getString(R.string
                                            .str_working));
                                    break;
                                case Global.REQUIREMENT_STATUS6:
                                    break;
                                case Global.REQUIREMENT_STATUS8:
                                    statusView.setTextColor(context.getResources().getColor(R.color.green_color));
                                    statusView.setText(context.getResources().getString(R.string
                                            .str_done));
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case Global.PLAN_STATUS6:
                            statusView.setTextColor(context.getResources().getColor(R.color.blue_color));
                            statusView.setText(context.getResources().getString(R.string.str_wait_upload_plan));
                            break;
                        case Global.PLAN_STATUS7:
                            break;
                        case Global.PLAN_STATUS8:
                            statusView.setTextColor(context.getResources().getColor(R.color.middle_grey_color));
                            statusView.setText(context.getResources().getString(R.string.str_no_submit));
                            break;
                        case Global.PLAN_STATUS9:
                            statusView.setTextColor(context.getResources().getColor(R.color.middle_grey_color));
                            statusView.setText(context.getResources().getString(R.string.str_out_date));
                            break;
                        default:
                            break;
                    }
                    designerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickCallBack.click(position, XuQiuFragment.ITEM_GOTOMYDESI);
                        }
                    });
                }
            } else {
                highPointView.setVisibility(View.GONE);
                nameView.setText(context.getResources().getString(R.string.designer_high_point));
                headView.setImageResource(R.mipmap.icon_add_high_point);
                statusView.setText(context.getResources().getString(R.string.str_not_order));
                statusView.setTextColor(context.getResources().getColor(R.color.middle_grey_color));
                designerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickCallBack.click(position, XuQiuFragment.ITEM_GOTOODERDESI);
                    }
                });
                authView.setVisibility(View.GONE);
            }
        }

    }

    public static class RequirementView extends RecyclerView.ViewHolder {

        private ImageShow imageShow;
        private View rootView;

        public RequirementView(View view) {
            super(view);
            ButterKnife.bind(this, view);

            rootView = view;
            imageShow = ImageShow.getImageShow();
        }

        private View getRootView() {
            return rootView;
        }

        @Bind(R.id.ltm_req_baseinfo_layout)
        RelativeLayout ltm_req_baseinfo_layout;
        @Bind(R.id.ltm_req_cell)
        protected TextView ltm_req_cell;
        @Bind(R.id.ltm_req_starttime_cont)
        protected TextView ltm_req_starttime_cont;
        @Bind(R.id.ltm_req_updatetime_cont)
        protected TextView ltm_req_updatetime_cont;
        @Bind(R.id.ltm_req_owner_head)
        protected ImageView ltm_req_owner_head;
        @Bind(R.id.ltm_req_gotopro)
        protected TextView ltm_req_gotopro;
        @Bind(R.id.ltm_req_gotopro_layout)
        protected LinearLayout ltm_req_gotopro_layout;

        private Requirement mRequirement;

        public void bind(Context context, Requirement requirementInfo, final ClickCallBack clickCallBack, final int
                position) {
            mRequirement = requirementInfo;
            String cellPhase = requirementInfo.getBasic_address();
            if (!TextUtils.isEmpty(cellPhase)) {
                ltm_req_cell.setText(cellPhase);
            }
            ltm_req_starttime_cont.setText(DateFormatTool.longToString(requirementInfo.getCreate_at()));
            ltm_req_updatetime_cont.setText(DateFormatTool.longToString(requirementInfo.getLast_status_update_time
                    ()));
            DataManagerNew dataManagerNew = DataManagerNew.getInstance();
            if (!dataManagerNew.getUserImagePath().contains(Constant.DEFALUT_PIC_HEAD)) {
                imageShow.displayImageHeadWidthThumnailImage(context, dataManagerNew.getUserImagePath(),
                        ltm_req_owner_head);
            } else {
                ltm_req_owner_head.setImageResource(R.mipmap.icon_default_head);
            }
            String requirementStatus = requirementInfo.getStatus();
            String workType = requirementInfo.getWork_type();

            if (requirementStatus.equals(Global.REQUIREMENT_STATUS0)) {
                ltm_req_baseinfo_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickCallBack.click(position, XuQiuFragment.ITEM_EDIT);
                    }
                });

            } else {
                ltm_req_baseinfo_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickCallBack.click(position, XuQiuFragment.ITEM_PRIVIEW);
                    }
                });
            }

            ltm_req_gotopro_layout.setVisibility(View.VISIBLE);
            setNewActionText(context, requirementInfo.getOrder_designers(), requirementStatus, clickCallBack,
                    position);

            setDesignerItemLayout(context, requirementInfo, clickCallBack, position, requirementStatus, workType);
        }

        private void setNewActionText(Context context, List<Designer> orderDesignerInfos, String requirementStatus,
                                      final ClickCallBack
                                              clickCallBack, final int position) {
            switch (requirementStatus) {
                case Global.REQUIREMENT_STATUS0:
                    ltm_req_gotopro.setTextColor(context.getResources().getColor(R.color.orange_color));
                    ltm_req_gotopro.setText(context.getResources().getString(R.string.str_goto_order));
                    ltm_req_gotopro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickCallBack.click(position, XuQiuFragment.ITEM_GOTOODERDESI);
                        }
                    });
                    break;
                case Global.REQUIREMENT_STATUS5:
                case Global.REQUIREMENT_STATUS8:
                    ltm_req_gotopro.setTextColor(context.getResources().getColor(R.color.orange_color));
                    ltm_req_gotopro.setText(context.getResources().getString(R.string.str_goto_pro));
                    ltm_req_gotopro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickCallBack.click(position, XuQiuFragment.ITEM_GOTOPRO);
                        }
                    });
                    break;
                case Global.REQUIREMENT_STATUS7:
                case Global.REQUIREMENT_STATUS4:
                    ltm_req_gotopro.setTextColor(context.getResources().getColor(R.color.orange_color));
                    ltm_req_gotopro.setText(context.getResources().getString(R.string.str_view_contract));
                    ltm_req_gotopro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickCallBack.click(position, XuQiuFragment.ITEM_GOTOCONTRACT);
                        }
                    });
                    break;
                default:
                    setHasNewAction(context, orderDesignerInfos, clickCallBack, position);
                    break;
            }
        }

        private void setHasNewAction(Context context, List<Designer> orderDesignerInfos, final ClickCallBack
                clickCallBack, final int position) {
            boolean isHasNewAction = false;
            for (int i = 0; i < orderDesignerInfos.size(); i++) {
                String planStatus = orderDesignerInfos.get(i).getPlan().getStatus();
                if (planStatus.equals(Global.PLAN_STATUS2) || planStatus.equals(Global.PLAN_STATUS3)) {
                    isHasNewAction = true;
                    break;
                }
            }
            if (isHasNewAction) {
                ltm_req_gotopro.setTextColor(context.getResources().getColor(R.color.orange_color));
                ltm_req_gotopro.setText(context.getResources().getString(R.string.str_designer_new_action));
                ltm_req_gotopro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickCallBack.click(position, XuQiuFragment.ITEM_GOTOMYDESI);
                    }
                });
            } else {
                if(mRequirement.getWork_type().equals(RequirementBusiness.WORK_TYPE_PURE_DESIGNER)){
                    ltm_req_gotopro_layout.setVisibility(View.GONE);
                }else {
                    ltm_req_gotopro.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    ltm_req_gotopro.setText(context.getResources().getString(R.string.str_preview_pro));
                    ltm_req_gotopro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickCallBack.click(position, XuQiuFragment.ITEM_GOTOPRO);
                        }
                    });
                }
            }
        }

        private void setDesignerItemLayout(Context context, Requirement requirementInfo, final ClickCallBack
                clickCallBack, final int position, String requirementStatus, String workType) {
            List<Designer> orderDesignerInfos = requirementInfo.getOrder_designers();
            if (orderDesignerInfos != null) {
                int size = orderDesignerInfos.size();
                for (int i = 0; i < Constant.REC_DESIGNER_TOTAL; i++) {
                    RelativeLayout designerLayout = (RelativeLayout) getRootView().findViewById(context.getResources()
                            .getIdentifier("ltm_req_designer_layout" + i, "id", context.getPackageName()));
                    designerLayout.setAlpha(1.0f);
                    ImageView headView = (ImageView) getRootView().findViewById(context.getResources().getIdentifier
                            ("ltm_req_designer_head" + i, "id", context.getPackageName()));
                    TextView nameView = (TextView) getRootView().findViewById(context.getResources().getIdentifier
                            ("ltm_req_designer_name" + i, "id", context.getPackageName()));
                    TextView statusView = (TextView) getRootView().findViewById(context.getResources().getIdentifier
                            ("ltm_req_designer_status" + i, "id", context.getPackageName()));
                    ImageView authView = (ImageView) getRootView().findViewById(context.getResources().getIdentifier
                            ("designerinfo_auth" + i, "id", context.getPackageName()));
                    if (i < size) {
                        if (orderDesignerInfos.get(i).getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
                            authView.setVisibility(View.VISIBLE);
                        } else {
                            authView.setVisibility(View.GONE);
                        }
                        if (!TextUtils.isEmpty(orderDesignerInfos.get(i).getUsername())) {
                            nameView.setText(orderDesignerInfos.get(i).getUsername());
                        } else {
                            nameView.setText(context.getResources().getString(R.string.designer));
                        }
                        if (!TextUtils.isEmpty(orderDesignerInfos.get(i).getImageid())) {
                            imageShow.displayImageHeadWidthThumnailImage(context, orderDesignerInfos.get(i)
                                    .getImageid(), headView);
                        } else {
                            imageShow.displayLocalImage(Constant.DEFALUT_ADD_PIC, headView);
                        }
                        String status = orderDesignerInfos.get(i).getPlan().getStatus();
                        switch (status) {
                            case Global.PLAN_STATUS0:
                                statusView.setTextColor(context.getResources().getColor(R.color.blue_color));
                                statusView.setText(context.getResources().getString(R.string.str_wait_respond));
                                break;
                            case Global.PLAN_STATUS1:
                                statusView.setTextColor(
                                        context.getResources().getColor(R.color.middle_grey_color));
                                statusView.setText(context.getResources().getString(R.string.str_refuse));
                                break;
                            case Global.PLAN_STATUS2:
                                statusView.setTextColor(context.getResources().getColor(R.color.blue_color));
                                if (Calendar.getInstance().getTimeInMillis() > orderDesignerInfos.get(i).getPlan()
                                        .getHouse_check_time()) {
                                    statusView.setText(context.getResources().getString(R.string
                                            .str_wait_confirm_measure_house));
                                } else {
                                    statusView.setText(context.getResources().getString(R.string
                                            .str_wait_measure_house));
                                }
                                break;
                            case Global.PLAN_STATUS3:
                                statusView.setTextColor(context.getResources().getColor(R.color.blue_color));
                                statusView.setText(context.getResources().getString(R.string.str_wait_confirm_plan));
                                break;
                            case Global.PLAN_STATUS4:
                                statusView.setTextColor(context.getResources().getColor(R.color.middle_grey_color));
                                statusView.setText(context.getResources().getString(R.string.str_not_choose));
                                break;
                            case Global.PLAN_STATUS5:
                                switch (requirementStatus) {
                                    case Global.REQUIREMENT_STATUS1:
                                        break;
                                    case Global.REQUIREMENT_STATUS7:
                                        statusView.setTextColor(context.getResources().getColor(R.color.blue_color));
                                        statusView.setText(context.getResources().getString(R.string
                                                .str_wait_confirm_constract));
                                        break;
                                    case Global.REQUIREMENT_STATUS2:
                                        break;
                                    case Global.REQUIREMENT_STATUS4:
                                        if (workType.equals(Global.PURE_DESIGNER)) {
                                            statusView.setTextColor(context.getResources().getColor(R.color
                                                    .green_color));
                                            statusView.setText(context.getResources().getString(R.string
                                                    .str_finish));
                                        } else {
                                            statusView.setTextColor(context.getResources().getColor(R.color
                                                    .blue_color));
                                            statusView.setText(context.getResources().getString(R.string
                                                    .str_wait_setting_constract));
                                        }
                                        break;
                                    case Global.REQUIREMENT_STATUS3:
                                        break;
                                    case Global.REQUIREMENT_STATUS5:
                                        statusView.setTextColor(context.getResources().getColor(R.color.orange_color));
                                        statusView.setText(context.getResources().getString(R.string
                                                .str_working));
                                        break;
                                    case Global.REQUIREMENT_STATUS6:
                                        break;
                                    case Global.REQUIREMENT_STATUS8:
                                        statusView.setTextColor(context.getResources().getColor(R.color.green_color));
                                        statusView.setText(context.getResources().getString(R.string
                                                .str_done));
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case Global.PLAN_STATUS6:
                                statusView.setTextColor(context.getResources().getColor(R.color.blue_color));
                                statusView.setText(context.getResources().getString(R.string.str_wait_upload_plan));
                                break;
                            case Global.PLAN_STATUS7:
                                break;
                            case Global.PLAN_STATUS8:
                                statusView.setTextColor(context.getResources().getColor(R.color.middle_grey_color));
                                statusView.setText(context.getResources().getString(R.string.str_no_submit));
                                break;
                            case Global.PLAN_STATUS9:
                                statusView.setTextColor(context.getResources().getColor(R.color.middle_grey_color));
                                statusView.setText(context.getResources().getString(R.string.str_out_date));
                                break;
                            default:
                                break;
                        }
                        designerLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clickCallBack.click(position, XuQiuFragment.ITEM_GOTOMYDESI);
                            }
                        });
                    } else {
                        authView.setVisibility(View.GONE);
                        nameView.setText(context.getResources().getString(R.string.designer));
                        headView.setImageResource(R.mipmap.icon_add);
                        statusView.setText(context.getResources().getString(R.string.str_not_order));
                        statusView.setTextColor(context.getResources().getColor(R.color.middle_grey_color));
                        //需求已经选中方案之后就无法添加设计师
                        switch (requirementStatus) {
                            case Global.REQUIREMENT_STATUS0:
                            case Global.REQUIREMENT_STATUS1:
                            case Global.REQUIREMENT_STATUS2:
                            case Global.REQUIREMENT_STATUS3:
                            case Global.REQUIREMENT_STATUS6:
                                designerLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        clickCallBack.click(position, XuQiuFragment.ITEM_GOTOODERDESI);
                                    }
                                });
                                break;
                            case Global.REQUIREMENT_STATUS4:
                            case Global.REQUIREMENT_STATUS5:
                            case Global.REQUIREMENT_STATUS7:
                            case Global.REQUIREMENT_STATUS8:
                                designerLayout.setOnClickListener(null);
                                designerLayout.setAlpha(0.3f);
                                break;
                        }
                    }
                }
            } else {
                for (int i = 0; i < Constant.REC_DESIGNER_TOTAL; i++) {
                    RelativeLayout designerLayout = (RelativeLayout) getRootView().findViewById(context.getResources()
                            .getIdentifier("ltm_req_designer_layout" + i, "id", context.getPackageName()));
                    designerLayout.setAlpha(1.0f);
                    ImageView headView = (ImageView) getRootView().findViewById(context.getResources().getIdentifier
                            ("ltm_req_designer_head" + i, "id", context.getPackageName()));
                    TextView nameView = (TextView) getRootView().findViewById(context.getResources().getIdentifier
                            ("ltm_req_designer_name" + i, "id", context.getPackageName()));
                    TextView statusView = (TextView) getRootView().findViewById(context.getResources().getIdentifier
                            ("ltm_req_designer_status" + i, "id", context.getPackageName()));
                    ImageView authView = (ImageView) getRootView().findViewById(context.getResources().getIdentifier
                            ("designerinfo_auth" + i, "id", context.getPackageName()));
                    nameView.setText(context.getResources().getString(R.string.designer));
                    headView.setImageResource(R.mipmap.icon_add);
                    statusView.setText(context.getResources().getString(R.string.str_not_order));
                    statusView.setTextColor(context.getResources().getColor(R.color.middle_grey_color));
                    designerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickCallBack.click(position, XuQiuFragment.ITEM_GOTOODERDESI);
                        }
                    });
                    authView.setVisibility(View.GONE);
                }
            }
        }

    }

}
