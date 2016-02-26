package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.ImageInfo;
import com.jianfanjia.cn.bean.Product;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.TDevice;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by donglua on 15/6/21.
 */
public class HomeProductPagerAdapter extends PagerAdapter {
    private List<Product> productList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ViewPagerClickListener viewPagerClickListener;
    private int width;
    private int height;

    public HomeProductPagerAdapter(Context context, List<Product> products, ViewPagerClickListener viewPagerClickListener, int width, int height) {
        this.mContext = context;
        this.productList = products;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.viewPagerClickListener = viewPagerClickListener;
        this.width = width;
        this.height = height;
        LogTool.d(this.getClass().getName()," statusBar height =" + TDevice.getStatusBarHeight(context));
        LogTool.d(this.getClass().getName()," NavigationBar height =" + TDevice.getNavigationBarHeight(context));
        LogTool.d(this.getClass().getName(),"width =" + width + "height =" + height);
        LogTool.d(this.getClass().getName(),"TDevide height =" + TDevice.getScreenHeight());
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_item_home, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.viewpager_home_item_pic);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        Product productNew = productList.get(position);
        ImageInfo imageInfo = productNew.getImages().get(0);
        String uri = Url_New.getInstance().GET_THUMBNAIL_IMAGE2.replace(Url_New.WIDTH, width + "") + height + "/" + imageInfo.getImageid();
        LogTool.d(this.getClass().getName(), " uri =" + uri);
        Picasso.with(mContext).load(uri).resize(width, height)
                .placeholder(R.mipmap.icon_default_pic)
                .error(R.mipmap.icon_default_pic)
                .into(imageView);

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (viewPagerClickListener != null) {
//                    viewPagerClickListener.onClickItem(position);
//                }
//            }
//        });
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
