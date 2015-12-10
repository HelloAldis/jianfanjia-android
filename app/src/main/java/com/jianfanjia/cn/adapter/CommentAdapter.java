package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.CommentInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.StringUtils;

import java.util.List;

/**
 * Name: CommentAdapter
 * User: fengliang
 * Date: 2015-10-28
 * Time: 17:10
 */
public class CommentAdapter extends BaseRecyclerViewAdapter<CommentInfo> {

    public CommentAdapter(Context context, List<CommentInfo> list) {
        super(context, list);
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<CommentInfo> list) {
        CommentInfo commentInfo = list.get(position);
        CommentViewHolder holder = (CommentViewHolder) viewHolder;
        holder.itemNameView.setText(commentInfo.getByUser().getUsername());
        holder.itemContentView.setText(commentInfo.getContent());
        String userType = commentInfo.getUsertype();
        if (userType.equals(Constant.IDENTITY_OWNER)) {
            holder.itemIdentityView.setText(context
                    .getString(R.string.ower));
        } else {
            holder.itemIdentityView.setText(context
                    .getString(R.string.designer));
        }
        holder.itemTimeView.setText(StringUtils
                .covertLongToString(commentInfo.getDate()));
        String imageid = commentInfo.getByUser().getImageid();
        if (!imageid.contains(Constant.DEFALUT_PIC_HEAD)) {
            imageShow.displayImageHeadWidthThumnailImage(context, commentInfo.getByUser().getImageid(), holder.itemHeadView);
        } else {
            imageShow.displayLocalImage(commentInfo.getByUser().getImageid(), holder.itemHeadView);
        }
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_comment,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new CommentViewHolder(view);
    }

    private static class CommentViewHolder extends RecyclerViewHolderBase {
        public TextView itemNameView;// 评论人名称
        public TextView itemTimeView;// 评论时间
        public TextView itemContentView;// 评论内容
        public TextView itemIdentityView;// 评论人身份
        public ImageView itemHeadView;// 评论人头像

        public CommentViewHolder(View itemView) {
            super(itemView);
            itemNameView = (TextView) itemView
                    .findViewById(R.id.list_item_comment_username);
            itemContentView = (TextView) itemView
                    .findViewById(R.id.list_item_comment_content);
            itemIdentityView = (TextView) itemView
                    .findViewById(R.id.list_item_comment_userrole);
            itemTimeView = (TextView) itemView
                    .findViewById(R.id.list_item_comment_pubtime);
            itemHeadView = (ImageView) itemView
                    .findViewById(R.id.list_item_comment_userhead);
        }
    }
}
