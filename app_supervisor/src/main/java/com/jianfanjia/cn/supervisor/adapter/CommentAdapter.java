package com.jianfanjia.cn.supervisor.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.Comment;
import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.supervisor.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.supervisor.config.Constant;
import com.jianfanjia.common.tool.DateFormatTool;
import com.jianfanjia.common.tool.LogTool;

/**
 * Name: CommentAdapter
 * User: fengliang
 * Date: 2015-10-28
 * Time: 17:10
 */
public class CommentAdapter extends BaseRecyclerViewAdapter<Comment> {
    private static final String TAG = CommentAdapter.class.getName();

    public CommentAdapter(Context context, List<Comment> list) {
        super(context, list);
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<Comment> list) {
        Comment commentInfo = list.get(position);
        CommentViewHolder holder = (CommentViewHolder) viewHolder;
        holder.itemNameView.setText(commentInfo.getByUser().getUsername());
        holder.itemContentView.setText(commentInfo.getContent());
        String userType = commentInfo.getUsertype();
        if (userType.equals(Constant.IDENTITY_OWNER)) {
            holder.itemIdentityView.setText(context.getString(R.string.ower));
        } else {
            holder.itemIdentityView.setText(context.getString(R.string.designer));
        }
        holder.itemTimeView.setText(DateFormatTool.toLocalTimeString(commentInfo.getDate()));
        String imageid = commentInfo.getByUser().getImageid();
        LogTool.d(TAG, "imageid=" + imageid);
        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(context, imageid, holder.itemHeadView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, holder.itemHeadView);
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

    static class CommentViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.list_item_comment_username)
        TextView itemNameView;// 评论人名称
        @Bind(R.id.list_item_comment_pubtime)
        TextView itemTimeView;// 评论时间
        @Bind(R.id.list_item_comment_content)
        TextView itemContentView;// 评论内容
        @Bind(R.id.list_item_comment_userrole)
        TextView itemIdentityView;// 评论人身份
        @Bind(R.id.list_item_comment_userhead)
        ImageView itemHeadView;// 评论人头像

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
