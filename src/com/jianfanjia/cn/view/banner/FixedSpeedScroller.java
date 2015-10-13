package com.jianfanjia.cn.view.banner;

import android.content.Context;
import android.widget.Scroller;

public class FixedSpeedScroller extends Scroller {
	private int mDuration = 800;

	public FixedSpeedScroller(Context context) {
		super(context);
	}

	public FixedSpeedScroller(Context context,
			android.view.animation.Interpolator interpolator) {
		super(context, interpolator);
	}

	public FixedSpeedScroller(Context context,
			android.view.animation.Interpolator interpolator, boolean flywheel) {
		super(context, interpolator, flywheel);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration) {

		// if(duration == 200)//����setCurrentItem�����Ĺ̶��ٶ�
		super.startScroll(startX, startY, dx, dy, mDuration);
		// else //�������������ٶ���
		// super.startScroll(startX, startY, dx, dy, duration);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy) {
		super.startScroll(startX, startY, dx, dy, mDuration);
	}
}
