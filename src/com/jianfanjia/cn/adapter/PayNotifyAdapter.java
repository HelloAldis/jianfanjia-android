package com.jianfanjia.cn.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jianfanjia.cn.bean.NotifyCaiGouInfo;

import com.jianfanjia.cn.activity.R;

/**
 * @class CaiGouNotifyAdapter.clase
 * @author zhanghao
 * @date 2015-8-26 15:57
 * @param <NotifyCaiGouInfo>
 */
@SuppressWarnings("hiding")
public class PayNotifyAdapter extends BaseListAdapter<NotifyCaiGouInfo> {

	public PayNotifyAdapter(Context context,
			List<NotifyCaiGouInfo> caigouList) {
		super(context, caigouList);
	}

	@Override
	@SuppressWarnings("unchecked")
	public View initView(int position, View convertView){
		ViewHolder viewHolder = null;
		NotifyCaiGouInfo caiGouInfo =list.get(position);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.list_item_tip_caigou, null);
			viewHolder = new ViewHolder();
			viewHolder.itemContentView = (TextView)convertView.findViewById(R.id.list_item_tip_caigou_content);
			viewHolder.itemNameView = (TextView)convertView.findViewById(R.id.list_item_tip_caigou_name);
			viewHolder.itemNodeView = (TextView)convertView.findViewById(R.id.list_item_tip_caigou_node);
			viewHolder.itemPubTimeView= (TextView)convertView.findViewById(R.id.list_item_tip_caigou_time);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.itemNameView.setText(caiGouInfo.getTitle());
		viewHolder.itemContentView .setText(caiGouInfo.getContent());
		viewHolder.itemNodeView.setText(caiGouInfo.getStage());
		viewHolder.itemPubTimeView.setText(caiGouInfo.getTime());
		return convertView;
	}

	class ViewHolder {
		TextView itemNameView;// 采购工序视图
		TextView itemContentView;// 采购内容视图
		TextView itemNodeView;// 采购节点
		TextView itemPubTimeView;// 发布时间
	}

}