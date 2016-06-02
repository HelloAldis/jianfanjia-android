package com.jianfanjia.cn.activity.ui.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.ui.interf.cutom_annotation.ReqItemFinder;
import com.jianfanjia.cn.activity.ui.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.activity.tools.ImageShow;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-16 10:00
 */
public class RequirementItemLoveStyleAdapter extends BaseAdapter{

    List<ReqItemFinderImp.ItemMap> itemMaps;

    ReqItemFinder reqItemFinder;

    private LayoutInflater layoutInflater;

    private Context context;

    public RequirementItemLoveStyleAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        reqItemFinder = new ReqItemFinderImp(context);
    }

    public void changeShow(int requsetcode){
        itemMaps = reqItemFinder.findAll(requsetcode);
        notifyDataSetChanged();
    }

    public List<ReqItemFinderImp.ItemMap> getItemMaps(){
        return itemMaps;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = layoutInflater.inflate(R.layout.grid_item_req_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.bind(getItem(position));

        return view;
    }

    class ViewHolder{

        @Bind(R.id.gtm_req_image)
        ImageView gtm_req_image;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }

        public void bind(ReqItemFinderImp.ItemMap itemMap) {
            TypedArray ta = context.getResources().obtainTypedArray(R.array.arr_lovestyle_pic);
            String imageId = "drawable://" + ta.getResourceId(Integer.parseInt(itemMap.key), 0);
            ImageShow.getImageShow().displayLocalImage(imageId, gtm_req_image);
            ta.recycle();
        }
    }

    @Override
    public int getCount() {
        return itemMaps == null ? 0 : itemMaps.size();
    }

    @Override
    public ReqItemFinderImp.ItemMap getItem(int position) {
        return itemMaps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
