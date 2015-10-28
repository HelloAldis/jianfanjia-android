package com.jianfanjia.cn.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.NetTool;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: LoginActivity
 * @Description: 登录
 * @date 2015-8-18 下午12:11:23
 */
@EActivity(R.layout.activity_login_new)
public class RegisterNewActivity extends BaseAnnotationActivity implements OnClickListener,
        ApiUiUpdateListener {
    private static final String TAG = RegisterNewActivity.class.getName();
    private RelativeLayout loginLayout = null;
    @ViewById(R.id.login_viewpager)
    ViewPager viewPager;
    private List<View> viewList;
    private List<String> titleList;
    private View loginView;
    private View registerView;
    @ViewById(R.id.login_title)
    PagerTabStrip pagerTitleStrip;

    private EditText mEtUserName = null;// 用户名输入框
    private EditText mEtPassword = null;// 用户密码输入框
    private Button mBtnLogin = null;// 登录按钮
    private TextView mForgetPswView = null;
    private TextView mRegisterView = null;// 导航到用户注册
    private String mUserName = null;// 用户名
    private String mPassword = null;// 密码

    @AfterViews
    public void initView() {
        viewList = new ArrayList<>();
        loginView = inflater.inflate(R.layout.viewpager_item_login,null,false);
        registerView = inflater.inflate(R.layout.viewpager_item_register,null,false);
        viewList.add(loginView);
        viewList.add(registerView);
        titleList = new ArrayList<>();
        titleList.add(getString(R.string.login));
        titleList.add(getString(R.string.register));
        pagerTitleStrip.setTextColor(getResources().getColor(R.color.black_color));
        pagerTitleStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        pagerTitleStrip.setDrawFullUnderline(false);
        pagerTitleStrip.setTabIndicatorColorResource(R.color.font_white);
        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));

                return viewList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {

                return titleList.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mUserName = mEtUserName.getText().toString().trim();
                mPassword = mEtPassword.getText().toString().trim();
                if (checkInput(mUserName, mPassword)) {
                    login(mUserName, mPassword);
                }
                break;
            case R.id.register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.forget_password:
                startActivity(ForgetPswActivity.class);
                break;
            default:
                break;
        }
    }

    private boolean checkInput(String name, String password) {
        if (TextUtils.isEmpty(name)) {
            makeTextShort(getResources().getString(
                    R.string.tip_please_input_username));
            mEtUserName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            makeTextShort(getResources().getString(
                    R.string.tip_please_input_password));
            mEtPassword.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * 登录
     *
     * @param name
     * @param password
     */
    private void login(String name, String password) {
        // dataManager.login(name, password, this);
        if (NetTool.isNetworkAvailable(this)) {
            JianFanJiaClient.login(this, name, password, this, this);
        } else {
            makeTextLong(getString(R.string.tip_internet_not));
        }
    }

    @Override
    public void loadSuccess(Object data) {
        //登录成功，加载工地列表
        super.loadSuccess(data);
        startActivity(MainActivity.class);
        finish();
    }




}
