package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.bean.CommentInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.tools.StringUtils;

import java.util.List;

/**
 * Name: CommentAdapter
 * User: fengliang
 * Date: 2015-10-28
 * Time: 17:10
 */
public class CommentAdapter extends BaseListAdapter<CommentInfo> {

    public CommentAdapter(Context context, List<CommentInfo> list) {
        super(context, list);
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
        viewHolder.itemNameView.setText(commentInfo.getByUser().getUsername());
        viewHolder.itemContentView.setText(commentInfo.getContent());
        String userType = commentInfo.getUsertype();
        if (userType.equals(Constant.IDENTITY_OWNER)) {
            viewHolder.itemIdentityView.setText(context
                    .getString(R.string.ower));
        } else {
            viewHolder.itemIdentityView.setText(context
                    .getString(R.string.designer));
        }
        viewHolder.itemTimeView.setText(StringUtils
                .covertLongToString(commentInfo.getDate()));
        imageLoader.displayImage(Url_New.GET_IMAGE + commentInfo.getByUser().getImageid(), viewHolder.itemHeadView, options);
        return convertView;
    }

    private static class ViewHolder {
        TextView itemNameView;// 评论人名称
        TextView itemTimeView;// 评论时间
        TextView itemContentView;// 评论内容
        TextView itemIdentityView;// 评论人身份
        ImageView itemHeadView;// 评论人头像
    }

}
