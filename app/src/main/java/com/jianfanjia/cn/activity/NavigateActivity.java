package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.adapter.ViewPageAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Global;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author fengliang
 * @ClassName: NavigateActivity
 * @Description: 引导
 * @date 2015-8-28 下午3:23:37
 */
public class NavigateActivity extends BaseActivity implements OnPageChangeListener {
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.btnLayout)
    LinearLayout btnLayout;

    @Bind(R.id.btnRegister)
    Button btnRegister;

    @Bind(R.id.btnLogin)
    Button btnLogin;

    private List<View> list = new ArrayList<>();
    private ViewPageAdapter adapter = null;
    private int lastSelectorItem = 0;
    private int currentItem = 0; // 当前图片的索引号
    private RelativeLayout imageLayout = null;

    private int imgId[] = {R.mipmap.img_guide1, R.mipmap.img_guide2,
            R.mipmap.img_guide3, R.mipmap.img_guide4};

    private ImageView[] dots = new ImageView[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
    }

    private void initView() {
        for (int i = 0; i < imgId.length; i++) {
            imageLayout = (RelativeLayout) inflater.inflate(R.layout.viewpager_item_navigate, null, false);
            ImageView view = (ImageView) (imageLayout.findViewById(R.id.viewpager_navigate_item_pic));
            view.setImageResource(imgId[i]);
            list.add(view);
        }
        for (int i = 0; i < dots.length; i++) {
            ImageView imageView = (ImageView) findViewById(getResources().getIdentifier("welcome_dot" + i, "id",
                    getPackageName()));
            dots[i] = imageView;
        }
        adapter = new ViewPageAdapter(NavigateActivity.this, list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentItem);
        viewPager.setOnPageChangeListener(this);
    }

    @OnClick({R.id.btnRegister, R.id.btnLogin})
    public void onClick(View v) {
        dataManager.setFisrt(false);
        switch (v.getId()) {
            case R.id.btnRegister:
                Bundle bundle = new Bundle();
                bundle.putBoolean(Global.ISREGIISTER, true);
                startActivity(LoginNewActivity.class, bundle);
                appManager.finishActivity(this);
                break;
            case R.id.btnLogin:
                startActivity(LoginNewActivity.class);
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
        if (currentItem != lastSelectorItem) {
            dots[currentItem].setImageResource(R.mipmap.icon_dot_selector);
            dots[lastSelectorItem].setImageResource(R.mipmap.icon_dot_normal);
            lastSelectorItem = currentItem;
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_navigate;
    }

}
