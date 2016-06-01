package com.jianfanjia.cn.designer.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.EmailAuthActivity;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.designer.ui.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-30 15:59
 */
public class EmailAuthingFragment extends BaseFragment {

    @Bind(R.id.act_input_email)
    EditText mEtLoginUserName = null;// 邮箱

    private String mEmail;

    private EmailAuthingCallback mEmailAuthingCallback;

    public static EmailAuthingFragment getInstance(String email){
        EmailAuthingFragment emailAuthingFragment = new EmailAuthingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EmailAuthActivity.EMAIL,email);
        emailAuthingFragment.setArguments(bundle);
        return emailAuthingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle argument = getArguments();
        if(argument != null){
            mEmail = argument.getString(EmailAuthActivity.EMAIL, null);
        }
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
        initView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mEmailAuthingCallback = (EmailAuthingCallback)activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @OnClick({R.id.tv_update_email_action,R.id.tv_send_again})
    protected void click(View view){
        switch (view.getId()){
            case R.id.tv_update_email_action:
                mEmailAuthingCallback.updateEmil();
                break;
            case R.id.tv_send_again:
                mEmailAuthingCallback.sendAgain();
                break;
        }
    }

    public interface EmailAuthingCallback{
        void updateEmil();
        void sendAgain();
    }


    private void initView() {
        LogTool.d(this.getClass().getName(),"initview email =" + mEmail);
        if(!TextUtils.isEmpty(mEmail)){
            mEtLoginUserName.setText(mEmail);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_email_authing;
    }
}
