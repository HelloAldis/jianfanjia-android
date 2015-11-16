package com.jianfanjia.cn.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.config.Constant;

/**
 * Name: CheckableLinearLayout
 * User: fengliang
 * Date: 2015-11-16
 * Time: 11:20
 */
public class CheckableLinearLayout extends LinearLayout implements Checkable {
    private View view = null;
    private ImageView itemwHeadView;
    private ImageView itemAuthView = null;
    private TextView itemNameText;
    private RatingBar itemRatingBar;
    private CheckBox itemCheck;
    private int itemType = -1;

    public CheckableLinearLayout(Context context, AttributeSet attrs, int itemType) {
        super(context, attrs);
        this.itemType = itemType;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        if (itemType == Constant.LIST_ITEM_TAG) {
            view = inflater.inflate(R.layout.list_item_designer_by_appoint_tag, this, true);
        } else {
            view = inflater.inflate(R.layout.list_item_designer_by_appoint_info, this, true);
        }
        itemNameText = (TextView) view.findViewById(R.id.list_item_name_text);
        itemCheck = (CheckBox) view.findViewById(R.id.list_item_check);
    }

    public void setName(String text) {
        itemNameText.setText(text);
    }

    @Override
    public boolean isChecked() {
        return itemCheck.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        itemCheck.setChecked(checked);
        //根据是否选中来选择不同的背景图片
        if (checked) {
            itemCheck.setBackgroundResource(R.mipmap.icon_check2);
        } else {
            itemCheck.setBackgroundResource(R.mipmap.icon_check1);
        }
    }

    @Override
    public void toggle() {
        itemCheck.toggle();
    }

}

