package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.Comment;
import com.jianfanjia.api.model.DiaryInfo;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.common.tool.DateFormatTool;
import com.jianfanjia.common.tool.LogTool;

/**
 * Name: DiaryDetailInfoAdapter 日记详情
 * User: fengliang
 * Date: 2015-10-28
 * Time: 17:10
 */
public class DiaryDetailInfoAdapter extends BaseRecyclerViewAdapter<Comment> {

    private static final String TAG = DiaryDetailInfoAdapter.class.getName();

    public static final int DIARY_DETAIL_TYPE = 0;//日历详情
    public static final int COMMENT_TYPE = 1;//评论

    public DiaryDetailInfoAdapter(Context context, DiaryInfo diaryInfo) {
        super(context, null);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return DIARY_DETAIL_TYPE;
        } else {
            return COMMENT_TYPE;
        }
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<Comment> list) {
        switch (getItemViewType(position)) {
            case DIARY_DETAIL_TYPE:
                bindDiary((DiaryDynamicAdapter.DiaryViewHolder)viewHolder);
                break;
            case COMMENT_TYPE:
                bindComment((CommentViewHolder) viewHolder, position - 1, list);
                break;
        }
    }

    private void bindDiary(DiaryDynamicAdapter.DiaryViewHolder viewHolder) {

    }

    private void bindComment(CommentViewHolder viewHolder, int position, List<Comment> list) {
        Comment commentInfo = list.get(position);
        CommentViewHolder holder = viewHolder;
        holder.itemContentView.setText(commentInfo.getContent());
        String userType = commentInfo.getUsertype();
        String imageid = Constant.DEFALUT_OWNER_PIC;
        String userName = "";
        if (userType.equals(Constant.IDENTITY_OWNER)) {
            holder.itemIdentityView.setText(context.getString(R.string.me));
            holder.itemIdentityView.setTextColor(context.getResources().getColor(R.color.orange_color));
            imageid = commentInfo.getByUser().getImageid();
            userName = commentInfo.getByUser().getUsername();
        } else if (userType.equals(Constant.IDENTITY_DESIGNER)) {
            holder.itemIdentityView.setText(context.getString(R.string.designer));
            holder.itemIdentityView.setTextColor(context.getResources().getColor(R.color.blue_color));
            imageid = commentInfo.getByDesigner().getImageid();
            userName = commentInfo.getByDesigner().getUsername();
        } else if (userType.equals(Constant.IDENTITY_SUPERVISOR)) {
            holder.itemIdentityView.setText(context.getString(R.string.supervisor));
            holder.itemIdentityView.setTextColor(context.getResources().getColor(R.color.green_color));
            imageid = commentInfo.getBySupervisor().getImageid();
            userName = commentInfo.getBySupervisor().getUsername();
        }
        holder.itemNameView.setText(userName);
        holder.itemTimeView.setText(DateFormatTool.longToStringHasMini(commentInfo.getDate()));
        LogTool.d(TAG, "imageid=" + imageid);
        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(context, imageid, holder.itemHeadView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, holder.itemHeadView);
        }
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(int viewType) {
        View view;
        switch (viewType) {
            case DIARY_DETAIL_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_fragment_diary,
                        null);
                return new DiaryDynamicAdapter.DiaryViewHolder(view);
            case COMMENT_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_comment,
                        null);
                return new CommentViewHolder(view);
        }
        return null;
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


