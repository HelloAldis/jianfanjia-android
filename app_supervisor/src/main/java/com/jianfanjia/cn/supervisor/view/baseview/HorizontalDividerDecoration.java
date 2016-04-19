package com.jianfanjia.cn.supervisor.view.baseview;

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

    public HorizontalDividerDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.right = 0;
        outRect.bottom = space;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space;
        }else{
            outRect.top = 0;
        }
    }
}
