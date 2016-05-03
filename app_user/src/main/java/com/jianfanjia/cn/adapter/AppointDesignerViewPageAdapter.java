package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.cn.activity.R;

public class AppointDesignerViewPageAdapter extends PagerAdapter {
    private static final String TAG = "ViewPageAdapter";
    private Context context;
    private List<View> list;
    private List<Designer> mDesignerList;

    public AppointDesignerViewPageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return mDesignerList == null ? 0 : mDesignerList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(list.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        try {
            if (list.get(position).getParent() == null) {
                container.addView(list.get(position));
            } else {
                ((ViewGroup) list.get(position).getParent()).removeView(list
                        .get(position));
                container.addView(list.get(position));
            }
            setView(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list.get(position);
    }

    private void setView(final int position){

    }

    @Override
    public float getPageWidth(int position) {
//		return super.getPageWidth(position);
        if (position == (list.size() - 1)) {//最后一屏，显示屏幕宽
            return 1.0f;
        }
        return 0.8f;
    }

    static class ViewHolder{

        @Bind(R.id.ratingBar)
        protected RatingBar ratingBar = null;

        @Bind(R.id.designer_case_background)
        protected ImageView designerCaseBackgroundView = null;

        @Bind(R.id.designerinfo_head_img)
        protected ImageView designerinfo_head_img = null;

        @Bind(R.id.designerinfo_auth)
        protected ImageView designerinfo_auth = null;

        @Bind(R.id.designer_name)
        protected TextView designerName = null;

        @Bind(R.id.viewCountText)
        protected TextView viewCountText = null;

        @Bind(R.id.productCountText)
        protected TextView productCountText = null;

        @Bind(R.id.appointCountText)
        protected TextView appointCountText = null;

        @Bind(R.id.btn_apponit)
        protected TextView appointView = null;

        public ViewHolder(View itemView){
            ButterKnife.bind(this, itemView);
        }
    }

}
