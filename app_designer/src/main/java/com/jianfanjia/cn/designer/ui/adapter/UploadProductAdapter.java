package com.jianfanjia.cn.designer.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductImageInfo;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.tools.ImageShow;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info.ChooseProductSectionUtil;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info.CustomeUploadProdcutMenuLayout;
import com.jianfanjia.cn.designer.ui.fragment.UploadProduct2Fragment;
import com.jianfanjia.cn.designer.ui.interf.helper.ItemTouchHelperAdapter;
import com.jianfanjia.cn.designer.ui.interf.helper.ItemTouchHelperViewHolder;
import com.jianfanjia.cn.designer.view.ChooseProductSectionDialog;
import com.jianfanjia.cn.designer.view.custom_edittext.CustomEditText;
import com.jianfanjia.common.tool.ImageUtil;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Description: com.jianfanjia.cn.designer.ui.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-23 17:18
 */
public class UploadProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ItemTouchHelperAdapter {

    public static final int UPLOAD_TYPE_PLAN = 10;
    public static final int UPLOAD_TYPE_EFFECT = 20;
    public static final int REPLACE_TYPE_PLAN = 30;
    public static final int REPLACE_TYPE_EFFECT = 40;

    private static final int VIEW_TYPE_PRODUCT_INTRO = 0;//作品引言
    private static final int VIEW_TYPE_UPLOAD_TITLE = 1;//上传标题;
    private static final int VIEW_TYPE_UPLOAD_ADD_PLAN = 2;//上传平面图
    private static final int VIEW_TYPE_UPLOAD_ADD_EFFECT = 3;//上传效果图
    private static final int VIEW_TYPE_PLAN_IMG = 4;//平面图
    private static final int VIEW_TYPE_EFFECT_IMG = 5;//效果图

    List<ProductImageInfo> mPlanImgLists;
    List<ProductImageInfo> mEffectImgLists;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Product mProduct;
    private AddProductImageListener addProductImageListener;
    private ReplaceProductImageListener mReplaceProductImageListener;
    private UploadProduct2Fragment.NotifyRightTitleEnableListener mNotifyRightTitleEnableListener;
    private android.os.Handler mHandler = new android.os.Handler();

    public UploadProductAdapter(Context context, Product product) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mProduct = product;
        mPlanImgLists = product.getPlan_images();
        mEffectImgLists = product.getImages();

    }

    public void setAddProductImageListener(AddProductImageListener addProductImageListener) {
        this.addProductImageListener = addProductImageListener;
    }

    public void setReplaceProductImageListener(ReplaceProductImageListener replaceProductImageListener) {
        mReplaceProductImageListener = replaceProductImageListener;
    }

    public void setNotifyRightTitleEnableListener(UploadProduct2Fragment.NotifyRightTitleEnableListener
                                                          notifyRightTitleEnableListener) {
        mNotifyRightTitleEnableListener = notifyRightTitleEnableListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_PRODUCT_INTRO:
                view = mLayoutInflater.inflate(R.layout.list_item_upload_product_intro, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new ProductIntroViewHolder(view);
            case VIEW_TYPE_UPLOAD_TITLE:
                view = mLayoutInflater.inflate(R.layout.list_item_upload_product_plan_img_title, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new UploadTitleViewHolder(view);
            case VIEW_TYPE_UPLOAD_ADD_EFFECT:
                view = mLayoutInflater.inflate(R.layout.list_item_upload_product_plan_img_add, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new UploadAddImageViewHolder(view);
            case VIEW_TYPE_UPLOAD_ADD_PLAN:
                view = mLayoutInflater.inflate(R.layout.list_item_upload_product_plan_img_add, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new UploadAddImageViewHolder(view);
            case VIEW_TYPE_PLAN_IMG:
                view = mLayoutInflater.inflate(R.layout.list_item_upload_product_plan_img, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new UploadPlanImgViewHolder(view);
            case VIEW_TYPE_EFFECT_IMG:
                view = mLayoutInflater.inflate(R.layout.list_item_upload_product_effect_img, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new UploadEffectImgViewHolder(view,new EditTextTextWatcher());
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_PRODUCT_INTRO:
                bindProductIntro((ProductIntroViewHolder) holder);
                break;
            case VIEW_TYPE_UPLOAD_TITLE:
                bindProductUploadTitle((UploadTitleViewHolder) holder, position);
                break;
            case VIEW_TYPE_UPLOAD_ADD_EFFECT:
                bindProductAddEffect((UploadAddImageViewHolder) holder);
                break;
            case VIEW_TYPE_UPLOAD_ADD_PLAN:
                bindProductAddPlan((UploadAddImageViewHolder) holder);
                break;
            case VIEW_TYPE_PLAN_IMG:
                bindPlanImg((UploadPlanImgViewHolder) holder, position);
                break;
            case VIEW_TYPE_EFFECT_IMG:
                bindEffectImg((UploadEffectImgViewHolder) holder, position);
                break;

        }
    }

    private void bindProductUploadTitle(UploadTitleViewHolder holder, int position) {
        if (position == 1) {
            holder.mTitleView.setText(mContext.getString(R.string.upload_plan_img));
        } else {
            holder.mTitleView.setText(mContext.getString(R.string.upload_effect_img));
        }
    }

    private void bindProductAddPlan(UploadAddImageViewHolder holder) {
        holder.uploadPlanImgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addProductImageListener != null) {
                    addProductImageListener.addProductImage(UPLOAD_TYPE_PLAN);
                }
            }
        });
    }

    private void bindEffectImg(final UploadEffectImgViewHolder holder, final int position) {
        final ProductImageInfo productImageInfo = mEffectImgLists.get(position - 4 - mPlanImgLists.size());

        final String imageid = productImageInfo.getImageid();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                LogTool.d(this.getClass().getName(), "img width = " + holder.uploadProductImg.getWidth() + ",height =" +
                        holder.uploadProductImg.getHeight());
                ImageShow.getImageShow().loadImage(imageid, holder.uploadProductImg.getWidth(), holder.uploadProductImg
                        .getHeight(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        holder.uploadProductImg.setImageBitmap(ImageUtil.getRoundedCornerBitmap(loadedImage, TDevice
                                .dip2px(mContext, 5)));
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }
        });

        //设置封面
        if (!productImageInfo.isMenuOpen()) {
            holder.btnUploadProduct.setOpenStatus();
        } else {
            holder.btnUploadProduct.setCloseStatus();
        }

        if (!TextUtils.isEmpty(mProduct.getCover_imageid()) && mProduct.getCover_imageid().equals(productImageInfo
                .getImageid())) {
            holder.coverView.setVisibility(View.VISIBLE);
            holder.settingCoverView.setEnabled(false);
        } else {
            holder.coverView.setVisibility(View.GONE);
            holder.settingCoverView.setEnabled(true);
            holder.settingCoverView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProduct.setCover_imageid(productImageInfo.getImageid());
                    notifyItemRangeChanged(4 + mPlanImgLists.size(), mEffectImgLists.size());
                }
            });
        }

        holder.clearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductImageInfo deleteProductImageInfo = mEffectImgLists.get(position - 4 - mPlanImgLists.size());
                mEffectImgLists.remove(position - 4 - mPlanImgLists.size());
                notifyItemRemoved(position);
                if (!TextUtils.isEmpty(mProduct.getCover_imageid())
                        && mProduct.getCover_imageid().equals(deleteProductImageInfo.getImageid())
                        && mEffectImgLists.size() > 1) {
                    mProduct.setCover_imageid(mEffectImgLists.get(0).getImageid());
                    notifyItemChanged(4 + mPlanImgLists.size());
                }
                mNotifyRightTitleEnableListener.notifyRightTitleEnable();
            }
        });

        holder.editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReplaceProductImageListener != null) {
                    mReplaceProductImageListener.replaceProductImage(REPLACE_TYPE_EFFECT, position - 4 -
                            mPlanImgLists.size());
                }
            }
        });
        if (!TextUtils.isEmpty(productImageInfo.getSection())) {
            holder.designerSpaceView.setText(productImageInfo.getSection());
        }

        holder.mEditTextTextWatcher.updatePosition(position - 4 -
                mPlanImgLists.size());
        holder.mEditImageIntroText.setText(productImageInfo.getDescription());

        holder.designerSpaceChooseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseProductSectionUtil.show(mContext, new ChooseProductSectionDialog.ChooseItemListener() {
                    @Override
                    public void chooseItem(String section) {
                        holder.designerSpaceView.setText(section);
                        productImageInfo.setSection(section);
                    }
                });
            }
        });
    }

    private void bindPlanImg(UploadPlanImgViewHolder holder, final int position) {
        ProductImageInfo productImageInfo = mPlanImgLists.get(position - 2);
        String imageid = productImageInfo.getImageid();
        ImageShow.getImageShow().displayScreenWidthThumnailImage(mContext, imageid, holder.uploadProductImg);

        if (!productImageInfo.isMenuOpen()) {
            holder.btnUploadProduct.setOpenStatus();
        } else {
            holder.btnUploadProduct.setCloseStatus();
        }
        holder.settingCoverView.setVisibility(View.GONE);

        holder.editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReplaceProductImageListener != null) {
                    mReplaceProductImageListener.replaceProductImage(REPLACE_TYPE_PLAN, position - 2);
                }
            }
        });

        holder.clearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlanImgLists.remove(position - 2);
                notifyItemRemoved(position);
                mNotifyRightTitleEnableListener.notifyRightTitleEnable();
            }
        });
    }

    private void bindProductAddEffect(UploadAddImageViewHolder holder) {
        holder.uploadPlanImgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addProductImageListener != null) {
                    addProductImageListener.addProductImage(UPLOAD_TYPE_EFFECT);
                }
            }
        });
    }

    private void bindProductIntro(ProductIntroViewHolder holder) {
        holder.mEditProductIntroText.setText(mProduct.getDescription());
        holder.mEditProductIntroText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mProduct.setDescription(s.toString());
                mNotifyRightTitleEnableListener.notifyRightTitleEnable();
            }
        });
    }

    public void addPlanList(List<ProductImageInfo> productImageInfos) {
        productImageInfos.addAll(productImageInfos);
        notifyItemRangeInserted(1 + mPlanImgLists.size(), productImageInfos.size());
    }

    public void addEffectList(List<ProductImageInfo> productImageInfos) {
        productImageInfos.addAll(productImageInfos);
        notifyItemRangeInserted(3 + mPlanImgLists.size() + mEffectImgLists.size(), productImageInfos.size());
    }

    public void addPlanItem(ProductImageInfo productImageInfo) {
        mPlanImgLists.add(productImageInfo);
        notifyItemInserted(1 + mPlanImgLists.size());
    }

    public void addEffectItem(ProductImageInfo productImageInfo) {
        mEffectImgLists.add(productImageInfo);
        notifyItemInserted(3 + mPlanImgLists.size() + mEffectImgLists.size());
    }

    public void changeEffectItem(ProductImageInfo productImageInfo, int oldPosition) {
        mEffectImgLists.set(oldPosition, productImageInfo);
        notifyItemChanged(oldPosition + 4 + mPlanImgLists.size());
    }

    public void changePlanItem(ProductImageInfo productImageInfo, int oldPosition) {
        mPlanImgLists.set(oldPosition, productImageInfo);
        notifyItemChanged(2 + oldPosition);
    }

    @Override
    public void onItemDismiss(int position) {

    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition, int viewType) {
        if (viewType == VIEW_TYPE_PLAN_IMG) {
            Collections.swap(mPlanImgLists, fromPosition - 2, toPosition - 2);
        }
        if (viewType == VIEW_TYPE_EFFECT_IMG) {
            Collections.swap(mEffectImgLists, fromPosition - 4 - mPlanImgLists.size(), toPosition - 4 - mPlanImgLists
                    .size());
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
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
            return VIEW_TYPE_UPLOAD_ADD_PLAN;
        } else if (position == mPlanImgLists.size() + 3) {
            return VIEW_TYPE_UPLOAD_TITLE;
        } else if (position >= mPlanImgLists.size() + 4 && position < mPlanImgLists.size() + mEffectImgLists.size() +
                4) {
            return VIEW_TYPE_EFFECT_IMG;
        } else if (position == mPlanImgLists.size() + mEffectImgLists.size() + 4) {
            return VIEW_TYPE_UPLOAD_ADD_EFFECT;
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

    static class UploadPlanImgViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        @Bind(R.id.contentLayout)
        FrameLayout contentLayout;

        @Bind(R.id.upload_product_img)
        ImageView uploadProductImg;

        @Bind(R.id.btn_upload_product)
        CustomeUploadProdcutMenuLayout btnUploadProduct;

        @Bind(R.id.btn_setting_cover)
        ImageView settingCoverView;

        @Bind(R.id.btn_edit)
        ImageView editView;

        @Bind(R.id.move_indication)
        ImageView moveIndicationView;

        @Bind(R.id.btn_clear)
        ImageView clearView;

        private FrameLayout.LayoutParams sourceLayoutParams;

        public UploadPlanImgViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            sourceLayoutParams = (FrameLayout.LayoutParams) contentLayout.getLayoutParams();
        }

        @Override
        public void onItemClear() {
            contentLayout.setLayoutParams(sourceLayoutParams);
            btnUploadProduct.setVisibility(View.VISIBLE);
            moveIndicationView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onItemSelected() {
            contentLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TDevice
                    .dip2px(MyApplication.getInstance(), 80)));
            btnUploadProduct.setVisibility(View.GONE);
            moveIndicationView.setVisibility(View.GONE);
        }
    }

    static class UploadEffectImgViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        @Bind(R.id.contentLayout)
        LinearLayout contentLayout;

        @Bind(R.id.upload_product_img)
        ImageView uploadProductImg;

        @Bind(R.id.btn_upload_product)
        CustomeUploadProdcutMenuLayout btnUploadProduct;

        @Bind(R.id.design_space_layout)
        RelativeLayout designerSpaceChooseLayout;

        @Bind(R.id.design_space_content)
        TextView designerSpaceView;

        @Bind(R.id.product_intro_editext)
        CustomEditText mEditImageIntroText;

        @Bind(R.id.upload_product_cover)
        ImageView coverView;

        @Bind(R.id.btn_setting_cover)
        ImageView settingCoverView;

        @Bind(R.id.btn_edit)
        ImageView editView;

        @Bind(R.id.move_indication)
        ImageView moveIndicationView;

        @Bind(R.id.btn_clear)
        ImageView clearView;

        private LinearLayout.LayoutParams sourceLayoutParams;

        private EditTextTextWatcher mEditTextTextWatcher;

        public UploadEffectImgViewHolder(View view,EditTextTextWatcher editTextTextWatcher) {
            super(view);
            ButterKnife.bind(this, view);
            mEditImageIntroText.addTextChangedListener(editTextTextWatcher);
            this.mEditTextTextWatcher = editTextTextWatcher;
            sourceLayoutParams = (LinearLayout.LayoutParams) contentLayout.getLayoutParams();
        }

        @Override
        public void onItemClear() {
            contentLayout.setLayoutParams(sourceLayoutParams);
            btnUploadProduct.setVisibility(View.VISIBLE);
            moveIndicationView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onItemSelected() {
            contentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TDevice
                    .dip2px(MyApplication.getInstance(), 80)));
            btnUploadProduct.setVisibility(View.GONE);
            moveIndicationView.setVisibility(View.GONE);
        }

    }

    public interface AddProductImageListener {
        void addProductImage(int type);
    }

    public interface ReplaceProductImageListener {
        void replaceProductImage(int type, int position);
    }

    private class EditTextTextWatcher implements TextWatcher{

        private int position;

        public void updatePosition(int pos){
            this.position = pos;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mEffectImgLists.get(position).setDescription(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


}
