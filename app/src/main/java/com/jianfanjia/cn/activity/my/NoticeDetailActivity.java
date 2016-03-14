package com.jianfanjia.cn.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.activity.requirement.CheckActivity;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.NoticeDetailInfo;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description:通知详情
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class NoticeDetailActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG = NoticeDetailActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private TextView typeText = null;
    private TextView cellText = null;
    private TextView sectionText = null;
    private TextView dateText = null;
    private WebView contentView = null;
    private LinearLayout delayBtnLayout = null;
    private LinearLayout checkBtnLayout = null;
    private LinearLayout otherBtnLayout = null;
    private Button btnAgree = null;
    private Button btnReject = null;
    private Button btnConfirm = null;
    private Button btnOther = null;
    private String messageid = null;

    private String processid = null;
    private String sectionName = null;
    private ProcessInfo processInfo = null;

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
        delayBtnLayout = (LinearLayout) findViewById(R.id.delayBtnLayout);
        checkBtnLayout = (LinearLayout) findViewById(R.id.checkBtnLayout);
        otherBtnLayout = (LinearLayout) findViewById(R.id.otherBtnLayout);
        btnAgree = (Button) findViewById(R.id.btnAgree);
        btnReject = (Button) findViewById(R.id.btnReject);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnOther = (Button) findViewById(R.id.btnOther);
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
        btnOther.setOnClickListener(this);
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
            case R.id.btnConfirm:
                Bundle checkBundle = new Bundle();
                checkBundle.putString(Constant.SECTION, sectionName);
                checkBundle.putSerializable(Constant.PROCESS_INFO, processInfo);
                Intent checkIntent = new Intent(NoticeDetailActivity.this, CheckActivity.class);
                checkIntent.putExtras(checkBundle);
                startActivity(checkIntent);
                break;
            case R.id.btnOther:
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
                    typeText.setBackgroundResource(R.drawable.detail_text_bg_border);
                    String msgType = noticeDetailInfo.getMessage_type();
                    LogTool.d(TAG, "msgType:" + msgType);
                    if (msgType.equals(Constant.TYPE_DELAY_MSG)) {
                        processid = noticeDetailInfo.getProcessid();
                        LogTool.d(TAG, "processid=" + processid);
                        delayBtnLayout.setVisibility(View.VISIBLE);
                        checkBtnLayout.setVisibility(View.GONE);
                        otherBtnLayout.setVisibility(View.GONE);
                        typeText.setText(getResources().getString(R.string.delay_str));
                        cellText.setText(noticeDetailInfo.getProcess().getCell());
                        sectionText.setVisibility(View.VISIBLE);
                        sectionText.setText(MyApplication.getInstance().getStringById(noticeDetailInfo.getSection()) + "阶段");
                        if (noticeDetailInfo.getReschedule().getStatus().equals(Constant.YANQI_AGREE)) {
                            btnAgree.setText(getResources().getString(R.string.agree_str));
                            btnAgree.setEnabled(false);
                            btnReject.setEnabled(false);
                        } else if (noticeDetailInfo.getReschedule().getStatus().equals(Constant.YANQI_REFUSE)) {
                            btnReject.setText(getResources().getString(R.string.reject_str));
                            btnAgree.setEnabled(false);
                            btnReject.setEnabled(false);
                        } else {
                            btnAgree.setEnabled(true);
                            btnReject.setEnabled(true);
                        }
                    } else if (msgType.equals(Constant.TYPE_CAIGOU_MSG)) {
                        delayBtnLayout.setVisibility(View.GONE);
                        checkBtnLayout.setVisibility(View.GONE);
                        otherBtnLayout.setVisibility(View.VISIBLE);
                        typeText.setText(getResources().getString(R.string.caigou_str));
                        cellText.setText(noticeDetailInfo.getProcess().getCell());
                        sectionText.setVisibility(View.VISIBLE);
                        sectionText.setText(MyApplication.getInstance().getStringById(noticeDetailInfo.getSection()) + "阶段");
                    } else if (msgType.equals(Constant.TYPE_PAY_MSG)) {
                        delayBtnLayout.setVisibility(View.GONE);
                        checkBtnLayout.setVisibility(View.GONE);
                        otherBtnLayout.setVisibility(View.VISIBLE);
                        typeText.setText(getResources().getString(R.string.pay_str));
                        cellText.setText(noticeDetailInfo.getProcess().getCell());
                        sectionText.setVisibility(View.VISIBLE);
                        sectionText.setText(MyApplication.getInstance().getStringById(noticeDetailInfo.getSection()) + "阶段");
                    } else if (msgType.equals(Constant.TYPE_CONFIRM_CHECK_MSG)) {
                        sectionName = noticeDetailInfo.getSection();
                        processInfo = noticeDetailInfo.getProcess();
                        LogTool.d(TAG, "sectionName=" + sectionName + "  processInfo=" + processInfo);
                        delayBtnLayout.setVisibility(View.GONE);
                        checkBtnLayout.setVisibility(View.VISIBLE);
                        otherBtnLayout.setVisibility(View.GONE);
                        typeText.setText(getResources().getString(R.string.check_str));
                        cellText.setText(noticeDetailInfo.getProcess().getCell());
                        sectionText.setVisibility(View.VISIBLE);
                        sectionText.setText(MyApplication.getInstance().getStringById(noticeDetailInfo.getSection()) + "阶段");
                    } else if (msgType.equals(Constant.TYPE_SYSTEM_MSG)) {
                        typeText.setText(getResources().getString(R.string.sys_str));
                    } else {
                        delayBtnLayout.setVisibility(View.GONE);
                        checkBtnLayout.setVisibility(View.GONE);
                        otherBtnLayout.setVisibility(View.VISIBLE);
                        typeText.setText(getResources().getString(R.string.req_str));
                        cellText.setText(noticeDetailInfo.getRequirement().getCell());
                        sectionText.setVisibility(View.GONE);
                    }
                    dateText.setText(DateFormatTool.getRelativeTime(noticeDetailInfo.getCreate_at()));
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
                btnReject.setEnabled(false);
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
                btnAgree.setEnabled(false);
                btnReject.setEnabled(false);
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextShort(error_msg);
            }
        }, this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_detail;
    }
}
