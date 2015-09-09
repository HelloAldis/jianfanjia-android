package com.jianfanjia.cn.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.DesignerSiteInfo;
import com.jianfanjia.cn.bean.User;
import com.jianfanjia.cn.config.Url;

/**
 * @class DesignerSiteInfoAdapter
 * @author zhanghao
 * @date 2015-8-26 ����20:05
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
		User user = designerSiteInfo.getUser();
		viewHolder.itemNameView.setText(user.getUsername());
		viewHolder.itemAdressView.setText(designerSiteInfo.getDistrict());
		viewHolder.itemStageView.setText(MyApplication.getInstance()
				.getStringById(designerSiteInfo.getGoing_on()));
		// viewHolder.itemCurrentView ��û�ж�
		// viewHolder.itemCurrentView.setText("��ǰ����");
		viewHolder.itemVillageView.setText(designerSiteInfo.getCell());
		if (user.getImageid() != null) {
			imageLoader.displayImage(Url.GET_IMAGE + user.getImageid(),
					viewHolder.itemOwerHeadView);
		} else {
			viewHolder.itemOwerHeadView
					.setImageResource(R.drawable.site_listview_item_finish_circle);
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView itemNameView;// ҵ������
		TextView itemVillageView;// С������
		TextView itemAdressView;// ҵ�����ص�ַ
		TextView itemStageView;// �����׶�
		ImageView itemOwerHeadView;// ҵ��ͷ��
		TextView itemCurrentView;// �Ƿ��ǵ�ǰ����
	}

}
