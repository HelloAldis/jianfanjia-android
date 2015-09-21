package com.jianfanjia.cn.adapter;

import java.util.List;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.NotifyMessage;

/**
 * @class CaiGouNotifyAdapter
 * @author zhanghao
 * @date 2015-8-26 15:57
 * @param <NotifyCaiGouInfo>
 */
public class CaiGouNotifyAdapter extends BaseListAdapter<NotifyMessage> {

	public CaiGouNotifyAdapter(Context context, List<NotifyMessage> caigouList) {
		super(context, caigouList);
	}

	@Override
	public View initView(int position, View convertView) {
		ViewHolder viewHolder = null;
		NotifyMessage message = list.get(position);
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_item_tip_caigou,
					null);
			viewHolder = new ViewHolder();
			viewHolder.itemContentView = (TextView) convertView
					.findViewById(R.id.list_item_tip_caigou_content);
			viewHolder.itemNameView = (TextView) convertView
					.findViewById(R.id.list_item_tip_caigou_name);
			viewHolder.itemNodeView = (TextView) convertView
					.findViewById(R.id.list_item_tip_caigou_node);
			viewHolder.itemPubTimeView = (TextView) convertView
					.findViewById(R.id.list_item_tip_caigou_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.itemNameView.setText("");
		viewHolder.itemContentView.setText(message.getContent());
		String stage = message.getSection();
		if (!TextUtils.isEmpty(stage)) {
			viewHolder.itemNodeView.setText(MyApplication.getInstance()
					.getStringById(stage));
		}
		viewHolder.itemPubTimeView.setText(message.getTime());
		return convertView;
	}

	private static class ViewHolder {
		TextView itemNameView;// 采购工序视图
		TextView itemContentView;// 采购内容视图
		TextView itemNodeView;// 采购节点
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