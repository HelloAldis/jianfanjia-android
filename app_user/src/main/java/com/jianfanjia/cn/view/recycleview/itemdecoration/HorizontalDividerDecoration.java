package com.jianfanjia.cn.view.recycleview.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Description: com.jianfanjia.cn.view.baseview
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-01 14:46
 */
public class HorizontalDividerDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int firstSpace;
    private int lastSpace;

    public HorizontalDividerDecoration(int space) {
        this(space,space,space);
    }

    public HorizontalDividerDecoration(int dividerSpace,int firstSpace,int lastSpace){
        this.firstSpace = firstSpace;
        this.lastSpace = lastSpace;
        this.space = dividerSpace;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.right = 0;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = firstSpace;
        }else{
            outRect.top = 0;
        }
        outRect.bottom = space;
        if(parent.getChildAdapterPosition(view) == state.getItemCount() -1){
            outRect.bottom = lastSpace;
        }else{
            outRect.bottom = space;
        }
    }
}
