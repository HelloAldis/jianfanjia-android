package com.jianfanjia.cn.supervisor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.supervisor.AppManager;
import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.base.BaseActivity;
import com.jianfanjia.cn.supervisor.config.Constant;
import com.jianfanjia.cn.supervisor.fragment.ManageFragment;
import com.jianfanjia.cn.supervisor.fragment.MyNewFragment;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description:主界面
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getName();

    @Bind(R.id.site_layout)
    LinearLayout siteLayout;

    @Bind(R.id.my_layout)
    LinearLayout myLayout;
    private ManageFragment manageFragment = null;
    private MyNewFragment myFragment = null;
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
        initView();
    }

    public void initView() {
        switchTab(Constant.MANAGE);
    }

    @OnClick({R.id.site_layout, R.id.my_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.site_layout:
                switchTab(Constant.MANAGE);
                break;
            case R.id.my_layout:
                switchTab(Constant.MORE);
                break;
        }
    }

    public void switchTab(int index) {
        switch (index) {
            case Constant.MANAGE:
                siteLayout.setSelected(true);
                myLayout.setSelected(false);
                break;
            case Constant.MORE:
                siteLayout.setSelected(false);
                myLayout.setSelected(true);
                break;
        }
        setTabSelection(index);
    }

    private void setTabSelection(int index) {
        FragmentTransaction transaction = this.getSupportFragmentManager()
                .beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case Constant.MANAGE:
                if (manageFragment != null) {
                    transaction.show(manageFragment);
                } else {
                    manageFragment = new ManageFragment();
                    transaction.add(R.id.tablayout, manageFragment);
                }
                break;
            case Constant.MORE:
                if (myFragment != null) {
                    transaction.show(myFragment);
                } else {
                    myFragment = new MyNewFragment();
                    transaction.add(R.id.tablayout, myFragment);
                }
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    // 当fragment已被实例化，相当于发生过切换，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
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
            makeTextShort("再按一次退出简繁家");
            mExitTime = System.currentTimeMillis();// 更新mExitTime
        } else {
            AppManager.getAppManager().AppExit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
