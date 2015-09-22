package com.jianfanjia.cn.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.CommentInfo;
import com.jianfanjia.cn.bean.GridItem;
import com.jianfanjia.cn.bean.SectionInfo;
import com.jianfanjia.cn.bean.SectionItemInfo;
import com.jianfanjia.cn.cache.DataManagerNew;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.cn.tools.LogTool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SectionItemAdapterBack extends BaseAdapter {
	private static final int IMG_COUNT = 9;
	private static final int CHECK_VIEW = 0;
	private static final int SECTION_ITME_VIEW = 1;
	private ItemClickCallBack callBack = null;
	private int lastClickItem = -1;// 记录上次点击的位置
	private int currentClickItem = -1;// 记录当前点击位置
	private boolean isPos = false;
	private SiteGridViewAdapter siteGridViewAdapter;
	private List<GridItem> gridItem = new ArrayList<GridItem>();
	private String userType;
	private int section_status;// 节点的状态
	private SectionInfo sectionInfo;
	private Context context;
	private LayoutInflater layoutInflater;
	private List<SectionItemInfo> list = new ArrayList<SectionItemInfo>();
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private DataManagerNew dataManager;
	private boolean isHasCheck;// 是否有验收

	public SectionItemAdapterBack(Context context, int position,
			ItemClickCallBack callBack) {
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.pix_default)
				.showImageForEmptyUri(R.drawable.pix_default)
				.showImageOnFail(R.drawable.pix_default).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		dataManager = DataManagerNew.getInstance();
		sectionInfo = dataManager.getDefaultSectionInfoByPosition(position);
		setList();
		this.callBack = callBack;
		userType = dataManager.getUserType();
	}

	private void setList() {
		section_status = sectionInfo.getStatus();
		list.clear();
		if (!sectionInfo.getName().equals("kai_gong")
				&& !sectionInfo.getName().equals("chai_gai")) {
			isHasCheck = true;
			SectionItemInfo sectionItemInfo = new SectionItemInfo();
			sectionItemInfo.setName(context.getResources().getStringArray(
					R.array.site_check_name)[MyApplication.getInstance()
					.getPositionByItemName(sectionInfo.getName())]);
			sectionItemInfo.setOpen(false);
			list.add(sectionItemInfo);
		} else {
			isHasCheck = false;
		}
		for (SectionItemInfo sectionItemInfo : sectionInfo.getItems()) {
			sectionItemInfo.setOpen(false);
			list.add(sectionItemInfo);
		}
		Log.d(this.getClass().getName(), "list.size() =" + list.size());
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (isHasCheck) {
			if (position == 0) {
				return CHECK_VIEW;
			} else {
				return SECTION_ITME_VIEW;
			}
		} else {
			return SECTION_ITME_VIEW;
		}
	}

	public void setPosition(int position) {
		sectionInfo = dataManager.getDefaultSectionInfoByPosition(position);
		section_status = sectionInfo.getStatus();
		setList();
	}

	public void clearCurrentPosition() {
		this.currentClickItem = -1;
		this.lastClickItem = -1;
	}

	public void setCurrentClickItem(int position) {
		this.currentClickItem = position;
		if (currentClickItem != lastClickItem) {
			if (lastClickItem != -1) {
				list.get(lastClickItem).setOpen(false);
			}
			list.get(currentClickItem).setOpen(true);
			lastClickItem = currentClickItem;
		} else {
			if (list.get(currentClickItem).isOpen()) {
				list.get(currentClickItem).setOpen(false);
			} else {
				list.get(currentClickItem).setOpen(true);
			}
		}
		notifyDataSetChanged();
	}

	public boolean isHasCheck() {
		return isHasCheck;
	}

	public void setHasCheck(boolean isHasCheck) {
		this.isHasCheck = isHasCheck;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return initView(position, convertView);
	}

	public View initView(final int position, View convertView) {
		ViewHolder viewHolder = null;
		ViewHolder2 viewHolderf = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case CHECK_VIEW:
				convertView = layoutInflater.inflate(
						R.layout.site_listview_head, null);
				viewHolderf = new ViewHolder2();
				viewHolderf.smallcloseLayout = (RelativeLayout) convertView
						.findViewById(R.id.site_listview_item_content_small);
				viewHolderf.bigOpenLayout = (RelativeLayout) convertView
						.findViewById(R.id.site_listview_item_content_expand);
				viewHolderf.closeNodeName = (TextView) convertView
						.findViewById(R.id.site_list_item_content_small_node_name);
				viewHolderf.openNodeName = (TextView) convertView
						.findViewById(R.id.site_list_item_content_expand_node_name);
				viewHolderf.openDelay = (TextView) convertView
						.findViewById(R.id.site_list_head_delay);
				viewHolderf.openCheck = (TextView) convertView
						.findViewById(R.id.site_list_head_check);
				viewHolderf.openTip = (TextView) convertView
						.findViewById(R.id.site_list_item_content_expand_node_more);
				viewHolderf.closeTip = (TextView) convertView
						.findViewById(R.id.site_list_item_content_small_node_more);
				convertView.setTag(viewHolderf);
				break;
			case SECTION_ITME_VIEW:
				convertView = layoutInflater.inflate(
						R.layout.site_listview_item, null);
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
				break;
			default:
				break;
			}
		} else {
			switch (type) {
			case CHECK_VIEW:
				viewHolderf = (ViewHolder2) convertView.getTag();
				break;
			case SECTION_ITME_VIEW:
				viewHolder = (ViewHolder) convertView.getTag();
				break;
			default:
				break;
			}
		}

		switch (type) {
		case SECTION_ITME_VIEW:
			final SectionItemInfo sectionItemInfo = list.get(position);
			List<String> imageUrlList = sectionItemInfo.getImages();
			List<CommentInfo> commentInfoList = sectionItemInfo.getComments();
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
							.getString(
									R.string.site_example_node_confirm_finish));
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
								R.drawable.btn_icon_comment_pressed), null,
						null, null);
			} else {
				viewHolder.openComment.setText(R.string.comment);
				viewHolder.openComment.setCompoundDrawablesWithIntrinsicBounds(
						context.getResources().getDrawable(
								R.drawable.btn_icon_comment_normal), null,
						null, null);
			}

			// 未开工的点击无法展开
			// if (Integer.parseInt(sectionItemInfo.getStatus()) !=
			// Constant.NOT_START && position == lastClickItem) {
			if (section_status != Constant.NOT_START) {
				if (sectionItemInfo.isOpen()) {
					viewHolder.bigOpenLayout.setVisibility(View.VISIBLE);
					viewHolder.smallcloseLayout.setVisibility(View.GONE);
				} else {
					viewHolder.bigOpenLayout.setVisibility(View.GONE);
					viewHolder.smallcloseLayout.setVisibility(View.VISIBLE);
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
							if(isHasCheck){
								callBack.click(position - 1, Constant.CONFIRM_ITEM);
							}else{
								callBack.click(position, Constant.CONFIRM_ITEM);
							}
						}
					});
			viewHolder.openComment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(isHasCheck){
						callBack.click(position - 1, Constant.COMMENT_ITEM);
					}else{
						callBack.click(position, Constant.COMMENT_ITEM);
					}
				}
			});

			break;
		case CHECK_VIEW:
			final SectionItemInfo sectionItemInfo1 = list.get(position);
			viewHolderf.closeNodeName.setText(sectionItemInfo1.getName());
			viewHolderf.openNodeName.setText(sectionItemInfo1.getName());
			if (section_status != Constant.NOT_START) {
				if (sectionItemInfo1.isOpen()) {
					viewHolderf.bigOpenLayout.setVisibility(View.VISIBLE);
					viewHolderf.smallcloseLayout.setVisibility(View.GONE);
				} else {
					viewHolderf.bigOpenLayout.setVisibility(View.GONE);
					viewHolderf.smallcloseLayout.setVisibility(View.VISIBLE);
				}
			} else {
				viewHolderf.bigOpenLayout.setVisibility(View.GONE);
				viewHolderf.smallcloseLayout.setVisibility(View.VISIBLE);
			}
			// 根据不同的用户类型显示不同的文字
			if (userType.equals(Constant.IDENTITY_DESIGNER)) {
				viewHolderf.openCheck.setText(context
						.getString(R.string.upload_pic));
			} else if (userType.equals(Constant.IDENTITY_OWNER)) {
				viewHolderf.openCheck.setText(context
						.getString(R.string.site_example_node_check));
			}

			viewHolderf.openDelay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					callBack.click(position, Constant.DELAY_ITEM);
				}
			});
			viewHolderf.openCheck.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					callBack.click(position, Constant.CHECK_ITEM);
				}
			});
			break;
		default:
			break;
		}

		return convertView;
	}

	/**
	 * @des 设置item里gridview的照片
	 * @param imageUrlList
	 * @param gridView
	 */
	private void setImageData(final List<String> imageUrlList, GridView gridView) {
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
					callBack.click(position, Constant.IMG_ITEM, imageUrlList);
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

	private static class ViewHolder2 {
		RelativeLayout smallcloseLayout;
		RelativeLayout bigOpenLayout;
		TextView closeNodeName;
		TextView openNodeName;
		TextView openDelay;
		TextView openCheck;
		TextView openTip;
		TextView closeTip;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
