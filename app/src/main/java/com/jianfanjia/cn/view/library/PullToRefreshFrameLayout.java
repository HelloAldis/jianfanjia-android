/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.jianfanjia.cn.view.library;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.jianfanjia.cn.tools.LogTool;

public class PullToRefreshFrameLayout extends PullToRefreshBase<FrameLayout> {

	public PullToRefreshFrameLayout(Context context) {
		super(context);
	}

	public PullToRefreshFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshFrameLayout(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshFrameLayout(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected FrameLayout createRefreshableView(Context context, AttributeSet attrs) {
		FrameLayout frameLayout = new FrameLayout(context,attrs);
		return frameLayout;
	}

	@Override
	protected boolean isReadyForPullStart() {
		return mRefreshableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullEnd() {
		LogTool.d(this.getClass().getName(),"isReadyForPullEnd");
		return false;
	}

}
