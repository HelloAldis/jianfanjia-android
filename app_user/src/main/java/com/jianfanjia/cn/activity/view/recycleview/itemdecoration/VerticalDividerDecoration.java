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
public class VerticalDividerDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public VerticalDividerDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = 0;
        outRect.top = 0;
        outRect.right = 0;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = 0;
        }else{
            outRect.left = space;
        }
    }
}
