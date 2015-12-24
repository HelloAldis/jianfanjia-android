package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.DesignerListInfo;
import com.jianfanjia.cn.bean.OrderDesignerInfo;
import com.jianfanjia.cn.bean.Product;
import com.jianfanjia.cn.bean.Requirement;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.ListItemClickListener;
import com.jianfanjia.cn.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: DesignerListAdapter
 * User: fengliang
 * Date: 2015-10-14
 * Time: 14:03
 */
public class DesignerListAdapter extends BaseRecyclerViewAdapter<DesignerListInfo> {
    private static final String TAG = DesignerListAdapter.class.getName();
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_ITEM = 1;
    private Requirement requirement;
    private ListItemClickListener listener;
    private int viewType = -1;
    private static final int BANNER_ICON[] = {R.mipmap.bg_home_banner1,
            R.mipmap.bg_home_banner2, R.mipmap.bg_home_banner3,
            R.mipmap.bg_home_banner4};

    public DesignerListAdapter(Context context, List<DesignerListInfo> list, Requirement requirement, ListItemClickListener listener) {
        super(context, list);
        this.requirement = requirement;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            viewType = TYPE_HEAD;
        } else {
            viewType = TYPE_ITEM;
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<DesignerListInfo> list) {
        switch (viewType) {
            case TYPE_HEAD:
                DesignerHeadHolder designerHeadHolder = (DesignerHeadHolder) viewHolder;
                initBannerView(designerHeadHolder.myViewPager, designerHeadHolder.indicatorGroup_lib);
                if (null != requirement) {
                    final List<OrderDesignerInfo> designers = requirement.getDesigners();
                    if (null != designers) {
                        if (designers.size() > 0) {
                            designerHeadHolder.marchedView.setVisibility(View.VISIBLE);
                            designerHeadHolder.noMarchedView.setVisibility(View.GONE);
                            MarchDesignerAdapter marchDesignerAdapter = new MarchDesignerAdapter(context, designers);
                            designerHeadHolder.marchDesignerView.setAdapter(marchDesignerAdapter);
                            designerHeadHolder.marchDesignerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    OrderDesignerInfo orderDesignerInfo = designers.get(position);
                                    if (null != listener) {
                                        listener.onItemClick(position, orderDesignerInfo);
                                    }
                                }
                            });
                        } else {
                            designerHeadHolder.marchedView.setVisibility(View.GONE);
                            designerHeadHolder.noMarchedView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        designerHeadHolder.marchedView.setVisibility(View.GONE);
                        designerHeadHolder.noMarchedView.setVisibility(View.GONE);
                    }
                } else {
                    designerHeadHolder.marchedView.setVisibility(View.GONE);
                    designerHeadHolder.noMarchedView.setVisibility(View.VISIBLE);
                }
                designerHeadHolder.addXuQiu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != listener) {
                            listener.onClick();
                        }
                    }
                });
                break;
            case TYPE_ITEM:
                DesignerListInfo info = list.get(position - 1);
                Product product = info.getProduct();
                DesignerListViewHolder designerListViewHolder = (DesignerListViewHolder) viewHolder;
                designerListViewHolder.itemXiaoQuText.setText(product.getCell());
                if (info.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
                    designerListViewHolder.itemAuthView.setVisibility(View.VISIBLE);
                } else {
                    designerListViewHolder.itemAuthView.setVisibility(View.GONE);
                }
                String houseType = product.getHouse_type();
                String decStyle = product.getDec_style();
                designerListViewHolder.itemProduceText.setText(product.getHouse_area() + "㎡，" + BusinessManager.convertHouseTypeToShow(houseType) + "，" + BusinessManager.convertDecStyleToShow(decStyle));
                imageShow.displayScreenWidthThumnailImage(context, product.getImages().get(0).getImageid(), designerListViewHolder.itemProductView);
                if (!TextUtils.isEmpty(info.getImageid())) {
                    imageShow.displayImageHeadWidthThumnailImage(context, info.getImageid(), designerListViewHolder.itemHeadView);
                } else {
                    imageShow.displayLocalImage(Constant.DEFALUT_ADD_PIC, designerListViewHolder.itemHeadView);
                }
                designerListViewHolder.itemProductView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != listener) {
                            listener.onMaxClick(position - 1);
                        }
                    }
                });
                designerListViewHolder.itemHeadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != listener) {
                            listener.onMinClick(position - 1);
                        }
                    }
                });
                break;
        }
    }

    private void initBannerView(MyViewPager viewPager, LinearLayout indicatorGroup_lib) {
        indicatorGroup_lib.removeAllViews();
        List<View> bannerList = new ArrayList<View>();
        for (int i = 0; i < BANNER_ICON.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(BANNER_ICON[i]);
            bannerList.add(imageView);
        }
        final View[] indicators = new View[bannerList.size()];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                new ViewGroup.LayoutParams(20, 20));
        params.setMargins(0, 0, 15, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new View(context);
            if (i == 0) {
                indicators[i].setBackgroundResource(R.drawable.shape_indicator_selected_oval);
            } else {
                indicators[i].setBackgroundResource(R.drawable.shape_indicator_unselected_oval);
            }
            indicators[i].setLayoutParams(params);
            indicatorGroup_lib.addView(indicators[i]);
        }
        ViewPageAdapter mPagerAdapter = new ViewPageAdapter(context, bannerList);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < indicators.length; i++) {
                    if (i == arg0) {
                        indicators[i]
                                .setBackgroundResource(R.drawable.shape_indicator_selected_oval);
                    } else {
                        indicators[i]
                                .setBackgroundResource(R.drawable.shape_indicator_unselected_oval);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                View headView = layoutInflater.inflate(R.layout.list_item_designer, null);
                return headView;
            case TYPE_ITEM:
                View itemView = layoutInflater.inflate(R.layout.list_item_designer_info,
                        null);
                return itemView;
        }
        return null;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        switch (viewType) {
            case TYPE_HEAD:
                return new DesignerHeadHolder(view);
            case TYPE_ITEM:
                return new DesignerListViewHolder(view);
        }
        return null;
    }

    private static class DesignerHeadHolder extends RecyclerViewHolderBase {
        public View pagerView = null;
        public View marchedView = null;
        public View noMarchedView = null;
        public MyViewPager myViewPager = null;
        public LinearLayout indicatorGroup_lib = null;
        public GridView marchDesignerView = null;
        public Button addXuQiu = null;

        public DesignerHeadHolder(View itemView) {
            super(itemView);
            pagerView = itemView.findViewById(R.id.viewpager_layout);
            marchedView = itemView.findViewById(R.id.marched_layout);
            noMarchedView = itemView.findViewById(R.id.no_marched_layout);
            myViewPager = (MyViewPager) pagerView.findViewById(R.id.viewPager_lib);
            indicatorGroup_lib = (LinearLayout) pagerView.findViewById(R.id.indicatorGroup_lib);
            marchDesignerView = (GridView) marchedView.findViewById(R.id.marchGridview);
            addXuQiu = (Button) noMarchedView.findViewById(R.id.btn_add);
        }
    }

    private static class DesignerListViewHolder extends RecyclerViewHolderBase {
        public ImageView itemProductView;
        public ImageView itemHeadView;
        public ImageView itemAuthView;
        public TextView itemXiaoQuText;
        public TextView itemProduceText;

        public DesignerListViewHolder(View itemView) {
            super(itemView);
            itemProductView = (ImageView) itemView
                    .findViewById(R.id.list_item_product_img);
            itemHeadView = (ImageView) itemView
                    .findViewById(R.id.list_item_head_img);
            itemAuthView = (ImageView) itemView
                    .findViewById(R.id.list_item_auth);
            itemXiaoQuText = (TextView) itemView
                    .findViewById(R.id.list_item_xiaoqu_text);
            itemProduceText = (TextView) itemView
                    .findViewById(R.id.list_item_produce_text);
        }
    }
}
