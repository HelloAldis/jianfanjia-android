package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.bean.GridItem;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.interf.ItemClickCallBack;
import com.jianfanjia.cn.designer.interf.UploadListener;

/**
 * Name: CheckGridViewAdapter
 * User: fengliang
 * Date: 2016-01-29
 * Time: 14:11
 */
public class CheckGridViewAdapter extends BaseRecyclerViewAdapter<GridItem> {
    private static final String TAG = CheckGridViewAdapter.class.getName();
    private boolean isCanDelete;
    private UploadListener listener;
    private ItemClickCallBack itemClickCallBack;
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_ITEM = 1;
    private int viewType = -1;

    public CheckGridViewAdapter(Context context, List<GridItem> list, UploadListener listener, ItemClickCallBack itemClickCallBack) {
        super(context, list);
        this.itemClickCallBack = itemClickCallBack;
        this.listener = listener;
        isCanDelete = false;
    }

    public boolean isCanDelete() {
        return isCanDelete;
    }

    public void setCanDelete(boolean isCanDelete) {
        this.isCanDelete = isCanDelete;
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
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<GridItem> list) {
        switch (viewType) {
            case TYPE_HEAD:
                CheckHeadHolder checkHeadHolder = (CheckHeadHolder) viewHolder;
                checkHeadHolder.text_title_show_pic.setText(context.getResources().getString(R.string.show_pic));
                checkHeadHolder.text_title_upload_pic.setText(context.getResources().getString(R.string.upload_pic));
                break;
            case TYPE_ITEM:
                GridItem item = list.get(position - 1);
                CheckItemViewHolder checkItemViewHolder = (CheckItemViewHolder) viewHolder;
                String imgId = item.getImgId();
                if ((position - 1) % 2 != 0) {
                    if (imgId.equals(Constant.HOME_ADD_PIC)) {
                        imageShow.displayLocalImage(imgId, checkItemViewHolder.img);
                        if (!isCanDelete()) {
                            checkItemViewHolder.img.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    if ((position - 1) % 2 != 0) {
                                        listener.onUpload((position - 1) / 2);
                                    }
                                }

                            });
                        } else {
                            checkItemViewHolder.img.setOnClickListener(null);
                        }
                        checkItemViewHolder.delete.setVisibility(View.GONE);
                    } else {
                        imageShow.displayHalfScreenWidthThumnailImage(context, imgId, checkItemViewHolder.img);
                        if (isCanDelete()) {
                            checkItemViewHolder.delete.setVisibility(View.VISIBLE);
                            checkItemViewHolder.delete.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    if ((position - 1) % 2 != 0) {
                                        listener.delete((position - 1) / 2);
                                    }
                                }
                            });
                        } else {
                            checkItemViewHolder.img.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    itemClickCallBack.click(position - 1, Constant.IMG_ITEM);
                                }
                            });
                            checkItemViewHolder.delete.setVisibility(View.GONE);
                        }
                    }

                } else {
                    checkItemViewHolder.img.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            itemClickCallBack.click(position - 1, Constant.IMG_ITEM);
                        }
                    });
                    imageShow.displayLocalImage(imgId, checkItemViewHolder.img);
                    checkItemViewHolder.delete.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                View headView = layoutInflater.inflate(R.layout.grid_item_check_pic_tag, null);
                return headView;
            case TYPE_ITEM:
                View itemView = layoutInflater.inflate(R.layout.grid_item_check_pic, null);
                return itemView;
        }
        return null;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        switch (viewType) {
            case TYPE_HEAD:
                return new CheckHeadHolder(view);
            case TYPE_ITEM:
                return new CheckItemViewHolder(view);
        }
        return null;
    }

    static class CheckHeadHolder extends RecyclerViewHolderBase {
        @Bind(R.id.text_title_show_pic)
        public TextView text_title_show_pic;
        @Bind(R.id.text_title_upload_pic)
        public TextView text_title_upload_pic;

        public CheckHeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    static class CheckItemViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.img)
        public ImageView img = null;
        @Bind(R.id.delete)
        public ImageView delete = null;

        public CheckItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
