package com.jianfanjia.cn.designer.activity.my;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.view.MainHeadView;

import butterknife.Bind;
import butterknife.OnClick;

public class FeedBackActivity extends BaseActivity {
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
        setListener();
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

    private void setListener() {
        feedContentView.addTextChangedListener(textWatcher);
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
            default:
                break;
        }
    }

    private void feedBack(String content, String version, String platform) {
        JianFanJiaClient.feedBack(this, content, version, platform, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {
                showWaitDialog();
            }

            @Override
            public void loadSuccess(Object data) {
                hideWaitDialog();
                feedContentView.setText("");
                appManager.finishActivity(FeedBackActivity.this);
            }

            @Override
            public void loadFailture(String error_msg) {
                hideWaitDialog();
                makeTextShort(error_msg);
            }
        }, this);
    }


    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            String content = s.toString().trim();
            if (TextUtils.isEmpty(content)) {
                confirm.setEnabled(false);
            } else {
                confirm.setEnabled(true);
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_feed_back;
    }
}