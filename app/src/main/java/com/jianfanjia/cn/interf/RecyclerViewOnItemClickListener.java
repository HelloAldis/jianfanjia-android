package com.jianfanjia.cn.interf;

import android.view.View;

/**
 * Created by Administrator on 2015/11/26.
 */
public interface RecyclerViewOnItemClickListener {
    void OnItemClick(View view, int position);

    void OnLongItemClick(View view, int position);

    void OnViewClick(int position);
}
