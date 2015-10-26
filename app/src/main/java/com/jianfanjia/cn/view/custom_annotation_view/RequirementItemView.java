package com.jianfanjia.cn.view.custom_annotation_view;

import android.content.Context;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.interf.cutom_annotation.ReqItemFinderImp;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.view.custom_annotation_view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-16 09:39
 */
@EViewGroup(R.layout.list_item_req_item)
public class RequirementItemView extends BaseAnnotationView {

    @ViewById(R.id.ltm_req_simple_item)
    TextView ltm_req_simple_item;

    public RequirementItemView(Context context) {
        super(context);
    }

    public void bind(ReqItemFinderImp.ItemMap itemMap) {
        ltm_req_simple_item.setText(itemMap.value);
    }
}
