package com.jianfanjia.cn.adapter;

import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.CommentInfo;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.http.LoadClientHelper;
import com.jianfanjia.cn.http.request.DesignerInfoRequest;
import com.jianfanjia.cn.http.request.OwnerInfoRequest;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.StringUtils;

/**
 * @class CommentInfoAdapter
 * @author zhanghao
 * @date 2015-8-27 上午10:50
 * 
 */
public class CommentInfoAdapter extends BaseListAdapter<CommentInfo> {
	private boolean isLoadDesignerInfo = false;
	private boolean isLoadOwnerInfo = false;

	public CommentInfoAdapter(Context context, List<CommentInfo> caigouList) {
		super(context, caigouList);
	}

	@Override
	public View initView(int position, View convertView) {
		ViewHolder viewHolder = null;
		CommentInfo commentInfo = list.get(position);
		Log.i(this.getClass().getName(), commentInfo.getContent());
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_item_comment,
					null);
			viewHolder = new ViewHolder();
			viewHolder.itemNameView = (TextView) convertView
					.findViewById(R.id.list_item_comment_username);
			viewHolder.itemContentView = (TextView) convertView
					.findViewById(R.id.list_item_comment_content);
			viewHolder.itemIdentityView = (TextView) convertView
					.findViewById(R.id.list_item_comment_userrole);
			viewHolder.itemTimeView = (TextView) convertView
					.findViewById(R.id.list_item_comment_pubtime);
			viewHolder.itemHeadView = (ImageView) convertView
					.findViewById(R.id.list_item_comment_userhead);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.itemContentView.setText(commentInfo.getContent());
		viewHolder.itemTimeView.setText(StringUtils
				.covertLongToString(commentInfo.getDate()));
		String usertype = commentInfo.getUsertype();
		if (usertype.equals(Constant.IDENTITY_DESIGNER)) {
			String designerId = dataManager.getDefaultDesignerId();
			viewHolder.itemIdentityView.setText(context
					.getString(R.string.designer));
			if (designerId != null) {
				DesignerInfo designerInfo = dataManager
						.getDesignerInfoById(designerId);
				if (designerInfo != null) {
					viewHolder.itemNameView
							.setText(designerInfo.getUsername() == null ? context
									.getString(R.string.designer)
									: designerInfo.getUsername());
					String imageId = designerInfo.getImageid();
					imageLoader.displayImage(
							imageId == null ? Constant.DEFALUT_DESIGNER_PIC
									: (Url.GET_IMAGE + imageId),
							viewHolder.itemHeadView, options);
				} else {
					imageLoader.displayImage(Constant.DEFALUT_DESIGNER_PIC,
							viewHolder.itemHeadView, options);
					if (!isLoadDesignerInfo) {
						if (dataManager.getUserType().equals(usertype)) {
							LoadClientHelper
									.getDesignerInfoById(context,
											new DesignerInfoRequest(context,
													designerId), this);
							isLoadDesignerInfo = true;
						} else {
							LoadClientHelper
									.getOwnerDesignerInfoById(context,
											new DesignerInfoRequest(context,
													designerId), this);
							isLoadDesignerInfo = true;
						}
					}
				}
			} else {
				imageLoader.displayImage(Constant.DEFALUT_DESIGNER_PIC,
						viewHolder.itemHeadView, options);
			}
		} else if (usertype.equals(Constant.IDENTITY_OWNER)) {
			String ownerId = dataManager.getDefaultOwnerId();
			viewHolder.itemIdentityView.setText(context
					.getString(R.string.ower));
			if (ownerId != null) {
				OwnerInfo ownerInfo = dataManager.getOwnerInfoById(ownerId);
				if (ownerInfo != null) {
					LogTool.d("ownerInfo",
							"ownerInfo =" + JsonParser.beanToJson(ownerInfo));

					viewHolder.itemNameView
							.setText(ownerInfo.getUsername() == null ? context
									.getString(R.string.ower) : ownerInfo
									.getUsername());
					String imageId = ownerInfo.getImageid();
					LogTool.d("imageId", "ownerImageId =" + imageId);
					imageLoader.displayImage(
							imageId == null ? Constant.DEFALUT_OWNER_PIC
									: (Url.GET_IMAGE + imageId),
							viewHolder.itemHeadView, options);
				} else {
					imageLoader.displayImage(Constant.DEFALUT_OWNER_PIC,
							viewHolder.itemHeadView, options);
					if (!isLoadOwnerInfo) {
						LoadClientHelper.getOwnerInfoById(context,
								new OwnerInfoRequest(context, ownerId), this);
						isLoadOwnerInfo = true;
					}
				}
			} else {
				imageLoader.displayImage(Constant.DEFALUT_OWNER_PIC,
						viewHolder.itemHeadView, options);
			}
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView itemNameView;// 评论人名称
		TextView itemTimeView;// 评论时间
		TextView itemContentView;// 评论内容
		TextView itemIdentityView;// 评论人身份
		ImageView itemHeadView;// 评论人头像
	}

	@Override
	public void loadSuccess() {
		notifyDataSetChanged();
	}

	@Override
	public void loadFailture() {
		isLoadDesignerInfo = false;
		isLoadOwnerInfo = false;
	}

}