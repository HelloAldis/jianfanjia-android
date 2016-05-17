package com.jianfanjia.cn.designer.ui.activity.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.request.guest.FeedBackRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.view.MainHeadView;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class FeedBackActivity extends BaseSwipeBackActivity {
    private static final String TAG = FeedBackActivity.class.getName();

    @Bind(R.id.my_feedback_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.add_feedback)
    EditText feedContentView;

    @Bind(R.id.btn_commit)
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        initMainHeadView();
        confirm.setEnabled(false);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.feedback));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @OnClick({R.id.head_back_layout, R.id.btn_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.btn_commit:
                String content = feedContentView.getText().toString().trim();
                feedBack(content, "" + MyApplication
                        .getInstance().getVersionCode(), "0");
                break;
            default:
                break;
        }
    }

    @OnTextChanged(R.id.add_feedback)
    public void onTextChanged(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            confirm.setEnabled(false);
        } else {
            confirm.setEnabled(true);
        }
    }

    private void feedBack(String content, String version, String platform) {
        FeedBackRequest request = new FeedBackRequest();
        request.setContent(content);
        request.setVersion(version);
        request.setPlatform(platform);
        Api.feedBack(request, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                feedContentView.setText("");
                appManager.finishActivity(FeedBackActivity.this);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_feed_back;
    }
}