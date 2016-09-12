package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseListAdapter;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;

public class AddDiaryGridViewAdapter extends BaseListAdapter<String> {

    private DeleteListener mDeleteListener;

    public AddDiaryGridViewAdapter(Context context, List<String> list) {
        super(context, list);
    }

    public void setDeleteListener(DeleteListener deleteListener) {
        mDeleteListener = deleteListener;
    }

    @Override
    public View initView(final int position, View convertView) {
        String imgUrl = list.get(position);
        convertView = layoutInflater.inflate(R.layout.grid_item_showpic_with_delete, null);
        ViewHolder holder = new ViewHolder(convertView);
        LogTool.d("imgUrl" + imgUrl + ",position =" + position);
        if (imgUrl.equals(Constant.DEFALUT_ADD_DIARY_PIC)) {
            holder.img.setImageResource(R.mipmap.icon_diary_add_pic);
            holder.img.invalidate();
            holder.tvDiaryDelete.setVisibility(View.GONE);
        } else {
            holder.tvDiaryDelete.setVisibility(View.VISIBLE);
            imageShow.displayThumbnailImage(imgUrl, holder.img, TDevice.dip2px(context, Global.PIC_WIDTH_NODE));
            holder.tvDiaryDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDeleteListener != null) {
                        mDeleteListener.delete(position);
                    }
                }
            });
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.img)
        ImageView img;

        @Bind(R.id.ltm_diary_delte)
        ImageView tvDiaryDelete;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface DeleteListener {
        void delete(int position);
    }

}
