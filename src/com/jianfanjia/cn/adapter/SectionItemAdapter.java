package com.jianfanjia.cn.adapter;

import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.CommentInfo;
import com.jianfanjia.cn.bean.SectionItemInfo;
import com.jianfanjia.cn.config.Constant;

public class SectionItemAdapter extends BaseListAdapter<SectionItemInfo> {

	private int lastClickItem = -1;// 记录点击的位置
	private List<String> imageUrlList;// 该节点的图片list
	private List<CommentInfo> commentInfoList;// 该节点的评论list

	public SectionItemAdapter(Context context,
			List<SectionItemInfo> sectionItemInfos) {
		super(context, sectionItemInfos);
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
	public View initView(int position, View convertView) {
		ViewHolder viewHolder = null;
		final SectionItemInfo sectionItemInfo = list.get(position);
		imageUrlList = sectionItemInfo.getImages();
		commentInfoList = sectionItemInfo.getComments();
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
		// 未开工的点击无法展开
		// if (Integer.parseInt(sectionItemInfo.getStatus()) !=
		// Constant.NOT_START && position == lastClickItem) {
		if (position == lastClickItem) {
			viewHolder.bigOpenLayout.setVisibility(View.VISIBLE);
			viewHolder.smallcloseLayout.setVisibility(View.GONE);
		}else{
			viewHolder.bigOpenLayout.setVisibility(View.GONE);
			viewHolder.smallcloseLayout.setVisibility(View.VISIBLE);
		}
		viewHolder.openComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// MyApplication.getInstance().showToast("鎵撳紑璇勮");
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
}
