package com.jianfanjia.cn.designer.ui.activity.my_info_auth.base_info;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.DesignerAwardInfo;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.designer.UpdateDesignerInfoRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.business.DesignerBusiness;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.IntentUtil;
import com.jianfanjia.cn.designer.ui.activity.common.EditCityActivity;
import com.jianfanjia.cn.designer.ui.adapter.DesignerBaseInfoAdapter;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.dialog.CommonDialog;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;
import com.jianfanjia.common.tool.ImageUtil;
import com.jianfanjia.common.tool.LogTool;
import com.yalantis.ucrop.UCrop;
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

    public static final int CURRENT_STATUS_EDIT = 0;
    public static final int CURRENT_STATUS_PRIVIEW = 1;

    public static final int FROM_REGISTER_INTENT = 2;
    public static final int FROM_MAIN_INTENT = 3;

    public static final String INTENT_FROM_FLAG = "intent_from_flag";

    @Bind(R.id.designerinfo_auth_head_layout)
    MainHeadView mMainHeadView;

    @Bind(R.id.baseinfo_auth_recyclerview)
    RecyclerView mRecyclerView;

    private int currentStatus = CURRENT_STATUS_PRIVIEW;//默认进来是预览状态
    private int intentFrom;

    private DesignerBaseInfoAdapter mDesignerBaseInfoAdapter;

    private Designer mDesigner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();
        initView();
    }


    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            LogTool.d(this.getClass().getName(), "designer is not null");
            mDesigner = (Designer) bundle.getSerializable(Global.DESIGNER_INFO);
            intentFrom = bundle.getInt(INTENT_FROM_FLAG, FROM_MAIN_INTENT);
        }

        if (mDesigner == null) {
            mDesigner = new Designer();
        }
        if (intentFrom == FROM_MAIN_INTENT) {
            currentStatus = CURRENT_STATUS_PRIVIEW;
        } else {
            currentStatus = CURRENT_STATUS_EDIT;
        }
    }

    private void initView() {
        initMainView();
        initRecycleView();
        changeViewByStatus();
    }

    private void changeViewByStatus() {
        if (currentStatus == CURRENT_STATUS_EDIT) {
            mMainHeadView.setRightTitle(getString(R.string.commit));
            mMainHeadView.setRightTextListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateDesignerBaseInfo(mDesigner);
                }
            });
            setMianHeadRightTitleEnable();
        } else {
            mMainHeadView.setRightTitle(getString(R.string.edit));
            mMainHeadView.setRigthTitleEnable(true);
            mMainHeadView.setRightTitleColor(R.color.grey_color);
            mMainHeadView.setRightTextListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentStatus = CURRENT_STATUS_EDIT;
                    changeViewByStatus();
                }
            });
        }
        mDesignerBaseInfoAdapter.changeShowStatus(currentStatus);
        if (!TextUtils.isEmpty(mDesigner.getAuth_type()) && mDesigner.getAuth_type().equals(DesignerBusiness
                .DESIGNER_NOT_AUTH)) {
            mMainHeadView.setRightTitleVisable(View.GONE);
        } else {
            mMainHeadView.setRightTitleVisable(View.VISIBLE);
        }
    }

    public void setMianHeadRightTitleEnable() {
        if (currentStatus == CURRENT_STATUS_EDIT) {
            if (!TextUtils.isEmpty(mDesigner.getImageid()) && !TextUtils.isEmpty(mDesigner.getUsername())
                    && !TextUtils.isEmpty(mDesigner.getDistrict()) && !TextUtils.isEmpty(mDesigner.getAddress())
                    && !TextUtils.isEmpty(mDesigner.getPhilosophy()) && !TextUtils.isEmpty(mDesigner.getUniversity())
                    && !TextUtils.isEmpty(mDesigner.getCompany()) && !TextUtils.isEmpty(mDesigner.getAchievement())
                    && mDesigner.getWork_year() > 0) {
                mMainHeadView.setRigthTitleEnable(true);
            } else {
                mMainHeadView.setRigthTitleEnable(false);
            }
        }
    }

    private void initRecycleView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDesignerBaseInfoAdapter = new DesignerBaseInfoAdapter(this, mDesigner, new DesignerBaseInfoAdapter
                .UploadAwardImgViewHolder.ItemClickAction() {

            @Override
            public void changeCommitStatus() {
                setMianHeadRightTitleEnable();
            }

            @Override
            public void uploadHead() {
                pickPicture(Constant.REQUESTCODE_PICKER_HEAD_PIC);
            }

            @Override
            public void inputSex() {
                showSexChooseDialog();
            }

            @Override
            public void inputAddress() {
                Bundle address = new Bundle();
                address.putString(Constant.EDIT_PROVICE, mDesigner.getProvince());
                address.putString(Constant.EDIT_CITY, mDesigner.getCity());
                address.putString(Constant.EDIT_DISTRICT, mDesigner.getDistrict());
                address.putInt(EditCityActivity.PAGE, EditCityActivity.EDIT_USER_ADRESS);
                IntentUtil.startActivityForResult(BaseInfoAuthActicity.this, EditCityActivity.class, address, Constant
                        .REQUESTCODE_EDIT_ADDRESS);
            }

            @Override
            public void uploadDiplomaImage() {
                pickPicture(Constant.REQUESTCODE_PICKER_PIC);
            }

            public void uploadAwardImage() {
                pickPicture(Constant.REQUESTCODE_PICKER_AWARD_PIC);
            }

            public void updateAwardImage() {
                pickPicture(Constant.REQUESTCODE_PICKER_CHANGE_AWARD_PIC);
            }
        });
        mRecyclerView.setAdapter(mDesignerBaseInfoAdapter);
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.base_info_auth));
    }

    @OnClick({R.id.head_back_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                navagateNext();
                break;
        }
    }

    private void showSexChooseDialog() {
        CommonDialog commonDialog = DialogHelper
                .getPinterestDialogCancelable(this);
        View contentView = inflater.inflate(R.layout.sex_choose, null);
        final RadioGroup radioGroup = (RadioGroup) contentView
                .findViewById(R.id.sex_radioGroup);
        if (!TextUtils.isEmpty(mDesigner.getSex())) {
            radioGroup.check(mDesigner.getSex().equals(
                    Constant.SEX_MAN) ? R.id.sex_radio0 : R.id.sex_radio1);
        } else {
            radioGroup.check(R.id.sex_radio0);
        }
        commonDialog.setContent(contentView);
        commonDialog.setTitle(getString(R.string.choose_sex));
        commonDialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (radioGroup.getCheckedRadioButtonId() == R.id.sex_radio0) {
                            mDesigner.setSex(Constant.SEX_MAN);
                        } else {
                            mDesigner.setSex(Constant.SEX_WOMEN);
                        }
                        mDesignerBaseInfoAdapter.notifyItemChanged(0);
                        dialog.dismiss();
                    }
                });
        commonDialog.setNegativeButton(R.string.no, null);
        commonDialog.show();
    }

    private void pickPicture(int requestCode) {
        PhotoPickerIntent intent1 = new PhotoPickerIntent(this);
        intent1.setPhotoCount(1);
        intent1.setShowGif(false);
        intent1.setShowCamera(true);
        startActivityForResult(intent1, requestCode);
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(Constant.CROP_PATH));
        UCrop.of(source, destination)
                .withAspectRatio(1, 1)
                .withMaxResultSize(Global.PIC_WIDTH_UPLOAD_WIDTH, Global.PIC_WIDTH_UPLOAD_WIDTH)
                .start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri uri = UCrop.getOutput(result);
            LogTool.d(TAG, "uri path: " + uri.toString() + uri.getEncodedPath());
            Bitmap bitmap = null;
            try {
                InputStream is = this.getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            if (bitmap != null) {
                upload_image(bitmap, uploadHeadImage);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Toast.makeText(this, UCrop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constant.REQUESTCODE_PICKER_HEAD_PIC:
                if (data != null) {
                    List<String> photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                    if (photos != null && photos.size() > 0) {
                        Uri uri = Uri.fromFile(new File(photos.get(0)));
                        if (null != uri) {
                            beginCrop(uri);
                        }
                    }
                }
                break;
            case Constant.REQUESTCODE_PICKER_PIC:
                pickPicResult(data, uploadDiplomaImage);
                break;
            case Constant.REQUESTCODE_PICKER_AWARD_PIC:
                pickPicResult(data, uploadAwardImage);
                break;
            case Constant.REQUESTCODE_PICKER_CHANGE_AWARD_PIC:
                pickPicResult(data, changeAwardImage);
                break;
            case UCrop.REQUEST_CROP:
                handleCrop(resultCode, data);
                break;
            case Constant.REQUESTCODE_EDIT_ADDRESS:
                if (data != null) {
                    String provice = data.getStringExtra(Constant.EDIT_PROVICE);
                    String city = data.getStringExtra(Constant.EDIT_CITY);
                    String district = data.getStringExtra(Constant.EDIT_DISTRICT);
                    if (!TextUtils.isEmpty(provice) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(district)) {
                        mDesigner.setProvince(provice);
                        mDesigner.setCity(city);
                        mDesigner.setDistrict(district);
                        mDesignerBaseInfoAdapter.notifyItemChanged(0);
                    }
                }
                break;
        }
    }

    private void updateDesignerBaseInfo(Designer designer) {
        UpdateDesignerInfoRequest updateDesignerInfoRequest = new UpdateDesignerInfoRequest();
        updateDesignerInfoRequest.setDesigner(designer);

        Api.updateDesignerInfo(updateDesignerInfoRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                navagateNext();
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    private void navagateNext() {
        appManager.finishActivity(this);
    }

    private void pickPicResult(Intent data, ApiCallback<ApiResponse<String>> apiCallback) {
        if (data != null) {
            List<String> photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            for (String path : photos) {
                Bitmap imageBitmap = ImageUtil.getImage(path);
                LogTool.d(TAG, "imageBitmap: path :" + path);
                if (null != imageBitmap) {
                    upload_image(imageBitmap, apiCallback);
                }
            }
        }
    }

    private ApiCallback<ApiResponse<String>> uploadHeadImage = new ApiCallback<ApiResponse<String>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<String> apiResponse) {
            mDesigner.setImageid(apiResponse.getData());
            mDesignerBaseInfoAdapter.notifyItemChanged(0);
        }

        @Override
        public void onFailed(ApiResponse<String> apiResponse) {

        }

        @Override
        public void onNetworkError(int code) {

        }
    };

    private ApiCallback<ApiResponse<String>> uploadDiplomaImage = new ApiCallback<ApiResponse<String>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<String> apiResponse) {
            mDesigner.setDiploma_imageid(apiResponse.getData());
            mDesignerBaseInfoAdapter.notifyItemChanged(0);
        }

        @Override
        public void onFailed(ApiResponse<String> apiResponse) {

        }

        @Override
        public void onNetworkError(int code) {

        }
    };

    private ApiCallback<ApiResponse<String>> uploadAwardImage = new ApiCallback<ApiResponse<String>>() {

        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<String> apiResponse) {
            DesignerAwardInfo designerAwardInfo = new DesignerAwardInfo();
            designerAwardInfo.setAward_imageid(apiResponse.getData());
            mDesigner.getAward_details().add(designerAwardInfo);
            mDesignerBaseInfoAdapter.notifyItemInserted(1 + mDesigner.getAward_details().size());
        }

        @Override
        public void onFailed(ApiResponse<String> apiResponse) {

        }

        @Override
        public void onNetworkError(int code) {

        }
    };

    private ApiCallback<ApiResponse<String>> changeAwardImage = new ApiCallback<ApiResponse<String>>() {

        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<String> apiResponse) {
            DesignerAwardInfo designerAwardInfo = new DesignerAwardInfo();
            designerAwardInfo.setAward_imageid(apiResponse.getData());
            mDesignerBaseInfoAdapter.changeAwardImageItem(designerAwardInfo);
        }

        @Override
        public void onFailed(ApiResponse<String> apiResponse) {

        }

        @Override
        public void onNetworkError(int code) {

        }
    };

    private void upload_image(final Bitmap bitmap, ApiCallback<ApiResponse<String>> apiCallback) {
        UploadPicRequest uploadPicRequest = new UploadPicRequest();
        uploadPicRequest.setBytes(com.jianfanjia.common.tool.ImageUtil.transformBitmapToBytes(bitmap));
        Api.uploadImage(uploadPicRequest, apiCallback);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_base_info_auth;
    }
}
