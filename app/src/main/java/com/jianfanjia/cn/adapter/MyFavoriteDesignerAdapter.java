package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.view.custom_annotation_view.MyFavoriteDesignerView;
import com.jianfanjia.cn.view.custom_annotation_view.MyFavoriteDesignerView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-16 10:00
 */
@EBean
public class MyFavoriteDesignerAdapter extends BaseAdapter {

    List<DesignerInfo> designerInfos;

    @RootContext
    Context context;

    /*private void addItem(DesignerInfo designerInfo) {
        designerInfos.add(designerInfo);
        notifyDataSetChanged();
    }

    public void addItems(List<DesignerInfo> designers) {
        designerInfos.clear();
        for (DesignerInfo designerInfo : designers) {
            addItem(designerInfo);
        }
        notifyDataSetChanged();
    }*/

    public List<DesignerInfo> getDesignerInfos() {
        return designerInfos;
    }

    public void setDesignerInfos(List<DesignerInfo> designerInfos) {
        this.designerInfos = designerInfos;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyFavoriteDesignerView myFavoriteDesignerView;
        if (convertView == null) {
            myFavoriteDesignerView = MyFavoriteDesignerView_.build(context);
        } else {
            myFavoriteDesignerView = (MyFavoriteDesignerView) convertView;
        }

        myFavoriteDesignerView.bind(getItem(position));

        return myFavoriteDesignerView;
    }

    @Override
    public int getCount() {
        return designerInfos == null ? 0 : designerInfos.size();
    }

    @Override
    public DesignerInfo getItem(int position) {
        return designerInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
