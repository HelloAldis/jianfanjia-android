package com.jianfanjia.cn.view.scrollview;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.business.DataManagerNew;
import com.jianfanjia.cn.view.guideview.GuideViewManager;
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

    private float totaloffset;//滚动偏移量

    //ScrollView的子View， 也是ScrollView的唯一一个子View
    private View contentView;

    @Bind(R.id.content_viewpager)
    ViewPager contentViewPager;

    @Bind(R.id.riv_365packget)
    ImageView iv365Packget;

    @Bind(R.id.riv_jianjia)
    ImageView ivJianJia;

    @Bind(R.id.content_intent_to)
    protected ImageButton contentIntent;

    private GuideViewManager mGuideViewManager;

    private int contentFlag = ANCHOR_TOP;

    private boolean isjianJiaAndPackgetMeasure;

    private ScrollPullUpListener scrollPullUpListener;

    private FrameLayout.LayoutParams mLayoutParams =
            new FrameLayout.LayoutParams(ViewGroup.LayoutParams
                    .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public HomeScrollView(Context context) {
        this(context, null);
    }

    public HomeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        contentView = LayoutInflater.from(context).inflate(R.layout.include_home_content_new, null);
        addView(contentView);
        setOverScrollMode(OVER_SCROLL_NEVER);

        ButterKnife.bind(this);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        totaloffset = contentView.findViewById(R.id.head_layout).getMeasuredHeight();
        LogTool.d("totaloffset =" + totaloffset);

        setJianJiaAnd365PackgetHeight();

        setContentViewPagerLayoutParam();
    }

    private void setJianJiaAnd365PackgetHeight() {
        if (!isjianJiaAndPackgetMeasure) {
            int measureWidth = ivJianJia.getMeasuredWidth();
            int bitmapWidth = getResources().getDrawable(R.mipmap.img_home_jianjia).getIntrinsicWidth();
            int bitmapHeight = getResources().getDrawable(R.mipmap.img_home_jianjia).getIntrinsicHeight();

            int measureHeight = (int) (measureWidth * ((float) bitmapHeight / bitmapWidth));
            ViewGroup.LayoutParams jianJiaLp = ivJianJia.getLayoutParams();
            jianJiaLp.height = measureHeight;
            ivJianJia.setLayoutParams(jianJiaLp);

            ViewGroup.LayoutParams packget365Lp = iv365Packget.getLayoutParams();
            packget365Lp.height = measureHeight;
            iv365Packget.setLayoutParams(packget365Lp);

            LogTool.d("bitmap width =" + bitmapWidth + ",bitmap height =" + bitmapHeight + ",measureWidht =" +
                    measureWidth + ",measureHeight =" + bitmapHeight);
            isjianJiaAndPackgetMeasure = true;
        }
    }

    private void setContentViewPagerLayoutParam() {
        mLayoutParams.width = getMeasuredWidth();
        mLayoutParams.height = getMeasuredHeight();

        LogTool.d("mLayoutParams.width = " + mLayoutParams.width + ",mLayoutParams.height =" + mLayoutParams.height);
        contentViewPager.setLayoutParams(mLayoutParams);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogTool.d("h = " + h + ",oldh =" + oldh);

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


    public void setScrollPullUpListener(ScrollPullUpListener scrollPullUpListener) {
        this.scrollPullUpListener = scrollPullUpListener;
    }

    private float lastX, lastY;
    private boolean isIntent = false;

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (DataManagerNew.getInstance().isShowGuide() && !ViewCompat.canScrollVertically(this, 1) &&
                mGuideViewManager == null) {
            int[] location = new int[2];
            contentIntent.getLocationInWindow(location);
            mGuideViewManager = new GuideViewManager(getContext(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataManagerNew.getInstance().setShowGuide(false);
                    mGuideViewManager.hideGuideView();
                    if (scrollPullUpListener != null) {
                        scrollPullUpListener.scrollPullUp();
                    }
                }
            });
            mGuideViewManager.showGuideView(location[0], location[1], contentIntent.getWidth() / 2);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        LogTool.d("onTouchEvent");

        if (contentView.getBottom() - contentView.getTop() < TDevice.getScreenHeight()) {
            //图片加载失败，禁止滑动事件
            return false;
        }
        int action = ev.getAction();
        float nowY = ev.getY();
        float nowX = ev.getX();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if (nowY - lastY < 0 && !ViewCompat.canScrollVertically(this, 1)) {
                    if (scrollPullUpListener != null && !isIntent && !DataManagerNew.getInstance().isShowGuide()) {
                        LogTool.d("intentTo");
                        isIntent = true;
                        scrollPullUpListener.scrollPullUp();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                LogTool.d("ACTION_Up");
                LogTool.d("(nowY - lastY) =" + (nowY - lastY) + " (nowX - lastX) = " + (nowX- lastX));
//                if (getScrollY() > 0 && getScrollY() < totaloffset) {
//                    if (contentFlag == ANCHOR_TOP) {
//                        ainmatorToFooter();
//                        break;
//                    } else if (contentFlag == ANCHOR_BOTTOPM) {
//                        LogTool.d("scrollBottom");
//                        ainmatorToHead();
//                        break;
//                    }
//                }
                isIntent = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                LogTool.d("ACTION_cancel");
                isIntent = false;
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
                LogTool.d("ACTION_DOWN");
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
