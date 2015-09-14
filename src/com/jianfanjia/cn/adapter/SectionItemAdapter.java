package com.jianfanjia.cn.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.CommentInfo;
import com.jianfanjia.cn.bean.GridItem;
import com.jianfanjia.cn.bean.SectionItemInfo;
import com.jianfanjia.cn.cache.DataManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.cn.tools.LogTool;

public class SectionItemAdapter extends BaseListAdapter<SectionItemInfo> {
	private static final int IMG_COUNT = 9;
	private ItemClickCallBack callBack;
	private int lastClickItem = -1;// 记录点击的位置
	private boolean isPos = false;
	private SiteGridViewAdapter siteGridViewAdapter;
	private List<GridItem> gridItem = new ArrayList<GridItem>();
	private int currentPro = -1;// 记录第一个当前展开的工序

	private DataManager dataManager;
	private String userType;

	public SectionItemAdapter(Context context,
			List<SectionItemInfo> sectionItemInfos) {
		super(context, sectionItemInfos);
		dataManager = DataManager.getInstance();
		userType = dataManager.getUserType();
	}

	public SectionItemAdapter(Context context,
			List<SectionItemInfo> sectionItemInfos, ItemClickCallBack callBack) {
		super(context, sectionItemInfos);
		this.callBack = callBack;
		dataManager = DataManager.getInstance();
		userType = dataManager.getUserType();
	}

	public int getCurrentPro() {
		return currentPro;
	}

	public void setCurrentPro(int currentPro) {
		this.currentPro = currentPro;
	}

	public void setSectionItemInfos(List<SectionItemInfo> sectionItemInfos) {
		this.list = sectionItemInfos;
	}

	public void setLastClickItem(int position, boolean isPos) {
		this.lastClickItem = position;
		this.isPos = isPos;
		notifyDataSetChanged();
	}

	@Override
	public View initView(final int position, View convertView) {
		ViewHolder viewHolder = null;
		final SectionItemInfo sectionItemInfo = list.get(position);
		List<String> imageUrlList = sectionItemInfo.getImages();
		List<CommentInfo> commentInfoList = sectionItemInfo.getComments();
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
			viewHolder.finishTime = (TextView) convertView
					.findViewById(R.id.site_list_item_content_small_node_finishtime);
			viewHolder.openUploadTime = (TextView) convertView
					.findViewById(R.id.site_list_item_content_expand_node_time);
			viewHolder.openComment = (TextView) convertView
					.findViewById(R.id.site_list_item_content_expand_node_assess);
			viewHolder.openFinishStatus = (TextView) convertView
					.findViewById(R.id.site_list_item_content_expand_node_finish_status);
			viewHolder.confirmFinishStatus = (TextView) convertView
					.findViewById(R.id.site_list_item_content_expand_node_confirm_finish);
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
			viewHolder.confirmFinishStatus.setVisibility(View.GONE);
			viewHolder.openFinishStatus.setVisibility(View.VISIBLE);
			break;
		case Constant.NOT_START:
			viewHolder.finishStatusIcon
					.setImageResource(R.drawable.site_listview_item_notstart_circle);
			viewHolder.finishTime.setVisibility(View.GONE);
			viewHolder.openFinishStatus.setText(context.getResources()
					.getString(R.string.site_example_node_not_start));
			viewHolder.confirmFinishStatus.setVisibility(View.GONE);
			viewHolder.openFinishStatus.setVisibility(View.VISIBLE);
			break;
		case Constant.WORKING:
			viewHolder.finishTime.setVisibility(View.GONE);
			viewHolder.finishStatusIcon
					.setImageResource(R.drawable.icon_home_working);
			if (userType.equals(Constant.IDENTITY_OWNER)) {
				viewHolder.openFinishStatus.setVisibility(View.VISIBLE);
				viewHolder.confirmFinishStatus.setVisibility(View.GONE);
				viewHolder.openFinishStatus.setText(context.getResources()
						.getString(R.string.site_example_node_working));
			} else if (userType.equals(Constant.IDENTITY_DESIGNER)) {
				viewHolder.openFinishStatus.setVisibility(View.GONE);
				viewHolder.confirmFinishStatus.setVisibility(View.VISIBLE);
				viewHolder.openFinishStatus.setText(context.getResources()
						.getString(R.string.site_example_node_confirm_finish));
			}
			break;
		default:
			break;
		}

		if (null != imageUrlList && imageUrlList.size() > 0) {
			if (imageUrlList.size() < IMG_COUNT
					&& !imageUrlList.contains(Constant.HOME_ADD_PIC)) {// 最多上传9张照片
				Log.i(this.getClass().getName(), "addImage");
				imageUrlList.add(Constant.HOME_ADD_PIC);
			} else {
				for (String str : imageUrlList) {
					if (str.equals(Constant.HOME_ADD_PIC)) {
						list.remove(str);
					}
				}
			}
		} else {
			imageUrlList.add(Constant.HOME_ADD_PIC);
		}

		if (null != commentInfoList && commentInfoList.size() > 0) {
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
			if (isPos) {
				viewHolder.bigOpenLayout.setVisibility(View.GONE);
				viewHolder.smallcloseLayout.setVisibility(View.VISIBLE);
			} else {
				viewHolder.bigOpenLayout.setVisibility(View.VISIBLE);
				viewHolder.smallcloseLayout.setVisibility(View.GONE);
			}
		} else {
			viewHolder.bigOpenLayout.setVisibility(View.GONE);
			viewHolder.smallcloseLayout.setVisibility(View.VISIBLE);
		}
		// 设置上传照片
		setImageData(imageUrlList, viewHolder.gridView);

		viewHolder.confirmFinishStatus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						callBack.click(position, Constant.CONFIRM_ITEM);
					}
				});
		viewHolder.openComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callBack.click(position, Constant.COMMENT_ITEM);
			}
		});
		return convertView;
	}

	/**
	 * @des 设置item里gridview的照片
	 * @param imageUrlList
	 * @param gridView
	 */
	private void setImageData(final List<String> imageUrlList, GridView gridView) {
		Log.i(this.getClass().getName(), "imageUrlList:" + imageUrlList.size());
		siteGridViewAdapter = new SiteGridViewAdapter(context, imageUrlList);
		gridView.setAdapter(siteGridViewAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				String data = imageUrlList.get(position);
				Log.i(this.getClass().getName(), "data:" + data);
				if (data.equals(Constant.HOME_ADD_PIC)) {
					callBack.click(position, Constant.ADD_ITEM);
				} else {
					for (String str : imageUrlList) {
						if (str.equals(Constant.HOME_ADD_PIC)) {
							imageUrlList.remove(str);
						}
					}
					callBack.click(imageUrlList, Constant.IMG_ITEM);
				}
			}

		});
	}

	private static class ViewHolder {
		RelativeLayout smallcloseLayout;
		RelativeLayout bigOpenLayout;
		TextView closeNodeName;
		TextView openNodeName;
		TextView openUploadPic;
		TextView openComment;
		TextView openUploadTime;
		TextView finishTime;
		TextView openFinishStatus;
		TextView confirmFinishStatus;
		ImageView finishStatusIcon;
		GridView gridView;
	}
}
