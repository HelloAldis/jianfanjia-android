package com.jianfanjia.cn.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @ClassName: BaseListAdapter
 * @Description: adapter公共基类
 * @author fengliang
 * @date 2015-8-18 下午1:31:55
 * 
 * @param <T>
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
	protected Context context;
	protected LayoutInflater layoutInflater;
	protected List<T> list;
	protected ImageLoader imageLoader;
	protected DisplayImageOptions options;

	public BaseListAdapter(Context context, List<T> list) {
		this.context = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		// options = new DisplayImageOptions.Builder()
		// .showImageOnLoading(R.drawable.empty_photo)
		// .showImageForEmptyUri(R.drawable.empty_photo)
		// .showImageOnFail(R.drawable.empty_photo).cacheInMemory(true)
		// .cacheOnDisk(true).considerExifParams(true)
		// .bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	public void setList(List<T> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return null == list ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addItem(T t) {
		list.add(t);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return initView(position, convertView);
	}

	public abstract View initView(int position, View convertView);
}
