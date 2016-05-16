package com.jianfanjia.cn.ui.activity.loginandreg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.ui.fragment.LoginFragment;
import com.jianfanjia.cn.ui.fragment.RegisterFragment;
import com.jianfanjia.cn.tools.AuthUtil;
import com.jianfanjia.cn.view.viewpager.ViewPagerIndicator;
import com.jianfanjia.common.tool.LogTool;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * @author fengliang
 * @ClassName: LoginActivity
 * @Description: 登录
 * @date 2015-8-18 下午12:11:23
 */
public class LoginNewActivity extends BaseActivity {
    private static final String TAG = LoginNewActivity.class.getName();
    private static final String[] tabTitles = new String[]{
            "登录", "注册"
    };
    private int currentFragment = 0;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.viewpager_indicator)
    ViewPagerIndicator viewPagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        initData();
    }

    @OnClick({R.id.head_back_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
        }
    }

    private void initView() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            currentFragment = bundle.getInt(IntentConstant.LOGIN_REGIISTER_TYPE,0);
        }
        LogTool.d(TAG, "currentFragment=" + currentFragment);
        viewPagerIndicator.setTabItemTitles(Arrays.asList(tabTitles));
        fragmentList.add(new LoginFragment());
        fragmentList.add(new RegisterFragment());
    }

    private void initData() {
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        viewPager.setAdapter(mAdapter);
        viewPagerIndicator.setViewPager(viewPager, currentFragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogTool.d(this.getClass().getName(), "onActivityResult");
        UMSsoHandler ssoHandler = AuthUtil.getInstance(this).getUmSocialService().getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_login_new;
    }

}
