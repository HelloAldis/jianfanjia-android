package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.bean.DesignerCanOrderInfo;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.tools.LogTool;

import java.util.HashMap;
import java.util.List;

/**
 * Name: DesignerByAppointAdapter
 * User: fengliang
 * Date: 2015-10-19
 * Time: 14:52
 */
public class DesignerByAppointAdapter extends BaseListAdapter<DesignerCanOrderInfo> {
    private static final String TAG = "DesignerByAppointAdapter";
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;

    public DesignerByAppointAdapter(Context context, List<DesignerCanOrderInfo> list) {
        super(context, list);
        isSelected = new HashMap<Integer, Boolean>();
        initData();
    }

    // 初始化isSelected的数据
    private void initData() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public View initView(final int position, View convertView) {
        ViewHolder viewHolder = null;
        DesignerCanOrderInfo info = list.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_designer_by_appoint_info,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.itemwHeadView = (ImageView) convertView
                    .findViewById(R.id.list_item_head_img);
            viewHolder.itemNameText = (TextView) convertView
                    .findViewById(R.id.list_item_name_text);
            viewHolder.itemMarchText = (TextView) convertView
                    .findViewById(R.id.list_item_march_text);
            viewHolder.itemCheck = (CheckBox) convertView
                    .findViewById(R.id.list_item_check);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        imageLoader.displayImage(Url_New.GET_IMAGE + info.getImageid(), viewHolder.itemwHeadView, options);
        viewHolder.itemNameText.setText(info.getUsername());
        viewHolder.itemMarchText.setText(info.getWork_auth_type());
        // 监听checkBox并根据原来的状态来设置新的状态
        viewHolder.itemCheck.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (isSelected.get(position)) {
                    LogTool.d(TAG, "1111111111111111");
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                } else {
                    LogTool.d(TAG, "222222222222222");
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                }

            }
        });
        // 根据isSelected来设置checkbox的选中状况
        viewHolder.itemCheck.setChecked(getIsSelected().get(position));

        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        DesignerByAppointAdapter.isSelected = isSelected;
    }


    private static class ViewHolder {
        ImageView itemwHeadView;
        TextView itemNameText;
        TextView itemMarchText;
        CheckBox itemCheck;
    }
}
