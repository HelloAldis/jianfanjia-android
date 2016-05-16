package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.ui.activity.home.DesignerListActivity;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.business.RequirementBusiness;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.ui.interf.CheckListener;
import com.jianfanjia.cn.tools.IntentUtil;

/**
 * Name: DesignerByAppointOrReplaceAdapter
 * User: fengliang
 * Date: 2015-11-16
 * Time: 14:31
 */
public class DesignerByAppointOrReplaceAdapter extends BaseRecyclerViewAdapter<Map<String, Object>> {
    private static final String TAG = DesignerByAppointOrReplaceAdapter.class.getName();
    private CheckListener listener;
    private List<String> checkedList = new ArrayList<>();// 选中的数据
    private static final int TYPE_TAG = 0;
    private static final int TYPE_TITLE = 1;
    private List<Map<String, Object>> splitList = new ArrayList<>();
    private List<Integer> checkPositionlist;
    private int viewType = -1;
    private int canOrderCount = -1;

    public DesignerByAppointOrReplaceAdapter(Context context, List<Map<String, Object>> list, List<Map<String,
            Object>> splitList, int canOrderCount, CheckListener listener) {
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
                DesignerByAppointOrReplaceTagViewHolder tagViewHolder = (DesignerByAppointOrReplaceTagViewHolder)
                        viewHolder;
                String tag = (String) list.get(position).get(Constant.KEY);
                String text_tag = (String) list.get(position).get(Constant.TEXT_KEY);
                tagViewHolder.itemNameText.setText(tag);
                tagViewHolder.itemMoreText.setText(text_tag);
                tagViewHolder.itemMoreText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtil.startActivity(context, DesignerListActivity.class);
                    }
                });
                break;
            case TYPE_TITLE:
                final DesignerByAppointOrReplaceViewHolder holder = (DesignerByAppointOrReplaceViewHolder) viewHolder;
                final Designer designerCanOrderInfo = (Designer) list.get(position).get
                        (Constant.KEY);
                final String designerId = designerCanOrderInfo.get_id();
                holder.itemCheck.setTag(new Integer(position));//设置tag 否则划回来时选中消失
                holder.itemNameText.setText(designerCanOrderInfo.getUsername());
                if (designerCanOrderInfo.getMatch() != 0) {
                    holder.itemMarchText.setVisibility(View.VISIBLE);
                    holder.itemMarchText.setText("匹配度：" + designerCanOrderInfo.getMatch() + "%");
                } else {
                    holder.itemMarchText.setVisibility(View.GONE);
                }
                int respond_speed = (int) designerCanOrderInfo.getRespond_speed();
                int service_attitude = (int) designerCanOrderInfo.getService_attitude();
                holder.itemRatingBar.setRating((respond_speed + service_attitude) / 2);
                imageShow.displayImageHeadWidthThumnailImage(context, designerCanOrderInfo.getImageid(), holder
                        .itemwHeadView);
                if (designerCanOrderInfo.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
                    holder.itemInfoAuthView.setVisibility(View.VISIBLE);
                } else {
                    holder.itemInfoAuthView.setVisibility(View.GONE);
                }
                List<String> tags = designerCanOrderInfo.getTags();
                if(tags.size() > 0){
                    holder.itemTagText.setVisibility(View.VISIBLE);
                    holder.itemTagText.setText(tags.get(0));
                    switch (tags.get(0)){
                        case RequirementBusiness.TAG_NEW_GENERATE:
                            holder.itemTagText.setBackgroundResource(R.drawable.text_rectangle_blue_bg);
                            break;
                        case RequirementBusiness.TAG_MIDDER_GENERATE:
                            holder.itemTagText.setBackgroundResource(R.drawable.text_rectangle_pink_bg);
                            break;
                        case RequirementBusiness.TAG_HIGH_POINT:
                            holder.itemTagText.setBackgroundResource(R.drawable.text_rectangle_orange_bg);
                            break;
                    }
                }else {
                    holder.itemTagText.setVisibility(View.GONE);
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

    static class DesignerByAppointOrReplaceTagViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.list_item_name_text)
        TextView itemNameText;
        @Bind(R.id.list_item_more_text)
        TextView itemMoreText;

        public DesignerByAppointOrReplaceTagViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class DesignerByAppointOrReplaceViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.list_item_head_img)
        ImageView itemwHeadView;
        @Bind(R.id.list_item_name_text)
        TextView itemNameText;
        @Bind(R.id.ltm_req_tag)
        TextView itemTagText;
        @Bind(R.id.list_item_info_auth)
        ImageView itemInfoAuthView;
        @Bind(R.id.list_item_march_text)
        TextView itemMarchText;
        @Bind(R.id.list_item_ratingBar)
        RatingBar itemRatingBar;
        @Bind(R.id.list_item_check)
        CheckBox itemCheck;

        public DesignerByAppointOrReplaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

