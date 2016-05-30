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

/**
 * Description: com.jianfanjia.cn.designer.ui.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-30 15:59
 */
public class EmailAuthFinishedFragment extends BaseFragment {

    @Bind(R.id.act_input_email)
    EditText mEtLoginUserName = null;// 邮箱

    private String mEmail;

    private EmailFinishedCallback emailFinishedCallback;

    public static EmailAuthFinishedFragment getInstance(String email){
        EmailAuthFinishedFragment emailAuthFinishedFragment = new EmailAuthFinishedFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EmailAuthActivity.EMAIL,email);
        emailAuthFinishedFragment.setArguments(bundle);
        return emailAuthFinishedFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle argument = getArguments();
        if(argument != null){
            mEmail = argument.getString(EmailAuthActivity.EMAIL, null);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @OnClick({R.id.tv_update_email_action})
    protected void click(View view){
        switch (view.getId()){
            case R.id.tv_update_email_action:
                emailFinishedCallback.updateEmail();
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        emailFinishedCallback.updateEmail();
    }

    private void initView() {
        if(!TextUtils.isEmpty(mEmail)){
            mEtLoginUserName.setText(mEmail);
        }
    }

    public interface EmailFinishedCallback{
        void updateEmail();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_email_finished;
    }
}
