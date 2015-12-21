package com.jianfanjia.cn.designer.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.ViewPageAdapter;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.config.Global;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: NavigateActivity
 * @Description: 引导
 * @date 2015-8-28 下午3:23:37
 */
public class NavigateActivity extends BaseActivity implements OnClickListener,
        OnPageChangeListener {
    private static final int imgId[] = {R.drawable.p1, R.drawable.p2,
            R.drawable.p3, R.drawable.p4};
    private ViewPager viewPager = null;
    private ImageView btn_welcome_off = null;
    private LinearLayout btnLayout = null;
    private Button btnRegister = null;
    private Button btnLogin = null;
    private List<View> list = new ArrayList<View>();
    private ViewPageAdapter adapter = null;
    private int currentItem = 0; // 当前图片的索引号

    @Override
    public void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        btn_welcome_off = (ImageView) findViewById(R.id.btn_welcome_off);
        btnLayout = (LinearLayout) findViewById(R.id.btnLayout);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        // 导航测试资源
        for (int i = 0; i < imgId.length; i++) {
            ImageView view = new ImageView(this);
            view.setImageResource(imgId[i]);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            list.add(view);
        }
        adapter = new ViewPageAdapter(NavigateActivity.this, list);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void setListener() {
        viewPager.setOnPageChangeListener(this);
        btn_welcome_off.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dataManager.setFisrt(false);
        switch (v.getId()) {
            case R.id.btnRegister:
                Bundle bundle = new Bundle();
                bundle.putBoolean(Global.ISREGIISTER, true);
                startActivity(LoginNewActivity_.class, bundle);
                finish();
                break;
            case R.id.btnLogin:
            case R.id.btn_welcome_off:
                startActivity(LoginNewActivity_.class);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        currentItem = arg0;
        if (currentItem == list.size() - 1) {
            btnLayout.setVisibility(View.VISIBLE);
        } else {
            btnLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_navigate;
    }

}
