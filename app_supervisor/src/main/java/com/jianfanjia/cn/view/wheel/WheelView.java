package com.jianfanjia.cn.view.wheel;

/*
 *  Android Wheel Control.
 *  https://code.google.com/p/android-wheel/
 *  
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.util.LinkedList;
import java.util.List;

public class WheelView extends View {
	/** Scrolling duration */
	private static final int SCROLLING_DURATION = 400;

	/** Minimum delta for scrolling */
	private static final int MIN_DELTA_FOR_SCROLLING = 1;

	/** Current value & label text color */
	private static final int VALUE_TEXT_COLOR = 0xFFfe7003;

	/** Items text color */
	private static final int ITEMS_TEXT_COLOR = 0xDD333333;

	/** Additional items height (is added to standard text item height) */
	private float _ITEM_HEIGHT = 15.0f;

	private static float ADDITIONAL_ITEM_HEIGHT = 15;

	/** Text size */
	private int TEXT_SIZE = 15;

	private int ITEM_TEXT_SIZE = TEXT_SIZE;
	private int VALUE_TEXT_SIZE = TEXT_SIZE;
	private int LABEL_TEXT_SIZE = TEXT_SIZE;

	/** Top and bottom items offset (to hide that) */
	private final int ITEM_OFFSET = TEXT_SIZE / 5;
	/** Additional items width (is added to standard text item width) */
	private float _ITEM_WIDTH = 24.0f;
	/** Additional width for items layout */
	private static int ADDITIONAL_ITEMS_SPACE = 40;

	/** Label offset */
	private static final int LABEL_OFFSET = 5;

	/** Default count of visible items */
	private static final int DEF_VISIBLE_ITEMS = 3;

	// Wheel Values
	private WheelAdapter adapter = null;
	private int currentItem = 0;

	// Widths
	private int itemsWidth = 0;

	// Count of visible items
	private int visibleItems = DEF_VISIBLE_ITEMS;

	// Item height
	private int itemHeight = 0;

	// Text paints
	private TextPaint itemsPaint;
	private TextPaint valuePaint;
	private TextPaint labelPaint;

	/** 设置文本 */
	private int mItemsColor = ITEMS_TEXT_COLOR; // 条目颜色
	private int mValueColor = VALUE_TEXT_COLOR; // 选中项颜色
	private int mLabelColor = VALUE_TEXT_COLOR;

	// /////////////////////////////////////////

	// Layouts
	private StaticLayout itemsLayout;
	private StaticLayout labelLayout;
	private StaticLayout valueLayout;

	// Label & background
	private String label;
	private Drawable centerDrawable;

	// Scrolling
	private boolean isScrollingPerformed;
	private int scrollingOffset;

	// Scrolling animation
	private GestureDetector gestureDetector;
	private Scroller scroller;
	private int lastScrollY;

	// Cyclic
	boolean isCyclic = true;

	// Listeners
	private List<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener>();
	private List<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();

	private int mWheelVelId;

	/**
	 * Constructor
	 */
	public WheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mWheelVelId = attrs.getAttributeResourceValue(null, "vel", -1);

		initData(context);
	}

	/**
	 * Constructor
	 */
	public WheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mWheelVelId = attrs.getAttributeResourceValue(null, "vel", -1);
		// attrs.getAttributeResourceValue(namespace, attribute, defaultValue)

		initData(context);
	}

	/**
	 * Constructor
	 */
	public WheelView(Context context) {
		super(context);
		initData(context);
	}

	/**
	 * Initializes class data
	 * 
	 * @param context
	 *            the context
	 */
	private void initData(Context context) {
		gestureDetector = new GestureDetector(context, gestureListener);
		gestureDetector.setIsLongpressEnabled(false);
		scroller = new Scroller(context);
		final float scale = getResources().getDisplayMetrics().density;
		ADDITIONAL_ITEM_HEIGHT = (int) (_ITEM_HEIGHT * scale + 0.5f);
		ADDITIONAL_ITEMS_SPACE = (int) (_ITEM_WIDTH * scale + 0.5f);
	}

	/**
	 * Gets wheel adapter
	 * 
	 * @return the adapter
	 */
	public WheelAdapter getAdapter() {
		return adapter;
	}

	/**
	 * Sets wheel adapter
	 * 
	 * @param adapter
	 *            the new wheel adapter
	 */
	public void setAdapter(WheelAdapter adapter) {
		this.adapter = adapter;
		// 消息传递到回调
		notifyChangingListeners(currentItem, currentItem);
		invalidateLayouts();
		invalidate();
	}

	/**
	 * Set the the specified scrolling interpolator
	 * 
	 * @param interpolator
	 *            the interpolator
	 */
	public void setInterpolator(Interpolator interpolator) {
		scroller.forceFinished(true);
		scroller = new Scroller(getContext(), interpolator);
	}

	/**
	 * Gets count of visible items
	 * 
	 * @return the count of visible items
	 */
	public int getVisibleItems() {
		return visibleItems;
	}

	/**
	 * Sets count of visible items
	 * 
	 * @param count
	 *            the new count
	 */
	public void setVisibleItems(int count) {
		visibleItems = count;
		invalidate();
	}

	/**
	 * Gets label
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets label
	 * 
	 * @param newLabel
	 *            the label to set
	 */
	public void setLabel(String newLabel) {
		if (label == null || !label.equals(newLabel)) {
			label = newLabel;
			labelLayout = null;
			invalidate();
		}
	}

	/**
	 * Adds wheel changing listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addChangingListener(OnWheelChangedListener listener) {
		changingListeners.add(listener);
	}

	/**
	 * Removes wheel changing listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeChangingListener(OnWheelChangedListener listener) {
		changingListeners.remove(listener);
	}

	/**
	 * Notifies changing listeners
	 * 
	 * @param oldValue
	 *            the old wheel value
	 * @param newValue
	 *            the new wheel value
	 */
	protected void notifyChangingListeners(int oldValue, int newValue) {
		for (OnWheelChangedListener listener : changingListeners) {
			listener.onChanged(this, oldValue, newValue);
		}
	}

	/**
	 * Adds wheel scrolling listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.add(listener);
	}

	/**
	 * Removes wheel scrolling listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.remove(listener);
	}

	/**
	 * Notifies listeners about starting scrolling
	 */
	protected void notifyScrollingListenersAboutStart() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingStarted(this);
		}
	}

	/**
	 * Notifies listeners about ending scrolling
	 */
	protected void notifyScrollingListenersAboutEnd() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingFinished(this);
		}
	}

	/**
	 * Gets current value
	 * 
	 * @return the current value
	 */
	public int getCurrentItem() {
		return currentItem;
	}

	/**
	 * Sets the current item. Does nothing when index is wrong.
	 * 
	 * @param index
	 *            the item index
	 * @param animated
	 *            the animation flag
	 */
	public void setCurrentItem(int index, boolean animated) {
		if (adapter == null || adapter.getItemsCount() == 0) {
			return; // throw?
		}
		if (index < 0 || index >= adapter.getItemsCount()) {
			if (isCyclic) {
				while (index < 0) {
					index += adapter.getItemsCount();
				}
				index %= adapter.getItemsCount();
			} else {
				return; // throw?
			}
		}
		if (index != currentItem) {
			if (animated) {
				scroll(index - currentItem, SCROLLING_DURATION);
			} else {
				invalidateLayouts();

				int old = currentItem;
				currentItem = index;

				notifyChangingListeners(old, currentItem);

				invalidate();
			}
		}
	}

	/**
	 * Sets the current item w/o animation. Does nothing when index is wrong.
	 * 
	 * @param index
	 *            the item index
	 */
	public void setCurrentItem(int index) {
		setCurrentItem(index, false);
	}

	/**
	 * Tests if wheel is cyclic. That means before the 1st item there is shown
	 * the last one
	 * 
	 * @return true if wheel is cyclic
	 */
	public boolean isCyclic() {
		return isCyclic;
	}

	/**
	 * Set wheel cyclic flag
	 * 
	 * @param isCyclic
	 *            the flag to set
	 */
	public void setCyclic(boolean isCyclic) {
		this.isCyclic = isCyclic;

		invalidate();
		invalidateLayouts();
	}

	/**
	 * Invalidates layouts
	 */
	private void invalidateLayouts() {
		itemsLayout = null;
		valueLayout = null;
		scrollingOffset = 0;
	}

	/**
	 * setItemTextSize
	 * 
	 * @param size
	 */
	public void setItemTextSize(int size) {
		ITEM_TEXT_SIZE = size;
	}

	/**
	 * setValueTextSize
	 * 
	 * @param size
	 */
	public void setValueTextSize(int size) {
		VALUE_TEXT_SIZE = size;
	}

	public void setLabelTextSize(int size) {
		LABEL_TEXT_SIZE = size;
	}

	/**
	 * getFirstItem
	 * 
	 * @return the item of first
	 */
	public Object getFirstItem() {
		return adapter.getFirstItem();
	}

	/**
	 * getLastItem
	 * 
	 * @return the item of last
	 */
	public Object getLastItem() {
		return adapter.getLastItem();
	}

	/**
	 * Initializes resources
	 */
	private void initResourcesIfNecessary() {
		if (itemsPaint == null) {
			itemsPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
			final float fontScale = getResources().getDisplayMetrics().scaledDensity;
			// if (ITEM_TEXT_SIZE > 17) {
			// ITEM_TEXT_SIZE = 17;
			// }
			int size = (int) (ITEM_TEXT_SIZE * fontScale + 0.5f);
			itemsPaint.setTextSize(size);
		}

		if (valuePaint == null) {
			valuePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
			final float fontScale = getResources().getDisplayMetrics().scaledDensity;
			// if (VALUE_TEXT_SIZE > 17) {
			// VALUE_TEXT_SIZE = 17;
			// }
			int size = (int) (VALUE_TEXT_SIZE * fontScale + 0.5f);
			valuePaint.setTextSize(size);
			// valuePaint.setShadowLayer(0.1f, 0, 0.1f, 0xFFC0C0C0); // 设置阴影
		}

		if (labelPaint == null) {
			final float fontScale = getResources().getDisplayMetrics().scaledDensity;
			labelPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
			int size = (int) (LABEL_TEXT_SIZE * fontScale + 0.5f);
			labelPaint.setTextSize(size);
		}
		if (centerDrawable == null) {
			if (mWheelVelId != -1)
				centerDrawable = getContext().getResources().getDrawable(
						mWheelVelId);
		}

	}

	/**
	 * Calculates desired height for layout
	 * 
	 * @param layout
	 *            the source layout
	 * @return the desired layout height
	 */
	private int getDesiredHeight(Layout layout) {
		if (layout == null) {
			return 0;
		}

		float desired = getItemHeight() * visibleItems - ITEM_OFFSET * 2
				- ADDITIONAL_ITEM_HEIGHT;

		// Check against our minimum height
		desired = Math.max(desired, getSuggestedMinimumHeight());

		return (int) desired;
	}

	/**
	 * Returns text item by index
	 * 
	 * @param index
	 *            the item index
	 * @return the item or null
	 */
	private String getTextItem(int index) {
		if (adapter == null || adapter.getItemsCount() == 0) {
			return null;
		}
		int count = adapter.getItemsCount();
		if ((index < 0 || index >= count) && !isCyclic) {
			return null;
		} else {
			while (index < 0) {
				index = count + index;
			}
		}

		index %= count;
		return adapter.getItem(index);
	}

	/**
	 * Builds text depending on current value
	 * 
	 * @param useCurrentValue
	 * @return the text
	 */
	private String buildText(boolean useCurrentValue) {
		StringBuilder itemsText = new StringBuilder();
		int addItems = visibleItems / 2 + 1;

		for (int i = currentItem - addItems; i <= currentItem + addItems; i++) {
			if (useCurrentValue || i != currentItem) {
				String text = getTextItem(i);
				if (text != null) {
					itemsText.append(text);
				}
			}
			if (i < currentItem + addItems) {
				itemsText.append("\n");
			}
		}
		return itemsText.toString();
	}

	/**
	 * Returns the max item length that can be present
	 * 
	 * @return the max length
	 */
	private int getMaxTextLength() {
		WheelAdapter adapter = getAdapter();
		if (adapter == null) {
			return 0;
		}

		int adapterLength = adapter.getMaximumLength();
		if (adapterLength > 0) {
			return adapterLength;
		}

		String maxText = null;
		int addItems = visibleItems / 2;
		for (int i = Math.max(currentItem - addItems, 0); i < Math.min(
				currentItem + visibleItems, adapter.getItemsCount()); i++) {
			String text = adapter.getItem(i);
			if (text != null
					&& (maxText == null || maxText.length() < text.length())) {
				maxText = text;
			}
		}

		return maxText != null ? maxText.length() : 0;
	}

	/**
	 * Returns height of wheel item
	 * 
	 * @return the item height
	 */
	private int getItemHeight() {
		if (itemHeight != 0) {
			return itemHeight;
		} else if (itemsLayout != null && itemsLayout.getLineCount() > 2) {
			itemHeight = itemsLayout.getLineTop(2) - itemsLayout.getLineTop(1);
			return itemHeight;
		}

		return getHeight() / visibleItems;
	}

	/**
	 * Calculates control width and creates text layouts
	 * 
	 * @param widthSize
	 *            the input layout width
	 * @param mode
	 *            the layout mode
	 * @return the calculated control width
	 */
	private int calculateLayoutWidth(int widthSize, int mode) {
		initResourcesIfNecessary();

		int width = widthSize;

		int maxLength = getMaxTextLength();
		if (maxLength > 0) {
			float textWidth = (float)Math.ceil(Layout.getDesiredWidth("0",
					itemsPaint));
			itemsWidth = (int) (maxLength * textWidth);
		} else {
			itemsWidth = 0;
		}
		itemsWidth += ADDITIONAL_ITEMS_SPACE; // make it some more

		if (mode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width = itemsWidth;
			// Check against our minimum width
			width = Math.max(width, getSuggestedMinimumWidth());

			if (mode == MeasureSpec.AT_MOST && widthSize < width) {
				width = widthSize;
			}
		}

		if (itemsWidth > 0) {
			createLayouts(itemsWidth);
		}

		return width;
	}

	/**
	 * Creates layouts
	 * 
	 * @param widthItems
	 *            width of items layout
	 */
	private void createLayouts(int widthItems) {
		if (itemsLayout == null || itemsLayout.getWidth() > widthItems) {
			itemsLayout = new StaticLayout(buildText(isScrollingPerformed),
					itemsPaint, widthItems, Layout.Alignment.ALIGN_CENTER, 1,
					ADDITIONAL_ITEM_HEIGHT, false);
		} else {
			itemsLayout.increaseWidthTo(widthItems);
		}

		if (!isScrollingPerformed
				&& (valueLayout == null || valueLayout.getWidth() > widthItems)) {
			String text = getAdapter() != null ? getAdapter().getItem(
					currentItem) : null;
			valueLayout = new StaticLayout(text != null ? text : "",
					valuePaint, widthItems, Layout.Alignment.ALIGN_CENTER, 1,
					ADDITIONAL_ITEM_HEIGHT, false);
		} else if (isScrollingPerformed) {
			valueLayout = null;
		} else {
			valueLayout.increaseWidthTo(widthItems);
		}

		if (labelLayout == null || labelLayout.getWidth() > widthItems) {
			if (label != null) {
				labelLayout = new StaticLayout(label, labelPaint, widthItems,
						Layout.Alignment.ALIGN_NORMAL, 1,
						ADDITIONAL_ITEM_HEIGHT, false);
			}
		} else {
			labelLayout.increaseWidthTo(widthItems);
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width = calculateLayoutWidth(widthSize, widthMode);

		int height;
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = getDesiredHeight(itemsLayout);

			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(height, heightSize);
			}
		}

		setMeasuredDimension(width, height);
	}

	/** 设置颜色 */
	public int getmItemsColor() {
		return mItemsColor;
	}

	public void setmItemsColor(int mItemsColor) {
		this.mItemsColor = mItemsColor;
	}

	public int getmValueColor() {
		return mValueColor;
	}

	public void setmValueColor(int mValueColor) {
		this.mValueColor = mValueColor;
	}

	public void setmLabelColor(int mLabelColor) {
		this.mLabelColor = mLabelColor;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (itemsLayout == null) {
			if (itemsWidth == 0) {
				calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
			} else {
				createLayouts(itemsWidth);
			}
		}

		drawCenterRect(canvas);
		if (itemsWidth > 0) {
			canvas.save();
			drawItems(canvas);
			drawValue(canvas);
			drawLabel(canvas);
			canvas.restore();
		}
	}

	public void drawLabel(Canvas canvas) {

		labelPaint.setColor(mLabelColor);
		labelPaint.drawableState = getDrawableState();

		if (labelLayout != null) {
			float balancedY = Math
					.abs((getHeight() - labelLayout.getHeight()) / 2.0f);
			canvas.save();
			// canvas.translate(itemsLayout.getWidth() + LABEL_OFFSET ,
			// bounds.top );

			canvas.translate(itemsLayout.getWidth() + LABEL_OFFSET, balancedY
					+ ADDITIONAL_ITEM_HEIGHT / 2);
			labelLayout.draw(canvas);
			canvas.restore();
		}
	}

	/**
	 * Draws value and label layout
	 * 
	 * @param canvas
	 *            the canvas for drawing
	 */
	private void drawValue(Canvas canvas) {
		valuePaint.setColor(mValueColor);
		valuePaint.drawableState = getDrawableState();

		Rect bounds = new Rect();
		itemsLayout.getLineBounds(visibleItems / 2, bounds);

		// draw current value
		if (valueLayout != null) {

			float balancedY = Math
					.abs((getHeight() - valueLayout.getHeight()) / 2.0f);
			canvas.save();
			canvas.translate((getWidth() - itemsLayout.getWidth()) / 3,
					balancedY + scrollingOffset + ADDITIONAL_ITEM_HEIGHT / 2);
			valueLayout.draw(canvas);
			canvas.restore();
		}
	}

	/**
	 * Draws items
	 * 
	 * @param canvas
	 *            the canvas for drawing
	 */
	private void drawItems(Canvas canvas) {
		canvas.save();

		float balancedY = Math
				.abs((getHeight() - itemsLayout.getHeight()) / 2.0f);

		canvas.translate((getWidth() - itemsLayout.getWidth()) / 3,
				scrollingOffset - balancedY + ADDITIONAL_ITEM_HEIGHT / 2);
		itemsPaint.setColor(mItemsColor);
		itemsPaint.drawableState = getDrawableState();
		itemsLayout.draw(canvas);

		canvas.restore();
	}

	/**
	 * Draws rect for current value
	 * 
	 * @param canvas
	 *            the canvas for drawing
	 */
	private void drawCenterRect(Canvas canvas) {

		if (centerDrawable != null) {
			int center = getHeight() / 2;
			centerDrawable.setBounds(0,
					center - centerDrawable.getIntrinsicHeight() / 2,
					getWidth(), centerDrawable.getIntrinsicHeight() + center
							- centerDrawable.getIntrinsicHeight() / 2);
			centerDrawable.draw(canvas);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		WheelAdapter adapter = getAdapter();
		if (adapter == null) {
			return true;
		}

		if (!gestureDetector.onTouchEvent(event)
				&& event.getAction() == MotionEvent.ACTION_UP) {
			justify();
		}
		return true;
	}

	/**
	 * Scrolls the wheel
	 * 
	 * @param delta
	 *            the scrolling value
	 */
	private void doScroll(int delta) {
		scrollingOffset += delta;

		int count = scrollingOffset / getItemHeight();
		int pos = currentItem - count;
		if (isCyclic && adapter.getItemsCount() > 0) {
			// fix position by rotating
			while (pos < 0) {
				pos += adapter.getItemsCount();
			}
			pos %= adapter.getItemsCount();
		} else if (isScrollingPerformed) {
			//
			if (pos < 0) {
				count = currentItem;
				pos = 0;
			} else if (pos >= adapter.getItemsCount()) {
				count = currentItem - adapter.getItemsCount() + 1;
				pos = adapter.getItemsCount() - 1;
			}
		} else {
			// fix position
			pos = Math.max(pos, 0);
			pos = Math.min(pos, adapter.getItemsCount() - 1);
		}

		int offset = scrollingOffset;
		if (pos != currentItem) {
			setCurrentItem(pos, false);
		} else {
			invalidate();
		}

		// update offset
		scrollingOffset = offset - count * getItemHeight();
		if (scrollingOffset > getHeight()) {
			scrollingOffset = scrollingOffset % getHeight() + getHeight();
		}
	}

	// gesture listener
	private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
		@Override
		public boolean onDown(MotionEvent e) {
			if (isScrollingPerformed) {
				scroller.forceFinished(true);
				clearMessages();
				return true;
			}
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			startScrolling();
			doScroll((int) -distanceY);
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			lastScrollY = currentItem * getItemHeight() + scrollingOffset;
			int maxY = isCyclic ? 0x7FFFFFFF : adapter.getItemsCount()
					* getItemHeight();
			int minY = isCyclic ? -maxY : 0;
			scroller.fling(0, lastScrollY, 0, (int) -velocityY / 2, 0, 0, minY,
					maxY);
			setNextMessage(MESSAGE_SCROLL);
			return true;
		}
	};

	// Messages
	private final int MESSAGE_SCROLL = 0;
	private final int MESSAGE_JUSTIFY = 1;

	/**
	 * Set next message to queue. Clears queue before.
	 * 
	 * @param message
	 *            the message to set
	 */
	private void setNextMessage(int message) {
		clearMessages();
		animationHandler.sendEmptyMessage(message);
	}

	/**
	 * Clears messages from queue
	 */
	private void clearMessages() {
		animationHandler.removeMessages(MESSAGE_SCROLL);
		animationHandler.removeMessages(MESSAGE_JUSTIFY);
	}

	// animation handler
	private Handler animationHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			scroller.computeScrollOffset();
			int currY = scroller.getCurrY();
			int delta = lastScrollY - currY;
			lastScrollY = currY;
			if (delta != 0) {
				doScroll(delta);
			}

			// scrolling is not finished when it comes to final Y
			// so, finish it manually
			if (Math.abs(currY - scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
				currY = scroller.getFinalY();
				scroller.forceFinished(true);
			}
			if (!scroller.isFinished()) {
				animationHandler.sendEmptyMessage(msg.what);
			} else if (msg.what == MESSAGE_SCROLL) {
				justify();
			} else {
				finishScrolling();
			}
		}
	};

	/**
	 * Justifies wheel
	 */
	private void justify() {
		if (adapter == null) {
			return;
		}

		lastScrollY = 0;
		int offset = scrollingOffset;
		int itemHeight = getItemHeight();
		boolean needToIncrease = offset > 0 ? currentItem < adapter
				.getItemsCount() : currentItem > 0;
		if ((isCyclic || needToIncrease)
				&& Math.abs((float) offset) > (float) itemHeight / 2) {
			if (offset < 0)
				offset += itemHeight + MIN_DELTA_FOR_SCROLLING;
			else
				offset -= itemHeight + MIN_DELTA_FOR_SCROLLING;
		}
		if (Math.abs(offset) > MIN_DELTA_FOR_SCROLLING) {
			scroller.startScroll(0, 0, 0, offset, SCROLLING_DURATION);
			setNextMessage(MESSAGE_JUSTIFY);
		} else {
			finishScrolling();
		}
	}

	/**
	 * Starts scrolling
	 */
	private void startScrolling() {
		if (!isScrollingPerformed) {
			isScrollingPerformed = true;
			notifyScrollingListenersAboutStart();
		}
	}

	/**
	 * Finishes scrolling
	 */
	void finishScrolling() {
		if (isScrollingPerformed) {
			notifyScrollingListenersAboutEnd();
			isScrollingPerformed = false;
		}
		invalidateLayouts();
		invalidate();
	}

	/**
	 * Scroll the wheel
	 * 
	 * @param itemsToScroll
	 *            items to scroll
	 * @param time
	 *            scrolling duration
	 */
	public void scroll(int itemsToScroll, int time) {
		scroller.forceFinished(true);

		lastScrollY = scrollingOffset;
		int offset = itemsToScroll * getItemHeight();

		scroller.startScroll(0, lastScrollY, 0, offset - lastScrollY, time);
		setNextMessage(MESSAGE_SCROLL);

		startScrolling();
	}

}
