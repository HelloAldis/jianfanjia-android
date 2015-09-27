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
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.cn.interf.UploadListener;

public class MyGridViewAdapter extends BaseListAdapter<GridItem> {
	private UploadListener listener;
	private boolean isCanDelete;
	private ItemClickCallBack itemClickCallBack;

	public MyGridViewAdapter(Context context, List<GridItem> list) {
		super(context, list);
	}

	public MyGridViewAdapter(Context context, List<GridItem> list,
			UploadListener listener,ItemClickCallBack itemClickCallBack) {
		super(context, list);
		this.listener = listener;
		isCanDelete = false;
		this.itemClickCallBack = itemClickCallBack;
	}

	public boolean isCanDelete() {
		return isCanDelete;
	}

	public void setCanDelete(boolean isCanDelete) {
		this.isCanDelete = isCanDelete;
	}

	@Override
	public View initView(final int position, View convertView) {
		GridItem item = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.my_grid_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.delete = (ImageView) convertView.findViewById(R.id.delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String imgId = item.getImgId();
		if (position % 2 != 0) {
			if (dataManager.getUserType().equals(Constant.IDENTITY_DESIGNER)) {
				if (imgId.equals(Constant.HOME_ADD_PIC)) {
					imageLoader.displayImage(imgId, holder.img, options);
					if(!isCanDelete()){
						holder.img.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (position % 2 != 0) {
									listener.onUpload(position / 2);
								}
							}

						});
					}else{
						holder.img.setOnClickListener(null);
					}
					holder.delete.setVisibility(View.GONE);
				} else {
					imageLoader.displayImage(Url.GET_IMAGE + imgId, holder.img,
							options);
					
					if(isCanDelete()){
						holder.delete.setVisibility(View.VISIBLE);
						holder.delete.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if (position % 2 != 0) {
									listener.delete(position / 2);
								}
							}
						});
					}else{
						holder.img.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								itemClickCallBack.click(position, Constant.IMG_ITEM);
							}
						});
						holder.delete.setVisibility(View.GONE);
					}
				}
			} else if (dataManager.getUserType()
					.equals(Constant.IDENTITY_OWNER)) {
				if (imgId.equals(Constant.HOME_DEFAULT_PIC)) {
					imageLoader.displayImage(imgId, holder.img, options);
				} else {
					imageLoader.displayImage(Url.GET_IMAGE + imgId, holder.img,
							options);
					holder.img.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							itemClickCallBack.click(position, Constant.IMG_ITEM);
						}
					});
				}
			}
		} else {
			itemClickCallBack.click(position, Constant.IMG_ITEM);
			imageLoader.displayImage(imgId, holder.img, options);
			
		}
		

		return convertView;
	}

	private static class ViewHolder {
		public ImageView img = null;
		public ImageView delete = null;
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
