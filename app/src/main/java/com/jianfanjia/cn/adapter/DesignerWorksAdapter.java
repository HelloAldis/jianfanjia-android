package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.bean.Product;
import com.jianfanjia.cn.config.Url_New;

import java.util.List;

/**
 * Name: DesignerWorksAdapter
 * User: fengliang
 * Date: 2015-10-15
 * Time: 13:44
 */
public class DesignerWorksAdapter extends BaseListAdapter<Product> {

    public DesignerWorksAdapter(Context context, List<Product> list) {
        super(context, list);
    }

    @Override
    public View initView(int position, View convertView) {
        ViewHolder viewHolder = null;
        Product product = list.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_designer_works,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.itemwWorksView = (ImageView) convertView
                    .findViewById(R.id.list_item_works_img);
            viewHolder.itemXiaoQuText = (TextView) convertView
                    .findViewById(R.id.list_item_works_xiaoqu_text);
            viewHolder.itemProduceText = (TextView) convertView
                    .findViewById(R.id.list_item_works_produce_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        imageLoader.displayImage(Url_New.GET_IMAGE + product.getImages().get(0).getImageid(), viewHolder.itemwWorksView, options);
        viewHolder.itemXiaoQuText.setText(product.getCell());
        viewHolder.itemProduceText.setText(product.getHouse_area() + "㎡");
        return convertView;
    }

    private static class ViewHolder {
        ImageView itemwWorksView;
        TextView itemXiaoQuText;
        TextView itemProduceText;
    }
}
