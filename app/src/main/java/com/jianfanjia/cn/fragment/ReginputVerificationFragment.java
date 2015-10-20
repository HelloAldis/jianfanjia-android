package com.jianfanjia.cn.fragment;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.FragmentListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;


public class ReginputVerificationFragment extends BaseFragment implements ApiUiUpdateListener {
    private static final String TAG = ReginputVerificationFragment.class
            .getName();
    private FragmentListener fragemntListener = null;
    private Button nextView = null;// 下一步
    private TextView backView = null;// 返回
    private EditText mEdVerif = null;// 验证码输入框
    private ImageView indicatorView = null;// 指示器
    private TextView proTipView = null;// 提示操作

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fragemntListener = (FragmentListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initView(View view) {
        nextView = (Button) view.findViewById(R.id.btn_commit);
        backView = (TextView) view.findViewById(R.id.goback);
        mEdVerif = (EditText) view.findViewById(R.id.et_verification);
        indicatorView = (ImageView) view.findViewById(R.id.indicator);
        indicatorView.setImageResource(R.mipmap.rounded_register3);
        proTipView = (TextView) view.findViewById(R.id.register_pro);
        proTipView.setText(getString(R.string.verification_code_sended) + MyApplication.getInstance().getRegisterInfo().getPhone());
    }

    @Override
    public void setListener() {
        nextView.setOnClickListener(this);
        backView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                String verif = mEdVerif.getText().toString().trim();
                if (checkInput(verif)) {
                    MyApplication.getInstance().getRegisterInfo().setCode(verif);
                    register(MyApplication.getInstance().getRegisterInfo());
                }
                break;
            case R.id.goback:
                fragemntListener.onBack();
                break;
            default:
                break;
        }
    }

    private boolean checkInput(String verifCode) {
        if (TextUtils.isEmpty(verifCode)) {
            makeTextShort(getResources().getString(R.string.hint_verification));
            mEdVerif.requestFocus();
            return false;
        }
        if (!NetTool.isNetworkAvailable(getActivity())) {
            makeTextShort(getResources().getString(R.string.tip_error_internet));
            return false;
        }
        return true;
    }

    /**
     * 注册提交
     *
     * @param registerInfo
     */
    private void register(RegisterInfo registerInfo) {
        JianFanJiaClient.register(getActivity(), registerInfo, this, this);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data:" + data);
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextLong(error_msg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_register_input_verification;
    }
}
