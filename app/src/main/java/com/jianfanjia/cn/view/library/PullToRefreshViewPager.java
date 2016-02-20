<<<<<<< HEAD
=======
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
>>>>>>> jianfanjia-user
package com.jianfanjia.cn.view.library;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.jianfanjia.cn.activity.R;

<<<<<<< HEAD
/**
 * Name: PullToRefreshViewPager
 * User: fengliang
 * Date: 2016-02-17
 * Time: 10:52
 */
=======
>>>>>>> jianfanjia-user
public class PullToRefreshViewPager extends PullToRefreshBase<ViewPager> {

    public PullToRefreshViewPager(Context context) {
        super(context);
    }

    public PullToRefreshViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.HORIZONTAL;
    }

    @Override
    protected ViewPager createRefreshableView(Context context, AttributeSet attrs) {
        ViewPager viewPager = new ViewPager(context, attrs);
        viewPager.setId(R.id.viewpager);
        return viewPager;
    }

    @Override
    protected boolean isReadyForPullStart() {
        ViewPager refreshableView = getRefreshableView();
<<<<<<< HEAD

=======
>>>>>>> jianfanjia-user
        PagerAdapter adapter = refreshableView.getAdapter();
        if (null != adapter) {
            return refreshableView.getCurrentItem() == 0;
        }
<<<<<<< HEAD

=======
>>>>>>> jianfanjia-user
        return false;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        ViewPager refreshableView = getRefreshableView();
<<<<<<< HEAD

=======
>>>>>>> jianfanjia-user
        PagerAdapter adapter = refreshableView.getAdapter();
        if (null != adapter) {
            return refreshableView.getCurrentItem() == adapter.getCount() - 1;
        }
<<<<<<< HEAD

        return false;
    }
}  
=======
        return false;
    }
}
>>>>>>> jianfanjia-user
