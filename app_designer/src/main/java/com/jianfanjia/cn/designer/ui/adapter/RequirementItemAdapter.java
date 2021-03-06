package com.jianfanjia.cn.designer.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.ui.interf.cutom_annotation.ReqItemFinder;
import com.jianfanjia.cn.designer.ui.interf.cutom_annotation.ReqItemFinderImp;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-16 10:00
 */
public class RequirementItemAdapter extends BaseAdapter {

    List<ReqItemFinderImp.ItemMap> itemMaps;

    ReqItemFinder reqItemFinder;

    private LayoutInflater layoutInflater;

    private List<String> mChooseKyes;

    public RequirementItemAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        reqItemFinder = new ReqItemFinderImp(context);
    }

    public void changeShow(int requsetcode,List<String> currentChooseItem) {
        itemMaps = reqItemFinder.findAll(requsetcode);
        this.mChooseKyes = currentChooseItem;
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
            view = layoutInflater.inflate(R.layout.list_item_req_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.bind(getItem(position));

        String key = itemMaps.get(position).key;
        boolean isChoose = false;
        for(String chooseKey : mChooseKyes){
            if(key.equals(chooseKey)){
                isChoose = true;
            }
        }
        if(isChoose){
            holder.chooseImageView.setVisibility(View.VISIBLE);
        }else {
            holder.chooseImageView.setVisibility(View.GONE);
        }

        return view;
    }

    protected class ViewHolder {

        @Bind(R.id.ltm_req_simple_item)
        TextView ltm_req_simple_item;

        @Bind(R.id.ltm_item_choosed)
        ImageView chooseImageView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(ReqItemFinderImp.ItemMap itemMap) {
            ltm_req_simple_item.setText(itemMap.value);
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
