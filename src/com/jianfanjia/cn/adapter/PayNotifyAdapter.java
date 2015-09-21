package com.jianfanjia.cn.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.NotifyMessage;

/**
 * @class CaiGouNotifyAdapter.clase
 * @author zhanghao
 * @date 2015-8-26 15:57
 * @param <NotifypayInfo>
 */
public class PayNotifyAdapter extends BaseListAdapter<NotifyMessage> {

	public PayNotifyAdapter(Context context, List<NotifyMessage> caigouList) {
		super(context, caigouList);
	}

	@Override
	public View initView(int position, View convertView) {
		ViewHolder viewHolder = null;
		NotifyMessage messgae = list.get(position);
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_item_tip_pay,
					null);
			viewHolder = new ViewHolder();
			viewHolder.itemNameView = (TextView) convertView
					.findViewById(R.id.list_item_tip_pay_name);
			viewHolder.itemNodeView = (TextView) convertView
					.findViewById(R.id.list_item_tip_pay_node);
			viewHolder.itemPubTimeView = (TextView) convertView
					.findViewById(R.id.list_item_pay_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.itemNameView.setText("");
		viewHolder.itemNodeView.setText(messgae.getStage());
		viewHolder.itemPubTimeView.setText(messgae.getTime());
		return convertView;
	}

	private static class ViewHolder {
		TextView itemNameView;// 延迟工序
		TextView itemNodeView;// 延迟节点
		TextView itemPubTimeView;// 发布时间
	}

	@Override
	public void loadSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadFailture() {
		// TODO Auto-generated method stub

	}

}