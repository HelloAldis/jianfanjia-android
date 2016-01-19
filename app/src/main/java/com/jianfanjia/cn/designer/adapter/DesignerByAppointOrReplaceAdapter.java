package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.bean.DesignerCanOrderInfo;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.interf.CheckListener;
import com.jianfanjia.cn.designer.tools.LogTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Name: DesignerByAppointOrReplaceAdapter
 * User: fengliang
 * Date: 2015-11-16
 * Time: 14:31
 */
public class DesignerByAppointOrReplaceAdapter extends BaseRecyclerViewAdapter<Map<String, Object>> {
    private static final String TAG = DesignerByAppointOrReplaceAdapter.class.getName();
    private CheckListener listener;
    private List<String> checkedList = new ArrayList<String>();// 选中的数据
    private static final int TYPE_TAG = 0;
    private static final int TYPE_TITLE = 1;
    private List<Map<String, Object>> splitList = new ArrayList<Map<String, Object>>();
    private List<Integer> checkPositionlist;
    private int viewType = -1;
    private int canOrderCount = -1;

    public DesignerByAppointOrReplaceAdapter(Context context, List<Map<String, Object>> list, List<Map<String, Object>> splitList, int canOrderCount, CheckListener listener) {
        super(context, list);
        this.splitList = splitList;
        this.canOrderCount = canOrderCount;
        this.listener = listener;
        checkPositionlist = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (splitList.contains(list.get(position))) {
            viewType = TYPE_TAG;
        } else {
            viewType = TYPE_TITLE;
        }
        return viewType;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<Map<String, Object>> list) {
        switch (viewHolder.getItemViewType()) {
            case TYPE_TAG:
                DesignerByAppointOrReplaceTagViewHolder tagViewHolder = (DesignerByAppointOrReplaceTagViewHolder) viewHolder;
                String tag = (String) list.get(position).get(Constant.KEY);
                String text_tag = (String) list.get(position).get(Constant.TEXT_KEY);
                tagViewHolder.itemNameText.setText(tag);
                tagViewHolder.itemMoreText.setText(text_tag);
                tagViewHolder.itemMoreText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogTool.d(TAG, "44444444444");
                    }
                });
                break;
            case TYPE_TITLE:
                final DesignerByAppointOrReplaceViewHolder holder = (DesignerByAppointOrReplaceViewHolder) viewHolder;
                final DesignerCanOrderInfo designerCanOrderInfo = (DesignerCanOrderInfo) list.get(position).get(Constant.KEY);
                final String designerId = designerCanOrderInfo.get_id();
                holder.itemCheck.setTag(new Integer(position));//设置tag 否则划回来时选中消失
                holder.itemNameText.setText(designerCanOrderInfo.getUsername());
                if (designerCanOrderInfo.getMatch() != 0) {
                    holder.itemMarchText.setVisibility(View.VISIBLE);
                    holder.itemRatingBar.setVisibility(View.GONE);
                    holder.itemMarchText.setText("匹配度" + designerCanOrderInfo.getMatch() + "%");
                } else {
                    holder.itemMarchText.setVisibility(View.GONE);
                    holder.itemRatingBar.setVisibility(View.VISIBLE);
                    int respond_speed = (int) designerCanOrderInfo.getRespond_speed();
                    int service_attitude = (int) designerCanOrderInfo.getService_attitude();
                    holder.itemRatingBar.setRating((respond_speed + service_attitude) / 2);
                }
                imageShow.displayImageHeadWidthThumnailImage(context, designerCanOrderInfo.getImageid(), holder.itemwHeadView);
                if (designerCanOrderInfo.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
                    holder.itemAuthView.setVisibility(View.VISIBLE);
                } else {
                    holder.itemAuthView.setVisibility(View.GONE);
                }
                holder.itemwHeadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != listener) {
                            listener.getItemData(holder.getLayoutPosition(), designerId);
                        }
                    }
                });
                holder.itemCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked && checkedList.size() < canOrderCount) {
                            if (!checkPositionlist.contains(holder.itemCheck.getTag())) {
                                checkedList.add(designerId);
                                checkPositionlist.add(new Integer(position));
                            }
                            holder.itemCheck.setChecked(true);
                        } else {
                            if (checkPositionlist.contains(holder.itemCheck.getTag())) {
                                checkedList.remove(designerId);
                                checkPositionlist.remove(new Integer(position));
                            }
                            holder.itemCheck.setChecked(false);
                        }
                        if (null != listener) {
                            listener.getCheckedData(checkedList);
                        }
                    }
                });

                if (checkPositionlist != null) {//checkbox复用问题
                    holder.itemCheck.setChecked((checkPositionlist.contains(new Integer(position)) ? true : false));
                } else {
                    holder.itemCheck.setChecked(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TYPE_TAG:
                View tagView = layoutInflater.inflate(R.layout.list_item_designer_by_appoint_tag, null);
                return tagView;
            case TYPE_TITLE:
                View titleView = layoutInflater.inflate(R.layout.list_item_designer_by_appoint_info, null);
                return titleView;
        }
        return null;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        switch (viewType) {
            case TYPE_TAG:
                return new DesignerByAppointOrReplaceTagViewHolder(view);
            case TYPE_TITLE:
                return new DesignerByAppointOrReplaceViewHolder(view);
        }
        return null;
    }

    private static class DesignerByAppointOrReplaceTagViewHolder extends RecyclerViewHolderBase {
        public TextView itemNameText = null;
        public TextView itemMoreText = null;

        public DesignerByAppointOrReplaceTagViewHolder(View itemView) {
            super(itemView);
            itemNameText = (TextView) itemView.findViewById(R.id.list_item_name_text);
            itemMoreText = (TextView) itemView.findViewById(R.id.list_item_more_text);
        }
    }

    private static class DesignerByAppointOrReplaceViewHolder extends RecyclerViewHolderBase {
        public ImageView itemwHeadView = null;
        public ImageView itemAuthView = null;
        public TextView itemNameText = null;
        public TextView itemMarchText = null;
        public RatingBar itemRatingBar = null;
        public CheckBox itemCheck = null;

        public DesignerByAppointOrReplaceViewHolder(View itemView) {
            super(itemView);
            itemwHeadView = (ImageView) itemView.findViewById(R.id.list_item_head_img);
            itemAuthView = (ImageView) itemView.findViewById(R.id.list_item_auth);
            itemNameText = (TextView) itemView.findViewById(R.id.list_item_name_text);
            itemMarchText = (TextView) itemView.findViewById(R.id.list_item_march_text);
            itemRatingBar = (RatingBar) itemView.findViewById(R.id.list_item_ratingBar);
            itemCheck = (CheckBox) itemView.findViewById(R.id.list_item_check);
        }
    }
}
