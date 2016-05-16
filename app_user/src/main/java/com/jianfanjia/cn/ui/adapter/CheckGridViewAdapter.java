package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.GridItem;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.ui.interf.ItemClickCallBack;

/**
 * Name: CheckGridViewAdapter
 * User: fengliang
 * Date: 2016-01-29
 * Time: 14:11
 */
public class CheckGridViewAdapter extends BaseRecyclerViewAdapter<GridItem> {
    private static final String TAG = CheckGridViewAdapter.class.getName();
    private ItemClickCallBack itemClickCallBack;
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_ITEM = 1;
    private int viewType = -1;

    public CheckGridViewAdapter(Context context, List<GridItem> list, ItemClickCallBack itemClickCallBack) {
        super(context, list);
        this.itemClickCallBack = itemClickCallBack;
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
                    if (imgId.equals(Constant.DEFALUT_PIC)) {
                        imageShow.displayLocalImage(imgId, checkItemViewHolder.img);
                    } else {
                        imageShow.displayHalfScreenWidthThumnailImage(context, imgId, checkItemViewHolder.img);
                        checkItemViewHolder.img.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                if (null != itemClickCallBack) {
                                    itemClickCallBack.click(position - 1, Constant.IMG_ITEM);
                                }
                            }
                        });
                    }
                } else {
                    checkItemViewHolder.img.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            if (null != itemClickCallBack) {
                                itemClickCallBack.click(position - 1, Constant.IMG_ITEM);
                            }
                        }
                    });
                    imageShow.displayLocalImage(imgId, checkItemViewHolder.img);
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
        protected TextView text_title_show_pic;
        @Bind(R.id.text_title_upload_pic)
        protected TextView text_title_upload_pic;

        public CheckHeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class CheckItemViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.img)
        ImageView img = null;

        public CheckItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
