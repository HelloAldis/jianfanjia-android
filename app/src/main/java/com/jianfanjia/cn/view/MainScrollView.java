package com.jianfanjia.cn.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ScrollView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.cn.tools.TDevice;

/**
 * Description: com.jianfanjia.cn.view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-07 19:58
 */

public class MainScrollView extends ScrollView {

    private static final String TAG = "ElasticScrollView";

    private static final int ANCHOR_TOP = 0;//显示在顶部

    private static final int ANCHOR_BOTTOPM = 1;//显示在底部

    private float totaloffset;//滚动偏移量

    public static final int SMOOTH_SCROLL_DURATION_MS = 200;
    private SmoothScrollRunnable mCurrentSmoothScrollRunnable;

    private Interpolator mScrollAnimationInterpolator;

    //ScrollView的子View， 也是ScrollView的唯一一个子View
    private View contentView;


    //用于记录正常的布局位置
    private Rect originalRect = new Rect();

    private int contentFlag = ANCHOR_TOP;

    private ScrollPullUpListener scrollPullUpListener;

    private ShowGuideListener showGuideListener;

    public MainScrollView(Context context) {
        super(context);
    }

    public MainScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        contentView = LayoutInflater.from(context).inflate(R.layout.include_home_content, null);
        addView(contentView);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        totaloffset = contentView.findViewById(R.id.head_layout).getMeasuredHeight();

        LogTool.d(this.getClass().getName(), "totaloffset =" + totaloffset);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (contentView == null) return;

        //ScrollView中的唯一子控件的位置信息, 这个位置信息在整个控件的生命周期中保持不变
        originalRect.set(contentView.getLeft(), contentView.getTop(), contentView
                .getRight(), contentView.getBottom());

        LogTool.d(this.getClass().getName(), "contentView.getLeft() =" + contentView.getLeft() + " ,contentView" +
                        ".getTop() =" + contentView.getTop()
                        + ",contentView.getRight() =" + contentView.getRight() + " ,contentView.getBottom() =" +
                        contentView.getBottom()
        );
    }

    public interface ScrollPullUpListener {
        void scrollPullUp();
    }

    public interface ShowGuideListener {
        void showGuideView();
    }

    public void setScrollPullUpListener(ScrollPullUpListener scrollPullUpListener) {
        this.scrollPullUpListener = scrollPullUpListener;
    }

    public void setShowGuideListener(ShowGuideListener showGuideListener) {
        this.showGuideListener = showGuideListener;
    }

    private float lastX, lastY;
    private boolean isIntent = false;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        LogTool.d(this.getClass().getName(), "onTouchEvent");

        if (contentView.getBottom() - contentView.getTop() < TDevice.getScreenHeight()) {
            //图片加载失败，禁止滑动事件
            return false;
        }

        int action = ev.getAction();
        float nowY = ev.getY();
        float nowX = ev.getX();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if (nowY - lastY < 0 && contentFlag == ANCHOR_BOTTOPM) {
                    if (scrollPullUpListener != null && !isIntent) {
                        LogTool.d(this.getClass().getName(), "intentTo");
                        isIntent = true;
                        scrollPullUpListener.scrollPullUp();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                LogTool.d(this.getClass().getName(), "ACTION_Up");
                LogTool.d(this.getClass().getName(), "(nowY - lastY) =" + (nowY - lastY) + " (nowX - lastX)" + (nowX
                        - lastX));
                if (nowY - lastY < 0 && contentFlag == ANCHOR_TOP) {
                    LogTool.d(this.getClass().getName(), "scrollUP");
                    smoothScrollTo((int) totaloffset, onSmoothScrollFinishedListener);
                    break;
                }
                if (nowY - lastY > 0 && contentFlag == ANCHOR_BOTTOPM) {
                    LogTool.d(this.getClass().getName(), "scrollBottom");
                    smoothScrollTo(0, onSmoothScrollFinishedListener);
                    break;
                }
                isIntent = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                LogTool.d(this.getClass().getName(), "ACTION_cancel");
                break;
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 在触摸事件中, 处理上拉和下拉的逻辑
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (contentView == null) {
            return super.dispatchTouchEvent(ev);
        }
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                LogTool.d(this.getClass().getName(), "ACTION_DOWN");
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    private OnSmoothScrollFinishedListener onSmoothScrollFinishedListener = new OnSmoothScrollFinishedListener() {
        @Override
        public void onSmoothScrollFinished() {
            if (contentFlag == ANCHOR_BOTTOPM) {
                contentFlag = ANCHOR_TOP;
            } else {
                if (showGuideListener != null) {
                    showGuideListener.showGuideView();
                }
                contentFlag = ANCHOR_BOTTOPM;
            }
        }
    };

    private final void smoothScrollTo(int newScrollValue, long duration, long delayMillis,
                                      OnSmoothScrollFinishedListener listener) {
        if (null != mCurrentSmoothScrollRunnable) {
            mCurrentSmoothScrollRunnable.stop();
        }

        final int oldScrollValue = getScrollY();

        if (oldScrollValue != newScrollValue) {
            if (null == mScrollAnimationInterpolator) {
                // Default interpolator is a Decelerate Interpolator
                mScrollAnimationInterpolator = new DecelerateInterpolator();
            }
            mCurrentSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration, listener);

            if (delayMillis > 0) {
                postDelayed(mCurrentSmoothScrollRunnable, delayMillis);
            } else {
                post(mCurrentSmoothScrollRunnable);
            }
        }
    }

    /**
     * Smooth Scroll to position using the default duration of
     * {@value #SMOOTH_SCROLL_DURATION_MS} ms.
     *
     * @param scrollValue - Position to scroll to
     * @param listener    - Listener for scroll
     */
    protected final void smoothScrollTo(int scrollValue, OnSmoothScrollFinishedListener listener) {
        smoothScrollTo(scrollValue, SMOOTH_SCROLL_DURATION_MS, 0, listener);
    }

    public final void contentScroll(int currentY) {
        LogTool.d(this.getClass().getName(), "contentScroll =" + currentY);
        smoothScrollTo(0, currentY);
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

        public SmoothScrollRunnable(int fromY, int toY, long duration, OnSmoothScrollFinishedListener listener) {
            mScrollFromY = fromY;
            mScrollToY = toY;
            mInterpolator = mScrollAnimationInterpolator;
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
                long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime)) / mDuration;
                normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

                final int deltaY = Math.round((mScrollFromY - mScrollToY)
                        * mInterpolator.getInterpolation(normalizedTime / 1000f));
                mCurrentY = mScrollFromY - deltaY;
                contentScroll(mCurrentY);
            }

            // If we're not at the target Y, keep going...
            if (mContinueRunning && mScrollToY != mCurrentY) {
                ViewCompat.postOnAnimation(MainScrollView.this, this);
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

    public interface OnSmoothScrollFinishedListener {
        void onSmoothScrollFinished();
    }


}