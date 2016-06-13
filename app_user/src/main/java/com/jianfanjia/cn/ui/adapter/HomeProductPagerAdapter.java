package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import com.jianfanjia.api.model.Product;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.tools.ImageShow;
import com.jianfanjia.common.tool.TDevice;


/**
 * Created by donglua on 15/6/21.
 */
public class HomeProductPagerAdapter extends PagerAdapter {
    private List<Product> productList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public HomeProductPagerAdapter(Context context, List<Product> products) {
        this.mContext = context;
        this.productList = products;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_item_home, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.viewpager_home_item_pic);
        Product productNew = productList.get(position);
        ImageShow.getImageShow().displayThumbnailImageByHeightAndWidth(productNew.getCover_imageid(), imageView,
                (int) TDevice.getScreenWidth(),
                (int) TDevice.getScreenHeight());
        container.addView(itemView);
        return itemView;
    }


    @Override
    public int getCount() {
        return productList != null ? productList.size() : 0;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
