package com.jianfanjia.cn.ui.activity.requirement;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.request.guest.PostUserRequirementRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.business.RequirementBusiness;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.DesignerAppointSuccessDialog;

/**
 * Description: com.jianfanjia.cn.ui.activity.requirement
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-08-17 11:23
 */
public class GetFreePlanActivity extends BaseSwipeBackActivity {

    @Bind(R.id.mhv_get_free_plan)
    protected MainHeadView mainHeadView = null;

    @Bind(R.id.et_name)
    EditText etUserName;

    @Bind(R.id.et_phone)
    EditText etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        initMainView();
    }

    private void initMainView(){
        mainHeadView.setMianTitle(getString(R.string.free_get_plan));
    }

    @OnClick({R.id.head_back_layout, R.id.btn_apply})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.btn_apply:
                String username = etUserName.getEditableText().toString().trim();
                String phone = etPhone.getEditableText().toString().trim();
                if (checkLoginInput(phone, username)) {
                    addAppointRequirement(username, phone);
                }
                break;
        }
    }

    private boolean checkLoginInput(String phone, String username) {
        if (TextUtils.isEmpty(username)) {
            makeTextShort(getResources().getString(
                    R.string.input_username));
            etUserName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            makeTextShort(getResources().getString(
                    R.string.hint_username));
            etPhone.requestFocus();
            return false;
        }
        if (!phone.matches(Global.PHONE_MATCH)) {
            makeTextShort(getString(R.string.tip_input_corrent_phone));
            etPhone.requestFocus();
            return false;
        }
        return true;
    }

    private void addAppointRequirement(String userName, String phone) {
        PostUserRequirementRequest postUserRequirement = new PostUserRequirementRequest();
        postUserRequirement.setPhone(phone);
        postUserRequirement.setName(userName);
        postUserRequirement.setDistrict(RequirementBusiness.REQUIREMENT_DISTRICT_ADD);

        Api.postUserRequirement(postUserRequirement, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                etPhone.setText("");
                etUserName.setText("");
                showAppointSuccessDialog();
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

    private void showAppointSuccessDialog() {
        DesignerAppointSuccessDialog designerAppointSuccessDialog = new DesignerAppointSuccessDialog(getUiContext());
        designerAppointSuccessDialog.show();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_get_free_plan;
    }
}
