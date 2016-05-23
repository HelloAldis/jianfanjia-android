package com.jianfanjia.cn.designer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.ProductImageInfo;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info.CustomeUploadProdcutMenuLayout;
import com.jianfanjia.cn.designer.view.custom_edittext.CustomEditText;

/**
 * Description: com.jianfanjia.cn.designer.ui.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-23 17:18
 */
public class UploadProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_PRODUCT_INTRO = 0;//作品引言
    private static final int VIEW_TYPE_UPLOAD_TITLE = 1;//上传标题;
    private static final int View_TYPE_UPLOAD_ADD = 2;//上传按钮
    private static final int VIEW_TYPE_PLAN_IMG = 3;//平面图
    private static final int VIEW_TYPE_EFFECT_IMG = 4;//效果图

    private List<ProductImageInfo> mPlanImgLists = new ArrayList<>();
    private List<ProductImageInfo> mEffectImgLists = new ArrayList<>();

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public UploadProductAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_PRODUCT_INTRO:
                view = mLayoutInflater.inflate(R.layout.list_item_upload_product_intro, null,true);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new ProductIntroViewHolder(view);
            case VIEW_TYPE_UPLOAD_TITLE:
                view = mLayoutInflater.inflate(R.layout.list_item_upload_product_plan_img_title, null,true);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new UploadTitleViewHolder(view);
            case View_TYPE_UPLOAD_ADD:
                view = mLayoutInflater.inflate(R.layout.list_item_upload_product_plan_img_add, null,true);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new UploadAddImageViewHolder(view);
            case VIEW_TYPE_PLAN_IMG:
                view = mLayoutInflater.inflate(R.layout.list_item_upload_product_plan_img, null,true);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new UploadPlanImgViewHolder(view);
            case VIEW_TYPE_EFFECT_IMG:
                view = mLayoutInflater.inflate(R.layout.list_item_upload_product_effect_img, null,true);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new UploadEffectImgViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case VIEW_TYPE_PRODUCT_INTRO:

                break;

            case VIEW_TYPE_UPLOAD_TITLE:
                break;

            case View_TYPE_UPLOAD_ADD:
                break;

            case VIEW_TYPE_PLAN_IMG:
                break;

            case VIEW_TYPE_EFFECT_IMG:
                break;

        }


    }

    public void addPlanItem(ProductImageInfo productImageInfo){
        mPlanImgLists.add(productImageInfo);
        notifyItemInserted(1 + mPlanImgLists.size());
    }

    public void addEffectItem(ProductImageInfo productImageInfo){
        mEffectImgLists.add(productImageInfo);
        notifyItemInserted(2 + mPlanImgLists.size() + mEffectImgLists.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_PRODUCT_INTRO;
        } else if (position == 1) {
            return VIEW_TYPE_UPLOAD_TITLE;
        } else if (position >= 2 && position < mPlanImgLists.size() + 2) {
            return VIEW_TYPE_PLAN_IMG;
        } else if (position == mPlanImgLists.size() + 2) {
            return View_TYPE_UPLOAD_ADD;
        } else if (position == mPlanImgLists.size() + 3) {
            return VIEW_TYPE_UPLOAD_TITLE;
        } else if (position >= mPlanImgLists.size() + 4 && position < mPlanImgLists.size() + mEffectImgLists.size() +
                4) {
            return VIEW_TYPE_EFFECT_IMG;
        } else if (position == mPlanImgLists.size() + mEffectImgLists.size() + 4) {
            return View_TYPE_UPLOAD_ADD;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        int totalCount = 5;
        if (mPlanImgLists != null) {
            totalCount += mPlanImgLists.size();
        }
        if (mEffectImgLists != null) {
            totalCount += mEffectImgLists.size();
        }
        return totalCount;
    }

    static class ProductIntroViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.product_intro_editext)
        CustomEditText mEditProductIntroText;

        public ProductIntroViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class UploadTitleViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.upload_plan_img_title)
        TextView mTitleView;

        public UploadTitleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class UploadAddImageViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.upload_plan_img_layout)
        RelativeLayout uploadPlanImgLayout;

        public UploadAddImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class UploadPlanImgViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.upload_product_img)
        ImageView uploadProductImg;

        @Bind(R.id.btn_upload_product)
        CustomeUploadProdcutMenuLayout btnUploadProduct;

        public UploadPlanImgViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class UploadEffectImgViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.upload_product_img)
        ImageView uploadProductImg;

        @Bind(R.id.btn_upload_product)


        CustomeUploadProdcutMenuLayout btnUploadProduct;

        @Bind(R.id.design_space_layout)
        RelativeLayout designerSpaceChooseLayout;

        @Bind(R.id.product_intro_editext)
        CustomEditText mEditImageIntroText;

        public UploadEffectImgViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
