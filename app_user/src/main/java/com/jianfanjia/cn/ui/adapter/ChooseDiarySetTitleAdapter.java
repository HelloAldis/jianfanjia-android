package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.cn.activity.R;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-16 10:00
 */
public class ChooseDiarySetTitleAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    private List<DiarySetInfo> mChooseValues;

    private String currentChooseValue;

    public ChooseDiarySetTitleAdapter(Context context, List<DiarySetInfo> chooseValues, String currentChooseValue) {
        this.layoutInflater = LayoutInflater.from(context);
        this.mChooseValues = chooseValues;
        this.currentChooseValue = currentChooseValue;
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

        String diaryId = mChooseValues.get(position).get_id();
        String title = mChooseValues.get(position).getTitle();
        holder.ltm_req_simple_item.setText(title);

        boolean isChoose = false;
        if (currentChooseValue.equals(diaryId)) {
            isChoose = true;
        }
        if (isChoose) {
            holder.chooseImageView.setVisibility(View.VISIBLE);
        } else {
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
    }

    @Override
    public int getCount() {
        return mChooseValues.size();
    }

    @Override
    public Object getItem(int position) {
        return mChooseValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
