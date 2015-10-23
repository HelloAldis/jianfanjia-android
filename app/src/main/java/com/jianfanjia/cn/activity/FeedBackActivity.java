package com.jianfanjia.cn.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.view.MainHeadView;

public class FeedBackActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = FeedBackActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private EditText feedContentView = null;
    private Button confirm = null;

    @Override
    public void initView() {
        initMainHeadView();
        feedContentView = (EditText) findViewById(R.id.add_feedback);
        feedContentView.addTextChangedListener(textWatcher);
        confirm = (Button) findViewById(R.id.btn_commit);
        confirm.setEnabled(false);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_feedback_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.feedback));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            case R.id.btn_commit:
                String content = feedContentView.getText().toString().trim();
                feedBack(content, "0");
            default:
                break;
        }
    }

    private void feedBack(String content, String platform) {

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