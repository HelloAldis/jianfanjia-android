package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jianfanjia.cn.AppManager;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.fragment.ManageFragment;
import com.jianfanjia.cn.fragment.MoreFragment;
import com.jianfanjia.cn.fragment.OwnerFragment;
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
    private RadioGroup mTabRg = null;
    private OwnerFragment ownerFragment = null;
    private ManageFragment manageFragment = null;
    private MoreFragment moreFragment = null;
    private long mExitTime = 0L;
    private int tab = -1;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogTool.d(TAG, "onNewIntent");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mTabRg = (RadioGroup) findViewById(R.id.tab_rg_menu);
        setTabSelection(Constant.OWNER);
    }

    @Override
    public void setListener() {
        mTabRg.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.tab_rb_1:
                setTabSelection(Constant.OWNER);
                break;
            case R.id.tab_rb_2:
                setTabSelection(Constant.MANAGE);
                break;
            case R.id.tab_rb_3:
                setTabSelection(Constant.MORE);
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
            case Constant.OWNER:
                if (ownerFragment != null) {
                    transaction.show(ownerFragment);
                } else {
                    ownerFragment = new OwnerFragment();
                    transaction.add(R.id.tablayout, ownerFragment);
                }
                break;
            case Constant.MANAGE:
                if (manageFragment != null) {
                    transaction.show(manageFragment);
                } else {
                    manageFragment = new ManageFragment();
                    transaction.add(R.id.tablayout, manageFragment);
                }
                break;
            case Constant.MORE:
                if (moreFragment != null) {
                    transaction.show(moreFragment);
                } else {
                    moreFragment = new MoreFragment();
                    transaction.add(R.id.tablayout, moreFragment);
                }
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    // 当fragment已被实例化，相当于发生过切换，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (ownerFragment != null) {
            ft.hide(ownerFragment);
        }
        if (manageFragment != null) {
            ft.hide(manageFragment);
        }
        if (moreFragment != null) {
            ft.hide(moreFragment);
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
            makeTextShort("再按一次退出简繁家");
            mExitTime = System.currentTimeMillis();// 更新mExitTime
        } else {
            AppManager.getAppManager().AppExit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


}
