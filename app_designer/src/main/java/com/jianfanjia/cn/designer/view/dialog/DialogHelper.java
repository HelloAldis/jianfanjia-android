package com.jianfanjia.cn.designer.view.dialog;

import android.content.Context;

import com.jianfanjia.cn.designer.R;

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
