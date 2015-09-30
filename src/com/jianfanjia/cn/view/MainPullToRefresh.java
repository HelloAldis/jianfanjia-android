package com.jianfanjia.cn.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.tools.LogTool;

public class MainPullToRefresh extends PullToRefreshBase<RelativeLayout>{
	
	private static final String TAG = "MainPullToRefresh";
	
	MainPullToRefreshListener mainRefreshListener;
	
	MainRelativeLayout layoutMianInfo;//主刷新视图
//	//活动Layout
//	RelativeLayout layoutActivities;
//	//天气背景Layout
//	FrameLayout layoutAniBG;
//	//花
//	RelativeLayout layoutFlower;
	
	int titleHeight;
	int bannerHeight;
	long lastUpdateTime = 0;

	public MainPullToRefresh(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWeatherView(context);
	}

	public MainPullToRefresh(Context context,Mode mode,AnimationStyle animStyle) {
		super(context, mode, animStyle);
		initWeatherView(context);
	}

	public MainPullToRefresh(Context context,Mode mode) {
		super(context, mode);
		initWeatherView(context);
	}

	public MainPullToRefresh(Context context) {
		super(context);
		initWeatherView(context);
	}
	
	private void initWeatherView(Context context){
		titleHeight = MyApplication.dip2px(context, 48);
		bannerHeight = MyApplication.dip2px(context, 130);
	}
	
//	public final void setTouchLauout(RelativeLayout layoutActivities,FrameLayout layoutAniBG,RelativeLayout layoutFlower){
//		this.layoutActivities = layoutActivities;
//		this.layoutAniBG = layoutAniBG;
//		this.layoutFlower = layoutFlower;
//	}
	
	public void setMainRefreshListener(MainPullToRefreshListener listener){
		this.mainRefreshListener = listener;
		this.setOnRefreshListener(listener);
	}

	@Override
	protected int getMaximumPullScroll() {
		// TODO Auto-generated method stub
		//return super.getMaximumPullScroll();
		return Math.round((bannerHeight-titleHeight) / FRICTION);
//		return Math.round((App.getDM().heightPixels-App.statusBarHigh) / FRICTION);
	}

	@Override
	public Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}
	
	@Override
	protected RelativeLayout createRefreshableView(Context context,
			AttributeSet attrs) {
		layoutMianInfo = (MainRelativeLayout) LayoutInflater.from(context).inflate(R.layout.main_view, null);
		layoutMianInfo.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				if(event.getAction() == MotionEvent.ACTION_DOWN && event.getY() <= titleHeight){
//					return false;
//				}
				return true;
			}
		});
		return layoutMianInfo;
	}
	
	public void setHeadLayout(RelativeLayout relativeLayout){
		if(layoutMianInfo != null){
			layoutMianInfo.setHeadLayout(relativeLayout);
		}
	}
	
	
	public void resetRefreshingBackground(boolean b){
		isRefreshingBackground = b;
	}


	boolean isRefreshingBackground = false;
	
	public void setRefreshingBackgroundSmooth(){
		if(isRefreshingBackground == false && this.isRefreshing()){
			Log.e(TAG, "setRefreshingBack");
			isRefreshingBackground = true;
			this.smoothScrollTo(0);
		}
	}
	
	/**设置为后台刷新*/
	public void setRefreshingBackground(){
		if(isRefreshingBackground == false && this.isRefreshing()){
			Log.e(TAG, "setRefreshingBack");
			isRefreshingBackground = true;
			setHeaderScroll(0);
		}
	}
	
	//设置为刷新模式
	public void setRefreshingSub(int h){
		//if (!isRefreshing()){
			mState = State.MANUAL_REFRESHING;
			mHeaderLayout.refreshing();
			if(this.getScrollY() != h){
				setHeaderScroll(h);
			}
			isRefreshingBackground = false;
		//}
	}
	
	public boolean isRefreshingBackground(){
		return this.isRefreshingBackground;
	}
	
	/**设置为前台刷新*/
	public void setRefreshingDesk(int h){
		if(isRefreshingBackground && this.isRefreshing()){
			Log.e(TAG, "setRefreshingDesk");
			isRefreshingBackground = false;
			setHeaderScroll(h);
		}
	}
	
	public void setRefreshingNoListener(int h){
		if (!isRefreshing()){
			mState = State.MANUAL_REFRESHING;
			mHeaderLayout.refreshing();
			setHeaderScroll(h);
			isRefreshingBackground = false;
		}
	}
	
	
	/**viewpaper加载一项时需要重新开启动画*/
	public void refreshHeaderAnimation(){
		if (isRefreshing()){
			mHeaderLayout.refreshing();
		}
	}
	

	@Override
	protected void setState(State state, boolean... params) {
		super.setState(state, params);
	}

	@Override
	protected boolean isReadyForPullEnd() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean isReadyForPullStart() {
		// TODO Auto-generated method stub
		return true;
	}

//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		if(this.layoutFlower != null && this.layoutFlower.dispatchTouchEvent(ev)){
//			return true;
//		}
//		if(this.layoutAniBG != null && this.layoutAniBG.dispatchTouchEvent(ev)){
//			return true;
//		}
//		if(this.layoutActivities != null && this.layoutActivities.dispatchTouchEvent(ev)){
//			return true;
//		}
//		return super.dispatchTouchEvent(ev);
//	}
	
	// 计算点击的次数
	private int click_count;
	// 第一次点击的时间 long型
	private long firstClick;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if(isRefreshing()){
		LogTool.d("PullRefresh", "onInterceptTouchEvent");
			final int action = event.getAction();

			if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
				mIsBeingDragged = false;
				return false;
			}

			if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
				return true;
			}
			
			switch (action) {
				case MotionEvent.ACTION_MOVE: {
					if(mLastMotionY < titleHeight){
						return false;
					}
					final float y = event.getY(), x = event.getX();
					final float diff, oppositeDiff, absDiff;

					// We need to use the correct values, based on scroll
					// direction
					diff = y - mLastMotionY;
					oppositeDiff = x - mLastMotionX;
					absDiff = Math.abs(diff);

					if (absDiff > mTouchSlop && (!mFilterTouchEvents || absDiff > Math.abs(oppositeDiff))) {
						if (diff >= 1f || (isRefreshing() && diff <= -1f)) {
							mLastMotionY = y;
							mLastMotionX = x;
							mIsBeingDragged = true;
//							if (mMode == Mode.BOTH) {
//								mCurrentMode = Mode.PULL_FROM_START;
//							}
						}
					}
					break;
				}
				case MotionEvent.ACTION_DOWN: {
						mLastMotionY = mInitialMotionY = event.getY();
						mLastMotionX = mInitialMotionX = event.getX();
						mIsBeingDragged = false;
						// 判断双击事件
						if (firstClick != 0
								&& System.currentTimeMillis() - firstClick > 300) {
							click_count = 0;
						}
						click_count++;
						if (click_count == 1) {
							firstClick = System.currentTimeMillis();
						} else {
							if(mainRefreshListener != null){
								mainRefreshListener.onDoubleClick(this);
							}
//							Config.getConfig().setTip(Config.TIP_DOUBLE_CLICK_SCREEN);
							click_count = 0;
							firstClick = 0;
						}
						break;
				}
			}

			return mIsBeingDragged;
		}else{
			return super.onInterceptTouchEvent(event);
		}
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		LogTool.d("onTouchEvent", "onInterceptTouchEvent");
		if(isRefreshing()){
			if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
				return false;
			}
			switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE: {
					if (mIsBeingDragged) {
						mLastMotionY = event.getY();
						mLastMotionX = event.getX();
						int y;
						if(isRefreshingBackground){
							y = Math.round(Math.min(mInitialMotionY - mLastMotionY, 0) / FRICTION);
						}else{
							y = Math.min(Math.round((mInitialMotionY - mLastMotionY) / FRICTION)- getHeaderSize(), 0);
						}
						Log.i(TAG, "onTouchEvent:" + y);
						setHeaderScroll(y);
						return true;
					}
					break;
				}

				case MotionEvent.ACTION_DOWN: {

					mLastMotionY = mInitialMotionY = event.getY();
					mLastMotionX = mInitialMotionX = event.getX();
					//mIsBeingDragged = true;
					//Log.i(LOG_TAG, "---------onTouchEvent:xxxx");
					return true;
				}

				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP: {
					if (mIsBeingDragged) {
						mIsBeingDragged = false;
						// If we're already refreshing, just scroll back to the top
						if(this.getScrollY() > -getHeaderSize()){
							smoothScrollTo(0);
							if(!isRefreshingBackground){
								isRefreshingBackground = true;
								this.mainRefreshListener.onRefreshingStateChanged(this, true);
							}
							
						}else{
							smoothScrollTo(-getHeaderSize());
							if(isRefreshingBackground){
								isRefreshingBackground = false;
								this.mainRefreshListener.onRefreshingStateChanged(this, false);
							}
							
						}
						return true;
					}
					break;
				}
			}

			return false;
		}else{
			return super.onTouchEvent(event);
		}	
	}
	
	
	public static interface MainPullToRefreshListener extends OnRefreshListener<RelativeLayout>{
		public void onRefreshingStateChanged(MainPullToRefresh mainRefreshView,boolean isRefreshingBackground);
		public void onDoubleClick(MainPullToRefresh mainRefreshView);
	}
	
	
	public String getUpdateTimeString(){
		long now = System.currentTimeMillis();
		if(lastUpdateTime == 0){
			return getResources().getString(R.string.pull_to_refresh_pull_label);
		}else if(now-lastUpdateTime < 5*60*1000){
			return getResources().getString(R.string.main_refresh_updated1);
		}else if(now-lastUpdateTime < 60*60*1000){
			return getResources().getString(R.string.main_refresh_updated2,(now-lastUpdateTime)/60/1000);
		}else if(now-lastUpdateTime < 8*60*60*1000){
			return getResources().getString(R.string.main_refresh_updated3,(now-lastUpdateTime)/60/60/1000);
		}else{
			return getResources().getString(R.string.main_refresh_updated4);
		}
	}

}
