package com.jianfanjia.cn.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.ViewPageAdapter;
import com.jianfanjia.cn.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: ViewPagerManager
 * User: fengliang
 * Date: 2015-10-12
 * Time: 10:35
 */
public class ViewPagerManager {
    private Context mContext;
    private MyViewPager mViewPager;
    /**
     * ViewPager要显示的视图集合
     **/
    private List<View> mViews;
    private ViewPageAdapter mPagerAdapter;
    /**
     * 装指示器的LinearLayout
     **/
    private LinearLayout mIndicatorGroup;
    /**
     * 指示器的集合
     **/
    private View[] indicators;
    /**
     * 当指示器选择矩形时，默认的宽
     **/
    private static final int INDICATOR_WIDTH_FOR_RECT = 40;
    /**
     * 当指示器选择矩形时，默认的高
     **/
    private static final int INDICATOR_HEIGHT_FOR_RECT = 8;
    /**
     * 当指示器选择圆形时，默认的宽
     **/
    private static final int INDICATOR_WIDTH_FOR_OVAL = 20;
    /**
     * 当指示器选择圆形时，默认的高
     **/
    private static final int INDICATOR_HEIGHT_FOR_OVAL = 20;
    /**
     * 指示器的宽
     **/
    private int mIndicatorWidth;
    /**
     * 指示器的高
     **/
    private int mIndicatorHeight;

    private int mIndicatorBgResForSelected;
    private int mIndicatorBgResForUnselected;
    /**
     * 指示器默认为圆形
     **/
    private ShapeType mShapeType;

    private boolean autoScroll = false;
    private static final int CHANGE_PHOTO = 1;
    private static final int CHANGE_TIME = 5000;// 图片自动切换时间

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHANGE_PHOTO:
                    int index = mViewPager.getCurrentItem();
                    if (index == mViews.size() - 1) {
                        index = -1;
                    }
                    mViewPager.setCurrentItem(index + 1);
                    handler.sendEmptyMessageDelayed(CHANGE_PHOTO, CHANGE_TIME);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * @param context
     */
    public ViewPagerManager(Context context,MyViewPager viewPager) {
        super();
        this.mContext = context;
        this.mViewPager = viewPager;
    }

    /***
     * 设置数据,适用于前面页面是图片，最后一个页面是一个layout布局
     *
     * @param imgIds 图片的id数组
     * @param view
     */
    public void init(int[] imgIds, View view) {
        mViews = new ArrayList<View>();
        for (int i = 0; i < imgIds.length; i++) {
            ImageView iv = new ImageView(mContext);
            iv.setImageResource(imgIds[i]);
            iv.setScaleType(ScaleType.CENTER);
            mViews.add(iv);
        }
        mViews.add(view);
        set();
    }

    /**
     * 传入数据,当页面视图全是由layout生产的时候适用
     **/
    public void init(List<View> views) {
        mViews = views;
        set();
    }

    /**
     * 设置ViewPager和指示器
     **/
    private void set() {
        setViewPager();
        setIndicators();
    }

    /**
     * 设置ViewPager
     **/
    private void setViewPager() {
        mPagerAdapter = new ViewPageAdapter(mContext, mViews);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(pageChangeListener);
        mViewPager.setOnTouchListener(touchListener);
    }

    /**
     * 设置指示器
     **/
    private void setIndicators() {
        setConfigure(mShapeType);
        mIndicatorGroup = (LinearLayout) ((Activity) mContext)
                .findViewById(R.id.indicatorGroup_lib);
        indicators = new View[mViews.size()];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                new ViewGroup.LayoutParams(mIndicatorWidth, mIndicatorHeight));
        params.setMargins(0, 0, 15, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new View(mContext);
            if (i == 0) {
                indicators[i].setBackgroundResource(mIndicatorBgResForSelected);
            } else {
                indicators[i]
                        .setBackgroundResource(mIndicatorBgResForUnselected);
            }
            indicators[i].setLayoutParams(params);
            mIndicatorGroup.addView(indicators[i]);
        }
    }

    /**
     * 根据枚举参数，设置指示器的背景和宽高
     **/
    private void setConfigure(ShapeType shapeType) {
        if (shapeType != null) {
            if (shapeType == ShapeType.OVAL) {
                mIndicatorWidth = mIndicatorWidth == 0 ? INDICATOR_WIDTH_FOR_OVAL
                        : mIndicatorWidth;
                mIndicatorHeight = mIndicatorHeight == 0 ? INDICATOR_HEIGHT_FOR_OVAL
                        : mIndicatorHeight;
                mIndicatorBgResForSelected = R.drawable.shape_indicator_selected_oval;
                mIndicatorBgResForUnselected = R.drawable.shape_indicator_unselected_oval;
            } else if (shapeType == ShapeType.RECT) {
                mIndicatorWidth = mIndicatorWidth == 0 ? INDICATOR_WIDTH_FOR_RECT
                        : mIndicatorWidth;
                mIndicatorHeight = mIndicatorHeight == 0 ? INDICATOR_HEIGHT_FOR_RECT
                        : mIndicatorHeight;
                mIndicatorBgResForSelected = R.drawable.shape_indicator_selected_rect;
                mIndicatorBgResForUnselected = R.drawable.shape_indicator_unselected_rect;
            }
        } else {
            mIndicatorWidth = mIndicatorWidth == 0 ? INDICATOR_WIDTH_FOR_OVAL
                    : mIndicatorWidth;
            mIndicatorHeight = mIndicatorHeight == 0 ? INDICATOR_HEIGHT_FOR_OVAL
                    : mIndicatorHeight;
            mIndicatorBgResForSelected = R.drawable.shape_indicator_selected_oval;
            mIndicatorBgResForUnselected = R.drawable.shape_indicator_unselected_oval;
        }
    }

    public void setAutoSroll(boolean autoScroll) {
        this.autoScroll = autoScroll;
        if (autoScroll) {
            handler.sendEmptyMessageDelayed(CHANGE_PHOTO, CHANGE_TIME);
        } else {
            handler.removeMessages(CHANGE_PHOTO);
        }
    }

    /**
     * 监听ViewPager的页面变化
     **/
    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < indicators.length; i++) {
                if (i == arg0) {
                    indicators[i]
                            .setBackgroundResource(mIndicatorBgResForSelected);
                } else {
                    indicators[i]
                            .setBackgroundResource(mIndicatorBgResForUnselected);
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private OnTouchListener touchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    autoScroll = false;
                    handler.removeMessages(CHANGE_PHOTO);
                    break;
                case MotionEvent.ACTION_UP:
                    autoScroll = true;
                    handler.sendEmptyMessageDelayed(CHANGE_PHOTO, CHANGE_TIME);
                    break;
            }
            return false;
        }

    };

    // 定义一个枚举(用来指定指示器的形状)
    public enum ShapeType {
        RECT, OVAL
    }

    public void setmIndicatorWidth(int mIndicatorWidth) {
        this.mIndicatorWidth = mIndicatorWidth;
    }

    public void setmIndicatorHeight(int mIndicatorHeight) {
        this.mIndicatorHeight = mIndicatorHeight;
    }

    public void setmShapeType(ShapeType mShapeType) {
        this.mShapeType = mShapeType;
    }
}  
