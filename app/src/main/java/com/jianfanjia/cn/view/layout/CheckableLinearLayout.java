package com.jianfanjia.cn.view.layout;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.jianfanjia.cn.config.Url_New;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Name: CheckableLinearLayout
 * User: fengliang
 * Date: 2015-11-16
 * Time: 11:20
 */
public class CheckableLinearLayout extends LinearLayout implements Checkable {
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private View view = null;
    private ImageView itemwHeadView = null;
    private ImageView itemAuthView = null;
    private TextView itemNameText = null;
    private TextView itemMarchText = null;
    private RatingBar itemRatingBar = null;
    private CheckBox itemCheck = null;
    private int itemType = -1;

    public CheckableLinearLayout(Context context, AttributeSet attrs, int itemType) {
        super(context, attrs);
        this.itemType = itemType;
        LayoutInflater inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pix_default)
                .showImageForEmptyUri(R.mipmap.pix_default)
                .showImageOnFail(R.mipmap.pix_default).cacheInMemory(false)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
        if (itemType == Constant.LIST_ITEM_TAG) {
            view = inflater.inflate(R.layout.list_item_designer_by_appoint_tag, this, true);
        } else {
            view = inflater.inflate(R.layout.list_item_designer_by_appoint_info, this, true);
        }
        itemwHeadView = (ImageView) view.findViewById(R.id.list_item_head_img);
        itemNameText = (TextView) view.findViewById(R.id.list_item_name_text);
        itemMarchText = (TextView) view.findViewById(R.id.list_item_march_text);
        itemRatingBar = (RatingBar) view.findViewById(R.id.list_item_ratingBar);
        itemCheck = (CheckBox) view.findViewById(R.id.list_item_check);
    }

    public void setName(String text) {
        itemNameText.setText(text);
    }


    public void setHeadView(String imgId) {
        imageLoader.displayImage(Url_New.GET_THUMBNAIL_IMAGE + imgId, itemwHeadView, options);
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

