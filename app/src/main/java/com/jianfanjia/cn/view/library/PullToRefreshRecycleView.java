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
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;

import com.jianfanjia.cn.activity.R;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

public class PullToRefreshRecycleView extends PullToRefreshBase<RecyclerView> {

	public PullToRefreshRecycleView(Context context) {
		super(context);
	}

	public PullToRefreshRecycleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshRecycleView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshRecycleView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
		RecyclerView recyclerView = new RecyclerView(context,attrs);
		final LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
		// 创建一个线性布局管理器
		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setItemAnimator(new FadeInUpAnimator(new DecelerateInterpolator(0.5F)));
		recyclerView.setId(R.id.recycleview);
		return recyclerView;
	}

	public void setAdapter(RecyclerView.Adapter adapter){
		if(mRefreshableView != null){
			mRefreshableView.setAdapter(adapter);
		}
	}

	public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator){
		if(mRefreshableView != null){
			mRefreshableView.setItemAnimator(itemAnimator);
		}
	}

	public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration){
		if(mRefreshableView != null){
			mRefreshableView.addItemDecoration(itemDecoration);
		}
	}

	@Override
	protected boolean isReadyForPullStart() {
		return !ViewCompat.canScrollVertically(mRefreshableView, -1);
	}

	@Override
	protected boolean isReadyForPullEnd() {
		return false;
	}
}
