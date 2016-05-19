package com.jianfanjia.cn.designer.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.Team;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.base.RecyclerViewHolderBase;

/**
 * Description: com.jianfanjia.cn.designer.ui.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-19 14:36
 */
public class DesignerTeamAuthAdapter extends BaseRecyclerViewAdapter<Team> {

    private OnItemEditListener listener;

    private static final int HEAD_TYPE = 0;
    private static final int CONTENT_TYPE = 1;

    private boolean isEdit = false;

    public DesignerTeamAuthAdapter(Context context, List<Team> list, OnItemEditListener listener) {
        super(context, list);
        this.listener = listener;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<Team> list) {
        switch (getItemViewType(position)) {
            case CONTENT_TYPE:
                Team team = list.get(position - 1);
                DesignerTeamViewHolder holder = (DesignerTeamViewHolder) viewHolder;
                bindContentView(position - 1, team, holder);
                break;
            case HEAD_TYPE:
                DesignerWorksViewHolderHead holderHead = (DesignerWorksViewHolderHead) viewHolder;
                bindHeadView(holderHead);
                break;
        }

    }

    private void bindHeadView(DesignerWorksViewHolderHead viewHolder) {
        viewHolder.uploadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemAdd();
            }
        });
    }

    private void bindContentView(final int position, Team team, DesignerTeamViewHolder holder) {

        holder.teamManagerText.setText(TextUtils.isEmpty(team.getManager()) ? "" : team.getManager());

        String addr = team.getProvince() + team.getCity() + team.getDistrict();
        holder.teamAddrText.setText(addr);

        if (isEdit) {
            holder.deleteLayout.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(null);
            holder.teamAddrText.setVisibility(View.INVISIBLE);
        } else {
            holder.deleteLayout.setVisibility(View.GONE);
            holder.teamAddrText.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

        holder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemDelete(position);
            }
        });


    }

    @Override
    public void remove(int position) {
        if (list == null) return;
        list.remove(position);
        notifyItemRemoved(position + 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD_TYPE;
        } else {
            return CONTENT_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 1 : list.size() + 1;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(int viewType) {
        View view;
        switch (viewType) {
            case CONTENT_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_designer_team,
                        null);
                return new DesignerTeamViewHolder(view);
            case HEAD_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_designer_team_head,
                        null);
                return new DesignerWorksViewHolderHead(view);
        }
        return null;
    }


    static class DesignerTeamViewHolder extends RecyclerViewHolderBase {

        @Bind(R.id.team_manager_addr)
        TextView teamAddrText;

        @Bind(R.id.team_manager_name)
        TextView teamManagerText;

        @Bind(R.id.deleteTeamLayout)
        FrameLayout deleteLayout;

        public DesignerTeamViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class DesignerWorksViewHolderHead extends RecyclerViewHolderBase {
        @Bind(R.id.upload_product_layout)
        FrameLayout uploadLayout;

        public DesignerWorksViewHolderHead(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
