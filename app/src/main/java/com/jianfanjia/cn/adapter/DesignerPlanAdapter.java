package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.bean.PlanInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
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
            holder.numText = (TextView) convertView.findViewById(R.id.numText);
            holder.statusText = (TextView) convertView.findViewById(R.id.statusText);
            holder.viewPager = (ViewPager) convertView.findViewById(R.id.viewpager);
            holder.dateText = (TextView) convertView.findViewById(R.id.dateText);
            holder.previewText = (TextView) convertView.findViewById(R.id.previewText);
            holder.commentText = (TextView) convertView.findViewById(R.id.commentText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.numText.setText("方案" + (position + 1));
        holder.dateText.setText(DateFormatTool.longToString(info.getLast_status_update_time()));
        holder.commentText.setText("留言(" + info.getComment_count() + ")");
        String status = info.getStatus();
        if (status.equals(Global.PLAN_STATUS3)) {
            holder.statusText.setTextColor(context.getResources().getColor(R.color.orange_color));
            holder.statusText.setText("沟通中");
        } else if (status.equals(Global.PLAN_STATUS4)) {
            holder.statusText.setTextColor(context.getResources().getColor(R.color.grey_color));
            holder.statusText.setText("未中标");
        } else if (status.equals(Global.PLAN_STATUS5)) {
            holder.statusText.setTextColor(context.getResources().getColor(R.color.orange_color));
            holder.statusText.setText("已中标");
        }
        List<String> imgList = info.getImages();
        PlanViewAdapter adapter = new PlanViewAdapter(context, imgList, new ViewPagerClickListener() {
            @Override
            public void onClickItem(int pos) {
                if (null != itemClickListener) {
                    itemClickListener.onCallBack(position, pos);
                }
            }
        });
        holder.viewPager.setPageMargin(10);
        holder.viewPager.setAdapter(adapter);
        holder.commentText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.onItemCallBack(position, Constant.PLAN_COMMENT_ITEM);
                }
            }
        });

        holder.previewText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.onItemCallBack(position, Constant.PLAN_PREVIEW_ITEM);
                }
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView numText;
        TextView statusText;
        ViewPager viewPager;
        TextView dateText;
        TextView commentText;
        TextView previewText;
    }
}
