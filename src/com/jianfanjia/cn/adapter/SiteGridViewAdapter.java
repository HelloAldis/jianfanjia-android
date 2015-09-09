package com.jianfanjia.cn.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.R;
import com.jianfanjia.cn.bean.GridItem;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SiteGridViewAdapter extends BaseListAdapter<GridItem> {

	public SiteGridViewAdapter(Context context, List<GridItem> list) {
		super(context, list);
	}

	@Override
	public View initView(final int position, View convertView) {
		GridItem item = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.my_grid_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageLoader.getInstance().displayImage(item.getPath(), holder.img);
		return convertView;
	}

	private static class ViewHolder {
		public ImageView img = null;
		public TextView name_tv = null;
	}
}
