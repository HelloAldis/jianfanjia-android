package com.jianfanjia.cn.designer.ui.activity.my_info_auth.receive_business_info;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.io.Serializable;

import butterknife.Bind;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth.receive_business_info
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-30 13:48
 */
public class ChooseWorkFeeActivity extends BaseSwipeBackActivity {

    public static final String WORK_FEE_INFO = "work_fee_info";

    private WorkFeeInfo mWorkFeeInfo;

    @Bind(R.id.choose_work_fee_head_layout)
    MainHeadView mMainHeadView;

    @Bind(R.id.work_fee_all_min_content)
    EditText workFeeAllMinEditText;

    @Bind(R.id.work_fee_half_min_content)
    EditText workFeeHalfMinEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();
        initView();
        initData();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mWorkFeeInfo = (WorkFeeInfo) bundle.getSerializable(WORK_FEE_INFO);
            if (mWorkFeeInfo == null) {
                mWorkFeeInfo = new WorkFeeInfo();
            }
        }
    }

    private void initView() {
        initMainView();
    }

    private void initData() {
        setMianHeadRightTitleEnable();

        if (mWorkFeeInfo.getWork_fee_all_min() > 0) {
            workFeeAllMinEditText.setText(mWorkFeeInfo.getWork_fee_all_min() + "");
        }
        workFeeAllMinEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    mWorkFeeInfo.setWork_fee_all_min(Integer.parseInt(s.toString()));
                } else {
                    mWorkFeeInfo.setWork_fee_all_min(0);
                }
                setMianHeadRightTitleEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (mWorkFeeInfo.getWork_fee_half_min() > 0) {
            workFeeHalfMinEditText.setText(mWorkFeeInfo.getWork_fee_half_min() + "");
        }
        workFeeHalfMinEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    mWorkFeeInfo.setWork_fee_half_min(Integer.parseInt(s.toString()));
                } else {
                    mWorkFeeInfo.setWork_fee_half_min(0);
                }
                setMianHeadRightTitleEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.str_receive_business_work_fee));
        mMainHeadView.setRightTitle(getString(R.string.str_save));
        mMainHeadView.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = getIntent();
                data.putExtra(WORK_FEE_INFO, mWorkFeeInfo);
                setResult(RESULT_OK, data);
                appManager.finishActivity(ChooseWorkFeeActivity.this);
            }
        });
        mMainHeadView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appManager.finishActivity(ChooseWorkFeeActivity.this);
            }
        });
    }

    private void setMianHeadRightTitleEnable() {
        /*if (mWorkFeeInfo.getWork_fee_half_min() > 0 && mWorkFeeInfo.getWork_fee_all_min() > 0) {
            mMainHeadView.setRigthTitleEnable(true);
        } else {
            mMainHeadView.setRigthTitleEnable(false);
        }*/
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_work_fee;
    }

    public static class WorkFeeInfo implements Serializable {

        private int work_fee_half_min;
        private int work_fee_all_min;

        public int getWork_fee_half_min() {
            return work_fee_half_min;
        }

        public void setWork_fee_half_min(int work_fee_half_min) {
            this.work_fee_half_min = work_fee_half_min;
        }

        public int getWork_fee_all_min() {
            return work_fee_all_min;
        }

        public void setWork_fee_all_min(int work_fee_all_min) {
            this.work_fee_all_min = work_fee_all_min;
        }
    }
}
