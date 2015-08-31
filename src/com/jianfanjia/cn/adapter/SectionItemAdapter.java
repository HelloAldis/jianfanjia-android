package com.jianfanjia.cn.adapter;

import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.NodeInfo;
import com.jianfanjia.cn.bean.SectionItemInfo;

public class SectionItemAdapter extends BaseListAdapter<SectionItemInfo> {
	private int lastClickItem = 0;// 记录上一次点击的条目
	private Animation animation;

	public SectionItemAdapter(Context context,
			List<SectionItemInfo> sectionItemInfos) {
		super(context, sectionItemInfos);
		animation = AnimationUtils.loadAnimation(context,
				R.anim.fragment_list_right_enter);
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
	}

	@Override
	public View initView(int position, View convertView) {
		ViewHolder viewHolder = null;
		final SectionItemInfo nodeInfo = list.get(position);
		Log.i(this.getClass().getName(), nodeInfo.getName());
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
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.closeNodeName.setText(MyApplication.getInstance()
				.getStringById(nodeInfo.getName()));
		viewHolder.openNodeName.setText(MyApplication.getInstance()
				.getStringById(nodeInfo.getName()));
		switch (Integer.parseInt(nodeInfo.getStatus())) {
		case NodeInfo.FINISH:
			viewHolder.finishStatusIcon
					.setImageResource(R.drawable.site_listview_item_finish_circle);
			viewHolder.openFinishStatus.setText(context.getResources()
					.getString(R.string.site_example_node_finish));
			viewHolder.finishTime.setVisibility(View.VISIBLE);
			break;
		case NodeInfo.NOT_START:
			viewHolder.finishStatusIcon
					.setImageResource(R.drawable.site_listview_item_notstart_circle);
			viewHolder.finishTime.setVisibility(View.GONE);
			break;
		case NodeInfo.WORKING:
			viewHolder.finishTime.setVisibility(View.GONE);
			viewHolder.finishStatusIcon
					.setImageResource(R.drawable.site_listview_item_working_circle);
			// 根据用户的不同设置不同的显示
			/*
			 * if(AppContext.getInstance().getLoginUser().getUserIdentity() ==
			 * UserInfo.IDENTITY_COMMON_USER){
			 * viewHolder.openFinishStatus.setText
			 * (context.getResources().getString
			 * (R.string.site_example_node_working)); }else{
			 * viewHolder.openFinishStatus
			 * .setText(context.getResources().getString
			 * (R.string.site_example_node_confirm_finish)); //设计师确认完工操�?
			 * viewHolder.openFinishStatus.setOnClickListener(new
			 * OnClickListener() {
			 * 
			 * @Override public void onClick(View v) {
			 * v.setOnClickListener(null);
			 * NodeInfo.setFinishStatus(NodeInfo.FINISH); } }); }
			 */
			break;
		default:
			break;
		}
		viewHolder.bigOpenLayout.setVisibility(View.GONE);
		viewHolder.smallcloseLayout.setVisibility(View.VISIBLE);
//		viewHolder.smallcloseLayout.startAnimation(animation);
		viewHolder.openComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// MyApplication.getInstance().showToast("打开评论");
			}
		});
		viewHolder.openUploadPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// AppContext.showToast("上传照片");
			}
		});
		return convertView;
	}
}
