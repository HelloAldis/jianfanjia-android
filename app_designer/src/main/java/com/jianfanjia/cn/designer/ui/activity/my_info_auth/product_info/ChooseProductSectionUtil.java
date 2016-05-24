package com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info;

import android.content.Context;

import com.jianfanjia.cn.designer.view.ChooseProductSectionDialog;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-24 13:57
 */
public class ChooseProductSectionUtil {

    private static ChooseProductSectionDialog sChooseProductSectionDialog;

    public static void show(Context context,ChooseProductSectionDialog.ChooseItemListener chooseItemListener){
        if(sChooseProductSectionDialog != null && sChooseProductSectionDialog.isShowing()){
            return;
        }

        sChooseProductSectionDialog = new ChooseProductSectionDialog(context,chooseItemListener);
        sChooseProductSectionDialog.show();
    }

    public static void dismiss(){
        if(sChooseProductSectionDialog != null && sChooseProductSectionDialog.isShowing()){
            sChooseProductSectionDialog.dismiss();
            sChooseProductSectionDialog = null;
        }
    }
}
