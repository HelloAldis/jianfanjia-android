package com.jianfanjia.cn.view.banner;

import java.lang.reflect.Field;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BannerView extends InfiniteViewPager {
	private boolean autoScroll = true;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (autoScroll && msg.what == 0) {
				setCurrentItem(getCurrentItem() + 1, true);
				handler.sendEmptyMessageDelayed(0, 6000);
			}
		}
	};

	public BannerView(Context context) {
		super(context);
		initView(context);
	}

	public BannerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		setViewPagerScrollSpeed(this);
		setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					autoScroll = false;
					handler.removeMessages(0);
					break;
				case MotionEvent.ACTION_UP:
					autoScroll = true;
					handler.sendEmptyMessageDelayed(0, 6000);
					break;
				}
				return false;
			}
		});
		handler.sendEmptyMessageDelayed(0, 6000);
	}
	
	private void setViewPagerScrollSpeed(ViewPager viewPager) {
		try {
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext());
			mScroller.set(viewPager, scroller);
		} catch (NoSuchFieldException e) {

		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
	}
}
