package com.jianfanjia.cn.activity;

import android.view.View;
import android.widget.ImageView;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.tools.ViewPagerManager;
import com.jianfanjia.cn.tools.ViewPagerManager.ShapeType;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:帮助
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class HelpActivity extends BaseActivity {
    private static final String TAG = HelpActivity.class.getName();
    private static final int IMG_ID[] = {R.mipmap.p1, R.mipmap.p2,
            R.mipmap.p3, R.mipmap.p4};

    @Override
    public void initView() {
        init();
    }

    private void init() {
        ViewPagerManager contoler = new ViewPagerManager(HelpActivity.this);
        contoler.setmShapeType(ShapeType.OVAL);// 设置指示器的形状为矩形，默认是圆形
        List<View> bannerList = new ArrayList<View>();
        for (int i = 0; i < IMG_ID.length; i++) {
            ImageView imageView = new ImageView(HelpActivity.this);
            imageView.setBackgroundResource(IMG_ID[i]);
            bannerList.add(imageView);
        }
        contoler.init(bannerList);
        contoler.setAutoSroll(false);
    }

    @Override
    public void setListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_help;
    }
}