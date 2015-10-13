package com.jianfanjia.cn.fragment;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.interf.FragmentListener;
import com.jianfanjia.cn.tools.NetTool;

/**
 * @author zhanghao
 * @version 1.0
 * @Description 注册选择角色Fragment
 * @date 2015-8-21 上午9:15
 */
public class FrgPswInputVerificationFragment extends BaseFragment {
    private static final String TAG = FrgPswInputVerificationFragment.class
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
        indicatorView.setImageResource(R.mipmap.rounded_forget2);
        proTipView = (TextView) view.findViewById(R.id.register_pro);
        proTipView.setText(getString(R.string.verification_code_sended));
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


    @Override
    public int getLayoutId() {
        return R.layout.fragment_forget_psw_input_verification;
    }
}
