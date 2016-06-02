package com.jianfanjia.cn.activity.view.recycleview.itemdecoration;

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
    private int firstAndLastSpace;

    public HorizontalDividerDecoration(int space) {
        this(space,space);
    }

    public HorizontalDividerDecoration(int dividerSpace,int firstAndLastSpace){
        this.firstAndLastSpace = firstAndLastSpace;
        this.space = dividerSpace;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.right = 0;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = firstAndLastSpace;
        }else{
            outRect.top = 0;
        }
        if(parent.getChildAdapterPosition(view) == state.getItemCount() -1){
            outRect.bottom = firstAndLastSpace;
        }else{
            outRect.bottom = space;
        }
    }
}
