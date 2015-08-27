package com.jianfanjia.cn.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.CommentInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @class CommentInfoAdapter
 * @author zhanghao
 * @date 2015-8-27 ����10:50
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
		viewHolder.itemNameView.setText(commentInfo.getUserName());
		viewHolder.itemContentView.setText(commentInfo.getContent());
		viewHolder.itemTimeView.setText(commentInfo.getTime());
		viewHolder.itemIdentityView.setText(commentInfo.getUserIdentity());
		if (commentInfo.getUserImageUrl() != null) {
			ImageLoader.getInstance().displayImage(
					commentInfo.getUserImageUrl(), viewHolder.itemHeadView);
		} else {
			viewHolder.itemHeadView
					.setImageResource(R.drawable.site_listview_item_finish_circle);
		}
		return convertView;
	}

	class ViewHolder {
		TextView itemNameView;// ����������
		TextView itemTimeView;// ����ʱ��
		TextView itemContentView;// ��������
		TextView itemIdentityView;// ���������
		ImageView itemHeadView;// ������ͷ��
	}

}