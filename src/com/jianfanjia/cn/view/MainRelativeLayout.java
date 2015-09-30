package com.jianfanjia.cn.view;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.tools.LogTool;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class MainRelativeLayout extends RelativeLayout {

	private LayoutInflater inflater;
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;
	private static final int SNAP_VELOCITY = 200;
	private int mTouchState = TOUCH_STATE_REST;
	private int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;
	private RelativeLayout mainLayout;
	private RelativeLayout headLayout;
	private boolean isScroll = true;
	private boolean bannerIsGone = false;
	int bannerHeight;
	int titleHeight;
	int totalDiff;

	int lastY;
	int currentY;

	float alph = 255f;

	public MainRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView();
	}

	public MainRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}

	public MainRelativeLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	private void initView() {
		mScroller = new Scroller(getContext());
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		inflater = LayoutInflater.from(getContext());
		mainLayout = (RelativeLayout) inflater.inflate(
				R.layout.main_refresh_view, null);
		addView(mainLayout);
		bannerHeight = MyApplication.dip2px(getContext(), 130);
		titleHeight = MyApplication.dip2px(getContext(), 48);
		totalDiff = bannerHeight - titleHeight;
	}

	public void setHeadLayout(RelativeLayout relativeLayout) {
		this.headLayout = relativeLayout;
		// relativeLayout.setAlpha(0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		LogTool.d("MainLayout", "onTouchEvent");
		if (!isScroll || bannerIsGone) {
			return false;
		}
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// Log.e(TAG, "event down!");
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			// ---------------New Code----------------------
			mLastMotionY = y;
			// ---------------------------------------------

			break;
		case MotionEvent.ACTION_MOVE:
			LogTool.d("MainLayout", "getScrollY = " + getScrollY());
			int deltaX = (int) (mLastMotionX - x);
			int deltaY = (int) (mLastMotionY - y);
			if (Math.abs(deltaY) < 200 && Math.abs(deltaX) > 10)
				break;
			if (getScrollY() < -totalDiff)
				break;
			mLastMotionY = y;
			// -------------------------------------
			mLastMotionX = x;
			// headLayout.setAlpha((float) Math.abs(deltaY) / totalDiff);
			scrollBy(0, deltaY);
			break;
		case MotionEvent.ACTION_UP:
			// Log.e(TAG, "event : up");
			// if (mTouchState == TOUCH_STATE_SCROLLING) {
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			int velocityY = (int) velocityTracker.getYVelocity();
			// Log.e(TAG, "velocityX:" + velocityX);
			if (velocityY > SNAP_VELOCITY) {
				// Fling enough to move left
				// Log.e(TAG, "snap left");
				snapToScreen();
			} else if (velocityY < -SNAP_VELOCITY) {
				// Fling enough to move right
				// Log.e(TAG, "snap right");
				// snapToScreen(mCurScreen + 1);
			} else {
				snapToDestination();
			}
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			// }
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return true;
	}

	private void snapToScreen() {

	}

	private void snapToDestination() {
		final int diffY = totalDiff - Math.abs(getScrollY());
		if (Math.abs(getScrollY()) > (totalDiff / 2)) {
			mScroller.startScroll(0, getScrollY(), 0, diffY, diffY);
		} else {
			mScroller.startScroll(0, getScrollY(), 0, getScrollY(), diffY);
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
			// headLayout.setAlpha((float)Math.abs(mScroller.getCurrY()) /
			// totalDiff);
		}
		if (mScroller.isFinished()) {
			// bannerLayout.setVisibility(View.GONE);
			bannerIsGone = true;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!isScroll) {
			return super.onTouchEvent(ev);
		}
		LogTool.d("MainLayout", "onInterceptTouchEvent");
		// Log.e(TAG, "onInterceptTouchEvent-slop:" + mTouchSlop);
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}
		final float x = ev.getX();
		final float y = ev.getY();
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			LogTool.d("MainLayout", "ACTION_MOVE");
			final int yDiff = (int) Math.abs(mLastMotionY - y);
			if (yDiff > mTouchSlop) {
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;
		case MotionEvent.ACTION_DOWN:

			LogTool.d("MainLayout", "ACTION_DOWN");
			mLastMotionX = x;
			mLastMotionY = y;
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return mTouchState != TOUCH_STATE_REST;
	}

	final class SmoothScrollRunnable implements Runnable {
		private final Interpolator mInterpolator;
		private final int mScrollToY;
		private final int mScrollFromY;
		private final long mDuration;
		private OnSmoothScrollFinishedListener mListener;

		private boolean mContinueRunning = true;
		private long mStartTime = -1;
		private int mCurrentY = -1;

		public SmoothScrollRunnable(int fromY, int toY, long duration,
				OnSmoothScrollFinishedListener listener) {
			mScrollFromY = fromY;
			mScrollToY = toY;
			mInterpolator = new DecelerateInterpolator();
			mDuration = duration;
			mListener = listener;
		}

		@Override
		public void run() {

			/**
			 * Only set mStartTime if this is the first time we're starting,
			 * else actually calculate the Y delta
			 */
			if (mStartTime == -1) {
				mStartTime = System.currentTimeMillis();
			} else {

				/**
				 * We do do all calculations in long to reduce software float
				 * calculations. We use 1000 as it gives us good accuracy and
				 * small rounding errors
				 */
				long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime))
						/ mDuration;
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

				final int deltaY = Math.round((mScrollFromY - mScrollToY)
						* mInterpolator
								.getInterpolation(normalizedTime / 1000f));
				mCurrentY = mScrollFromY - deltaY;
				scrollTo(0, mCurrentY);
			}

			// If we're not at the target Y, keep going...
			if (mContinueRunning && mScrollToY != mCurrentY) {
				ViewCompat.postOnAnimation(MainRelativeLayout.this, this);
			} else {
				if (null != mListener) {
					mListener.onSmoothScrollFinished();
				}
			}
		}

		public void stop() {
			mContinueRunning = false;
			removeCallbacks(this);
		}
	}

	static interface OnSmoothScrollFinishedListener {
		void onSmoothScrollFinished();
	}

}
