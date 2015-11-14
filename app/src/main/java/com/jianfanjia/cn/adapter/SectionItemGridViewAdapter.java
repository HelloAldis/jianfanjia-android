package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.config.Url_New;

import java.util.List;

public class SectionItemGridViewAdapter extends BaseListAdapter<String> {

    public SectionItemGridViewAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View initView(final int position, View convertView) {
        String imgUrl = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.my_grid_item, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (imgUrl.equals(Constant.HOME_ADD_PIC)) {
//            imageLoader.displayImage(imgUrl, holder.img, options);
            holder.img.setImageResource(R.mipmap.btn_icon_home_add);
        } else {
            imageLoader.displayImage(Url_New.GET_THUMBNAIL_IMAGE.replace(Url_New.WIDTH, MyApplication.dip2px(context, Global.PIC_WIDTH_NODE) + "") + imgUrl, holder.img,
                    options);
        }

        return convertView;
    }

    private static class ViewHolder {
        public ImageView img = null;
        public TextView name_tv = null;
    }

}
