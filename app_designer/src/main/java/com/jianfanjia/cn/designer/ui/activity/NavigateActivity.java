package com.jianfanjia.cn.designer.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.ui.activity.login_and_register.LoginNewActivity;
import com.jianfanjia.cn.designer.ui.adapter.ViewPageAdapter;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.common.tool.LogTool;

/**
 * @author fengliang
 * @ClassName: NavigateActivity
 * @Description: 引导
 * @date 2015-8-28 下午3:23:37
 */
public class NavigateActivity extends BaseActivity implements
        OnPageChangeListener {

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.btnLayout)
    LinearLayout btnLayout;

    private List<View> list = new ArrayList<View>();
    private ViewPageAdapter adapter = null;
    private int lastSelectorItem = 0;
    private int currentItem = 0; // 当前图片的索引号

    private RelativeLayout imageLayout;

    private int imgId[] = {R.mipmap.img_guide1, R.mipmap.img_guide2};

    private ImageView[] dots = new ImageView[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();
    }

    public void initView() {
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
        viewPager.setOnPageChangeListener(this);
    }

    int currentState;
    boolean intentTo = false;//是否已经发了intent请求，防止重复发

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub
        currentState = arg0;
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
        LogTool.d(this.getClass().getName(), "arg0 =" + arg0 + " arg1 =" + arg1 + " arg2 =" + arg2);
        if (currentState == ViewPager.SCROLL_STATE_DRAGGING && arg0 == list.size() - 1 && arg1 == 0.0f) {
            if(!intentTo){
                dataManager.setFisrt(false);
                startActivity(LoginNewActivity.class);
                appManager.finishActivity(this);
                intentTo = true;
            }
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        currentItem = arg0;
        if (currentItem == list.size() - 1) {
            btnLayout.setVisibility(View.GONE);
        } else {
            btnLayout.setVisibility(View.GONE);
        }
        if(currentItem != lastSelectorItem){
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
