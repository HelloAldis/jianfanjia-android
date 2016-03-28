package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.api.model.DecorateLive;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.OnItemClickListener;
import com.jianfanjia.cn.tools.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-11 11:15
 */
public class DecorateLiveAdapter extends BaseRecycleAdapter<DecorateLive> {

    private OnItemClickListener onItemClickListener;

    public DecorateLiveAdapter(Context context, RecyclerView recyclerView, OnItemClickListener onItemClickListener) {
        super(context, recyclerView);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DecorateLive decorateLiveInfo = mDatas.get(position);
        DecorateLiveViewHolder viewHolder = (DecorateLiveViewHolder) holder;
        imageShow.displayScreenWidthThumnailImage(context, decorateLiveInfo.getCover_imageid(), viewHolder.bgImage);
        String designerImageid = decorateLiveInfo.getDesigner().getImageid();
        if (!TextUtils.isEmpty(designerImageid)) {
            imageShow.displayImageHeadWidthThumnailImage(context, designerImageid, viewHolder.headImage);
        }

        String status = decorateLiveInfo.getProcess().get(decorateLiveInfo.getProcess().size() - 1).getName();
        viewHolder.statusText.setText(BusinessManager.convertSectionNameToLiveShow(status));
        if (decorateLiveInfo.getProgress().equals(Constant.DECORATE_LIVE_GOING)) {
            viewHolder.statusText.setBackgroundColor(context.getResources().getColor(R.color.orange_color));
        } else {
            viewHolder.statusText.setBackgroundColor(context.getResources().getColor(R.color.green_color));
        }

        viewHolder.cellName.setText(decorateLiveInfo.getCell());
        viewHolder.updateDate.setText(context.getString(R.string.live_date) + StringUtils.covertLongToString
                (decorateLiveInfo.getCreate_at()));

        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(decorateLiveInfo.getHouse_area())) {
            sb.append(decorateLiveInfo.getHouse_area() + "㎡ ");
        }
        if (!TextUtils.isEmpty(BusinessManager.convertHouseTypeToShow(decorateLiveInfo.getHouse_type()))) {
            sb.append(BusinessManager.convertHouseTypeToShow(decorateLiveInfo.getHouse_type()) + " ");
        }
        if (!TextUtils.isEmpty(BusinessManager.convertDecStyleToShow(decorateLiveInfo.getDec_style()))) {
            sb.append(BusinessManager.convertDecStyleToShow(decorateLiveInfo.getDec_style()) + " ");
        }
        if (!TextUtils.isEmpty(BusinessManager.convertDectypeToShow(decorateLiveInfo.getDec_type()))) {
            sb.append(BusinessManager.convertDectypeToShow(decorateLiveInfo.getDec_type()) + " ");
        }
        if (!TextUtils.isEmpty(BusinessManager.getWorkType(decorateLiveInfo.getWork_type()))) {
            sb.append(BusinessManager.getWorkType(decorateLiveInfo.getWork_type()));
        }
        viewHolder.descriptionText.setText(sb.toString());

        viewHolder.bgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(position);
                }
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_decorate_live, null, true);
        return new DecorateLiveViewHolder(view);
    }

    static class DecorateLiveViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.cover_image)
        ImageView bgImage;
        @Bind(R.id.cell_name)
        TextView cellName;
        @Bind(R.id.produce_text)
        TextView descriptionText;
        @Bind(R.id.live_date)
        TextView updateDate;
        @Bind(R.id.head_image)
        ImageView headImage;
        @Bind(R.id.process_status)
        TextView statusText;
        @Bind(R.id.list_item_auth)
        ImageView authImage;

        public DecorateLiveViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
