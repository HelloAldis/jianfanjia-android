package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
import com.jianfanjia.cn.bean.DecorateLiveInfo;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.StringUtils;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-11 11:15
 */
public class DecorateLiveAdapter extends BaseRecycleAdapter<DecorateLiveInfo> {

    public DecorateLiveAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder holder, int position) {
        DecorateLiveInfo decorateLiveInfo = mDatas.get(position);

        DecorateLiveViewHolder viewHolder = (DecorateLiveViewHolder) holder;
        imageShow.displayScreenWidthThumnailImage(context, decorateLiveInfo.getCover_imageid(), viewHolder.bgImage);

        String designerImageid = decorateLiveInfo.getDesigner().getImageid();
        if (!TextUtils.isEmpty(designerImageid)) {
            imageShow.displayImageHeadWidthThumnailImage(context, designerImageid, viewHolder.headImage);
        }

        String status = decorateLiveInfo.getProcess().get(decorateLiveInfo.getProcess().size() -1 ).getName();
        viewHolder.statusText.setText(BusinessManager.convertSectionNameToLiveShow(status));
        if(decorateLiveInfo.getProgress().equals(Constant.DECORATE_LIVE_GOING)){
            viewHolder.statusText.setBackgroundColor(context.getResources().getColor(R.color.orange_color));
        }else {
            viewHolder.statusText.setBackgroundColor(context.getResources().getColor(R.color.green_color));
        }

        viewHolder.cellName.setText(decorateLiveInfo.getCell());

        viewHolder.updateDate.setText(context.getString(R.string.live_date) + StringUtils.covertLongToString(decorateLiveInfo.getCreate_at()));

        viewHolder.descriptionText.setText(decorateLiveInfo.getHouse_area() + "㎡，"
//                + BusinessManager.convertDectypeToShow(decorateLiveInfo.getDec_type()) + "，"
                + BusinessManager.convertHouseTypeToShow(decorateLiveInfo.getHouse_type()) + "，"
                + BusinessManager.convertDecStyleToShow(decorateLiveInfo.getDec_style()) + "风格");


    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_decorate_live, null,true);
        return new DecorateLiveViewHolder(view);
    }

    private static class DecorateLiveViewHolder extends RecyclerViewHolderBase {

        public ImageView bgImage;
        public TextView cellName;
        public TextView descriptionText;
        public TextView updateDate;
        public ImageView headImage;
        public TextView statusText;
        public ImageView authImage;

        public DecorateLiveViewHolder(View itemView) {
            super(itemView);
            this.bgImage = (ImageView) itemView.findViewById(R.id.cover_image);
            this.cellName = (TextView) itemView.findViewById(R.id.cell_name);
            this.descriptionText = (TextView) itemView.findViewById(R.id.produce_text);
            this.updateDate = (TextView) itemView.findViewById(R.id.live_date);
            this.headImage = (ImageView) itemView.findViewById(R.id.head_image);
            this.authImage = (ImageView) itemView.findViewById(R.id.list_item_auth);
            this.statusText = (TextView) itemView.findViewById(R.id.process_status);
        }
    }
}
