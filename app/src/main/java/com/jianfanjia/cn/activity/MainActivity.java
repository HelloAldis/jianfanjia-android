package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.fragment.HomeFragment;
import com.jianfanjia.cn.fragment.MyFragment;
import com.jianfanjia.cn.fragment.XuQiuFragment;
import com.jianfanjia.cn.fragment.XuQiuFragment_;
import com.jianfanjia.cn.interf.OnActivityResultCallBack;
import com.jianfanjia.cn.tools.LogTool;

/**
 * Description:主界面
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MainActivity extends BaseActivity implements
        OnCheckedChangeListener {
    private static final String TAG = MainActivity.class.getName();
    private OnActivityResultCallBack onActivityResultCallBack = null;
    private RadioGroup mTabRg = null;
    private HomeFragment homeFragment = null;
    private XuQiuFragment xuqiuFragment = null;
    private MyFragment myFragment = null;
    private long mExitTime = 0L;
    private int tab = -1;

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        try {
            onActivityResultCallBack = (OnActivityResultCallBack) fragment;
        } catch (ClassCastException e) {
            LogTool.d(TAG, "e:" + e);
        }
        LogTool.d(TAG, "onActivityResultCallBack:" + onActivityResultCallBack);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mTabRg = (RadioGroup) findViewById(R.id.tab_rg_menu);
        setTabSelection(Constant.HOME);
        //如果是注册直接点击发布需求，先创建mainactivity再启动editrequirementactivity
        Intent intent = getIntent();
        boolean flag = intent.getBooleanExtra(Global.IS_PUBLISHREQUIREMENT, false);
        LogTool.d(TAG, "flag:" + flag);
        if (flag) {
            LogTool.d(TAG, "REGISTER PUBLISH REQUIREMENG");
            startActivityForResult(new Intent(this, EditRequirementActivity_.class), XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT);
        }
    }

    @Override
    public void setListener() {
        mTabRg.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.tab_rb_1:
                setTabSelection(Constant.HOME);
                break;
            case R.id.tab_rb_2:
                setTabSelection(Constant.MANAGE);
                break;
            case R.id.tab_rb_3:
                setTabSelection(Constant.MY);
                break;
            default:
                break;
        }
    }

    private void setTabSelection(int index) {
        FragmentTransaction transaction = this.getSupportFragmentManager()
                .beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case Constant.HOME:
                if (homeFragment != null) {
                    transaction.show(homeFragment);
                } else {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.tabLayout, homeFragment);
                }
                break;
            case Constant.MANAGE:
                if (xuqiuFragment != null) {
                    transaction.show(xuqiuFragment);
                } else {
                    xuqiuFragment = XuQiuFragment_.builder().build();
                    transaction.add(R.id.tabLayout, xuqiuFragment);
                }
                break;
            case Constant.MY:
                if (myFragment != null) {
                    transaction.show(myFragment);
                } else {
                    myFragment = new MyFragment();
                    transaction.add(R.id.tabLayout, myFragment);
                }
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    // 当fragment已被实例化，相当于发生过切换，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (homeFragment != null) {
            ft.hide(homeFragment);
        }
        if (xuqiuFragment != null) {
            ft.hide(xuqiuFragment);
        }
        if (myFragment != null) {
            ft.hide(myFragment);
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
            makeTextLong("再按一次退出程序");
            mExitTime = System.currentTimeMillis();// 更新mExitTime
        } else {
            activityManager.exit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        LogTool.d(TAG, "onActivityResult requestCode =" + requestCode);
        if (requestCode == XuQiuFragment.REQUESTCODE_EDIT_REQUIREMENT) {
            xuqiuFragment.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT) {
            mTabRg.check(R.id.tab_rb_2);
            setTabSelection(Constant.MANAGE);
            xuqiuFragment.onActivityResult(requestCode, resultCode, data);
            homeFragment.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


}
