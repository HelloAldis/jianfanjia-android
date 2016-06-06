package com.jianfanjia.cn.designer.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.jianfanjia.cn.designer.R;

/**
 * Description: com.jianfanjia.cn.designer.view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-03 16:30
 */
public class DesignerAuthCommitDialog extends Dialog {

    public DesignerAuthCommitDialog(Context context, View.OnClickListener listener) {
        super(context);
        initWindow();
        setListener(listener);
    }

    private void initWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_designer_auth_commit);
        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
    }

    private void setListener(final View.OnClickListener listener) {
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                DesignerAuthCommitDialog.this.dismiss();
            }
        });

        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DesignerAuthCommitDialog.this.dismiss();
            }
        });
    }

}
