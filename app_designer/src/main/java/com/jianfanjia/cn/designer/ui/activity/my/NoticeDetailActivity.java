package com.jianfanjia.cn.designer.ui.activity.my;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.model.UserMessage;
import com.jianfanjia.api.request.common.AgreeRescheduleRequest;
import com.jianfanjia.api.request.common.GetMsgDetailRequest;
import com.jianfanjia.api.request.common.RefuseRescheduleRequest;
import com.jianfanjia.api.request.designer.RefuseRequirementRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.business.ProcessBusiness;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.ui.Event.UpdateEvent;
import com.jianfanjia.cn.designer.ui.activity.SettingMeasureDateActivity;
import com.jianfanjia.cn.designer.ui.activity.common.WebViewActivity;
import com.jianfanjia.cn.designer.ui.activity.requirement.PreviewBusinessRequirementActivity;
import com.jianfanjia.cn.designer.ui.activity.requirement.PreviewDesignerPlanActivity;
import com.jianfanjia.cn.designer.ui.activity.requirement.PreviewHomeRequirementActivity;
import com.jianfanjia.cn.designer.ui.interf.RefuseRequirementCallback;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.common.tool.DateFormatTool;
import com.jianfanjia.common.tool.LogTool;
import de.greenrobot.event.EventBus;

/**
 * Description:通知详情
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class NoticeDetailActivity extends BaseSwipeBackActivity implements View.OnClickListener {
    private static final String TAG = NoticeDetailActivity.class.getName();
    @Bind(R.id.my_notice_detail_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.typeText)
    TextView typeText;

    @Bind(R.id.cellText)
    TextView cellText;

    @Bind(R.id.sectionText)
    TextView sectionText;

    @Bind(R.id.dateText)
    TextView dateText;

    @Bind(R.id.contentView)
    WebView contentView;

    @Bind(R.id.doubleBtnLayout)
    LinearLayout doubleBtnLayout;

    @Bind(R.id.singleBtnLayout)
    LinearLayout singleBtnLayout;

    @Bind(R.id.appointBtnLayout)
    LinearLayout appointBtnLayout;

    @Bind(R.id.btnAgree)
    Button btnAgree;

    @Bind(R.id.btnReject)
    Button btnReject;

    @Bind(R.id.btnPlan)
    Button btnPlan;

    @Bind(R.id.btnRefuse)
    Button btnRefuse;

    @Bind(R.id.btnRespond)
    Button btnRespond;

    @Bind(R.id.btnConfirm)
    Button btnConfirm;

    private String messageid = null;
    private String processid = null;
    private Plan plan = null;
    private Requirement requirement = null;

    private String phone = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        Intent intent = this.getIntent();
        Bundle planBundle = intent.getExtras();
        messageid = planBundle.getString(Global.MSG_ID);
        LogTool.d("messageid=" + messageid);
        initMainHeadView();
        contentView.getSettings().setJavaScriptEnabled(true);
        contentView.setBackgroundColor(0); // 设置背景色
        contentView.setWebChromeClient(new WebChromeClient());
        getNoticeDetailInfo(messageid);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.my_notice_detail));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitle(getResources().getString(R.string.checkRequire));
        mainHeadView.setRightTitleVisable(View.GONE);
    }

    @OnClick({R.id.head_back_layout, R.id.btnAgree, R.id.btnReject, R.id.btnCheck, R.id.btnPlan, R.id.btnContract, R
            .id.btnRefuse, R.id.btnRespond, R.id.btnConfirm, R.id.head_right_title})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.btnAgree:
                agreeReschedule(processid);
                break;
            case R.id.btnReject:
                refuseReschedule(processid);
                break;
            case R.id.btnCheck:
                break;
            case R.id.btnPlan:
                Bundle planBundle = new Bundle();
                planBundle.putSerializable(Global.PLAN_DETAIL, plan);
                planBundle.putSerializable(Global.REQUIREMENT_INFO, requirement);
                startActivity(PreviewDesignerPlanActivity.class, planBundle);
                break;
            case R.id.btnContract:
                break;
            case R.id.btnRefuse:
                showRefuseDialog(requirement.get_id());
                break;
            case R.id.btnRespond:
                Intent settingHouseTimeIntent = new Intent(NoticeDetailActivity.this
                        , SettingMeasureDateActivity.class);
                Bundle settingHouseTimeBundle = new Bundle();
                settingHouseTimeBundle.putString(Global.REQUIREMENT_ID, requirement.get_id());
                settingHouseTimeBundle.putString(Global.PHONE, phone);
                settingHouseTimeIntent.putExtras(settingHouseTimeBundle);
                startActivity(settingHouseTimeIntent);
                overridePendingTransition(R.anim.slide_and_fade_in_from_bottom, R.anim.fade_out);
                break;
            case R.id.btnConfirm:
                appManager.finishActivity(this);
                break;
            case R.id.head_right_title:
                Intent gotoPriviewRequirement = null;
                if (requirement.getDec_type().equals(Global.DEC_TYPE_BUSINESS)) {
                    gotoPriviewRequirement = new Intent(NoticeDetailActivity.this, PreviewBusinessRequirementActivity
                            .class);
                } else {
                    gotoPriviewRequirement = new Intent(NoticeDetailActivity.this, PreviewHomeRequirementActivity.class);
                }
                gotoPriviewRequirement.putExtra(Global.REQUIREMENT_INFO, requirement);
                startActivity(gotoPriviewRequirement);
                break;
            default:
                break;
        }
    }

    //获取详情
    private void getNoticeDetailInfo(String messageid) {
        GetMsgDetailRequest request = new GetMsgDetailRequest();
        request.setMessageid(messageid);
        Api.getUserMsgDetail(request, new ApiCallback<ApiResponse<UserMessage>>() {
            @Override
            public void onPreLoad() {
                Hud.show(getUiContext());
            }

            @Override
            public void onHttpDone() {
                Hud.dismiss();
            }

            @Override
            public void onSuccess(ApiResponse<UserMessage> apiResponse) {
                UserMessage noticeDetailInfo = apiResponse.getData();
                if (null != noticeDetailInfo) {
                    String msgType = noticeDetailInfo.getMessage_type();
                    ProcessSection processSection = ProcessBusiness.getSectionInfoByName(noticeDetailInfo
                                    .getProcess(),
                            noticeDetailInfo.getSection());
                    LogTool.d("msgType:" + msgType);
                    if (msgType.equals(Constant.TYPE_DELAY_MSG)) {
                        typeText.setBackgroundResource(R.drawable.site_detail_text_bg_border);
                        processid = noticeDetailInfo.getProcessid();
                        LogTool.d("processid=" + processid);
                        doubleBtnLayout.setVisibility(View.VISIBLE);
                        singleBtnLayout.setVisibility(View.GONE);
                        typeText.setText(getResources().getString(R.string.delay_str));
                        cellText.setText(noticeDetailInfo.getProcess().getBasic_address());
                        sectionText.setVisibility(View.VISIBLE);
                        sectionText.setText(processSection.getLabel()
                                + "阶段");
                        if (noticeDetailInfo.getReschedule().getStatus().equals(Constant.YANQI_AGREE)) {
                            btnAgree.setText(getResources().getString(R.string.agree_str));
                            btnAgree.setEnabled(false);
                            btnReject.setVisibility(View.GONE);
                        } else if (noticeDetailInfo.getReschedule().getStatus().equals(Constant.YANQI_REFUSE)) {
                            btnReject.setText(getResources().getString(R.string.reject_str));
                            btnAgree.setVisibility(View.GONE);
                            btnReject.setEnabled(false);
                        } else {
                            btnAgree.setEnabled(true);
                            btnReject.setEnabled(true);
                        }
                    } else if (msgType.equals(Constant.TYPE_REJECT_DELAY_MSG) || msgType.equals(Constant
                            .TYPE_AGREE_DELAY_MSG)) {
                        typeText.setBackgroundResource(R.drawable.site_detail_text_bg_border);
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.VISIBLE);
                        typeText.setText(getResources().getString(R.string.delay_str));
                        cellText.setText(noticeDetailInfo.getProcess().getBasic_address());
                        sectionText.setVisibility(View.VISIBLE);
                        sectionText.setText(processSection.getLabel()
                                + "阶段");
                    } else if (msgType.equals(Constant.TYPE_CAIGOU_MSG)) {
                        typeText.setBackgroundResource(R.drawable.site_detail_text_bg_border);
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.VISIBLE);
                        typeText.setText(getResources().getString(R.string.caigou_str));
                        cellText.setText(noticeDetailInfo.getProcess().getBasic_address());
                        sectionText.setVisibility(View.VISIBLE);
                        sectionText.setText(processSection.getLabel()
                                + "阶段");
                    } else if (msgType.equals(Constant.TYPE_CONFIRM_CHECK_MSG)) {
                        typeText.setBackgroundResource(R.drawable.site_detail_text_bg_border);
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.VISIBLE);
                        typeText.setText(getResources().getString(R.string.check_str));
                        cellText.setText(noticeDetailInfo.getProcess().getBasic_address());
                        sectionText.setVisibility(View.VISIBLE);
                        sectionText.setText(processSection.getLabel()
                                + "阶段");
                    } else if (msgType.equals(Constant.TYPE_USER_APPOINT_MSG)) {
                        typeText.setBackgroundResource(R.drawable.req_detail_text_bg_border);
                        requirement = noticeDetailInfo.getRequirement();
                        phone = noticeDetailInfo.getUser().getPhone();
                        LogTool.d("requirement==" + requirement + " phone=" + phone);
                        typeText.setText(getResources().getString(R.string.req_str));
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        appointBtnLayout.setVisibility(View.VISIBLE);
                        mainHeadView.setRightTitleVisable(View.VISIBLE);
                        btnConfirm.setVisibility(View.GONE);
                        cellText.setText(noticeDetailInfo.getRequirement().getBasic_address());
                        sectionText.setVisibility(View.GONE);
                        if (noticeDetailInfo.getPlan().getStatus().equals(Global.PLAN_STATUS0)) {
                            btnRefuse.setEnabled(true);
                            btnRespond.setEnabled(true);
                        } else if (noticeDetailInfo.getPlan().getStatus().equals(Global.PLAN_STATUS1)) {
                            btnRefuse.setEnabled(false);
                            btnRespond.setVisibility(View.GONE);
                            btnRefuse.setText(getResources().getString(R.string.reject_str));
                        } else if (noticeDetailInfo.getPlan().getStatus().equals(Global.PLAN_STATUS2)) {
                            btnRespond.setEnabled(false);
                            btnRefuse.setVisibility(View.GONE);
                            btnRespond.setText(getResources().getString(R.string.repond_str));
                        } else {
                            appointBtnLayout.setVisibility(View.GONE);
                            btnConfirm.setVisibility(View.VISIBLE);
                        }
                    } else if (msgType.equals(Constant.TYPE_PLAN_CHOOSED_MSG) || msgType.equals(Constant
                            .TYPE_PLAN_NOT_CHOOSED_MSG)) {
                        typeText.setBackgroundResource(R.drawable.req_detail_text_bg_border);
                        plan = noticeDetailInfo.getPlan();
                        requirement = noticeDetailInfo.getRequirement();
                        LogTool.d("requirement==" + requirement + "   plan==" + plan);
                        typeText.setText(getResources().getString(R.string.req_str));
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnPlan.setVisibility(View.VISIBLE);
                        cellText.setText(noticeDetailInfo.getRequirement().getBasic_address());
                        sectionText.setVisibility(View.GONE);
                    } else if (msgType.equals(Constant.TYPE_SYSTEM_MSG)
                            || msgType.equals(Constant.TYPE_AUTH_TYPE_AGRAEE)
                            || msgType.equals(Constant.TYPE_AUTH_TYPE_DISGREE)
                            || msgType.equals(Constant.TYPE_UID_TYPE_AGRAEE)
                            || msgType.equals(Constant.TYPE_UID_TYPE_DISGRAEE)
                            || msgType.equals(Constant.TYPE_PROCESS_AGRAEE)
                            || msgType.equals(Constant.TYPE_PROCESS_DISGRAEE)
                            || msgType.equals(Constant.TYPE_PRODUCT_AGRAEE)
                            || msgType.equals(Constant.TYPE_PRODUCT_DISGRAEE)
                            || msgType.equals(Constant.TYPE_PRODUCT_OFFLINE)) {
                        typeText.setBackgroundResource(R.drawable.sys_detail_text_bg_border);
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.VISIBLE);
                        typeText.setText(getResources().getString(R.string.sys_str));
                    } else {
                        typeText.setBackgroundResource(R.drawable.req_detail_text_bg_border);
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.VISIBLE);
                        typeText.setText(getResources().getString(R.string.req_str));
                        cellText.setText(noticeDetailInfo.getRequirement().getBasic_address());
                        sectionText.setVisibility(View.GONE);
                    }
                    dateText.setText(DateFormatTool.getHumReadDateString(noticeDetailInfo.getCreate_at()));
                    contentView.loadDataWithBaseURL(null, noticeDetailInfo.getHtml(), "text/html", "utf-8", null);
                    contentView.setWebViewClient(new WebViewClient() {

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            if (url.startsWith("tel:")) {
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(url));
                                startActivity(intent);
                            } else if (url.startsWith("http:") || url.startsWith("https:")) {
                                WebViewActivity.intentToWebView(NoticeDetailActivity.this, url);
                            }
                            return true;
                        }

                    });
                }
            }

            @Override
            public void onFailed(ApiResponse<UserMessage> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.getMsg(code));
            }
        },this);
    }

    //同意改期
    private void agreeReschedule(String processid) {
        AgreeRescheduleRequest agreeRescheduleRequest = new AgreeRescheduleRequest();
        agreeRescheduleRequest.setProcessid(processid);
        Api.agreeReschedule(agreeRescheduleRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                btnAgree.setText(getResources().getString(R.string.agree_str));
                btnAgree.setEnabled(false);
                btnReject.setVisibility(View.GONE);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.getMsg(code));
            }
        },this);
    }

    // 拒绝改期
    private void refuseReschedule(String processid) {
        RefuseRescheduleRequest refuseRescheduleRequest = new RefuseRescheduleRequest();
        refuseRescheduleRequest.setProcessid(processid);
        Api.refuseReschedule(refuseRescheduleRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                btnReject.setText(getResources().getString(R.string.reject_str));
                btnAgree.setVisibility(View.GONE);
                btnReject.setEnabled(false);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.getMsg(code));
            }
        },this);
    }

    private void showRefuseDialog(final String requirementid) {
        UiHelper.showRefuseDialog(this, new RefuseRequirementCallback() {
            @Override
            public void refuseRequirementSuccess(String refuseReason) {
                refuseRequirement(requirementid, refuseReason);
            }
        });
    }

    private void refuseRequirement(String requirementid, String msg) {
        RefuseRequirementRequest request = new RefuseRequirementRequest();
        request.setRequirementid(requirementid);
        request.setReject_respond_msg(msg);
        Api.refuseRequirement(request, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
//                if (refuseDialog != null) {
//                    refuseDialog.dismiss();
//                }
                btnRefuse.setEnabled(false);
                btnRespond.setVisibility(View.GONE);
                btnRefuse.setText(getResources().getString(R.string.reject_str));
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.getMsg(code));
            }
        },this);
    }

    public void onEventMainThread(UpdateEvent event) {
        LogTool.d("UpdateEvent event");
        getNoticeDetailInfo(messageid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_detail;
    }
}
