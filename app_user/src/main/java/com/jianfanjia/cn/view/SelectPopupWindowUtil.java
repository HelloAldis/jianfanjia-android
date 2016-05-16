package com.jianfanjia.cn.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import java.util.List;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.ui.adapter.PopWindowAdapter;
import com.jianfanjia.cn.ui.interf.GetItemCallback;
import com.jianfanjia.cn.tools.BusinessCovertUtil;

/**
 * Description: com.jianfanjia.cn.view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-04 15:17
 */
public class SelectPopupWindowUtil {

    private Context mContext;
    private PopupWindow mPopupWindow;
    private View popView = null;
    private GridView gridView = null;
    private PopWindowAdapter adapter = null;

    public SelectPopupWindowUtil(Context context) {
        this.mContext = context;
        initPopupWindow();
    }

    private void initPopupWindow() {
        mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setAnimationStyle(0);
        ColorDrawable dw = new ColorDrawable(0x44000000);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
    }

    public void showAsDropDown(View view, int resId, final GetItemCallback callback, int currentPosition) {
        if (mPopupWindow != null) {
            popView = LayoutInflater.from(mContext).inflate(R.layout.gird_item_pop, null);
            popView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPopupWindow != null) {
                        mPopupWindow.dismiss();
                    }
                }
            });
            gridView = (GridView) popView.findViewById(R.id.popGridview);
            final List<String> list = BusinessCovertUtil.getListByResource(mContext, resId);
            adapter = new PopWindowAdapter(mContext, list);
            gridView.setAdapter(adapter);
            adapter.setSelectedPos(currentPosition);
            gridView.post(new Runnable() {
                @Override
                public void run() {
                    gridView.setTranslationY(-gridView.getHeight());
                    gridView.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).setDuration(400)
                            .setStartDelay(100);
                }
            });
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String title = list.get(position);
                    callback.onItemCallback(position, title);
                    if (mPopupWindow != null) {
                        mPopupWindow.dismiss();
                    }
                }
            });
            mPopupWindow.setContentView(popView);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                // 在dismiss中恢复透明度
                @Override
                public void onDismiss() {
                    callback.onDismissCallback();
                }
            });
            mPopupWindow.showAsDropDown(view, 0, 0);
        }
    }


}
