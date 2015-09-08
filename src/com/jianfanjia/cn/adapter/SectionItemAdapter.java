package com.jianfanjia.cn.adapter;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jianfanjia.cn.activity.CommentActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.CommentInfo;
import com.jianfanjia.cn.bean.GridItem;
import com.jianfanjia.cn.bean.SectionItemInfo;
import com.jianfanjia.cn.config.Constant;

public class SectionItemAdapter extends BaseListAdapter<SectionItemInfo> {
	private int lastClickItem = -1;// 记录点击的位置
	private SiteGridViewAdapter siteGridViewAdapter;
	private List<GridItem> gridItem = new ArrayList<GridItem>();
	private int currentPro = -1;// 记录第一个当前展开的工序

	public SectionItemAdapter(Context context,
			List<SectionItemInfo> sectionItemInfos, int currentPro) {
		super(context, sectionItemInfos);
		this.currentPro = currentPro;
	}

	public int getCurrentPro() {
		return currentPro;
	}

	public void setCurrentPro(int currentPro) {
		this.currentPro = currentPro;
	}

	public int getLastClickItem() {
		return lastClickItem;
	}

	public List<SectionItemInfo> getSectionItemInfos() {
		return list;
	}

	public void setSectionItemInfos(List<SectionItemInfo> sectionItemInfos) {
		this.list = sectionItemInfos;
	}

	public void setLastClickItem(int lastClickItem) {
		this.lastClickItem = lastClickItem;
	}

	class ViewHolder {
		RelativeLayout smallcloseLayout;
		RelativeLayout bigOpenLayout;
		TextView closeNodeName;
		TextView openNodeName;
		TextView openUploadPic;
		TextView openComment;
		TextView openUploadTime;
		TextView finishTime;
		TextView openFinishStatus;
		ImageView finishStatusIcon;
		GridView gridView;
	}

	@Override
	public View initView(final int position, View convertView) {
		ViewHolder viewHolder = null;
		final SectionItemInfo sectionItemInfo = list.get(position);
		List<String> imageUrlList = sectionItemInfo.getImages();
		List<CommentInfo> commentInfoList = sectionItemInfo.getComments();
		Log.i(this.getClass().getName(), sectionItemInfo.getName());
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.site_listview_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.smallcloseLayout = (RelativeLayout) convertView
					.findViewById(R.id.site_listview_item_content_small);
			viewHolder.bigOpenLayout = (RelativeLayout) convertView
					.findViewById(R.id.site_listview_item_content_expand);
			viewHolder.closeNodeName = (TextView) convertView
					.findViewById(R.id.site_list_item_content_small_node_name);
			viewHolder.openNodeName = (TextView) convertView
					.findViewById(R.id.site_list_item_content_expand_node_name);
			viewHolder.openUploadPic = (TextView) convertView
					.findViewById(R.id.site_list_item_content_expand_node_upload_picture);
			viewHolder.finishTime = (TextView) convertView
					.findViewById(R.id.site_list_item_content_small_node_finishtime);
			viewHolder.openUploadTime = (TextView) convertView
					.findViewById(R.id.site_list_item_content_expand_node_time);
			viewHolder.openComment = (TextView) convertView
					.findViewById(R.id.site_list_item_content_expand_node_assess);
			viewHolder.openFinishStatus = (TextView) convertView
					.findViewById(R.id.site_list_item_content_expand_node_finish_status);
			viewHolder.finishStatusIcon = (ImageView) convertView
					.findViewById(R.id.site_listview_item_status);
			viewHolder.gridView = (GridView) convertView
					.findViewById(R.id.site_list_item_gridview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.closeNodeName.setText(MyApplication.getInstance()
				.getStringById(sectionItemInfo.getName()));
		viewHolder.openNodeName.setText(MyApplication.getInstance()
				.getStringById(sectionItemInfo.getName()));
		switch (Integer.parseInt(sectionItemInfo.getStatus())) {
		case Constant.FINISH:
			viewHolder.finishStatusIcon
					.setImageResource(R.drawable.icon_home_finish);
			viewHolder.openFinishStatus.setText(context.getResources()
					.getString(R.string.site_example_node_finish));
			viewHolder.finishTime.setVisibility(View.VISIBLE);
			break;
		case Constant.NOT_START:
			viewHolder.finishStatusIcon
					.setImageResource(R.drawable.site_listview_item_notstart_circle);
			viewHolder.finishTime.setVisibility(View.GONE);
			viewHolder.openFinishStatus.setText("");
			break;
		case Constant.WORKING:
			viewHolder.finishTime.setVisibility(View.GONE);
			viewHolder.finishStatusIcon
					.setImageResource(R.drawable.icon_home_working);
			viewHolder.openFinishStatus.setText(context.getResources()
					.getString(R.string.site_example_node_working));
			break;
		default:
			break;
		}

		if (imageUrlList.size() > 0) {
			viewHolder.openUploadPic.setText(imageUrlList.size() + "");
			viewHolder.openUploadPic.setCompoundDrawablesWithIntrinsicBounds(
					context.getResources().getDrawable(
							R.drawable.btn_icon_upload_pic_pressed), null,
					null, null);
		} else {
			viewHolder.openUploadPic.setCompoundDrawablesWithIntrinsicBounds(
					context.getResources().getDrawable(
							R.drawable.btn_icon_upload_pic_normal), null, null,
					null);
			viewHolder.openUploadPic.setText(R.string.upload_pic);
		}

		if (commentInfoList.size() > 0) {
			viewHolder.openComment.setText(commentInfoList.size() + "");
			viewHolder.openComment.setCompoundDrawablesWithIntrinsicBounds(
					context.getResources().getDrawable(
							R.drawable.btn_icon_comment_pressed), null, null,
					null);
		} else {
			viewHolder.openComment.setText(R.string.comment);
			viewHolder.openComment.setCompoundDrawablesWithIntrinsicBounds(
					context.getResources().getDrawable(
							R.drawable.btn_icon_comment_normal), null, null,
					null);
		}

		// 未开工的点击无法展开
		// if (Integer.parseInt(sectionItemInfo.getStatus()) !=
		// Constant.NOT_START && position == lastClickItem) {
		if (position == lastClickItem) {
			viewHolder.bigOpenLayout.setVisibility(View.VISIBLE);
			viewHolder.smallcloseLayout.setVisibility(View.GONE);
			// 设置上传照片
			setImageData(imageUrlList, viewHolder.gridView);
		} else {
			viewHolder.bigOpenLayout.setVisibility(View.GONE);
			viewHolder.smallcloseLayout.setVisibility(View.VISIBLE);
		}

		viewHolder.openComment.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CommentActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt(Constant.CURRENT_LIST, currentPro);
				Log.i(this.getClass().getName(), "positon = " + position);
				bundle.putInt(Constant.CURRENT_Item, position);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		viewHolder.openUploadPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// AppContext.showToast("涓婁紶鐓х墖");
			}
		});
		return convertView;
	}

	/**
	 * @des 设置item里gridview的照片
	 * @param imageUrlList
	 * @param gridView
	 */
	private void setImageData(List<String> imageUrlList, GridView gridView) {
		Log.i(this.getClass().getName(), "size =" + imageUrlList.size());

		gridItem.clear();
		gridView.setAdapter(null);

		// 最多上传9张照片
		if (imageUrlList.size() < 9
				&& !imageUrlList.contains(Constant.HOME_ADD_PIC)) {
			Log.i(this.getClass().getName(), "addImage");
			imageUrlList.add(Constant.HOME_ADD_PIC);
		}
		for (int i = 0; i < imageUrlList.size(); i++) {
			GridItem item = new GridItem();
			item.setPath(imageUrlList.get(i));
			gridItem.add(item);
		}
		Log.i(this.getClass().getName(), "grid =" + gridItem.size());

		siteGridViewAdapter = new SiteGridViewAdapter(context, gridItem);
		gridView.setAdapter(siteGridViewAdapter);

	}

}
