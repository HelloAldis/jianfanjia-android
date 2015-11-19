package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.bean.OrderDesignerInfo;

import java.util.List;

/**
 * Name: MarchDesignerAdapter
 * User: fengliang
 * Date: 2015-10-20
 * Time: 15:31
 */
public class MarchDesignerAdapter extends BaseListAdapter<OrderDesignerInfo> {

    public MarchDesignerAdapter(Context context, List<OrderDesignerInfo> list) {
        super(context, list);
    }

    @Override
    public View initView(int position, View convertView) {
        OrderDesignerInfo info = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_march_designer_item, null);
            holder = new ViewHolder();
            holder.mHeadView = (ImageView) convertView
                    .findViewById(R.id.image_item);
            holder.mAuthView = (ImageView) convertView
                    .findViewById(R.id.auth_item);
            holder.mName = (TextView) convertView.findViewById(R.id.name_item);
            holder.nBar = (RatingBar) convertView.findViewById(R.id.ratingBar_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mName.setText(info.getUsername());
        int respond_speed = (int) info.getRespond_speed();
        int service_attitude = (int) info.getService_attitude();
        holder.nBar.setRating((respond_speed + service_attitude) / 2);
        imageShow.displayImageHeadWidthThumnailImage(context,info.getImageid(),holder.mHeadView);
//        imageLoader.displayImage(Url_New.GET_THUMBNAIL_IMAGE + info.getImageid(),
//                holder.mHeadView, options);

        return convertView;
    }

    private static class ViewHolder {
        public ImageView mHeadView = null;
        public ImageView mAuthView = null;
        public TextView mName = null;
        public RatingBar nBar = null;
    }
}
