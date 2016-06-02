package com.jianfanjia.cn.activity.view.recycleview.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Name: ItemSpaceDecoration
 * User: fengliang
 * Date: 2016-03-04
 * Time: 15:16
 */
public class ItemSpaceDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public ItemSpaceDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = space;
        outRect.bottom = space;
        outRect.left = space;
        if (parent.getChildAdapterPosition(view) == 1 || parent.getChildAdapterPosition(view) == 2) {
            outRect.top = 0;
        } else {
            outRect.top = space;
        }
    }
}  
