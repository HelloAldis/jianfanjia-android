package com.jianfanjia.cn.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
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

    //移动因子, 是一个百分比, 比如手指移动了100px, 那么View就只移动50px
    //目的是达到一个延迟的效果
    private static final float MOVE_FACTOR = 2.5f;

    //松开手指后, 界面回到正常位置需要的动画时间
    private static final int ANIM_TIME = 300;

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

    private ScrollPullUpListener scrollPullUpListener;

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
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (contentView == null) return;

        //ScrollView中的唯一子控件的位置信息, 这个位置信息在整个控件的生命周期中保持不变
        originalRect.set(contentView.getLeft(), contentView.getTop(), contentView
                .getRight(), contentView.getBottom());
    }

    public interface ScrollPullUpListener {
        public void scrollPullUp();
    }

    public void setScrollPullUpListener(ScrollPullUpListener scrollPullUpListener) {
        this.scrollPullUpListener = scrollPullUpListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    private float lastX, lastY, currentX, currentY;

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

                if (!isMoved) break;  //如果没有移动布局， 则跳过执行

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
                isMoved = false;

                break;
            case MotionEvent.ACTION_MOVE:

                currentX = ev.getX();
                currentY = ev.getY();

                if (canIntentTo) {
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

                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }


    /**
     * 判断是否滚动到顶部
     */
    private boolean isCanPullDown() {
        return getScrollY() == 0 ||
                contentView.getHeight() <= getHeight() + getScrollY();
    }

    /**
     * 判断是否滚动到底部
     */
    private boolean isCanPullUp() {
        return true;
    }

    private boolean isOnBottom(){
        LogTool.d(this.getClass().getName(), "isCanPullUp" + !ViewCompat.canScrollVertically(this, 0));
        return !ViewCompat.canScrollVertically(this, 0);
    }

}