package me.iwf.photopicker.widget;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

/**
 * Name: SpacesItemDecoration
 * User: fengliang
 * Date: 2015-12-14
 * Time: 15:00
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        Log.d(this.getClass().getName(),"spanCount =" + spanCount);
        return spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int spanPos = parent.getChildAdapterPosition(view) % getSpanCount(parent);

        switch (spanPos) {
            case 0:
                outRect.left = space;
                outRect.top = space;
                outRect.bottom = 0;
                outRect.right = 0;
                break;
            case 1:
                outRect.left = space;
                outRect.top = space;
                outRect.bottom = 0;
                outRect.right = 0;
                break;
            case 2:
                outRect.left = space;
                outRect.top = space;
                outRect.bottom = 0;
                outRect.right = space;
                break;

        }

    }
}
