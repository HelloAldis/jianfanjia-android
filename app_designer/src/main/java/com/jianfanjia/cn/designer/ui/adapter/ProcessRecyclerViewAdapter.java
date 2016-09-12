package com.jianfanjia.cn.designer.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.config.Constant;

/**
 * Name: ProcessRecyclerViewAdapter
 * User: fengliang
 * Date: 2016-01-20
 * Time: 10:51
 */
public class ProcessRecyclerViewAdapter extends BaseRecyclerViewAdapter<ProcessSection> {

    private static final String TAG = ProcessRecyclerViewAdapter.class.getName();
    private OnItemClickListener listener;

    public ProcessRecyclerViewAdapter(Context context, List<ProcessSection> list,
                                      OnItemClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<ProcessSection> list) {
        ProcessSection item = list.get(position);
        ProcessViewHolder holder = (ProcessViewHolder) viewHolder;
        holder.itemImgView.setImageResource(context.getResources()
                .getIdentifier("icon_home_bg" + (position + 1), "drawable",
                        context.getPackageName()));
        holder.itemTitleView.setText(item.getLabel());
        if (position == 0) {
            holder.LeftLineView.setVisibility(View.INVISIBLE);
            holder.rightLineView.setVisibility(View.VISIBLE);
        } else if (position == list.size() - 1) {
            holder.rightLineView.setVisibility(View.INVISIBLE);
            holder.LeftLineView.setVisibility(View.VISIBLE);
        } else {
            holder.rightLineView.setVisibility(View.VISIBLE);
            holder.LeftLineView.setVisibility(View.VISIBLE);
        }
        String sectionStatus = list.get(position).getStatus();
        if(sectionStatus.equals(Constant.FINISHED)){//已完工
            holder.itemImgView.setSelected(true);
            holder.itemTitleView.setSelected(true);
        }else if(sectionStatus.equals(Constant.NO_START)){//未开工

        }else {//其余状态
            holder.itemImgView.setEnabled(false);
            holder.itemTitleView.setSelected(true);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_process_view_item,
                null);
        return new ProcessViewHolder(view);
    }

    static class ProcessViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.list_item_process_img)
        public ImageView itemImgView;
        @Bind(R.id.list_item_process_text)
        public TextView itemTitleView;
        @Bind(R.id.left_line)
        public View LeftLineView;
        @Bind(R.id.right_line)
        public View rightLineView;

        public ProcessViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
