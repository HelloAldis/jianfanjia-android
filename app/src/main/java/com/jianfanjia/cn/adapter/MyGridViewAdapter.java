package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.bean.GridItem;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.ItemClickCallBack;

import java.util.List;

public class MyGridViewAdapter extends BaseListAdapter<GridItem> {
    private ItemClickCallBack itemClickCallBack;

    public MyGridViewAdapter(Context context, List<GridItem> list) {
        super(context, list);
    }

    public MyGridViewAdapter(Context context, List<GridItem> list,
                             ItemClickCallBack itemClickCallBack) {
        super(context, list);
        this.itemClickCallBack = itemClickCallBack;
    }

    @Override
    public View initView(final int position, View convertView) {
        GridItem item = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.my_grid_item, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.delete = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String imgId = item.getImgId();
        if (position % 2 != 0) {
            if (imgId.equals(Constant.DEFALUT_PIC)) {
                imageShow.displayLocalImage(imgId, holder.img);
            } else {
                imageShow.displayHalfScreenWidthThumnailImage(context, imgId, holder.img);
                holder.img.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        itemClickCallBack.click(position, Constant.IMG_ITEM);
                    }
                });
            }
        } else {
            holder.img.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    itemClickCallBack.click(position, Constant.IMG_ITEM);
                }
            });
            imageShow.displayLocalImage(imgId, holder.img);
//            imageLoader.displayImage(imgId, holder.img, options);

        }

        return convertView;
    }

    private static class ViewHolder {
        public ImageView img = null;
        public ImageView delete = null;
        public TextView name_tv = null;
    }

}
