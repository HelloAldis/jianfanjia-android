package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ShowPicPagerAdapter extends PagerAdapter {
	private List<String> images;
	private LayoutInflater inflater;
	private ViewPagerClickListener viewPagerClickListener;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	View.OnLongClickListener onLongClickListener;

	public ShowPicPagerAdapter(Context context, List<String> imageList,
			ViewPagerClickListener viewPagerClickListener) {
		this.images = imageList;
		this.inflater = LayoutInflater.from(context);
		this.viewPagerClickListener = viewPagerClickListener;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.pix_default)
				.showImageForEmptyUri(R.drawable.pix_default)
				.showImageOnFail(R.drawable.pix_default).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
		this.onLongClickListener = onLongClickListener;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public void deleteItem(int position) {
		if (position > -1 && position < images.size()) {
			images.remove(position);
			notifyDataSetChanged();
		}
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		Log.i(this.getClass().getName(), images.get(position));
		View view = inflater.inflate(R.layout.itme_show_pic, null);
		PhotoView imageView = (PhotoView) view.findViewById(R.id.image_item);
		if(!images.get(position).contains(Constant.DEFALUT_PIC_HEAD)){
			imageLoader.displayImage(Url_New.GET_IMAGE + images.get(position),
				imageView, options);
		}else{
			imageLoader.displayImage(images.get(position),
					imageView, options);
		}
		imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View view, float x, float y) {
				if (viewPagerClickListener != null) {
					viewPagerClickListener.onClickItem(position);
				}
			}
		});
		if (onLongClickListener != null) {
			imageView.setOnLongClickListener(onLongClickListener);
		}
		container.addView(view, 0);
		return view;
	}

}
