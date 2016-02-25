package com.jianfanjia.cn.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ScrollView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.tools.LogTool;

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

    //移动因子, 是一个百分比, 比如手指移动了100px, 那么View就只移动50px
    //目的是达到一个延迟的效果
    private static final float MOVE_FACTOR = 2.5f;

    //松开手指后, 界面回到正常位置需要的动画时间
    private static final int ANIM_TIME = 300;

    public static final int SMOOTH_SCROLL_DURATION_MS = 200;
    private SmoothScrollRunnable mCurrentSmoothScrollRunnable;

    private Interpolator mScrollAnimationInterpolator;

    //ScrollView的子View， 也是ScrollView的唯一一个子View
    private View contentView;

    //手指按下时的Y值, 用于在移动时计算移动距离
    //如果按下时不能上拉和下拉， 会在手指移动时更新为当前手指的Y值
    private float startY;

    //用于记录正常的布局位置
    private Rect originalRect = new Rect();

    //手指按下时记录是否可以继续上拉
    private boolean canPullUp = false;

    //是否可以滑动导向另一个界面
    private boolean canIntentTo = false;

    //在手指滑动的过程中记录是否移动了布局
    private boolean isMoved = false;

    private int contentFlag = ANCHOR_TOP;

    private ScrollPullUpListener scrollPullUpListener;

    private GestureDetector gestureDetector;

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

        LogTool.d(this.getClass().getName(), "contentView.getLeft() =" + contentView.getLeft() + " ,contentView.getTop() =" + contentView.getTop()
                        + ",contentView.getRight() =" + contentView.getRight() + " ,contentView.getBottom() =" + contentView.getBottom()
        );
    }

    public interface ScrollPullUpListener {
        void scrollPullUp();
    }

    public void setScrollPullUpListener(ScrollPullUpListener scrollPullUpListener) {
        this.scrollPullUpListener = scrollPullUpListener;
    }

    private float lastX, lastY, currentX, currentY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

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

                //判断是否可以上拉和下拉
                canPullUp = isCanPullUp();
                canIntentTo = isOnBottom();

                lastX = ev.getX();
                lastY = ev.getY();

                //记录按下时的Y值
                startY = ev.getY();
                break;

            case MotionEvent.ACTION_UP:

                float nowY = ev.getY();
                float nowX = ev.getX();

                if (Math.abs(nowY - lastY) > Math.abs(nowX - lastX)) {
                    if (nowY - lastY < 0 && contentFlag == ANCHOR_TOP) {
                        LogTool.d(this.getClass().getName(), "scrollUP");
                        smoothScrollTo((int) totaloffset, null);
//                        scrollTo(0,(int) totaloffset);
                        contentFlag = ANCHOR_BOTTOPM;
                        break;
                    }
                    if (nowY - lastY < 0 && contentFlag == ANCHOR_BOTTOPM) {
                        LogTool.d(this.getClass().getName(), "intentTo");
                        if (scrollPullUpListener != null) {
                            scrollPullUpListener.scrollPullUp();
                            return false;
                        }
                        break;
                    }
                    if (nowY - lastY > 0 && contentFlag == ANCHOR_BOTTOPM) {
                        LogTool.d(this.getClass().getName(), "scrollBottom");
                        smoothScrollTo(0, null);
//                        scrollTo(0,0);
                        contentFlag = ANCHOR_TOP;
                        break;
                    }
                }

              /*  if (!isMoved) break;  //如果没有移动布局， 则跳过执行

                // 开启动画
                TranslateAnimation anim = new TranslateAnimation(0, 0, contentView.getTop(),
                        originalRect.top);
                anim.setDuration(ANIM_TIME);

                contentView.startAnimation(anim);

                // 设置回到正常的布局位置
                contentView.layout(originalRect.left, originalRect.top,
                        originalRect.right, originalRect.bottom);

                //将标志位设回false
                canPullUp = false;
                isMoved = false;*/

                break;
            case MotionEvent.ACTION_MOVE:

                currentX = ev.getX();
                currentY = ev.getY();

              /*  if (canIntentTo) {
                    if (currentY - lastY < 0 && Math.abs(currentX - lastX) < Math.abs(currentY - lastY)) {
                        if (scrollPullUpListener != null) {
                            scrollPullUpListener.scrollPullUp();
                            return false;
                        }
                    }
                } else {

                    //在移动的过程中， 既没有滚动到可以上拉的程度， 也没有滚动到可以下拉的程度
                    if (!canPullUp) {
                        startY = ev.getY();
                        canPullUp = isCanPullUp();
                        break;
                    }

                    //计算手指移动的距离
                    float nowY = ev.getY();
                    int deltaY = (int) (nowY - startY);

                    //是否应该移动布局
                    boolean shouldMove = (canPullUp && deltaY < 0);   //可以上拉， 并且手指向上移动

                    if (shouldMove) {
                        //计算偏移量
                        int offset = (int) (deltaY / MOVE_FACTOR);
                        LogTool.d(this.getClass().getName(), "offset =" + offset);

                        //随着手指的移动而移动布局
                        contentView.layout(originalRect.left, originalRect.top + offset,
                                originalRect.right, originalRect.bottom + offset);

                        isMoved = true;  //记录移动了布局
                    }
                }
*/
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        LogTool.d(this.getClass().getName(), "computeScroll");
    }

    /**
     * 判断是否滚动到底部
     */
    private boolean isCanPullUp() {
        return true;
    }

    private boolean isOnBottom() {
        LogTool.d(this.getClass().getName(), "isCanPullUp" + !ViewCompat.canScrollVertically(this, 0));
        return !ViewCompat.canScrollVertically(this, 0);
    }

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

    static interface OnSmoothScrollFinishedListener {
        void onSmoothScrollFinished();
    }


}