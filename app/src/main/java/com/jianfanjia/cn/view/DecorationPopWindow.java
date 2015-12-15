package com.jianfanjia.cn.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.PopWindowAdapter;
import com.jianfanjia.cn.interf.GetItemCallback;

import java.util.List;

/**
 * Name: DecorationPopWindow
 * User: fengliang
 * Date: 2015-12-14
 * Time: 15:56
 */
public class DecorationPopWindow extends PopupWindow {
    private LayoutInflater inflater = null;
    private View popView = null;
    private GridView gridView = null;
    private PopWindowAdapter adapter = null;
    private WindowManager.LayoutParams lp = null;
    private Window window = null;

    public DecorationPopWindow(Activity activity, List<String> list, GetItemCallback callback) {
        super(activity);
        inflater = LayoutInflater.from(activity);
        popView = inflater.inflate(R.layout.gird_item_pop, null);
        gridView = (GridView) popView.findViewById(R.id.popGridview);
        adapter = new PopWindowAdapter(activity, list, callback);
        gridView.setAdapter(adapter);
        this.setContentView(popView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.popwindow_anim);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
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
            }
        });
    }

    public void show(View view) {
        showAsDropDown(view, 0, 0);
    }
}
