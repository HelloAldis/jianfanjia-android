package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseListAdapter;
import com.jianfanjia.cn.business.DiaryBusiness;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.common.tool.TDevice;

public class AddDiaryGridViewAdapter extends BaseListAdapter<String> {

    public AddDiaryGridViewAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View initView(final int position, View convertView) {
        String imgUrl = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_item_add_diary_pic, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (imgUrl.equals(Constant.HOME_ADD_PIC)) {
            holder.img.setImageResource(R.mipmap.btn_icon_home_add);
            holder.tvDiaryDelete.setVisibility(View.GONE);
        } else {
            holder.tvDiaryDelete.setVisibility(View.VISIBLE);
            imageShow.displayThumbnailImage(imgUrl, holder.img, TDevice.dip2px(context, Global.PIC_WIDTH_NODE));
            holder.tvDiaryDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.size() == DiaryBusiness.UPLOAD_MAX_PIC_COUNT) {
                        list.remove(position);
                        if (!list.contains(Constant.HOME_ADD_PIC)) {
                            list.add(Constant.HOME_ADD_PIC);
                        }
                    } else {
                        list.remove(position);
                    }
                    notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.img)
        ImageView img;

        @Bind(R.id.ltm_diary_delte)
        TextView tvDiaryDelete;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
