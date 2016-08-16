package com.jianfanjia.cn.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.jianfanjia.cn.activity.R;

/**
 * Description: com.jianfanjia.cn.view.dialog
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-08-16 14:00
 */
public class DesignerAppointSuccessDialog extends Dialog {

    public DesignerAppointSuccessDialog(Context context) {
        super(context);
        initWindow();
        setListener();
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


    private void setListener() {
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DesignerAppointSuccessDialog.this.dismiss();
            }
        });

    }

}

