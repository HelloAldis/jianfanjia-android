/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.jianfanjia.cn.designer.view.library;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.application.MyApplication;

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
        RecyclerView recyclerView = new RecyclerView(context, attrs);
        recyclerView.setId(R.id.recycleview);
        recyclerView.setPadding(0, MyApplication.dip2px(context, 10), 0, 0);//4个参数按顺序分别是左上右下
        return recyclerView;
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        if (onScrollListener != null) {
            mRefreshableView.addOnScrollListener(onScrollListener);
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager mLayoutManager) {
        if (mLayoutManager != null) {
            mRefreshableView.setLayoutManager(mLayoutManager);
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mRefreshableView.setAdapter(adapter);
        }
    }

    public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
        if (itemAnimator != null) {
            mRefreshableView.setItemAnimator(itemAnimator);
        }
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration){
        if(itemDecoration != null){
            mRefreshableView.removeItemDecoration(itemDecoration);
        }
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (itemDecoration != null) {
            mRefreshableView.addItemDecoration(itemDecoration);
        }
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        mRefreshableView.setHasFixedSize(hasFixedSize);
    }

    public void scrollToPosition(int position) {
        if (position > -1) {
            mRefreshableView.scrollToPosition(position);
        }
    }

    @Override
    protected boolean isReadyForPullStart() {
        return !ViewCompat.canScrollVertically(mRefreshableView, -1);
    }

    @Override
    protected boolean isReadyForPullEnd() {
        return !ViewCompat.canScrollVertically(mRefreshableView, 0);
    }
}