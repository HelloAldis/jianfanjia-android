package com.jianfanjia.cn.designer.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductImageInfo;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.ui.activity.common.ShowPicActivity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info.UploadProductActivity;
import com.jianfanjia.cn.designer.ui.adapter.UploadProductAdapter;
import com.jianfanjia.cn.designer.ui.interf.helper.SimpleItemTouchHelperCallback;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.common.tool.ImageUtil;
import com.jianfanjia.common.tool.LogTool;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Description: com.jianfanjia.cn.designer.ui.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-23 10:05
 */
public class UploadProduct2Fragment extends BaseFragment {

    private static final String TAG = UploadProduct2Fragment.class.getName();
    @Bind(R.id.upload_product_head_layout)
    MainHeadView mMainHeadView;

    private ItemTouchHelper mItemTouchHelper;

    private UploadProductActivity mUploadProductActivity;

    @Bind(R.id.upload_product_recyclerview)
    RecyclerView mRecyclerView;

    UploadProductAdapter mUploadProductAdapter;

    private int currentAddType;

    private int currentPosition;

    private Product mProduct;

    private List<String> photos;

    private List<String> showImages;

    public static UploadProduct2Fragment getInstance(Product product) {
        UploadProduct2Fragment uploadProduct2Fragment = new UploadProduct2Fragment();
        uploadProduct2Fragment.setmProduct(product);
        return uploadProduct2Fragment;
    }

    public void setmProduct(Product mProduct) {
        this.mProduct = mProduct;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUploadProductActivity = (UploadProductActivity) getActivity();
        initView();
    }

    @OnClick({R.id.head_back_layout, R.id.head_right_title})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                if (mUploadProductActivity != null) {
                    mUploadProductActivity.lastFragment();
                }
                break;
            case R.id.head_right_title:
                if (mUploadProductActivity != null) {
//                    mUploadProductActivity.nextFragment();
                    mUploadProductActivity.finishProduct(mProduct);
                }
                break;
        }
    }

    private void initView() {
        initMainView();
        initRecyclerView();
    }

    private NotifyRightTitleEnableListener mNotifyRightTitleEnableListener = new NotifyRightTitleEnableListener() {
        @Override
        public void notifyRightTitleEnable() {
            setMianHeadRightTitleEnable();
        }
    };

    private void setMianHeadRightTitleEnable() {
        if (!TextUtils.isEmpty(mProduct.getDescription()) && mProduct
                .getImages().size() > 0) {
            mMainHeadView.setRigthTitleEnable(true);
        } else {
            mMainHeadView.setRigthTitleEnable(false);
        }
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUploadProductAdapter = new UploadProductAdapter(getContext(), mProduct);
        mRecyclerView.setAdapter(mUploadProductAdapter);

        mUploadProductAdapter.setAddProductImageListener(new UploadProductAdapter.AddProductImageListener() {
            @Override
            public void addProductImage(int type) {
                currentAddType = type;
                pickPicture(9);
            }
        });
        mUploadProductAdapter.setReplaceProductImageListener(new UploadProductAdapter.ReplaceProductImageListener() {
            @Override
            public void replaceProductImage(int type, int position) {
                currentAddType = type;
                currentPosition = position;
                pickPicture(1);
            }
        });
        mUploadProductAdapter.setShowProductBigImageListener(new UploadProductAdapter.ShowProductBigImageListener() {
            @Override
            public void showProductBigImage(int type, int position) {
                showImages = new ArrayList<>();
                if (type == UploadProductAdapter.SHOW_TYPE_PLAN) {
                    for (ProductImageInfo productImageInfo : mProduct.getPlan_images()) {
                        showImages.add(productImageInfo.getImageid());
                    }
                } else {
                    for (ProductImageInfo productImageInfo : mProduct.getImages()) {
                        showImages.add(productImageInfo.getImageid());
                    }
                }

                Bundle showPicBundle = new Bundle();
                showPicBundle.putInt(Constant.CURRENT_POSITION, position);
                showPicBundle.putStringArrayList(Constant.IMAGE_LIST,
                        (ArrayList<String>) showImages);
                startActivity(ShowPicActivity.class, showPicBundle);
            }
        });
        mUploadProductAdapter.setNotifyRightTitleEnableListener(mNotifyRightTitleEnableListener);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mUploadProductAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void pickPicture(int totalCount) {
        PhotoPickerIntent intent1 = new PhotoPickerIntent(getContext());
        intent1.setPhotoCount(totalCount);
        intent1.setShowGif(false);
        intent1.setShowCamera(true);
        startActivityForResult(intent1, Constant.REQUESTCODE_PICKER_PIC);
    }

    private void upload_image(final Bitmap bitmap) {
        UploadPicRequest uploadPicRequest = new UploadPicRequest();
        uploadPicRequest.setBytes(com.jianfanjia.common.tool.ImageUtil.transformBitmapToBytes(bitmap));
        Api.uploadImage(uploadPicRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                Hud.show(getUiContext());
            }

            @Override
            public void onHttpDone() {
                Hud.dismiss();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                ProductImageInfo productImageInfo = new ProductImageInfo();
                productImageInfo.setImageid(apiResponse.getData());
                switch (currentAddType) {
                    case UploadProductAdapter.UPLOAD_TYPE_EFFECT:
                        productImageInfo.setSection(getString(R.string.keting));//默认设置为客厅
                        if (mProduct.getImages().size() == 0) {
                            mProduct.setCover_imageid(productImageInfo.getImageid());
                        }
                        mUploadProductAdapter.addEffectItem(productImageInfo);
                        mNotifyRightTitleEnableListener.notifyRightTitleEnable();
                        break;
                    case UploadProductAdapter.UPLOAD_TYPE_PLAN:
                        mUploadProductAdapter.addPlanItem(productImageInfo);
                        mNotifyRightTitleEnableListener.notifyRightTitleEnable();
                        break;
                    case UploadProductAdapter.REPLACE_TYPE_EFFECT:
                        productImageInfo.setSection(getString(R.string.keting));//默认设置为客厅
                        if (!TextUtils.isEmpty(mProduct.getCover_imageid()) && mProduct.getCover_imageid().equals
                                (mProduct.getImages().get(currentPosition).getImageid())) {
                            mProduct.setCover_imageid(productImageInfo.getImageid());
                        }
                        mUploadProductAdapter.changeEffectItem(productImageInfo, currentPosition);
                        break;
                    case UploadProductAdapter.REPLACE_TYPE_PLAN:
                        mUploadProductAdapter.changePlanItem(productImageInfo, currentPosition);
                        break;
                }

                UploadProduct2Fragment.this.uploadImageSync();
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
                UploadProduct2Fragment.this.uploadImageSync();
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.getMsg(code));
                UploadProduct2Fragment.this.uploadImageSync();
            }
        },this);
    }

    public void uploadImageSync() {
        if (!this.photos.isEmpty()) {
            String path = this.photos.get(0);
            this.photos.remove(0);
            Bitmap imageBitmap = ImageUtil.getImage(path);
            LogTool.d("imageBitmap: path :" + path);
            if (null != imageBitmap) {
                upload_image(imageBitmap);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constant.REQUESTCODE_PICKER_PIC:
                if (data != null) {
                    this.photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                    LogTool.d("all path :" + this.photos);
                    this.uploadImageSync();
                }
                break;
        }
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(Html.fromHtml("上传作品<font color=\"#fe7003\">（2/2）</font>"));
        mMainHeadView.setRightTitle(getString(R.string.commit));
        setMianHeadRightTitleEnable();
    }

    public interface NotifyRightTitleEnableListener {
        void notifyRightTitleEnable();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_upload_product2;
    }
}
