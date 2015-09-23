package com.jianfanjia.cn.adapter;

import java.util.List;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.DateFormatTool;

/**
 * @class DelayNotifyAdapter
 * @author zhanghao
 * @date 2015-8-26 15:57
 * @param <NotifyCaiGouInfo>
 */
public class DelayNotifyAdapter extends BaseListAdapter<NotifyMessage> {

	public DelayNotifyAdapter(Context context, List<NotifyMessage> delayList) {
		super(context, delayList);
	}

	@Override
	public View initView(int position, View convertView) {
		ViewHolder viewHolder = null;
		NotifyMessage message = list.get(position);
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_item_tip_delay,
					null);
			viewHolder = new ViewHolder();
			viewHolder.itemContentView = (TextView) convertView
					.findViewById(R.id.list_item_tip_delay_content);
			viewHolder.itemNameView = (TextView) convertView
					.findViewById(R.id.list_item_tip_delay_name);
			viewHolder.itemNodeView = (TextView) convertView
					.findViewById(R.id.list_item_tip_delay_node);
			viewHolder.itemPubTimeView = (TextView) convertView
					.findViewById(R.id.list_item_tip_time);
			viewHolder.itemAgressView = (TextView) convertView
					.findViewById(R.id.list_item_tip_delay_status);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// viewHolder.itemNameView.setText(caiGouInfo.getTitle());
		String status = message.getStatus();
		if (!TextUtils.isEmpty(status)) {
			if (status.equals("1")) {
				viewHolder.itemAgressView.setText("��ͬ��");
			} else {
				viewHolder.itemAgressView.setText("�Ѿܾ�");
			}
		}
		viewHolder.itemContentView.setText(message.getContent());
		viewHolder.itemNodeView.setText(MyApplication.getInstance()
				.getStringById(message.getSection()));
		viewHolder.itemPubTimeView.setText(DateFormatTool
				.toLocalTimeString(message.getTime()));
		return convertView;
	}

	private static class ViewHolder {
		TextView itemNameView;// �ӳٱ���
		TextView itemContentView;// �ӳ�����
		TextView itemNodeView;// �ӳٽڵ�
		TextView itemPubTimeView;// ����ʱ��
		TextView itemAgressView;// �Ƿ�ͬ��
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