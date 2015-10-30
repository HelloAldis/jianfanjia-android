package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.igexin.sdk.PushManager;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.fragment.HomeFragment;
import com.jianfanjia.cn.fragment.ManageFragment;
import com.jianfanjia.cn.fragment.MyFragment;
import com.jianfanjia.cn.fragment.XuQiuFragment;
import com.jianfanjia.cn.fragment.XuQiuFragment_;
import com.jianfanjia.cn.interf.SwitchTabCallBack;
import com.jianfanjia.cn.tools.LogTool;

/**
 * Description:主界面
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MainActivity extends BaseActivity implements
        OnCheckedChangeListener, SwitchTabCallBack {
    private static final String TAG = MainActivity.class.getName();
    private RadioGroup mTabRg = null;
    private HomeFragment homeFragment = null;
    private XuQiuFragment xuqiuFragment = null;
    private ManageFragment manageFragment = null;
    private MyFragment myFragment = null;
    private long mExitTime = 0L;
    private int tab = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushManager.getInstance().initialize(getApplicationContext());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogTool.d(TAG, "onNewIntent");
        initIntent(intent);
    }

    private void initIntent(Intent intent) {
        tab = intent.getIntExtra(Constant.TAB_POSITION, 0);
        LogTool.d(TAG, "tab=" + tab);
        mTabRg.check(getResources().getIdentifier("tab_rb_" + (tab + 1), "id", getPackageName()));
        setTabSelection(tab);
    }

    @Override
    public void initView() {
        mTabRg = (RadioGroup) findViewById(R.id.tab_rg_menu);
        setTabSelection(Constant.HOME);
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
                setTabSelection(Constant.XUQIU);
                break;
            case R.id.tab_rb_3:
                setTabSelection(Constant.MANAGE);
                break;
            case R.id.tab_rb_4:
                setTabSelection(Constant.MY);
                break;
            default:
                break;
        }
    }

    private void setTabSelection(int index) {
        // 开启一个Fragment事务
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
            case Constant.XUQIU:
                if (xuqiuFragment != null) {
                    transaction.show(xuqiuFragment);
                } else {
                    xuqiuFragment = XuQiuFragment_.builder().build();
                    transaction.add(R.id.tabLayout, xuqiuFragment);
                }
                break;
            case Constant.MANAGE:
                if (manageFragment != null) {
                    transaction.show(manageFragment);
                } else {
                    manageFragment = new ManageFragment();
                    transaction.add(R.id.tabLayout, manageFragment);
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
        transaction.commit();
    }

    // 当fragment已被实例化，相当于发生过切换，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (homeFragment != null) {
            ft.hide(homeFragment);
        }
        if (xuqiuFragment != null) {
            ft.hide(xuqiuFragment);
        }
        if (manageFragment != null) {
            ft.hide(manageFragment);
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
    protected void onDestroy() {
        super.onDestroy();
        PushManager.getInstance().stopService(this);// 完全终止SDK的服务
    }

    @Override
    public void switchTab(int index, String params) {
        LogTool.d(TAG, "index=" + index + " params=" + params);
        setTabSelection(index);
        mTabRg.check(getResources().getIdentifier("tab_rb_" + (index + 1), "id", getPackageName()));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}
