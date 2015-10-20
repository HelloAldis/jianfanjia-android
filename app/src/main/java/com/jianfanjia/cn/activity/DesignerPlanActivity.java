package com.jianfanjia.cn.activity;

import android.view.View;
import android.widget.ImageView;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.tools.ViewPagerManager;
import com.jianfanjia.cn.tools.ViewPagerManager.ShapeType;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:预览设计师方案
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerPlanActivity extends BaseActivity {
    private static final int BANNER_ICON[] = {R.mipmap.bg_home_banner1,
            R.mipmap.bg_home_banner2, R.mipmap.bg_home_banner3,
            R.mipmap.bg_home_banner4};

    @Override
    public void initView() {
        initBannerView();
    }

    private void initBannerView() {
        ViewPagerManager contoler = new ViewPagerManager(DesignerPlanActivity.this);
        contoler.setmShapeType(ShapeType.OVAL);
        List<View> bannerList = new ArrayList<View>();
        for (int i = 0; i < BANNER_ICON.length; i++) {
            ImageView imageView = new ImageView(DesignerPlanActivity.this);
            imageView.setBackgroundResource(BANNER_ICON[i]);
            bannerList.add(imageView);
        }
        contoler.init(bannerList);
        contoler.setAutoSroll(true);
    }

    @Override
    public void setListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_plan;
    }
}
