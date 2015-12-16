package com.jianfanjia.cn.designer.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.fragment.RegInputPhoneFragment;
import com.jianfanjia.cn.designer.fragment.ReginputVerificationFragment;
import com.jianfanjia.cn.designer.interf.FragmentListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: RegisterActivity
 * @Description: 注册界面
 * @date 2015-8-18 下午12:11:35
 */
public class RegisterActivity extends BaseActivity implements FragmentListener {
    private static final String TAG = RegisterActivity.class.getName();
    private RegInputPhoneFragment regInputPhoneFragment;
    private ReginputVerificationFragment reginputVerificationFragment;
    private List<Fragment> fragments = new ArrayList<Fragment>();

    private int currentPage = 0;// 所在的Fragment页面的位置

    @Override
    public void initView() {
        regInputPhoneFragment = new RegInputPhoneFragment();
        reginputVerificationFragment = new ReginputVerificationFragment();
        fragments.add(regInputPhoneFragment);
        fragments.add(reginputVerificationFragment);
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.reg_content, regInputPhoneFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onBack() {
        fragmentManager.popBackStack();
        if (currentPage >= 1) {
            currentPage--;
        } else {
            finish();
        }
    }

    @Override
    public void onNext() {
        if (currentPage < 1) {
            Log.i(TAG, "next");
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.setCustomAnimations(
                    R.anim.fragment_slide_right_enter,
                    R.anim.fragment_slide_left_exit,
                    R.anim.fragment_slide_left_enter,
                    R.anim.fragment_slide_right_exit);
            fragmentTransaction.replace(R.id.reg_content,
                    fragments.get(++currentPage));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (currentPage >= 1) {
            currentPage--;
        } else {
            finish();
        }
    }

}
