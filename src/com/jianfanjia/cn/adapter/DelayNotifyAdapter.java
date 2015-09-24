package com.jianfanjia.cn.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.NotifyDelayInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.DateFormatTool;

/**
 * @class DelayNotifyAdapter
 * @author zhanghao
 * @date 2015-8-26 15:57
 * @param <NotifyCaiGouInfo>
 */
public class DelayNotifyAdapter extends BaseListAdapter<NotifyDelayInfo> {

	public DelayNotifyAdapter(Context context, List<NotifyDelayInfo> delayList) {
		super(context, delayList);
	}

	@Override
	public View initView(int position, View convertView) {
		ViewHolder viewHolder = null;
		NotifyDelayInfo info = list.get(position);
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
		String requestRole = info.getRequest_role();
		if (requestRole.equals(Constant.IDENTITY_OWNER)) {
			viewHolder.itemNameView.setText("������������������");
		} else if (requestRole.equals(Constant.IDENTITY_DESIGNER)) {
			viewHolder.itemNameView.setText("�������ʦ����������������");
		}
		String status = info.getStatus();
		if (status.equals(Constant.NO_START)) {
			viewHolder.itemAgressView.setText("δ����");
		} else if (status.equals(Constant.DOING)) {
			viewHolder.itemAgressView.setText("������");
		} else if (status.equals(Constant.FINISHED)) {
			viewHolder.itemAgressView.setText("�����");
		} else if (status.equals(Constant.YANQI_BE_DOING)) {
			viewHolder.itemAgressView.setText("����������");
		} else if (status.equals(Constant.YANQI_AGREE)) {
			viewHolder.itemAgressView.setText("����ͬ��");
		} else if (status.equals(Constant.YANQI_REFUSE)) {
			viewHolder.itemAgressView.setText("���ھܾ�");
		}
		viewHolder.itemContentView.setText(DateFormatTool.longToString(info
				.getNew_date()));
		viewHolder.itemNodeView.setText(MyApplication.getInstance()
				.getStringById(info.getSection()) + "�׶�");
		viewHolder.itemPubTimeView.setText(DateFormatTool
				.toLocalTimeString(info.getRequest_date()));
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