/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.jianfanjia.cn.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.jianfanjia.cn.pulltorefresh.R;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.Mode;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.Orientation;

public class BrushLoadingLayout extends LoadingLayout {

    private List<Integer> resIdList = new ArrayList<>();

    private int resHeadIndex = 0;//头部帧图片的索引，默认为0;
    private int resHeadCount;//头部图片总帧数

    private AnimationDrawable mAnimationDrawable;


    public BrushLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);

        initResIdArray();
        resHeadCount = resIdList.size();

    }

    private void initResIdArray() {
        int arrId = getResources().getIdentifier("pull_head_icons", "array",
                getContext().getPackageName());
        TypedArray ta = getResources().obtainTypedArray(arrId);
        for (int i = 0; i < ta.length(); i++) {
            resIdList.add(ta.getResourceId(i, 0));
        }
        ta.recycle();
    }

    private void initAnimationDrawable() {
        mAnimationDrawable = new AnimationDrawable();
        for (int i = 0; i <= 2; i++) {
            //根据资源名称和目录获取R.java中对应的资源ID
            int id = getResources().getIdentifier("icon_refresh_head" + i, "drawable", getContext().getPackageName());
            //根据资源ID获取到Drawable对象
            Drawable drawable = getResources().getDrawable(id);
            //将此帧添加到AnimationDrawable中
            mAnimationDrawable.addFrame(drawable, 100);
        }
        mAnimationDrawable.setOneShot(false);
    }

    public void onLoadingDrawableSet(Drawable imageDrawable) {
        if (null != imageDrawable) {

        }
    }

    private int getResIndexByPullScale(float scaleOfLayout) {
        Log.d(this.getClass().getName(), " scale =" + scaleOfLayout);
        return (int) (scaleOfLayout * (resHeadCount - 1));
    }

    protected void onPullImpl(float scaleOfLayout) {
        if (scaleOfLayout > 1.0) {
            return;
        }
        resHeadIndex = getResIndexByPullScale(scaleOfLayout);
        Log.d(this.getClass().getName(), "resHeadIndex =" + resHeadIndex);

        mHeaderImage.setImageResource(resIdList.get(resHeadIndex));
    }

    @Override
    protected void refreshingImpl() {

        mHeaderImage.setVisibility(View.INVISIBLE);
        mHeaderProgress.setVisibility(View.VISIBLE);
    }

    @Override
    protected void resetImpl() {
        resHeadIndex = 0;

        mHeaderProgress.setVisibility(View.GONE);
        mHeaderImage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void pullToRefreshImpl() {
        // NO-OP
    }

    @Override
    protected void releaseToRefreshImpl() {
        // NO-OP
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.icon_pull_head1;
    }


}
