package com.jianfanjia.cn.view;

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
import com.jianfanjia.cn.config.Url_New;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Name: ChoiceListItemView
 * User: fengliang
 * Date: 2015-11-14
 * Time: 17:12
 */
public class ChoiceListItemView extends LinearLayout implements Checkable {
    protected ImageLoader imageLoader;
    protected DisplayImageOptions options;
    private ImageView itemwHeadView = null;
    private ImageView itemAuthView = null;
    private TextView itemNameText = null;
    private RatingBar itemRatingBar = null;
    private CheckBox itemCheck = null;

    public ChoiceListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initImageLoader();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_designer_by_intention_info, this, true);
        itemwHeadView = (ImageView) view
                .findViewById(R.id.list_item_head_img);
        itemAuthView = (ImageView) view
                .findViewById(R.id.list_item_auth);
        itemNameText = (TextView) view
                .findViewById(R.id.list_item_name_text);
        itemRatingBar = (RatingBar) view
                .findViewById(R.id.list_item_ratingBar);
        itemCheck = (CheckBox) view
                .findViewById(R.id.list_item_check);
    }

    private void initImageLoader() {
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pix_default)
                .showImageForEmptyUri(R.mipmap.pix_default)
                .showImageOnFail(R.mipmap.pix_default).cacheInMemory(false)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
    }

    public void setName(String text) {
        itemNameText.setText(text);
    }

    public void setRating(int rate) {
        itemRatingBar.setRating(rate);
    }

    public void setHeadImg(String imgPath) {
        imageLoader.displayImage(Url_New.GET_THUMBNAIL_IMAGE + imgPath, itemwHeadView, options);
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