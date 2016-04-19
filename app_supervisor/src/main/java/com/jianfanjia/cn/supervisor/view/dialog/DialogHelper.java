package com.jianfanjia.cn.supervisor.view.dialog;

import android.content.Context;

import com.jianfanjia.cn.supervisor.R;


public class DialogHelper {

    public static CommonDialog getPinterestDialog(Context context) {
        return new CommonDialog(context, R.style.common_dialog);
    }

    public static CommonDialog getPinterestDialogCancelable(Context context) {
        CommonDialog dialog = new CommonDialog(context, R.style.common_dialog);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }


}
