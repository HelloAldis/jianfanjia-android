package com.jianfanjia.cn.view.scrollview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;

/**
 * Description: com.jianfanjia.cn.view.scrollview
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-12 11:38
 */
public class HomeScrollView extends ScrollView {

    private static final String TAG = "ElasticScrollView";

    private static final int ANCHOR_TOP = 0;//显示在顶部

    private static final int ANCHOR_BOTTOPM = 1;//显示在底部

    private float totaloffset;//滚动偏移量

    public static final int SMOOTH_SCROLL_DURATION_MS = 200;

    //ScrollView的子View， 也是ScrollView的唯一一个子View
    private View contentView;

    private ViewPager contentViewPager;

    private int contentFlag = ANCHOR_TOP;

    private ScrollPullUpListener scrollPullUpListener;

    private ShowGuideListener showGuideListener;

    private FrameLayout.LayoutParams mLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams
            .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public HomeScrollView(Context context) {
        this(context, null);
    }

    public HomeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        contentView = LayoutInflater.from(context).inflate(R.layout.include_home_content_new, null);
        addView(contentView);
        setOverScrollMode(OVER_SCROLL_NEVER);

        contentViewPager = (ViewPager) contentView.findViewById(R.id.content_viewpager);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        totaloffset = contentView.findViewById(R.id.head_layout).getMeasuredHeight();
        LogTool.d(this.getClass().getName(), "totaloffset =" + totaloffset);

        setContentViewPagerLayoutParam();
    }

    private void setContentViewPagerLayoutParam() {
        mLayoutParams.width = getMeasuredWidth();
        mLayoutParams.height = getMeasuredHeight();

        LogTool.d(TAG, "mLayoutParams.width = " + mLayoutParams.width + ",mLayoutParams.height =" + mLayoutParams
                .height);
        contentViewPager.setLayoutParams(mLayoutParams);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogTool.d(TAG, "h = " + h + ",oldh =" + oldh);

        if (h != oldh) {
//            resetInitView();

            setContentViewPagerLayoutParam();
        }
    }

    private void resetInitView(int contentFlag) {
        if (contentFlag == ANCHOR_TOP) {
            smoothScrollTo(0, 0);
        } else {
            smoothScrollTo(0, (int) totaloffset);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SavedState savedState = new SavedState(parcelable);
        savedState.currentFlag = contentFlag;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        resetInitView(ss.currentFlag);
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
                LogTool.d(this.getClass().getName(), "(nowY - lastY) =" + (nowY - lastY) + " (nowX - lastX) = " + (nowX
                        - lastX));
                if (getScrollY() > 0 && getScrollY() < totaloffset) {
                    if (contentFlag == ANCHOR_TOP) {
                        ainmatorToFooter();
                        break;
                    } else if (contentFlag == ANCHOR_BOTTOPM) {
                        LogTool.d(this.getClass().getName(), "scrollBottom");
                        ainmatorToHead();
                        break;
                    }
                }
                isIntent = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                LogTool.d(this.getClass().getName(), "ACTION_cancel");
                isIntent = false;
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void ainmatorToHead() {
        ValueAnimator animator = ValueAnimator.ofInt(getScrollY(), 0);
        animator.setDuration(SMOOTH_SCROLL_DURATION_MS);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                smoothScrollTo(0, (Integer) animation.getAnimatedValue());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onSmoothScrollFinishedListener != null) {
                    onSmoothScrollFinishedListener.onSmoothScrollFinished();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void ainmatorToFooter() {
        ValueAnimator animator = ValueAnimator.ofInt(getScrollY(), (int) totaloffset);
        animator.setDuration(SMOOTH_SCROLL_DURATION_MS);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                smoothScrollTo(0, (Integer) animation.getAnimatedValue());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onSmoothScrollFinishedListener != null) {
                    onSmoothScrollFinishedListener.onSmoothScrollFinished();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
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
            case MotionEvent.ACTION_MOVE:
                if (isIntent) return true;
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
                contentFlag = ANCHOR_BOTTOPM;
                if (showGuideListener != null) {
                    showGuideListener.showGuideView();
                }
            }
        }
    };

    public interface OnSmoothScrollFinishedListener {
        void onSmoothScrollFinished();
    }

    static class SavedState extends BaseSavedState {
        int currentFlag;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentFlag = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentFlag);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
