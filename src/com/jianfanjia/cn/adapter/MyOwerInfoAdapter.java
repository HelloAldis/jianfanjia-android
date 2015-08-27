package com.jianfanjia.cn.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.MyOwerInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @class MyOwerInfoAdapter
 * @author zhanghao
 * @date 2015-8-26 15:57
 * @param <NotifyCaiGouInfo>
 */
public class MyOwerInfoAdapter extends BaseListAdapter<MyOwerInfo> {

	public MyOwerInfoAdapter(Context context, List<MyOwerInfo> caigouList) {
		super(context, caigouList);
	}

	@Override
	public View initView(int position, View convertView) {
		ViewHolder viewHolder = null;
		MyOwerInfo myOwerInfo = list.get(position);
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.list_item_designer_ower, null);
			viewHolder = new ViewHolder();
			viewHolder.itemNameView = (TextView) convertView
					.findViewById(R.id.list_item_site_owername);
			viewHolder.itemAdressView = (TextView) convertView
					.findViewById(R.id.list_item_site_address);
			viewHolder.itemStageView = (TextView) convertView
					.findViewById(R.id.list_item_site_currentpro);
			viewHolder.itemOwerHeadView = (ImageView) convertView
					.findViewById(R.id.list_item_site_owerhead);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.itemNameView.setText(myOwerInfo.getName());
		viewHolder.itemAdressView.setText(myOwerInfo.getAddress());
		viewHolder.itemStageView.setText(myOwerInfo.getStage());
		if (myOwerInfo.getImageUrl() != null) {
			ImageLoader.getInstance().displayImage(myOwerInfo.getImageUrl(),
					viewHolder.itemOwerHeadView);
		} else {
			viewHolder.itemOwerHeadView
					.setImageResource(R.drawable.site_listview_item_finish_circle);
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView itemNameView;// 业主名称
		TextView itemAdressView;// 业主工地地址
		TextView itemStageView;// 所处阶段
		ImageView itemOwerHeadView;// 业主头像
	}

}