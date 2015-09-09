package com.jianfanjia.cn.layout;

import java.util.ArrayDeque;
import java.util.Deque;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;

/**
 * @version 1.0
 * @author zhanghao
 * @date 2015-8-25 10:13
 */
public class ScrollLayout extends ViewGroup {
	private static final String TAG = "ScrollLayout";
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	private int mCurScreen;
	private int mDefaultScreen = 0;
	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;
	private static final int SNAP_VELOCITY = 200;
	private int mTouchState = TOUCH_STATE_REST;
	private int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;
	private OnViewChangeListener mOnViewChangeListener;

	private Deque<View> views = new ArrayDeque<View>();

	private String[] pro = null;

	private LayoutInflater inflater;

	private int perChildWidth;

	private boolean isScroll = true;

	public void setIsScroll(boolean b) {
		this.isScroll = b;
	}

	public ScrollLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mScroller = new Scroller(context);
		mCurScreen = mDefaultScreen;
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		pro = getResources().getStringArray(R.array.site_procedure);
		inflater = LayoutInflater.from(context);

		initView();
	}

	private void initView() {
		for (int i = 0; i < pro.length; i++) {
			View siteHead = inflater.inflate(R.layout.site_head_item, null);
			initItem(siteHead, i);
			views.add(siteHead);
		}
	}

	private void addView() {

	}

	private void initItem(View siteHead, int position) {
		Log.i(TAG, "initItem" + position);
		TextView proName = (TextView) siteHead
				.findViewById(R.id.site_head_procedure_name);
		proName.setText(pro[position]);
		ImageView icon = (ImageView) siteHead
				.findViewById(R.id.site_head_procedure_icon);
		icon.setImageResource(getResources().getIdentifier(
				"icon_home_normal" + (position + 1), "drawable",
				MyApplication.getInstance().getPackageName()));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = 0;
		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				final int childWidth = childView.getMeasuredWidth();
				childView.layout(childLeft, 0, childLeft + childWidth,
						childView.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Log.e(TAG, "onMeasure");
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(0, 0);
		}
		int height = 0;
		if (getChildCount() > 0) {
			perChildWidth = getChildAt(0).getMeasuredWidth();
			height = getChildAt(0).getHeight();
		}
		setMeasuredDimension(getChildCount() * perChildWidth, height);
		// Log.e(TAG, "moving to screen "+mCurScreen);

		scrollTo(mCurScreen * perChildWidth, 0);
	}

	public int getmCurScreen() {
		return mCurScreen;
	}

	public void setmCurScreen(int mCurScreen) {
		this.mCurScreen = mCurScreen;
		snapToScreen(mCurScreen);
	}

	/**
	 * According to the position of current layout scroll to the destination
	 * page.
	 */
	public void snapToDestination() {
		final int screenWidth = perChildWidth;
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		snapToScreen(destScreen);
	}

	public void snapToScreen(int whichScreen) {
		// 鏄惁鍙粦鍔�
		if (!isScroll) {
			this.setToScreen(whichScreen);
			return;
		}

		scrollToScreen(whichScreen);
	}

	public void scrollToScreen(int whichScreen) {
		// get the valid layout page
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		if (getScrollX() != (whichScreen * perChildWidth)) {
			final int delta = whichScreen * perChildWidth - getScrollX();
			/*
			 * mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta)
			 * * 1);// 鎸佺画婊氬姩鏃堕棿 浠ユ绉掍负鍗曚綅
			 */
			post(new SmoothScrollRunnable(getScrollX(), whichScreen
					* perChildWidth, 200, null));
			mCurScreen = whichScreen;
			// invalidate(); // Redraw the layout

			if (mOnViewChangeListener != null) {
				mOnViewChangeListener.OnViewChange(mCurScreen);
			}
		}
	}

	public void setToScreen(int whichScreen) {
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		mCurScreen = whichScreen;
		scrollTo(whichScreen * getWidth(), 0);

		if (mOnViewChangeListener != null) {
			mOnViewChangeListener.OnViewChange(mCurScreen);
		}
	}

	public int getCurScreen() {
		return mCurScreen;
	}

	/*
	 * @Override public void computeScroll() { if
	 * (mScroller.computeScrollOffset()) { scrollTo(mScroller.getCurrX(),
	 * mScroller.getCurrY()); postInvalidate(); } }
	 */

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 鏄惁鍙粦鍔�
		if (!isScroll) {
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
			int deltaX = (int) (mLastMotionX - x);
			int deltaY = (int) (mLastMotionY - y);
			if (Math.abs(deltaX) < 200 && Math.abs(deltaY) > 10)
				break;
			if (getScrollX() < 0)
				break;
			if (getScrollX() > perChildWidth * 6)
				break;
			mLastMotionY = y;
			// -------------------------------------
			mLastMotionX = x;

			scrollBy(deltaX, 0);
			break;
		case MotionEvent.ACTION_UP:
			// Log.e(TAG, "event : up");
			// if (mTouchState == TOUCH_STATE_SCROLLING) {
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int) velocityTracker.getXVelocity();
			// Log.e(TAG, "velocityX:" + velocityX);
			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
				// Fling enough to move left
				// Log.e(TAG, "snap left");
				snapToScreen(mCurScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& mCurScreen < getChildCount() - 4) {
				// Fling enough to move right
				// Log.e(TAG, "snap right");
				snapToScreen(mCurScreen + 1);
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

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!isScroll) {
			return super.onTouchEvent(ev);
		}
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
			final int xDiff = (int) Math.abs(mLastMotionX - x);
			if (xDiff > mTouchSlop) {
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;
		case MotionEvent.ACTION_DOWN:
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

	/**
	 * 璁剧疆灞忓箷鍒囨崲鐩戝惉鍣�
	 * 
	 * @param listener
	 */
	public void SetOnViewChangeListener(OnViewChangeListener listener) {
		mOnViewChangeListener = listener;
	}

	/**
	 * 灞忓箷鍒囨崲鐩戝惉鍣�
	 * 
	 * @author liux
	 */
	public interface OnViewChangeListener {
		public void OnViewChange(int view);
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
				scrollTo(mCurrentY, 0);
			}

			// If we're not at the target Y, keep going...
			if (mContinueRunning && mScrollToY != mCurrentY) {
				ViewCompat.postOnAnimation(ScrollLayout.this, this);
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