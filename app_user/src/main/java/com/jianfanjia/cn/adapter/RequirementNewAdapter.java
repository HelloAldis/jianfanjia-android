package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.RecyclerViewAdapterBase;
import com.jianfanjia.cn.business.DataManagerNew;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.fragment.XuQiuFragment;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.tools.ImageShow;
import com.jianfanjia.cn.tools.StringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    private DataManagerNew dataManagerNew;

    public RequirementNewAdapter(Context context, ClickCallBack cickCallBack) {
        this.context = context;
        this.clickCallBack = cickCallBack;
        dataManagerNew = DataManagerNew.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_req, null);
        return new RequirementView(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RequirementView requirementView = (RequirementView) holder;
        requirementView.bind(context, items.get(position), clickCallBack, position);
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
        @Bind(R.id.ltm_req_status)
        protected TextView ltm_req_status;
        @Bind(R.id.ltm_req_owner_head)
        protected ImageView ltm_req_owner_head;
        @Bind(R.id.ltm_req_gotopro)
        protected TextView ltm_req_gotopro;
        @Bind(R.id.ltm_req_designer_head0)
        protected ImageView ltm_req_designer_head0;
        @Bind(R.id.ltm_req_designer_name0)
        protected TextView ltm_req_designer_name0;
        @Bind(R.id.ltm_req_designer_status0)
        protected TextView ltm_req_designer_status0;
        @Bind(R.id.ltm_req_designer_head1)
        protected ImageView ltm_req_designer_head1;
        @Bind(R.id.ltm_req_designer_name1)
        protected TextView ltm_req_designer_name1;
        @Bind(R.id.ltm_req_designer_status1)
        protected TextView ltm_req_designer_status1;
        @Bind(R.id.ltm_req_designer_head2)
        protected ImageView ltm_req_designer_head2;
        @Bind(R.id.ltm_req_designer_name2)
        protected TextView ltm_req_designer_name2;
        @Bind(R.id.ltm_req_designer_status2)
        protected TextView ltm_req_designer_status2;

        public void bind(Context context, Requirement requirementInfo, final ClickCallBack clickCallBack, final int
                position) {
            String cellPhase = requirementInfo.getBasic_address();
            if (!TextUtils.isEmpty(cellPhase)) {
                ltm_req_cell.setText(cellPhase);
            }
            ltm_req_starttime_cont.setText(StringUtils.covertLongToString(requirementInfo.getCreate_at()));
            ltm_req_updatetime_cont.setText(StringUtils.covertLongToString(requirementInfo.getLast_status_update_time
                    ()));
            ltm_req_status.setText(context.getResources().getStringArray(R.array.requirement_status)[Integer.parseInt
                    (requirementInfo.getStatus())]);
            DataManagerNew dataManagerNew = DataManagerNew.getInstance();
            if (!dataManagerNew.getUserImagePath().contains(Constant.DEFALUT_PIC_HEAD)) {
                imageShow.displayImageHeadWidthThumnailImage(context, dataManagerNew.getUserImagePath(),
                        ltm_req_owner_head);
            } else {
                ltm_req_owner_head.setImageResource(R.mipmap.icon_default_head);
            }
            String requirementStatus = requirementInfo.getStatus();
            String workType = requirementInfo.getWork_type();
            if (workType.equals(Global.PURE_DESIGNER)) {
                ltm_req_gotopro.setVisibility(View.GONE);
            } else {
                ltm_req_gotopro.setVisibility(View.VISIBLE);
            }

            if (requirementStatus.equals(Global.REQUIREMENT_STATUS5) || requirementStatus.equals(Global
                    .REQUIREMENT_STATUS8)) {
                ltm_req_gotopro.setText(context.getResources().getString(R.string.str_goto_pro));
                ltm_req_gotopro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickCallBack.click(position, XuQiuFragment.ITEM_GOTOPRO);
                    }
                });
            } else if (requirementStatus.equals(Global.REQUIREMENT_STATUS0)) {
                ltm_req_gotopro.setText(context.getResources().getString(R.string.str_goto_order));
                ltm_req_gotopro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickCallBack.click(position, XuQiuFragment.ITEM_GOTOODERDESI);
                    }
                });
            } else {
                ltm_req_gotopro.setText(context.getResources().getString(R.string.str_preview_pro));
                ltm_req_gotopro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickCallBack.click(position, XuQiuFragment.ITEM_GOTOPRO);
                    }
                });
            }
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
            switch (requirementStatus) {
                case Global.REQUIREMENT_STATUS1:
                case Global.REQUIREMENT_STATUS7:
                    ltm_req_status.setTextColor(context.getResources().getColor(R.color.green_color));
                    break;
                case Global.REQUIREMENT_STATUS2:
                    ltm_req_status.setTextColor(context.getResources().getColor(R.color.blue_color));
                    break;
                case Global.REQUIREMENT_STATUS4:
                    ltm_req_status.setTextColor(context.getResources().getColor(R.color.orange_color));
                    if (requirementInfo.getWork_type().equals(Global.PURE_DESIGNER)) {
                        ltm_req_status.setText(context.getResources().getString(R.string.already_finish));
                    }
                    break;
                case Global.REQUIREMENT_STATUS3:
                case Global.REQUIREMENT_STATUS5:
                case Global.REQUIREMENT_STATUS6:
                case Global.REQUIREMENT_STATUS8:
                    ltm_req_status.setTextColor(context.getResources().getColor(R.color.orange_color));
                    break;
                default:
                    ltm_req_status.setTextColor(context.getResources().getColor(R.color.middle_grey_color));
                    break;
            }

            List<Designer> recDesignerInfos = requirementInfo.getRec_designers();
            List<Designer> orderDesignerInfos = requirementInfo.getOrder_designers();
            if (orderDesignerInfos != null) {
                int size = orderDesignerInfos.size();
                for (int i = 0; i < Constant.REC_DESIGNER_TOTAL; i++) {
                    RelativeLayout designerLayout = (RelativeLayout) getRootView().findViewById(context.getResources()
                            .getIdentifier("ltm_req_designer_layout" + i, "id", context.getPackageName()));
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
                        statusView.setText(context.getResources().getStringArray(R.array.plan_status)[Integer.parseInt
                                (status)]);
                        switch (status) {
                            case Global.PLAN_STATUS0:
                                statusView.setTextColor(context.getResources().getColor(R.color.green_color));
                                break;
                            case Global.PLAN_STATUS5:
                            case Global.PLAN_STATUS6:
                            case Global.PLAN_STATUS3:
                                statusView.setTextColor(context.getResources().getColor(R.color.orange_color));
                                break;
                            case Global.PLAN_STATUS2:
                                statusView.setTextColor(context.getResources().getColor(R.color.blue_color));
                                break;
                            default:
                                statusView.setTextColor(context.getResources().getColor(R.color.middle_grey_color));
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
                                break;
                        }

                    }
                }
            } else {
                for (int i = 0; i < Constant.REC_DESIGNER_TOTAL; i++) {
                    RelativeLayout designerLayout = (RelativeLayout) getRootView().findViewById(context.getResources()
                            .getIdentifier("ltm_req_designer_layout" + i, "id", context.getPackageName()));
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
