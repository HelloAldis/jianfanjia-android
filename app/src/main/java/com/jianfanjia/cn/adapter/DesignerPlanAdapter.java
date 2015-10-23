package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.bean.PlanInfo;
import com.jianfanjia.cn.interf.ItemClickListener;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.DateFormatTool;

import java.util.List;

/**
 * Name: DesignerPlanAdapter
 * User: fengliang
 * Date: 2015-10-22
 * Time: 17:54
 */
public class DesignerPlanAdapter extends BaseListAdapter<PlanInfo> {
    private ItemClickListener itemClickListener;

    public DesignerPlanAdapter(Context context, List<PlanInfo> list) {
        super(context, list);
    }

    public DesignerPlanAdapter(Context context, List<PlanInfo> list, ItemClickListener itemClickListener) {
        super(context, list);
        this.itemClickListener = itemClickListener;
    }


    @Override
    public View initView(final int position, View convertView) {
        ViewHolder holder = null;
        PlanInfo info = list.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_plan_info,
                    null);
            holder = new ViewHolder();
            holder.cellText = (TextView) convertView.findViewById(R.id.cellText);
            holder.statusText = (TextView) convertView.findViewById(R.id.statusText);
            holder.vp = (ViewPager) convertView.findViewById(R.id.viewpager);
            holder.dateText = (TextView) convertView.findViewById(R.id.dateText);
            holder.previewText = (TextView) convertView.findViewById(R.id.previewText);
            holder.commentText = (TextView) convertView.findViewById(R.id.commentText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cellText.setText("中建康城东区");
        holder.dateText.setText(DateFormatTool.longToString(info.getLast_status_update_time()));
        holder.commentText.setText("留言(" + info.getComment_count() + ")");
        String status = info.getStatus();
        holder.statusText.setText("已中标");
        List<String> imgList = info.getImages();
        PlanViewAdapter adapter = new PlanViewAdapter(context, imgList, new ViewPagerClickListener() {
            @Override
            public void onClickItem(int pos) {
                itemClickListener.onCallBack(position, pos);
            }
        });
        holder.vp.setAdapter(adapter);

        return convertView;
    }

    private static class ViewHolder {
        TextView cellText;
        TextView statusText;
        ViewPager vp;
        TextView dateText;
        TextView commentText;
        TextView previewText;
    }
}
