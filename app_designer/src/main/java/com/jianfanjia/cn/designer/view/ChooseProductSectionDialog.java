package com.jianfanjia.cn.designer.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jianfanjia.cn.designer.R;

/**
 * Description: com.jianfanjia.cn.designer.view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-24 11:32
 */
public class ChooseProductSectionDialog extends Dialog {

    ListView mListView;
    String[] productSection;
    private ChooseItemListener mChooseItemListener;

    public ChooseProductSectionDialog(Context context,ChooseItemListener listener) {
        super(context);
        productSection = context.getResources().getStringArray(R.array.arr_product_section);
        this.mChooseItemListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_choose_product_section);

        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        //设置窗口宽度为充满全屏
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.dimAmount = 0.5f;

        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);

        setCanceledOnTouchOutside(true);

        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.choose_section_recyclerview);
        mListView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, productSection));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mChooseItemListener != null){
                    mChooseItemListener.chooseItem(productSection[position]);
                }
                dismiss();
            }
        });
    }

    public interface ChooseItemListener{
        public void chooseItem(String section);
    }
}
