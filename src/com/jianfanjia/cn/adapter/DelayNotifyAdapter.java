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
			viewHolder.itemNameView.setText("您已申请延期验收至");
		} else if (requestRole.equals(Constant.IDENTITY_DESIGNER)) {
			viewHolder.itemNameView.setText("您的设计师已申请延期验收至");
		}
		String status = info.getStatus();
		if (status.equals(Constant.NO_START)) {
			viewHolder.itemAgressView.setText("未开工");
		} else if (status.equals(Constant.DOING)) {
			viewHolder.itemAgressView.setText("进行中");
		} else if (status.equals(Constant.FINISHED)) {
			viewHolder.itemAgressView.setText("已完成");
		} else if (status.equals(Constant.YANQI_BE_DOING)) {
			viewHolder.itemAgressView.setText("改期申请中");
		} else if (status.equals(Constant.YANQI_AGREE)) {
			viewHolder.itemAgressView.setText("改期同意");
		} else if (status.equals(Constant.YANQI_REFUSE)) {
			viewHolder.itemAgressView.setText("改期拒绝");
		}
		viewHolder.itemContentView.setText(DateFormatTool.longToString(info
				.getNew_date()));
		viewHolder.itemNodeView.setText(MyApplication.getInstance()
				.getStringById(info.getSection()) + "阶段");
		viewHolder.itemPubTimeView.setText(DateFormatTool
				.toLocalTimeString(info.getRequest_date()));
		return convertView;
	}

	private static class ViewHolder {
		TextView itemNameView;// 延迟标题
		TextView itemContentView;// 延迟内容
		TextView itemNodeView;// 延迟节点
		TextView itemPubTimeView;// 发布时间
		TextView itemAgressView;// 是否同意
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