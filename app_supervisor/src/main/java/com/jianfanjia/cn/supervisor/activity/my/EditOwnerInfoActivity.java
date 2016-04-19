package com.jianfanjia.cn.supervisor.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.OnClick;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.SuperVisor;
import com.jianfanjia.api.request.supervisor.UpdateSuperVisorRequest;
import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.api.Api;
import com.jianfanjia.cn.supervisor.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.supervisor.config.Constant;
import com.jianfanjia.cn.supervisor.view.MainHeadView;


public class EditOwnerInfoActivity extends BaseSwipeBackActivity implements OnClickListener {
    private static final String TAG = EditOwnerInfoActivity.class.getName();

    @Bind(R.id.edit_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.edit_info)
    EditText editInfoView;

    @Bind(R.id.btn_commit)
    Button confirmView;

    private Intent intent;
    private int type;// 输入类型
    private String content;//输入内容
    private SuperVisor user = new SuperVisor();
    InputFilter[] namefilters = {new InputFilter.LengthFilter(20)};
    InputFilter[] addressfilters = {new InputFilter.LengthFilter(100)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        editInfoView.addTextChangedListener(textWatcher);
        confirmView.setEnabled(false);

        intent = getIntent();
        type = intent.getIntExtra(Constant.EDIT_TYPE, 0);
        content = intent.getStringExtra(Constant.EDIT_CONTENT);
        if (!TextUtils.isEmpty(content)) {
            editInfoView.setText(content);
            editInfoView.setSelection(content.length());
        }
        if (type == Constant.REQUESTCODE_EDIT_USERNAME) {
            editInfoView.setHint(R.string.input_name);
            editInfoView.setFilters(namefilters);
            mainHeadView.setMianTitle(getString(R.string.user_name));
        } else {
            editInfoView.setHint(R.string.input_home);
            editInfoView.setFilters(addressfilters);
            mainHeadView.setMianTitle(getString(R.string.user_home));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//		editInfoView.requestFocus();
    }

    @OnClick({R.id.head_back_layout, R.id.btn_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.btn_commit:
                content = editInfoView.getEditableText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    switch (type) {
                        case Constant.REQUESTCODE_EDIT_USERNAME:
                            user.setUsername(content);
                            break;
                        case Constant.REQUESTCODE_EDIT_HOME:
                            user.setAddress(content);
                            break;
                    }
                    put_Owner_Info();
                }
                break;
            default:
                break;
        }
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
            if (!TextUtils.isEmpty(s.toString().trim())) {
                confirmView.setEnabled(true);
            } else {
                confirmView.setEnabled(false);
            }
        }
    };

    // 修改设计师个人资料
    private void put_Owner_Info() {
        UpdateSuperVisorRequest request = new UpdateSuperVisorRequest();
        request.setSupervisor(user);

        Api.updateSuperVisorInfo(request, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                Hud.show(EditOwnerInfoActivity.this);
            }

            @Override
            public void onHttpDone() {
                Hud.dismiss();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                intent.putExtra(Constant.EDIT_CONTENT, content);
                setResult(RESULT_OK, intent);
                appManager.finishActivity(EditOwnerInfoActivity.this);
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
        return R.layout.activity_edit_info;
    }

}
