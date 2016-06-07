package com.jianfanjia.cn.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.jianfanjia.cn.activity.R;

public class DeletePicDialog extends Dialog implements OnClickListener {
    private Button secondButton = null;
    private Button cancel = null;
    private LayoutInflater inflater = null;
    private View menuView = null;
    private WindowManager.LayoutParams lp = null;
    private Window window = null;

    public DeletePicDialog(Context context, View.OnClickListener listener) {
        super(context, R.style.Theme_Light_Dialog);

        window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.popub_anim);
        window.setGravity(Gravity.BOTTOM);

        inflater = LayoutInflater.from(context);
        menuView = inflater.inflate(R.layout.dialog_delete_pic, null);
        secondButton = (Button) menuView.findViewById(R.id.dialog_btn_delete_pic);
        cancel = (Button) menuView.findViewById(R.id.btn_cancel);
        secondButton.setOnClickListener(listener);
        cancel.setOnClickListener(this);
        // 设置SelectPicPopupWindow的View
        this.setContentView(menuView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

}
