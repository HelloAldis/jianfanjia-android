package com.jianfanjia.cn.designer.ui.activity.my_info_auth.base_info;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.common.tool.ImageUtil;
import com.jianfanjia.common.tool.LogTool;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-17 15:19
 */
public class BaseInfoAuthActicity extends BaseSwipeBackActivity {

    private static final String TAG = BaseInfoAuthActicity.class.getName();

    @Bind(R.id.designerinfo_auth_head_layout)
    MainHeadView mMainHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        initMainView();
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.base_info_auth));
    }

    @OnClick({R.id.head_back_layout,R.id.upload_diploma_image_showview})
    protected void click(View view){
        switch (view.getId()){
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.upload_diploma_image_showview:
                PhotoPickerIntent intent1 = new PhotoPickerIntent(this);
                intent1.setColumn(1);
                intent1.setShowGif(false);
                intent1.setShowCamera(true);
                startActivityForResult(intent1, Constant.REQUESTCODE_PICKER_PIC);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }
        switch (requestCode){
            case Constant.REQUESTCODE_PICKER_PIC:
                if (data != null) {
                    List<String> photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                    for (String path : photos) {
                        Bitmap imageBitmap = ImageUtil.getImage(path);
                        LogTool.d(TAG, "imageBitmap: path :" + path);
                        if (null != imageBitmap) {
                            upload_image(imageBitmap);
                        }
                    }
                }
                break;
        }
    }

    private void upload_image(Bitmap bitmap) {
        UploadPicRequest uploadPicRequest = new UploadPicRequest();
        uploadPicRequest.setBytes(com.jianfanjia.common.tool.ImageUtil.transformBitmapToBytes(bitmap));
        Api.uploadImage(uploadPicRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {

            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_base_info_auth;
    }
}
