package com.jianfanjia.cn.designer.view;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;


public class QuickReturnFooterBehavior extends CoordinatorLayout.Behavior<View> {
    private int mTotalDyDistance;//dy往一个方向移动的总距离
    private boolean hide = false;//footer是否隐藏
    private int childHeight;//footer height

    public QuickReturnFooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        childHeight = child.getHeight();
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        //判断手指是否先往一个方向移动一段距离后再往反方向移动，如果是，则在往反方向移动时mTotalDyDistance初始化为0，再计算该方向的距离总和
        if (dy > 0 && mTotalDyDistance < 0 || dy < 0 && mTotalDyDistance > 0) {
            mTotalDyDistance = 0;
        }
        //计算该方向的总距离
        mTotalDyDistance += dy;
        //判断当前操作是向上滑动还是向下滑动
        if (!hide && mTotalDyDistance > childHeight) {
            hideView(child);
            hide = true;
        } else if (hide && mTotalDyDistance < -childHeight) {
            showView(child);
            hide = false;
        }
    }

    private void hideView(final View child) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(child, "translationY", 0, childHeight);
        animator.setDuration(500);
        animator.start();
    }

    private void showView(final View child) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(child, "translationY", childHeight, 0);
        animator.setDuration(500);
        animator.start();
    }
}
