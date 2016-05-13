package com.jianfanjia.cn.activity.requirement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.request.user.ConfirmContractRequest;
import com.jianfanjia.api.request.user.GetContractInfoRequest;
import com.jianfanjia.cn.Event.ChoosedContractEvent;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.business.RequirementBusiness;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.common.tool.LogTool;
import de.greenrobot.event.EventBus;

/**
 * Description:合同查看
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class ContractActivity extends BaseSwipeBackActivity implements
        View.OnKeyListener {
    private static final String TAG = ContractActivity.class.getName();
    public static final String CONSTRACT_INTENT_FLAG = "contract_intent_flag";

    public static final int NOTICE_INTENT = 0;//通知进入的
    public static final int DESIGNER_LIST_INTENT = 1;//我的设计师列表
    private int flagIntent = -1;
    @Bind(R.id.my_contract_head_layout)
    protected MainHeadView mainHeadView = null;
    @Bind(R.id.btn_choose)
    protected Button checkBtn = null;
    @Bind(R.id.webView)
    protected WebView webView = null;

    private Requirement requirement = null;
    private String final_planid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
        initData();
    }

    protected void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle contractBundle = intent.getExtras();
        if (contractBundle != null) {
            requirement = (Requirement) contractBundle.getSerializable(IntentConstant.REQUIREMENT_INFO);
            flagIntent = contractBundle.getInt(ContractActivity.CONSTRACT_INTENT_FLAG, DESIGNER_LIST_INTENT);//默认是设计师列表
            LogTool.d(TAG, "requirement:" + requirement + "  flagIntent:" + flagIntent);
        }
    }

    public void initView() {
        initMainHeadView();
        initWebView();
        if(requirement.getWork_type().equals(RequirementBusiness.WORK_TYPE_PURE_DESIGNER)){
            checkBtn.setVisibility(View.GONE);
        }else{
            checkBtn.setVisibility(View.VISIBLE);
            if (requirement.getStatus().equals(Global.REQUIREMENT_STATUS5) ||
                    requirement.getStatus().equals(Global.REQUIREMENT_STATUS8)) {
                checkBtn.setEnabled(false);
                checkBtn.setText(getString(R.string.already_open_process));
            } else if (requirement.getStatus().equals(Global.REQUIREMENT_STATUS4)) {
                checkBtn.setEnabled(false);
                checkBtn.setText(getString(R.string.str_wait_setting_open_process_time));
            } else {
                checkBtn.setEnabled(true);
                checkBtn.setText(getString(R.string.str_check_contract));
            }
        }
    }

    private void initData() {
        getContractInfo(requirement.get_id());
    }

    private void initWebView() {
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm
                .SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl(Url_New.getInstance().CONTRACT_URL);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setOnKeyListener(this);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string
                .contractText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @OnClick({R.id.head_back_layout, R.id.btn_choose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.btn_choose:
                chooseContractDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK &&
                    webView.canGoBack()) {  //表示按返回键时的操作
                webView.goBack();   //后退
                return true;    //已处理
            }
        }
        return false;
    }

    private void chooseContractDialog() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(ContractActivity.this);
        dialog.setTitle(getResources().getString(R.string.hint_contract_text));
        dialog.setMessage(getResources().getString(R.string.hint_contract_str));
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        postUserProcess(requirement.get_id(), final_planid);
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }

    //查看合同
    private void getContractInfo(String requirementid) {
        GetContractInfoRequest getContractInfoRequest = new GetContractInfoRequest();
        getContractInfoRequest.setRequirementid(requirementid);
        Api.getContractInfo(getContractInfoRequest, new ApiCallback<ApiResponse<Requirement>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<Requirement> apiResponse) {
                Requirement contractInfo = apiResponse.getData();
                LogTool.d(TAG, "contractInfo:" + contractInfo);
                if (null != contractInfo) {
                    final_planid = contractInfo.getFinal_planid();
                    LogTool.d(TAG, "final_planid:" + final_planid);
                }
            }

            @Override
            public void onFailed(ApiResponse<Requirement> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }


    //确认开启工地  确认合同
    private void postUserProcess(String requirementid, String final_planid) {
        LogTool.d(TAG, "requirementid=" + requirementid + "  final_planid=" +
                final_planid);
        ConfirmContractRequest confirmContractRequest = new ConfirmContractRequest();
        confirmContractRequest.setRequirementid(requirementid);
        confirmContractRequest.setFinal_planid(final_planid);
        Api.confirmContract(confirmContractRequest, new ApiCallback<ApiResponse<Process>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<Process> apiResponse) {
                checkBtn.setEnabled(false);
                postProcessSuccess();
            }

            @Override
            public void onFailed(ApiResponse<Process> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
                checkBtn.setEnabled(true);
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    private void postProcessSuccess() {
        switch (flagIntent) {
            case NOTICE_INTENT:
                EventBus.getDefault().post(new ChoosedContractEvent());
                break;
            case DESIGNER_LIST_INTENT:
//                setResult(RESULT_OK);
                startActivity(MainActivity.class);
                appManager.finishActivity(ContractActivity.this);
                break;
            default:
                break;
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_contract;
    }
}
