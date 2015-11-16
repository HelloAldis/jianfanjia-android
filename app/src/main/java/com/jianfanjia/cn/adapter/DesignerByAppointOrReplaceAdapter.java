package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;

import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.view.layout.CheckableLinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Name: DesignerByAppointOrReplaceAdapter
 * User: fengliang
 * Date: 2015-11-16
 * Time: 14:31
 */
public class DesignerByAppointOrReplaceAdapter extends BaseListAdapter<Map<String, String>> {
    private static final String TAG = "DesignerByReplaceAdapter";
    private List<Map<String, String>> splitList = new ArrayList<Map<String, String>>();

    public DesignerByAppointOrReplaceAdapter(Context context, List<Map<String, String>> list, List<Map<String, String>> splitList) {
        super(context, list);
        this.splitList = splitList;
    }

    @Override
    public boolean isEnabled(int position) {
        if (splitList.contains(list.get(position))) {
            return false;
        }
        return super.isEnabled(position);
    }

    @Override
    public View initView(final int position, View convertView) {
        CheckableLinearLayout checkableLinearLayout = null;
        if (splitList.contains(list.get(position))) {
            checkableLinearLayout = new CheckableLinearLayout(context, null, Constant.LIST_ITEM_TAG);
        } else {
            checkableLinearLayout = new CheckableLinearLayout(context, null, Constant.LIST_ITEM);
        }
        checkableLinearLayout.setName(list.get(position).get("itemTitle"));
        checkableLinearLayout.setHeadView(list.get(position).get("itemImg"));
        return checkableLinearLayout;
    }
}

