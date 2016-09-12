package com.jianfanjia.cn.designer.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.jianfanjia.cn.designer.R;


/**
 * @author hongyangAndroid
 *         http://blog.csdn.net/lmj623565791/
 */
public class ViewPagerIndicator extends LinearLayout {

    private List<String> mTitles;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setTabItemTitles(List<String> titles) {
        if (titles != null && titles.size() > 0) {
            this.removeAllViews();
            mTitles = titles;
            for (String title : mTitles) {
                addView(inflateView(title));
            }

            setItemClickEvent();
        }
    }

    private View inflateView(String title) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.include_login_title, null, true);
        TextView tv = (TextView) view.findViewById(R.id.title);
        tv.setText(title);
        tv.setTextColor(getResources().getColor(R.color.middle_grey_color));
        return view;
    }

    private ViewPager mViewPager;

    /**
     * @param viewPager
     * @param pos
     */
    public void setViewPager(ViewPager viewPager, int pos) {
        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                highLightTextView(position);
//				if (position <= (mTabVisibleCount - 2))
//					scrollTo(0, 0);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
//				scroll(position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(pos);
        highLightTextView(pos);
    }

    /**
     */
    private void resetTextViewColor() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i).findViewById(R.id.title);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.middle_grey_color));
            }
            ImageView imageView = (ImageView)getChildAt(i).findViewById(R.id.indicator);
            imageView.setVisibility(View.GONE);
        }

    }

    /**
     * @param pos
     */
    private void highLightTextView(int pos) {
        resetTextViewColor();
        View view = getChildAt(pos).findViewById(R.id.title);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(getResources().getColor(R.color.light_black_color));
        }

        ImageView imageView = (ImageView)getChildAt(pos).findViewById(R.id.indicator);
        imageView.setVisibility(View.VISIBLE);
    }

    /**
     */
    private void setItemClickEvent() {
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            final int j = i;
            View view = getChildAt(i);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });

        }

    }

}
