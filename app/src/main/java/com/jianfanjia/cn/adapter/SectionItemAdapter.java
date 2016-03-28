package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.StringUtils;

public class SectionItemAdapter extends BaseAdapter {
    private static final String TAG = SectionItemAdapter.class.getName();
    private static final int IMG_COUNT = 9;
    private static final int CHECK_VIEW = 0;
    private static final int SECTION_ITME_VIEW = 1;
    private ItemClickCallBack callBack = null;
    private int lastClickItem = -1;// 记录上次点击的位置
    private int currentClickItem = -1;// 记录当前点击位置
    private SectionItemGridViewAdapter sectionItemGridViewAdapter;
    private String section_status;// 节点的状态
    private ProcessSection sectionInfo;
    private Context context;
    private LayoutInflater layoutInflater;
    private List<ProcessSectionItem> list = new ArrayList<>();
    private List<String> imageUrlList = new ArrayList<>();//源数据的List
    private List<String> showImageUrlList = new ArrayList<>();//用来展示图片的List
    private boolean isHasCheck;// 是否有验收
    private List<ProcessSection> showSectionInfoList = new ArrayList<>();

    public SectionItemAdapter(Context context, int position,
                              List<ProcessSection> showSectionInfoList, ItemClickCallBack callBack) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.callBack = callBack;
        setSectionInfoList(showSectionInfoList, position);
    }

    public void setSectionInfoList(List<ProcessSection> sectionInfos, int position) {
        if (sectionInfos != null) {
            showSectionInfoList.clear();
            showSectionInfoList.addAll(sectionInfos);
            setPosition(position);
        }
    }

    public void setPosition(int position) {
        sectionInfo = showSectionInfoList.get(position);
        section_status = sectionInfo.getStatus();
        initList();
    }

    private void initList() {
        section_status = sectionInfo.getStatus();
        list.clear();
        clearCurrentPosition();
        if (!sectionInfo.getName().equals("kai_gong")
                && !sectionInfo.getName().equals("chai_gai")) {
            isHasCheck = true;
            ProcessSectionItem sectionItemInfo = new ProcessSectionItem();
            sectionItemInfo.setName(context.getResources().getStringArray(
                    R.array.site_check_name)[MyApplication.getInstance()
                    .getPositionByItemName(sectionInfo.getName())]);
            sectionItemInfo.setDate(sectionInfo.getYs().getDate());
            sectionItemInfo.setOpen(false);
            list.add(sectionItemInfo);
        } else {
            isHasCheck = false;
        }

        for (ProcessSectionItem sectionItemInfo : sectionInfo.getItems()) {
            sectionItemInfo.setOpen(false);
            list.add(sectionItemInfo);
        }

        setLastOpen();
    }

    public void clearCurrentPosition() {
        this.currentClickItem = -1;
        this.lastClickItem = -1;
    }

    public void setCurrentOpenItem(int position) {
        imageUrlList.clear();
        if (list.get(position).getImages() != null) {
            for (String iamgeId : list.get(position).getImages()) {
                imageUrlList.add(iamgeId);
            }
        }
        this.currentClickItem = position;
        if (currentClickItem != lastClickItem) {
            if (lastClickItem != -1) {
                list.get(lastClickItem).setOpen(false);
            }
            list.get(currentClickItem).setOpen(true);
            lastClickItem = currentClickItem;
        } else {
            if (list.get(currentClickItem).isOpen()) {
                list.get(currentClickItem).setOpen(false);
            } else {
                list.get(currentClickItem).setOpen(true);
            }
        }
        notifyDataSetChanged();
    }

    public void setLastOpen() {
        if (list != null && list.size() > 0) {
            long max = list.get(0).getDate();
            for (int i = 0; i < list.size(); i++) {
                if (max < list.get(i).getDate()) {
                    max = list.get(i).getDate();
                }
            }
            for (int i = list.size() - 1; i >= 0; i--) {
                if (list.get(i).getDate() == max) {
                    setCurrentOpenItem(i);
                }
            }
        }
    }

    public String getCurrentItem() {
        if (isHasCheck) {
            return sectionInfo.getItems().get(currentClickItem - 1).getName();
        } else {
            return sectionInfo.getItems().get(currentClickItem).getName();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView);
    }

    public View initView(final int position, View convertView) {
        ViewHolder viewHolder = null;
        ViewHolder2 viewHolderf = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case CHECK_VIEW:
                    convertView = layoutInflater.inflate(
                            R.layout.list_item_process_head, null);
                    viewHolderf = new ViewHolder2(convertView);
                    convertView.setTag(viewHolderf);
                    break;
                case SECTION_ITME_VIEW:
                    convertView = layoutInflater.inflate(
                            R.layout.list_item_process_item, null);
                    viewHolder = new ViewHolder(convertView);
                    convertView.setTag(viewHolder);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case CHECK_VIEW:
                    viewHolderf = (ViewHolder2) convertView.getTag();
                    break;
                case SECTION_ITME_VIEW:
                    viewHolder = (ViewHolder) convertView.getTag();
                    break;
                default:
                    break;
            }
        }

        final ProcessSectionItem sectionItemInfo = list.get(position);
        switch (type) {
            case SECTION_ITME_VIEW:
                LogTool.d(TAG, "sectionItemInfo.getName()=" + sectionItemInfo.getName());
                viewHolder.closeNodeName.setText(MyApplication.getInstance()
                        .getStringById(sectionItemInfo.getName()));
                viewHolder.openNodeName.setText(MyApplication.getInstance()
                        .getStringById(sectionItemInfo.getName()));
                switch (sectionItemInfo.getStatus()) {
                    case Constant.FINISHED:
                        viewHolder.finishStatusIcon
                                .setImageResource(R.mipmap.icon_home_finish);
                        viewHolder.openFinishStatus.setText(context.getResources()
                                .getString(R.string.site_example_node_finish));
                        viewHolder.finishTime.setVisibility(View.VISIBLE);
                        viewHolder.openFinishStatus.setVisibility(View.VISIBLE);
                        viewHolder.bigOpenLayout
                                .setBackgroundResource(R.mipmap.list_item_text_bg2);
                        viewHolder.smallcloseLayout
                                .setBackgroundResource(R.mipmap.list_item_text_bg2);
                        break;
                    case Constant.NO_START:
                        viewHolder.finishStatusIcon
                                .setImageResource(R.drawable.site_listview_item_notstart_circle);
                        viewHolder.finishTime.setVisibility(View.GONE);
                        viewHolder.openFinishStatus.setText(context.getResources()
                                .getString(R.string.site_example_node_not_start));
                        viewHolder.openFinishStatus.setVisibility(View.VISIBLE);
                        viewHolder.bigOpenLayout
                                .setBackgroundResource(R.mipmap.list_item_text_bg1);
                        viewHolder.smallcloseLayout
                                .setBackgroundResource(R.mipmap.list_item_text_bg1);
                        break;
                    case Constant.DOING:
                        viewHolder.finishTime.setVisibility(View.GONE);
                        viewHolder.finishStatusIcon
                                .setImageResource(R.mipmap.icon_home_working);
                        viewHolder.bigOpenLayout
                                .setBackgroundResource(R.mipmap.list_item_text_bg2);
                        viewHolder.smallcloseLayout
                                .setBackgroundResource(R.mipmap.list_item_text_bg2);
                        viewHolder.openFinishStatus.setText(context.getResources()
                                .getString(R.string.site_example_node_working));
                        break;
                    default:
                        break;
                }

                // 设置最新动态的时间
                long date = sectionItemInfo.getDate();
                if (date != 0L) {
                    viewHolder.openUploadTime.setText(StringUtils
                            .covertLongToString(date));
                } else {
                    viewHolder.openUploadTime.setText("");
                }
                showImageUrlList.clear();
                if (null != imageUrlList && imageUrlList.size() > 0) {
                    if (imageUrlList.size() < IMG_COUNT) {// 最多上传9张照片
                        Log.i(TAG, "addImage");
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
                    viewHolder.openComment.setText(commentCount + "");
                    viewHolder.openComment.setCompoundDrawablesWithIntrinsicBounds(
                            context.getResources().getDrawable(
                                    R.mipmap.btn_icon_comment_pressed), null,
                            null, null);
                } else {
                    viewHolder.openComment.setText(R.string.commentText);
                    viewHolder.openComment.setCompoundDrawablesWithIntrinsicBounds(
                            context.getResources().getDrawable(
                                    R.mipmap.btn_icon_comment_normal), null,
                            null, null);
                }

                // 未开工的点击无法展开
                if (!section_status.equals(Constant.NO_START)) {
                    if (sectionItemInfo.isOpen()) {
                        viewHolder.bigOpenLayout.setVisibility(View.VISIBLE);
                        viewHolder.smallcloseLayout.setVisibility(View.GONE);
                    } else {
                        viewHolder.bigOpenLayout.setVisibility(View.GONE);
                        viewHolder.smallcloseLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    viewHolder.bigOpenLayout.setVisibility(View.GONE);
                    viewHolder.smallcloseLayout.setVisibility(View.VISIBLE);

                }
                // 设置上传照片
                setImageData(viewHolder.gridView);
                viewHolder.openComment.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isHasCheck) {
                            callBack.click(position - 1, Constant.COMMENT_ITEM);
                        } else {
                            callBack.click(position, Constant.COMMENT_ITEM);
                        }
                    }
                });

                break;
            case CHECK_VIEW:
                viewHolderf.closeNodeName.setText(sectionItemInfo.getName());
                viewHolderf.openNodeName.setText(sectionItemInfo.getName());
                if (section_status.equals(Constant.FINISHED)) {
                    viewHolderf.bigOpenLayout
                            .setBackgroundResource(R.mipmap.list_item_text_bg2);
                    viewHolderf.smallcloseLayout
                            .setBackgroundResource(R.mipmap.list_item_text_bg2);
                } else {
                    viewHolderf.bigOpenLayout
                            .setBackgroundResource(R.mipmap.list_item_text_bg1);
                    viewHolderf.smallcloseLayout
                            .setBackgroundResource(R.mipmap.list_item_text_bg1);
                }
                if (!section_status.equals(Constant.NO_START)) {
                    if (sectionItemInfo.isOpen()) {
                        viewHolderf.bigOpenLayout.setVisibility(View.VISIBLE);
                        viewHolderf.smallcloseLayout.setVisibility(View.GONE);
                    } else {
                        viewHolderf.bigOpenLayout.setVisibility(View.GONE);
                        viewHolderf.smallcloseLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    viewHolderf.bigOpenLayout.setVisibility(View.GONE);
                    viewHolderf.smallcloseLayout.setVisibility(View.VISIBLE);
                }
                LogTool.d(TAG, "section_status======" + section_status);
                switch (section_status) {
                    case Constant.FINISHED:
                        viewHolderf.finishStatusIcon
                                .setImageResource(R.mipmap.icon_home_finish);
                        viewHolderf.openDelay.setEnabled(false);
                        viewHolderf.openDelay.setTextColor(context.getResources().getColor(R.color.grey_color));
                        viewHolderf.openDelay.setText(context.getResources().getText(R.string
                                .site_example_node_delay_no));
                        break;
                    case Constant.YANQI_AGREE:
                    case Constant.YANQI_REFUSE:
                    case Constant.NO_START:
                    case Constant.DOING:
                        viewHolderf.site_list_head_delay_layout.setVisibility(View.GONE);
                        viewHolderf.site_list_head_checkbutton_layout.setVisibility(View.VISIBLE);
                        viewHolderf.openDelay.setEnabled(true);
                        viewHolderf.openDelay.setTextColor(context.getResources().getColor(R.color.orange_color));
                        viewHolderf.openDelay.setText(context.getResources().getText(R.string.site_example_node_delay));
                        viewHolderf.finishStatusIcon
                                .setImageResource(R.drawable.site_listview_item_notstart_circle);
                        viewHolderf.openDelay.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                callBack.click(position, Constant.DELAY_ITEM);
                            }
                        });
                        break;
                    case Constant.YANQI_BE_DOING:
                        LogTool.d(TAG, "this section is yanqi_doing");
                        Reschedule rescheduleInfo = sectionInfo.getReschedule();
                        if (null != rescheduleInfo) {
                            String role = rescheduleInfo.getRequest_role();
                            if (role.equals(Constant.IDENTITY_OWNER)) {
                                viewHolderf.site_list_head_delay_layout.setVisibility(View.GONE);
                                viewHolderf.site_list_head_checkbutton_layout.setVisibility(View.VISIBLE);
                                viewHolderf.openDelay.setTextColor(context.getResources().getColor(R.color.grey_color));
                                viewHolderf.openDelay.setText(context.getResources().getText(R.string
                                        .site_example_node_delay_doing));
                                viewHolderf.openDelay.setEnabled(false);
                            } else {
                                viewHolderf.site_list_head_delay_layout.setVisibility(View.VISIBLE);
                                viewHolderf.site_list_head_checkbutton_layout.setVisibility(View.GONE);
                                viewHolderf.site_list_head_delay_text.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        callBack.click(position, Constant.OPERATE_ITEM);
                                    }
                                });
                            }
                        }
                        break;
                    default:
                        break;
                }
                viewHolderf.openCheck.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        callBack.click(position, Constant.CHECK_ITEM);
                    }
                });
                break;
            default:
                break;
        }

        return convertView;
    }

    /**
     * @param
     * @param gridView
     * @des 设置item里gridview的照片
     */
    private void setImageData(GridView gridView) {
        sectionItemGridViewAdapter = new SectionItemGridViewAdapter(context, showImageUrlList);
        gridView.setAdapter(sectionItemGridViewAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                String data = showImageUrlList.get(position);
                Log.i(this.getClass().getName(), "data:" + data);
                Log.i(this.getClass().getName(), "imageUrlList size=" + imageUrlList.size());
                if (data.equals(Constant.HOME_ADD_PIC)) {
                    callBack.click(position, Constant.ADD_ITEM, imageUrlList);
                } else {
                    callBack.click(position, Constant.IMG_ITEM, imageUrlList);
                }
            }

        });
    }

    static class ViewHolder {
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

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder2 {
        @Bind(R.id.site_listview_item_content_small)
        RelativeLayout smallcloseLayout;
        @Bind(R.id.site_listview_item_content_expand)
        RelativeLayout bigOpenLayout;
        @Bind(R.id.site_list_head_checkbutton_layout)
        LinearLayout site_list_head_checkbutton_layout;
        @Bind(R.id.site_list_head_delay_layout)
        LinearLayout site_list_head_delay_layout;
        @Bind(R.id.site_list_head_delay_text)
        TextView site_list_head_delay_text;
        @Bind(R.id.site_list_item_content_small_node_name)
        TextView closeNodeName;
        @Bind(R.id.site_list_item_content_expand_node_name)
        TextView openNodeName;
        @Bind(R.id.site_list_head_delay)
        TextView openDelay;
        @Bind(R.id.site_list_head_check)
        TextView openCheck;
        @Bind(R.id.site_list_item_content_expand_node_more)
        TextView openTip;
        @Bind(R.id.site_list_item_content_small_node_more)
        TextView closeTip;
        @Bind(R.id.site_listview_item_status)
        ImageView finishStatusIcon;

        public ViewHolder2(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHasCheck) {
            if (position == 0) {
                return CHECK_VIEW;
            } else {
                return SECTION_ITME_VIEW;
            }
        } else {
            return SECTION_ITME_VIEW;
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}
