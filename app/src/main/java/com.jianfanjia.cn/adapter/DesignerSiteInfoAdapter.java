package com.jianfanjia.cn.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.bean.User;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;

/**
 * @class DesignerSiteInfoAdapter
 * @author zhanghao
 * @date 2015-8-26 下午20:05
 * 
 */
public class DesignerSiteInfoAdapter extends BaseListAdapter<Process> {

	public DesignerSiteInfoAdapter(Context context, List<Process> caigouList) {
		super(context, caigouList);
	}

	@Override
	public View initView(int position, View convertView) {
		ViewHolder viewHolder = null;
		Process designerSiteInfo = list.get(position);
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
		User user = designerSiteInfo.getUser();
		viewHolder.itemNameView.setText(user.getUsername());
		viewHolder.itemAdressView.setText(designerSiteInfo.getDistrict());
		viewHolder.itemStageView.setText(MyApplication.getInstance()
				.getStringById(designerSiteInfo.getGoing_on()) + "阶段");
		if (dataManager.getDefaultPro() == position) {
			viewHolder.itemCurrentView.setVisibility(View.VISIBLE);
		} else {
			viewHolder.itemCurrentView.setVisibility(View.GONE);
		}
		viewHolder.itemVillageView.setText(designerSiteInfo.getCell());
		String imageId = user.getImageid();
		if (imageId != null) {
			imageLoader.displayImage(Url.GET_IMAGE + imageId,
					viewHolder.itemOwerHeadView, options);
		} else {
			imageLoader.displayImage(Constant.DEFALUT_OWNER_PIC,
					viewHolder.itemOwerHeadView, options);
		}

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

	@Override
	public void loadSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadFailture() {
		// TODO Auto-generated method stub

	}

}
