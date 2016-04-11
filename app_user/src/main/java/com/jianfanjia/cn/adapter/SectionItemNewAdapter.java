package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.api.model.ProcessSectionItem;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.ViewPagerItem;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.view.MyViewPager;
import com.jianfanjia.common.tool.DateFormatTool;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Name: SectionItemNewAdapter
 * User: fengliang
 * Date: 2016-04-06
 * Time: 17:11
 */
public class SectionItemNewAdapter extends BaseRecyclerViewAdapter<ProcessSection> {
    private static final String TAG = SectionItemNewAdapter.class.getName();
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_ITEM = 1;
    private int viewType = -1;
    private static final int IMG_COUNT = 9;
    private static final int CHECK_VIEW = 0;
    private static final int SECTION_ITME_VIEW = 1;
    private ItemClickCallBack callBack = null;
    private int lastClickItem = -1;// 记录上次点击的位置
    private int currentClickItem = -1;// 记录当前点击位置
    private int currentPosition = 0;
    private SectionItemGridViewAdapter sectionItemGridViewAdapter;
    private String section_status;// 节点的状态
    private ProcessSection processSection;
    private List<String> imageUrlList = new ArrayList<>();//源数据的List
    private List<String> showImageUrlList = new ArrayList<>();//用来展示图片的List
    private boolean isHasCheck;// 是否有验收
    private List<ViewPagerItem> processList = new ArrayList<>();
    private List<ProcessSectionItem> processSectionList = new ArrayList<>();
    private SectionViewPageAdapter sectionViewPageAdapter;

    private List<ProcessSection> list = new ArrayList<>();

    public SectionItemNewAdapter(Context context, List<ProcessSection> list, int position,
                                 ItemClickCallBack callBack) {
        super(context, list);
        this.callBack = callBack;
        setSectionInfoList(list, position);
    }

    public void setSectionInfoList(List<ProcessSection> sectionInfos, int position) {
        LogTool.d(TAG, "sectionInfos=" + sectionInfos.size());
        this.currentPosition = position;
        if (sectionInfos != null) {
            list.clear();
            list.addAll(sectionInfos);
            setPosition(position);
        }
        LogTool.d(TAG, "currentPosition----" + currentPosition);
    }

    public void setPosition(int position) {
        processSection = list.get(position);
        LogTool.d(TAG, "processSection======" + processSection.getLabel());
        section_status = processSection.getStatus();
        initList();
    }

    private void initList() {
        processSectionList.clear();
        clearCurrentPosition();
        for (ProcessSectionItem sectionItemInfo : processSection.getItems()) {
            sectionItemInfo.setIsOpen(false);
            processSectionList.add(sectionItemInfo);
        }
    }

    public void clearCurrentPosition() {
        this.currentClickItem = -1;
        this.lastClickItem = -1;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            viewType = TYPE_HEAD;
        } else {
            viewType = TYPE_ITEM;
        }
        return viewType;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, final List<ProcessSection> list) {
        switch (viewType) {
            case TYPE_HEAD:
                LogTool.d(TAG, "1111111111111111111");
                final HeadViewHolder headViewHolder = (HeadViewHolder) viewHolder;
                for (int i = 0; i < list.size(); i++) {
                    ViewPagerItem viewPagerItem = new ViewPagerItem();
                    viewPagerItem.setTitle(list.get(i).getLabel());
                    if (list.get(i).getStart_at() > 0) {
                        viewPagerItem.setDate(DateFormatTool.covertLongToString(
                                list.get(i).getStart_at(), "M.dd")
                                + "-"
                                + DateFormatTool.covertLongToString(list.get(i).getEnd_at(), "M.dd"));
                    }
                    if (list.get(i).getStatus().equals(Constant.NO_START)) {
                        int drawableId = context.getResources()
                                .getIdentifier("icon_home_normal" + (i + 1),
                                        "mipmap",
                                        context.getPackageName());
                        viewPagerItem.setResId(drawableId);
                    } else if (list.get(i).getStatus().equals(Constant.FINISHED)) {
                        int drawableId = context.getResources()
                                .getIdentifier("icon_home_checked" + (i + 1),
                                        "mipmap",
                                        context.getPackageName());
                        viewPagerItem.setResId(drawableId);
                    } else {
                        int drawableId = context.getResources()
                                .getIdentifier("icon_home_normal_" + (i + 1),
                                        "mipmap",
                                        context.getPackageName());
                        viewPagerItem.setResId(drawableId);
                    }
                    processList.add(viewPagerItem);
                }
                for (int i = 0; i < 3; i++) {
                    ViewPagerItem viewPagerItem = new ViewPagerItem();
                    viewPagerItem.setResId(R.mipmap.icon_process_no);
                    viewPagerItem.setTitle("");
                    viewPagerItem.setDate("");
                    processList.add(viewPagerItem);
                }
                SectionViewPageAdapter sectionViewPageAdapter = new SectionViewPageAdapter(context, processList,
                        new ViewPagerClickListener() {

                            @Override
                            public void onClickItem(int pos) {
                                LogTool.d(TAG, "pos-------------------------" + pos);
                                if (pos < list.size()) {
                                    headViewHolder.process_viewpager.setCurrentItem(pos);
                                    callBack.click(pos, Constant.DELAY_ITEM);
                                    ProcessSection processSection = list.get(pos);
                                    if (!processSection.getName().equals("kai_gong")
                                            && !processSection.getName().equals("chai_gai")) {
                                        headViewHolder.site_list_head_checkbutton_layout.setVisibility(View.VISIBLE);
                                    } else {
                                        headViewHolder.site_list_head_checkbutton_layout.setVisibility(View.GONE);
                                    }
                                }
                            }
                        });
                headViewHolder.process_viewpager.setAdapter(sectionViewPageAdapter);
                headViewHolder.process_viewpager.setCurrentItem(currentPosition);
                headViewHolder.process_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onPageSelected(int arg0) {
                        if (arg0 < list.size()) {
                            LogTool.d(TAG, "arg0=============================" + arg0);
                            callBack.click(arg0, Constant.DELAY_ITEM);
                            ProcessSection processSection = list.get(arg0);
                            if (!processSection.getName().equals("kai_gong")
                                    && !processSection.getName().equals("chai_gai")) {
                                headViewHolder.site_list_head_checkbutton_layout.setVisibility(View.VISIBLE);
                            } else {
                                headViewHolder.site_list_head_checkbutton_layout.setVisibility(View.GONE);
                            }
                        }
                    }
                });
                break;
            case TYPE_ITEM:
                LogTool.d(TAG, "333333333333333333333");
                LogTool.d(TAG, "processSectionList.size()===" + processSectionList.size() + "    " + (position - 1));
                ProcessSectionItem sectionItemInfo = processSectionList.get(position - 1);
                LogTool.d(TAG, "sectionItemInfo.getLabel()==================" + sectionItemInfo.getLabel());
                ItemViewHolder holder = (ItemViewHolder) viewHolder;
                holder.closeNodeName.setText(sectionItemInfo.getLabel());
                holder.openNodeName.setText(sectionItemInfo.getLabel());
                switch (sectionItemInfo.getStatus()) {
                    case Constant.FINISHED:
                        holder.finishStatusIcon
                                .setImageResource(R.mipmap.icon_home_finish);
                        holder.openFinishStatus.setText(context.getResources()
                                .getString(R.string.site_example_node_finish));
                        holder.finishTime.setVisibility(View.VISIBLE);
                        holder.openFinishStatus.setVisibility(View.VISIBLE);
                        holder.bigOpenLayout
                                .setBackgroundResource(R.mipmap.list_item_text_bg2);
                        holder.smallcloseLayout
                                .setBackgroundResource(R.mipmap.list_item_text_bg2);
                        break;
                    case Constant.NO_START:
                        holder.finishStatusIcon
                                .setImageResource(R.drawable.site_listview_item_notstart_circle);
                        holder.finishTime.setVisibility(View.GONE);
                        holder.openFinishStatus.setText(context.getResources()
                                .getString(R.string.site_example_node_not_start));
                        holder.openFinishStatus.setVisibility(View.VISIBLE);
                        holder.bigOpenLayout
                                .setBackgroundResource(R.mipmap.list_item_text_bg1);
                        holder.smallcloseLayout
                                .setBackgroundResource(R.mipmap.list_item_text_bg1);
                        break;
                    case Constant.DOING:
                        holder.finishTime.setVisibility(View.GONE);
                        holder.finishStatusIcon
                                .setImageResource(R.mipmap.icon_home_working);
                        holder.bigOpenLayout
                                .setBackgroundResource(R.mipmap.list_item_text_bg2);
                        holder.smallcloseLayout
                                .setBackgroundResource(R.mipmap.list_item_text_bg2);
                        holder.openFinishStatus.setText(context.getResources()
                                .getString(R.string.site_example_node_working));
                        break;
                    default:
                        break;
                }
                // 设置最新动态的时间
                long date = sectionItemInfo.getDate();
                if (date != 0L) {
                    holder.openUploadTime.setText(StringUtils
                            .covertLongToString(date));
                } else {
                    holder.openUploadTime.setText("");
                }
                showImageUrlList.clear();
                if (null != imageUrlList && imageUrlList.size() > 0) {
                    if (imageUrlList.size() < IMG_COUNT) {// 最多上传9张照片
                        LogTool.d(TAG, "addImage");
                        showImageUrlList.addAll(imageUrlList);
                        showImageUrlList.add(Constant.HOME_ADD_PIC);
                    } else {
                        showImageUrlList.addAll(imageUrlList);
                    }
                } else {
                    showImageUrlList.add(Constant.HOME_ADD_PIC);
                }
                int commentCount = sectionItemInfo.getComment_count();
                if (commentCount > 0) {
                    holder.openComment.setText(commentCount + "");
                    holder.openComment.setCompoundDrawablesWithIntrinsicBounds(
                            context.getResources().getDrawable(
                                    R.mipmap.btn_icon_comment_pressed), null,
                            null, null);
                } else {
                    holder.openComment.setText(R.string.commentText);
                    holder.openComment.setCompoundDrawablesWithIntrinsicBounds(
                            context.getResources().getDrawable(
                                    R.mipmap.btn_icon_comment_normal), null,
                            null, null);
                }

                // 设置上传照片
                setImageData(holder.gridView);
                holder.openComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBack.click(position - 1, Constant.COMMENT_ITEM);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                View headView = layoutInflater.inflate(R.layout.list_item_process_item_head, null);
                return headView;
            case TYPE_ITEM:
                View itemView = layoutInflater.inflate(R.layout.list_item_process_item, null);
                return itemView;
        }
        return null;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        switch (viewType) {
            case TYPE_HEAD:
                return new HeadViewHolder(view);
            case TYPE_ITEM:
                return new ItemViewHolder(view);
        }
        return null;
    }

    /**
     * @param
     * @param gridView
     * @des 设置item里gridview的照片
     */
    private void setImageData(GridView gridView) {
        sectionItemGridViewAdapter = new SectionItemGridViewAdapter(context, showImageUrlList);
        gridView.setAdapter(sectionItemGridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                String data = showImageUrlList.get(position);
                Log.i(TAG, "data:" + data);
                Log.i(TAG, "imageUrlList size=" + imageUrlList.size());
                if (data.equals(Constant.HOME_ADD_PIC)) {
                    callBack.click(position, Constant.ADD_ITEM, imageUrlList);
                } else {
                    callBack.click(position, Constant.IMG_ITEM, imageUrlList);
                }
            }

        });
    }

    static class HeadViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.process_viewpager)
        MyViewPager process_viewpager;
        @Bind(R.id.site_list_head_checkbutton_layout)
        LinearLayout site_list_head_checkbutton_layout;
        @Bind(R.id.site_list_head_delay_layout)
        LinearLayout site_list_head_delay_layout;
        @Bind(R.id.site_list_head_delay_text)
        TextView site_list_head_delay_text;
        @Bind(R.id.site_list_head_delay)
        TextView openDelay;
        @Bind(R.id.site_list_head_check)
        TextView openCheck;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.site_listview_item_content_small)
        RelativeLayout smallcloseLayout;
        @Bind(R.id.site_listview_item_content_expand)
        RelativeLayout bigOpenLayout;
        @Bind(R.id.site_list_item_content_small_node_name)
        TextView closeNodeName;
        @Bind(R.id.site_list_item_content_expand_node_name)
        TextView openNodeName;
        @Bind(R.id.site_list_item_content_expand_node_assess)
        TextView openComment;
        @Bind(R.id.site_list_item_content_expand_node_time)
        TextView openUploadTime;
        @Bind(R.id.site_list_item_content_small_node_finishtime)
        TextView finishTime;
        @Bind(R.id.site_list_item_content_expand_node_finish_status)
        TextView openFinishStatus;
        @Bind(R.id.site_listview_item_status)
        ImageView finishStatusIcon;
        @Bind(R.id.site_list_item_gridview)
        GridView gridView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
