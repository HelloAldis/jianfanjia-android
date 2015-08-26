package com.jianfanjia.cn.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jianfanjia.cn.bean.NotifyCaiGouInfo;
import com.jianfanjia.cn.bean.NotifyDelayInfo;

import com.jianfanjia.cn.activity.R;

/**
 * @class DelayNotifyAdapter.clase
 * @author zhanghao
 * @date 2015-8-26 15:57
 * @param <NotifyCaiGouInfo>
 */
@SuppressWarnings("hiding")
public class DelayNotifyAdapter extends BaseListAdapter<NotifyDelayInfo> {

	public DelayNotifyAdapter(Context context,
			List<NotifyDelayInfo> caigouList) {
		super(context, caigouList);
	}

	@SuppressLint("ResourceAsColor") @Override
	@SuppressWarnings("unchecked")
	public View initView(int position, View convertView){
		ViewHolder viewHolder = null;
		NotifyDelayInfo caiGouInfo =list.get(position);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.list_item_tip_delay, null);
			viewHolder = new ViewHolder();
			viewHolder.itemContentView = (TextView)convertView.findViewById(R.id.list_item_tip_delay_content);
			viewHolder.itemNameView = (TextView)convertView.findViewById(R.id.list_item_tip_delay_name);
			viewHolder.itemNodeView = (TextView)convertView.findViewById(R.id.list_item_tip_delay_node);
			viewHolder.itemPubTimeView= (TextView)convertView.findViewById(R.id.list_item_tip_time);
			viewHolder.itemAgressView = (TextView)convertView.findViewById(R.id.list_item_tip_delay_status);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.itemNameView.setText(caiGouInfo.getTitle());
		viewHolder.itemContentView.setText(caiGouInfo.getContent());
		viewHolder.itemNodeView.setText(caiGouInfo.getStage());
		viewHolder.itemPubTimeView.setText(caiGouInfo.getTime());
		if(caiGouInfo.getIsagree() == NotifyDelayInfo.AGREE){
			viewHolder.itemAgressView.setTextColor(context.getResources().getColor(R.color.font_green));
			viewHolder.itemAgressView.setText("已同意");
		}else{
			viewHolder.itemAgressView.setTextColor(context.getResources().getColor(R.color.font_red));
			viewHolder.itemAgressView.setText("已拒绝");
		}
		return convertView;
	}

	class ViewHolder {
		TextView itemNameView;// 采购工序视图
		TextView itemContentView;// 采购内容视图
		TextView itemNodeView;// 采购节点
		TextView itemPubTimeView;// 发布时间
		TextView itemAgressView;//是否同意
	}

}