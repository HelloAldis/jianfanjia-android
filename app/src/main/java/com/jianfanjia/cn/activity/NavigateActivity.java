package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.adapter.ViewPageAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Global;

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
    private ViewPager viewPager = null;
    private LinearLayout btnLayout = null;
    private Button btnRegister = null;
    private Button btnLogin = null;
    private List<View> list = new ArrayList<View>();
    private ViewPageAdapter adapter = null;
    private int lastSelectorItem = 0;
    private int currentItem = 0; // 当前图片的索引号
    private LinearLayout dotLayout;
    private RelativeLayout imageLayout;

    private int imgId[] = {R.mipmap.p1, R.mipmap.p2,
            R.mipmap.p3, R.mipmap.p4};

    private ImageView[] dots = new ImageView[4];

    @Override
    public void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        btnLayout = (LinearLayout) findViewById(R.id.btnLayout);
        dotLayout = (LinearLayout) findViewById(R.id.dot_layout);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        // 导航测试资源
        for (int i = 0; i < imgId.length; i++) {
            imageLayout = (RelativeLayout)inflater.inflate(R.layout.viewpager_item_navigate,null,false);
            ImageView view = (ImageView)(imageLayout.findViewById(R.id.viewpager_navigate_item_pic));
            view.setImageResource(imgId[i]);
            list.add(view);
        }
        for(int i = 0;i < dots.length; i++){
            ImageView imageView = (ImageView)findViewById(getResources().getIdentifier("welcome_dot"+i,"id",getPackageName()));
            dots[i] = imageView;
        }
        adapter = new ViewPageAdapter(NavigateActivity.this, list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentItem);
    }

    @Override
    public void setListener() {
        viewPager.setOnPageChangeListener(this);
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
                appManager.finishActivity(this);
                break;
            case R.id.btnLogin:
                startActivity(LoginNewActivity_.class);
                appManager.finishActivity(this);
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
        if(currentItem != lastSelectorItem){
            dots[currentItem].setImageResource(R.mipmap.dot_selector);
            dots[lastSelectorItem].setImageResource(R.mipmap.dot_normal);
            lastSelectorItem = currentItem;
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_navigate;
    }

}
