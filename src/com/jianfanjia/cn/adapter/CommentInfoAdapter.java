package com.jianfanjia.cn.adapter;

import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.CommentInfo;
import com.jianfanjia.cn.bean.UserByDesignerInfo;
import com.jianfanjia.cn.bean.UserByOwnerInfo;
import com.jianfanjia.cn.cache.DataManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.tools.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @class CommentInfoAdapter
 * @author zhanghao
 * @date 2015-8-27 上午10:50
 * 
 */
public class CommentInfoAdapter extends BaseListAdapter<CommentInfo> {

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
		if (commentInfo.getUsertype().equals(Constant.IDENTITY_DESIGNER)) {
			String designerId = DataManager.getInstance()
					.getDefaultDesignerId();
			viewHolder.itemIdentityView.setText(context
					.getString(R.string.designer));
			if (designerId != null) {
				UserByDesignerInfo designerInfo = DataManager.getInstance()
						.getDesignerInfo(designerId);
				if (designerInfo != null) {
					viewHolder.itemNameView
							.setText(designerInfo.getUsername() == null ? context
									.getString(R.string.designer)
									: designerInfo.getUsername());
					String imageId = designerInfo.getImageid();
					ImageLoader.getInstance().displayImage(
							imageId == null ? Constant.DEFALUT_DESIGNER_PIC
									: (Url.GET_IMAGE + imageId),
							viewHolder.itemHeadView);
				}
			}
		} else {
			String ownerId = DataManager.getInstance().getDefaultOwnerId();
			if (ownerId != null) {
				UserByOwnerInfo ownerInfo = DataManager.getInstance()
						.getOwnerInfo(ownerId);
				viewHolder.itemIdentityView.setText(context
						.getString(R.string.ower));
				if (ownerInfo != null) {
					viewHolder.itemNameView
							.setText(ownerInfo.getUsername() == null ? context
									.getString(R.string.ower) : ownerInfo
									.getUsername());
					String imageId = ownerInfo.getImageId();
					imageLoader.displayImage(
							imageId == null ? Constant.DEFALUT_OWNER_PIC
									: (Url.GET_IMAGE + imageId),
							viewHolder.itemHeadView, options);
				}
			}

		}

		return convertView;
	}

	class ViewHolder {
		TextView itemNameView;// 评论人名称
		TextView itemTimeView;// 评论时间
		TextView itemContentView;// 评论内容
		TextView itemIdentityView;// 评论人身份
		ImageView itemHeadView;// 评论人头像
	}

}