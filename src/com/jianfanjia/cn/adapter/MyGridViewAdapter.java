package com.jianfanjia.cn.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.GridItem;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.interf.UploadListener;

public class MyGridViewAdapter extends BaseListAdapter<GridItem> {
	private UploadListener listener;

	public MyGridViewAdapter(Context context, List<GridItem> list) {
		super(context, list);
	}

	public MyGridViewAdapter(Context context, List<GridItem> list,
			UploadListener listener) {
		super(context, list);
		this.listener = listener;
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

		if (position % 2 != 0) {
			if (item.getImgId().equals(Constant.HOME_ADD_PIC)) {
				imageLoader.displayImage(item.getImgId(), holder.img, options);
			} else {
				imageLoader.displayImage(Url.GET_IMAGE + item.getImgId(),
						holder.img, options);
			}
		} else {
			imageLoader.displayImage(item.getImgId(), holder.img, options);
		}
		holder.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (position % 2 != 0) {
					listener.onUpload(position);
				}
			}

		});

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
