package com.jianfanjia.cn.designer.view.custom_annotation_view;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.tools.ImageShow;

/**
 * Description: com.jianfanjia.cn.designer.view.custom_annotation_view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-30 19:55
 */
public class MyPlanViewTypeBase extends RecyclerView.ViewHolder {

    protected Context context;
    protected ImageShow imageShow;

    protected MyPlanViewTypeBase(View view){
        super(view);
        context = MyApplication.getInstance();
        imageShow = ImageShow.getImageShow();
    }

    protected Resources getResources(){
        return context.getResources();
    }
}
