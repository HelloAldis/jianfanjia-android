package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.bean.ProcessSectionItem;
import com.jianfanjia.cn.designer.interf.OnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Name: ProcessRecyclerViewAdapter
 * User: fengliang
 * Date: 2016-01-20
 * Time: 10:51
 */
public class ProcessRecyclerViewAdapter extends BaseRecyclerViewAdapter<ProcessSectionItem> {
    private static final String TAG = ProcessRecyclerViewAdapter.class.getName();
    private OnItemClickListener listener;
    private int processIndex;

    public static final int FIRST = 0;
    public static final int LAST = 1;
    public static final int NORMAL = 2;

    public ProcessRecyclerViewAdapter(Context context, List<ProcessSectionItem> list, int processIndex,
                                      OnItemClickListener listener) {
        super(context, list);
        this.processIndex = processIndex;
        this.listener = listener;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<ProcessSectionItem> list) {
        ProcessSectionItem item = list.get(position);
        ProcessViewHolder holder = (ProcessViewHolder) viewHolder;
        holder.itemImgView.setImageResource(item.getRes());
        holder.itemTitleView.setText(item.getTitle());
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
        if (position < processIndex) {
            holder.itemImgView.setSelected(true);
            holder.itemTitleView.setSelected(true);
        } else if (position == processIndex) {
            holder.itemImgView.setEnabled(false);
            holder.itemTitleView.setSelected(true);
        } else {

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.OnItemClick(position);
                }
            }
        });
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_process_view_item,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
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
