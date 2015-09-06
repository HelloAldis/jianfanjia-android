package com.jianfanjia.cn.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.DesignerSiteInfo;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @class DesignerSiteInfoAdapter
 * @author zhanghao
 * @date 2015-8-26 下午20:05
 * 
 */
public class DesignerSiteInfoAdapter extends BaseListAdapter<DesignerSiteInfo> {

	public DesignerSiteInfoAdapter(Context context,
			List<DesignerSiteInfo> caigouList) {
		super(context, caigouList);
	}

	@Override
	public View initView(int position, View convertView) {
		ViewHolder viewHolder = null;
		DesignerSiteInfo designerSiteInfo = list.get(position);
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.list_item_designer_site, null);
			viewHolder = new ViewHolder();
			viewHolder.itemNameView = (TextView) convertView
					.findViewById(R.id.list_item_site_owername);
			viewHolder.itemAdressView = (TextView) convertView
					.findViewById(R.id.list_item_site_address);
			viewHolder.itemCurrentView = (TextView) convertView
					.findViewById(R.id.list_item_site_currentsite);
			viewHolder.itemStageView = (TextView) convertView
					.findViewById(R.id.list_item_site_currentstage);
			viewHolder.itemOwerHeadView = (ImageView) convertView
					.findViewById(R.id.list_item_site_owerhead);
			viewHolder.itemVillageView = (TextView) convertView
					.findViewById(R.id.list_item_site_villagename);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		OwnerInfo ownerInfo = designerSiteInfo.getInfo();
//		viewHolder.itemNameView.setText(ownerInfo.getName());
//		viewHolder.itemAdressView.setText(designerSiteInfo.getDistrict());
//		viewHolder.itemStageView.setText(designerSiteInfo.getGoingon());
		// viewHolder.itemCurrentView 还没判断
		viewHolder.itemVillageView.setText(designerSiteInfo.getCell());
		viewHolder.itemOwerHeadView
				.setImageResource(R.drawable.site_listview_item_finish_circle);
		// if (ownerInfo.getImageid() != null) {
		// ImageLoader.getInstance().displayImage(ownerInfo.getImageid(),
		// viewHolder.itemOwerHeadView);
		// } else {
		// viewHolder.itemOwerHeadView
		// .setImageResource(R.drawable.site_listview_item_finish_circle);
		// }
		return convertView;
	}

	private static class ViewHolder {
		TextView itemNameView;// 业主名称
		TextView itemVillageView;// 小区名称
		TextView itemAdressView;// 业主工地地址
		TextView itemStageView;// 所处阶段
		ImageView itemOwerHeadView;// 业主头像
		TextView itemCurrentView;// 是否是当前工地
	}

}