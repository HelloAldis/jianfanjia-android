package com.jianfanjia.cn.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;

public class SiteGridViewAdapter extends BaseListAdapter<String> {

	public SiteGridViewAdapter(Context context, List<String> list) {
		super(context, list);
	}

	@Override
	public View initView(final int position, View convertView) {
		String imgUrl = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.my_grid_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (imgUrl.equals(Constant.HOME_ADD_PIC)) {
			imageLoader.displayImage(imgUrl, holder.img, options);
		} else {
			imageLoader.displayImage(Url.GET_IMAGE + imgUrl, holder.img,
					options);
		}

		return convertView;
	}

	private static class ViewHolder {
		public ImageView img = null;
		public TextView name_tv = null;
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
