package com.jianfanjia.cn.designer.activity.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.SettingMeasureDateActivity_;
import com.jianfanjia.cn.designer.activity.requirement.PreviewDesignerPlanActivity;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.bean.NoticeDetailInfo;
import com.jianfanjia.cn.designer.bean.PlanInfo;
import com.jianfanjia.cn.designer.bean.ProcessInfo;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.tools.DateFormatTool;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.dialog.CommonDialog;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;

/**
 * Description:通知详情
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class NoticeDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = NoticeDetailActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private TextView typeText = null;
    private TextView cellText = null;
    private TextView sectionText = null;
    private TextView dateText = null;
    private WebView contentView = null;
    private LinearLayout doubleBtnLayout = null;
    private LinearLayout singleBtnLayout = null;
    private LinearLayout appointBtnLayout = null;
    private Button btnAgree = null;
    private Button btnReject = null;
    private Button btnCheck = null;
    private Button btnPlan = null;
    private Button btnContract = null;
    private Button btnRefuse = null;
    private Button btnRespond = null;
    private Button btnConfirm = null;

    private String messageid = null;
    private String processid = null;
    private String sectionName = null;
    private ProcessInfo processInfo = null;
    private PlanInfo planInfo = null;
    private RequirementInfo requirement = null;

    private CommonDialog refuseDialog = null;
    private String refuseMsg = null;
    private String phone = null;

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        Bundle planBundle = intent.getExtras();
        messageid = planBundle.getString(Global.MSG_ID);
        LogTool.d(TAG, "messageid=" + messageid);
        initMainHeadView();
        typeText = (TextView) findViewById(R.id.typeText);
        cellText = (TextView) findViewById(R.id.cellText);
        sectionText = (TextView) findViewById(R.id.sectionText);
        dateText = (TextView) findViewById(R.id.dateText);
        contentView = (WebView) findViewById(R.id.contentView);
        contentView.getSettings().setJavaScriptEnabled(true);
        contentView.setBackgroundColor(0); // 设置背景色
        contentView.setWebChromeClient(new WebChromeClient());
        doubleBtnLayout = (LinearLayout) findViewById(R.id.doubleBtnLayout);
        singleBtnLayout = (LinearLayout) findViewById(R.id.singleBtnLayout);
        appointBtnLayout = (LinearLayout) findViewById(R.id.appointBtnLayout);
        btnAgree = (Button) findViewById(R.id.btnAgree);
        btnReject = (Button) findViewById(R.id.btnReject);
        btnCheck = (Button) findViewById(R.id.btnCheck);
        btnPlan = (Button) findViewById(R.id.btnPlan);
        btnContract = (Button) findViewById(R.id.btnContract);
        btnRefuse = (Button) findViewById(R.id.btnRefuse);
        btnRespond = (Button) findViewById(R.id.btnRespond);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        getNoticeDetailInfo(messageid);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.
                my_notice_detail_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setMianTitle(getResources().getString(R.string.my_notice_detail));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {
        btnAgree.setOnClickListener(this);
        btnReject.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        btnPlan.setOnClickListener(this);
        btnContract.setOnClickListener(this);
        btnRefuse.setOnClickListener(this);
        btnRespond.setOnClickListener(this);
    }

    @Override
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
                planBundle.putSerializable(Global.PLAN_DETAIL, planInfo);
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
                        , SettingMeasureDateActivity_.class);
                Bundle settingHouseTimeBundle = new Bundle();
                settingHouseTimeBundle.putString(Global.REQUIREMENT_ID, requirement.get_id());
                settingHouseTimeBundle.putString(Global.PHONE, phone);
                settingHouseTimeIntent.putExtras(settingHouseTimeBundle);
                startActivity(settingHouseTimeIntent);
                overridePendingTransition(R.anim.slide_and_fade_in_from_bottom, R.anim.fade_out);
                responseRequirement(requirement.get_id(), 0);
                break;
            case R.id.btnConfirm:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }
    }

    //获取详情
    private void getNoticeDetailInfo(String messageid) {
        JianFanJiaClient.getUserMsgDetail(NoticeDetailActivity.this, messageid, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {
                showWaitDialog();
            }

            @Override
            public void loadSuccess(Object data) {
                LogTool.d(TAG, "data:" + data.toString());
                hideWaitDialog();
                NoticeDetailInfo noticeDetailInfo = JsonParser.jsonToBean(data.toString(), NoticeDetailInfo.class);
                if (null != noticeDetailInfo) {
                    String msgType = noticeDetailInfo.getMessage_type();
                    LogTool.d(TAG, "msgType:" + msgType);
                    if (msgType.equals(Constant.TYPE_DELAY_MSG)) {
                        typeText.setBackgroundResource(R.drawable.site_detail_text_bg_border);
                        processid = noticeDetailInfo.getProcessid();
                        LogTool.d(TAG, "processid=" + processid);
                        doubleBtnLayout.setVisibility(View.VISIBLE);
                        singleBtnLayout.setVisibility(View.GONE);
                        typeText.setText(getResources().getString(R.string.delay_str));
                        cellText.setText(noticeDetailInfo.getProcess().getCell());
                        sectionText.setVisibility(View.VISIBLE);
                        sectionText.setText(MyApplication.getInstance().getStringById(noticeDetailInfo.getSection())
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
                        cellText.setText(noticeDetailInfo.getProcess().getCell());
                        sectionText.setVisibility(View.VISIBLE);
                        sectionText.setText(MyApplication.getInstance().getStringById(noticeDetailInfo.getSection())
                                + "阶段");
                    } else if (msgType.equals(Constant.TYPE_CAIGOU_MSG)) {
                        typeText.setBackgroundResource(R.drawable.site_detail_text_bg_border);
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.VISIBLE);
                        typeText.setText(getResources().getString(R.string.caigou_str));
                        cellText.setText(noticeDetailInfo.getProcess().getCell());
                        sectionText.setVisibility(View.VISIBLE);
                        sectionText.setText(MyApplication.getInstance().getStringById(noticeDetailInfo.getSection())
                                + "阶段");
                    } else if (msgType.equals(Constant.TYPE_CONFIRM_CHECK_MSG)) {
                        typeText.setBackgroundResource(R.drawable.site_detail_text_bg_border);
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.VISIBLE);
                        typeText.setText(getResources().getString(R.string.check_str));
                        cellText.setText(noticeDetailInfo.getProcess().getCell());
                        sectionText.setVisibility(View.VISIBLE);
                        sectionText.setText(MyApplication.getInstance().getStringById(noticeDetailInfo.getSection())
                                + "阶段");
                    } else if (msgType.equals(Constant.TYPE_USER_APPOINT_MSG)) {
                        typeText.setBackgroundResource(R.drawable.req_detail_text_bg_border);
                        requirement = noticeDetailInfo.getRequirement();
                        phone = noticeDetailInfo.getUser().getPhone();
                        LogTool.d(TAG, "requirement==" + requirement + " phone=" + phone);
                        typeText.setText(getResources().getString(R.string.req_str));
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        appointBtnLayout.setVisibility(View.VISIBLE);
                        cellText.setText(noticeDetailInfo.getRequirement().getCell());
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

                        }
                    } else if (msgType.equals(Constant.TYPE_PLAN_CHOOSED_MSG) || msgType.equals(Constant
                            .TYPE_PLAN_NOT_CHOOSED_MSG)) {
                        typeText.setBackgroundResource(R.drawable.req_detail_text_bg_border);
                        planInfo = noticeDetailInfo.getPlan();
                        requirement = noticeDetailInfo.getRequirement();
                        LogTool.d(TAG, "requirement==" + requirement + "   planInfo==" + planInfo);
                        typeText.setText(getResources().getString(R.string.req_str));
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnPlan.setVisibility(View.VISIBLE);
                        cellText.setText(noticeDetailInfo.getRequirement().getCell());
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
                        typeText.setText(getResources().getString(R.string.sys_str));
                    } else {
                        typeText.setBackgroundResource(R.drawable.req_detail_text_bg_border);
                        doubleBtnLayout.setVisibility(View.GONE);
                        singleBtnLayout.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.VISIBLE);
                        typeText.setText(getResources().getString(R.string.req_str));
                        cellText.setText(noticeDetailInfo.getRequirement().getCell());
                        sectionText.setVisibility(View.GONE);
                    }
                    dateText.setText(DateFormatTool.getHumReadDateString(noticeDetailInfo.getCreate_at()));
                    contentView.loadDataWithBaseURL(null, noticeDetailInfo.getHtml(), "text/html", "utf-8", null);
                }
            }

            @Override
            public void loadFailture(String error_msg) {
                hideWaitDialog();
                makeTextShort(error_msg);
            }
        }, this);
    }

    //同意改期
    private void agreeReschedule(String processid) {
        JianFanJiaClient.agreeReschedule(NoticeDetailActivity.this, processid, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                LogTool.d(TAG, "data:" + data.toString());
                btnAgree.setText(getResources().getString(R.string.agree_str));
                btnAgree.setEnabled(false);
                btnReject.setVisibility(View.GONE);
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextShort(error_msg);
            }
        }, this);
    }

    // 拒绝改期
    private void refuseReschedule(String processid) {
        JianFanJiaClient.refuseReschedule(NoticeDetailActivity.this, processid, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                LogTool.d(TAG, "data:" + data.toString());
                btnReject.setText(getResources().getString(R.string.reject_str));
                btnAgree.setVisibility(View.GONE);
                btnReject.setEnabled(false);
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextShort(error_msg);
            }
        }, this);
    }

    private void showRefuseDialog(final String requirementid) {
        refuseDialog = DialogHelper
                .getPinterestDialogCancelable(NoticeDetailActivity.this);
        refuseDialog.setTitle(getString(R.string.refuse_reason));
        View contentView = LayoutInflater.from(NoticeDetailActivity.this).inflate(R.layout.dialog_refuse_requirement,
                null);
        RadioGroup radioGroup = (RadioGroup) contentView
                .findViewById(R.id.refuse_radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group.getCheckedRadioButtonId() == R.id.refuse_radio0) {
                    refuseMsg = getString(R.string.refuse_msg0);
                } else if (group.getCheckedRadioButtonId() == R.id.refuse_radio1) {
                    refuseMsg = getString(R.string.refuse_msg1);
                } else if (group.getCheckedRadioButtonId() == R.id.refuse_radio2) {
                    refuseMsg = getString(R.string.refuse_msg2);
                } else {
                    refuseMsg = getString(R.string.refuse_msg3);
                }
            }
        });
        refuseDialog.setContent(contentView);
        refuseDialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (refuseMsg != null) {
                            refuseRequirement(requirementid, refuseMsg);
                            dialog.dismiss();
                        } else {
                            makeTextShort(getString(R.string.tip_choose_refuse_reason));
                        }
                    }
                });
        refuseDialog.setNegativeButton(R.string.no, null);
        refuseDialog.show();
    }

    private void refuseRequirement(String requirementid, String msg) {
        JianFanJiaClient.refuseRequirement(NoticeDetailActivity.this, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }


            public void loadSuccess(Object data) {
                if (refuseDialog != null) {
                    refuseDialog.dismiss();
                }
                btnRefuse.setEnabled(false);
                btnRespond.setVisibility(View.GONE);
                btnRefuse.setText(getResources().getString(R.string.reject_str));
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextShort(error_msg);
            }
        }, requirementid, msg, this);
    }

    private void responseRequirement(String requirementid, long houseCheckTime) {
        JianFanJiaClient.responseRequirement(NoticeDetailActivity.this, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                btnRespond.setEnabled(false);
                btnRefuse.setVisibility(View.GONE);
                btnRespond.setText(getResources().getString(R.string.repond_str));
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextShort(error_msg);
            }
        }, requirementid, houseCheckTime, this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_detail;
    }
}
