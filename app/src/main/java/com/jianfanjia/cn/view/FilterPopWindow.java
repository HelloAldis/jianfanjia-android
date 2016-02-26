package com.jianfanjia.cn.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.PopWindowAdapter;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.interf.GetItemCallback;

import java.util.List;

/**
 * Name: FilterPopWindow
 * User: fengliang
 * Date: 2016-02-22
 * Time: 09:25
 */
public class FilterPopWindow extends PopupWindow {
    private static final String TAG = FilterPopWindow.class.getName();
    private View popView = null;
    private GridView gridView = null;
    private PopWindowAdapter adapter = null;
    private WindowManager.LayoutParams lp = null;
    private Window window = null;

    public FilterPopWindow(Activity activity, int resId, final GetItemCallback callback, int currentPosition) {
        super(activity);
        popView = LayoutInflater.from(activity).inflate(R.layout.gird_item_pop, null);
        popView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        gridView = (GridView) popView.findViewById(R.id.popGridview);
        final List<String> list = BusinessManager.getListByResource(activity, resId);
        adapter = new PopWindowAdapter(activity, list);
        gridView.setAdapter(adapter);
        adapter.setSelectedPos(currentPosition);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = list.get(position);
                callback.onItemCallback(position, title);
            }
        });
        this.setContentView(popView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
//        this.setAnimationStyle(R.style.popwindow_anim);
        ColorDrawable dw = new ColorDrawable(0x80000000);
        this.setBackgroundDrawable(dw);
        // 产生背景变暗效果
        lp = activity.getWindow().getAttributes();
        window = activity.getWindow();
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setOnDismissListener(new OnDismissListener() {

            // 在dismiss中恢复透明度
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                window.setAttributes(lp);
                callback.onDismissCallback();
            }
        });
    }

    public void show(View view) {
        showAsDropDown(view, 0, 0);
    }
}
