package com.jianfanjia.cn.ui.activity.my;

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
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.model.UserMessage;
import com.jianfanjia.api.request.common.AgreeRescheduleRequest;
import com.jianfanjia.api.request.common.GetMsgDetailRequest;
import com.jianfanjia.api.request.common.RefuseRescheduleRequest;
import com.jianfanjia.api.request.user.ConfirmMeasureHouseRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.business.ProcessBusiness;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.tools.BusinessCovertUtil;
import com.jianfanjia.cn.ui.Event.CheckEvent;
import com.jianfanjia.cn.ui.Event.ChoosedContractEvent;
import com.jianfanjia.cn.ui.Event.ChoosedPlanEvent;
import com.jianfanjia.cn.ui.activity.home.WebViewActivity;
import com.jianfanjia.cn.ui.activity.requirement.CheckActivity;
import com.jianfanjia.cn.ui.activity.requirement.ContractActivity;
import com.jianfanjia.cn.ui.activity.requirement.PreviewDesignerPlanActivity;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.common.tool.DateFormatTool;
import com.jianfanjia.common.tool.LogTool;
import de.greenrobot.event.EventBus;

/**
 * Description:通知详情
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class NoticeDetailActivity extends BaseSwipeBackActivity {
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

    @Bind(R.id.btnAgree)
    Button btnAgree;

    @Bind(R.id.btnReject)
    Button btnReject;

    @Bind(R.id.btnCheck)
    Button btnCheck;

    @Bind(R.id.btnPlan)
    Button btnPlan;

    @Bind(R.id.btnContract)
    Button btnContract;

    @Bind(R.id.btnCheckHouse)
    Button btnCheckHouse;

    @Bind(R.id.btnConfirm)
    Button btnConfirm;

    private String messageid = null;
    private String processid = null;
    private String sectionName = null;
    private String requirementid = null;
    private String designerid = null;
    private Process processInfo = null;
    private Plan planInfo = null;
    private Requirement requirement = null;
    private ProcessSection sectionInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        Intent intent = this.getIntent();
        Bundle planBundle = intent.getExtras();
        if (planBundle != null) {
            messageid = planBundle.getString(IntentConstant.MSG_ID);
            getNoticeDetailInfo(messageid);
            LogTool.d("messageid=" + messageid);
        }
        initMainHeadView();
        contentView.getSettings().setJavaScriptEnabled(true);
        contentView.setBackgroundColor(0); // 设置背景色
        contentView.setWebChromeClient(new WebChromeClient());

    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.my_notice_detail));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @OnClick({R.id.head_back_layout, R.id.btnAgree, R.id.btnReject, R.id.btnCheck, R.id.btnPlan, R.id.btnContract, R
            .id.btnCheckHouse, R.id.btnConfirm})
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
                Bundle checkBundle = new Bundle();
                checkBundle.putString(Constant.SECTION, sectionName);
                checkBundle.putSerializable(Constant.PROCESS_INFO, processInfo);
                checkBundle.putSerializable(Constant.SECTION_INFO, sectionInfo);
                checkBundle.putInt(CheckActivity.CHECK_INTENT_FLAG, CheckActivity.NOTICE_INTENT);
                Intent checkIntent = new Intent(NoticeDetailActivity.this, CheckActivity.class);
                checkIntent.putExtras(checkBundle);
                startActivity(checkIntent);
                break;
            case R.id.btnPlan:
                Bundle planBundle = new Bundle();
                planBundle.putSerializable(IntentConstant.PLAN_DETAIL, planInfo);
                planBundle.putSerializable(IntentConstant.REQUIREMENT_INFO, requirement);
                planBundle.putInt(PreviewDesignerPlanActivity.PLAN_INTENT_FLAG, PreviewDesignerPlanActivity
                        .NOTICE_INTENT);
                startActivity(PreviewDesignerPlanActivity.class, planBundle);
                break;
            case R.id.btnContract:
                Bundle contractBundle = new Bundle();
                contractBundle.putSerializable(IntentConstant.REQUIREMENT_INFO, requirement);
                contractBundle.putInt(ContractActivity.CONSTRACT_INTENT_FLAG, ContractActivity.NOTICE_INTENT);
                startActivity(ContractActivity.class, contractBundle);
                break;
            case R.id.btnCheckHouse:
                confirmMeasureHouse(requirementid, designerid);
                break;
            case R.id.btnConfirm:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(ChoosedPlanEvent choosedPlanEvent) {
        LogTool.d("ChoosedPlanEvent onEventMainThread");
        planInfo.setStatus(Global.PLAN_STATUS5);
    }

    public void onEventMainThread(ChoosedContractEvent choosedContractEvent) {
        LogTool.d("ChoosedContractEvent onEventMainThread");
        requirement.setStatus(Global.REQUIREMENT_STATUS5);
    }

    public void onEventMainThread(CheckEvent checkEvent) {
        LogTool.d("CheckEvent onEventMainThread");
        sectionInfo.setStatus(Constant.FINISHED);
    }

    private void getNoticeDetailInfo(String messageid) {
        GetMsgDetailRequest request = new GetMsgDetailRequest();
        request.setMessageid(messageid);
        Api.getNoticeDetail(request, new ApiCallback<ApiResponse<UserMessage>>() {
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
                LogTool.d("noticeDetailInfo=" + noticeDetailInfo);
                if (null != noticeDetailInfo) {
                    String msgType = noticeDetailInfo.getMessage_type();
                    ProcessSection processSection = ProcessBusiness.getSectionInfoByName(noticeDetailInfo
                                    .getProcess(),
                            noticeDetailInfo.getSection());
                    LogTool.d("msgType==================" + msgType);
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
                    } else if (msgType.equals(Constant.TYPE_DESIGNER_REJECT_DELAY_MSG) || msgType.equals(Constant
                            .TYPE_DESIGNER_AGREE_DELAY_MSG)) {
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
                    } else if (msgType.equals(Constant.TYPE_PAY_MSG)) {
                        typeText.setBackgroundResource(R.drawable.site_detail_text_bg_border);
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.VISIBLE);
                        typeText.setText(getResources().getString(R.string.pay_str));
                        cellText.setText(noticeDetailInfo.getProcess().getBasic_address());
                        sectionText.setVisibility(View.VISIBLE);
                        sectionText.setText(processSection.getLabel()
                                + "阶段");
                    } else if (msgType.equals(Constant.TYPE_CONFIRM_CHECK_MSG)) {
                        typeText.setBackgroundResource(R.drawable.site_detail_text_bg_border);
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnCheck.setVisibility(View.VISIBLE);
                        sectionName = noticeDetailInfo.getSection();
                        processInfo = noticeDetailInfo.getProcess();
                        LogTool.d("sectionName=" + sectionName + "  processInfo=" + processInfo);
                        sectionInfo = BusinessCovertUtil.getSectionInfoByName(processInfo.getSections(), sectionName);
                        LogTool.d("sectionInfo=" + sectionInfo);
                        typeText.setText(getResources().getString(R.string.check_str));
                        cellText.setText(noticeDetailInfo.getProcess().getBasic_address());
                        sectionText.setVisibility(View.VISIBLE);
                        sectionText.setText(processSection.getLabel()
                                + "阶段");
                    } else if (msgType.equals(Constant.TYPE_SYSTEM_MSG)) {
                        typeText.setBackgroundResource(R.drawable.sys_detail_text_bg_border);
                        typeText.setText(getResources().getString(R.string.sys_str));
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.VISIBLE);
                    } else if (msgType.equals(Constant.TYPE_DESIGNER_UPLOAD_PLAN_MSG)) {
                        typeText.setBackgroundResource(R.drawable.req_detail_text_bg_border);
                        planInfo = noticeDetailInfo.getPlan();
                        requirement = noticeDetailInfo.getRequirement();
                        LogTool.d("requirement==" + requirement + "   planInfo==" + planInfo);
                        typeText.setText(getResources().getString(R.string.req_str));
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.VISIBLE);
                        cellText.setText(noticeDetailInfo.getRequirement().getBasic_address());
                        sectionText.setVisibility(View.GONE);
                    } else if (msgType.equals(Constant.TYPE_DESIGNER_CONFIG_CONTRACT_MSG)) {
                        typeText.setBackgroundResource(R.drawable.req_detail_text_bg_border);
                        requirement = noticeDetailInfo.getRequirement();
                        LogTool.d("requirement=" + requirement);
                        typeText.setText(getResources().getString(R.string.req_str));
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.VISIBLE);
                        cellText.setText(requirement.getBasic_address());
                        sectionText.setVisibility(View.GONE);
                    } else if (msgType.equals(Constant.TYPE_DESIGNER_REMIND_USER_HOUSE_CHECK_MSG)) {
                        typeText.setBackgroundResource(R.drawable.req_detail_text_bg_border);
                        planInfo = noticeDetailInfo.getPlan();
                        requirementid = noticeDetailInfo.getRequirementid();
                        designerid = noticeDetailInfo.getDesignerid();
                        LogTool.d("planInfo:" + planInfo + " requirementid:" + requirementid + " designerid:" +
                                designerid);
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.VISIBLE);
                        typeText.setText(getResources().getString(R.string.req_str));
                        cellText.setText(noticeDetailInfo.getRequirement().getBasic_address());
                        sectionText.setVisibility(View.GONE);
                        if (planInfo.getStatus().equals(Global.PLAN_STATUS2)) {
                            btnCheckHouse.setText(getResources().getString(R.string.confirm_measure_house));
                            btnCheckHouse.setEnabled(true);
                        } else if (planInfo.getStatus().equals(Global.PLAN_STATUS6)) {
                            btnCheckHouse.setText(getResources().getString(R.string.already_measure));
                            btnCheckHouse.setEnabled(false);
                        }
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
        }, this);
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
        }, this);

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
        }, this);
    }

    //确认量房
    private void confirmMeasureHouse(String requirementid, String designerid) {
        ConfirmMeasureHouseRequest confirmMeasureHouseRequest = new ConfirmMeasureHouseRequest();
        confirmMeasureHouseRequest.setRequirementid(requirementid);
        confirmMeasureHouseRequest.setDesignerid(designerid);
        Api.confirmMeasureHouse(confirmMeasureHouseRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                btnCheckHouse.setText(getResources().getString(R.string.already_measure));
                btnCheckHouse.setEnabled(false);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.getMsg(code));
            }
        }, this);

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
