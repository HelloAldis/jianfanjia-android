package com.jianfanjia.cn.view.custom_annotation_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.widget.ImageView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.view.baseview.BaseAnnotationView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.view.custom_annotation_view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-16 09:39
 */
@EViewGroup(R.layout.grid_item_req_item)
public class RequirementItemLovestyleView extends BaseAnnotationView {

    @ViewById(R.id.gtm_req_image)
    ImageView gtm_req_image;

    public RequirementItemLovestyleView(Context context) {
        super(context);
    }

    public void bind(ReqItemFinderImp.ItemMap itemMap) {
        TypedArray ta = getResources().obtainTypedArray(R.array.arr_lovestyle_pic);
        String imageId = "drawable://" + ta.getResourceId(Integer.parseInt(itemMap.key), 0);
        imageShow.displayLocalImage(imageId, gtm_req_image);
        ta.recycle();
    }
}
