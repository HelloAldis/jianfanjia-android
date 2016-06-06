package com.jianfanjia.cn.designer.ui.activity.my_info_auth.identity_info;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.designer.UpdateDesignerIdentityInfoRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.business.DesignerBusiness;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.BusinessCovertUtil;
import com.jianfanjia.cn.designer.tools.ImageShow;
import com.jianfanjia.cn.designer.ui.activity.common.ShowPicActivity;
import com.jianfanjia.cn.designer.ui.activity.common.choose_item.ChooseItemIntent;
import com.jianfanjia.cn.designer.ui.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.designer.view.DesignerAuthCommitDialog;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.common.tool.ImageUtil;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth.identity_info
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-18 11:25
 */
public class DesignerIdentityAuthActivity extends BaseSwipeBackActivity {

    private static final String TAG = DesignerIdentityAuthActivity.class.getName();

    private static final int REQUESTCODE_PICK_IDENTITY_BACK = 100;
    private static final int REQUESTCODE_PICK_IDENTITY_FRONT = 120;
    private static final int REQUESTCODE_PICK_BANK = 140;

    public static final int CURRENT_STATUS_EDIT = 0;
    public static final int CURRENT_STATUS_PRIVIEW = 1;


    @Bind(R.id.designer_auth_head_layout)
    MainHeadView mMainHeadView;

    @Bind(R.id.edit_real_name)
    EditText nameEditText;

    @Bind(R.id.edit_identity_number)
    EditText identityNumberEditText;

    @Bind(R.id.identity_front_example)
    ImageView identityFrontImageView;

    @Bind(R.id.identity_background_example)
    ImageView identityBackgroundImageView;

    @Bind(R.id.identity_front_delete)
    ImageView identityFrontDeleteImageView;

    @Bind(R.id.identity_background_delete)
    ImageView identityBackgroundDeleteImageView;

    @Bind(R.id.bank_content)
    TextView bankContentTextView;

    @Bind(R.id.bank_card_example)
    ImageView bankCardImageView;

    @Bind(R.id.bank_card_delete)
    ImageView bankCardDeleteImageView;

    @Bind(R.id.edit_bank_card_number)
    EditText backCardNumberEditText;

    @Bind(R.id.bank_layout)
    RelativeLayout bankLayout;

    private Designer mDesigner;
    private int currentStatus = CURRENT_STATUS_PRIVIEW;//默认进来是预览状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();
        initView();
        initData();
    }

    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mDesigner = (Designer) bundle.getSerializable(Global.DESIGNER_INFO);
        }
        if (mDesigner == null) {
            mDesigner = new Designer();
        }
        if (!TextUtils.isEmpty(mDesigner.getUid_auth_type()) && mDesigner.getUid_auth_type().equals(DesignerBusiness
                .DESIGNER_NOT_APPLY)) {
            currentStatus = CURRENT_STATUS_EDIT;
        } else {
            currentStatus = CURRENT_STATUS_PRIVIEW;
        }
    }

    private void initView() {
        initMainView();

        changeViewByStatus();

        setImageWidthHeight();
    }

    private void changeViewByStatus() {
        if (currentStatus == CURRENT_STATUS_EDIT) {
            mMainHeadView.setRightTitle(getString(R.string.commit));
            mMainHeadView.setRightTextListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAuthTipDialog();
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
        changeViewShowEditOrPreview();
        if (!TextUtils.isEmpty(mDesigner.getUid_auth_type()) && mDesigner.getUid_auth_type().equals(DesignerBusiness
                .DESIGNER_NOT_AUTH)) {
            mMainHeadView.setRightTitleVisable(View.GONE);
        } else {
            mMainHeadView.setRightTitleVisable(View.VISIBLE);
        }
    }

    private void changeViewShowEditOrPreview() {
        if (currentStatus == CURRENT_STATUS_EDIT) {
            nameEditText.setEnabled(true);
            identityNumberEditText.setEnabled(true);
            identityBackgroundImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickPicture(REQUESTCODE_PICK_IDENTITY_BACK);
                }
            });
            identityFrontImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickPicture(REQUESTCODE_PICK_IDENTITY_FRONT);
                }
            });
            identityBackgroundDeleteImageView.setEnabled(true);
            identityFrontDeleteImageView.setEnabled(true);
            bankCardDeleteImageView.setEnabled(true);
            backCardNumberEditText.setEnabled(true);
            bankCardImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickPicture(REQUESTCODE_PICK_BANK);
                }
            });
            bankLayout.setEnabled(true);
        } else {
            nameEditText.setEnabled(false);
            identityNumberEditText.setEnabled(false);
            identityBackgroundImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showIdentityBigImage(1);
                }
            });
            identityFrontImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showIdentityBigImage(0);
                }
            });
            bankCardImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(mDesigner.getBank_card_image1())) {
                        List<String> showImages = new ArrayList<>();
                        showImages.add(mDesigner.getBank_card_image1());
                        Bundle showPicBundle = new Bundle();
                        showPicBundle.putInt(Constant.CURRENT_POSITION, 0);
                        showPicBundle.putStringArrayList(Constant.IMAGE_LIST,
                                (ArrayList<String>) showImages);
                        startActivity(ShowPicActivity.class, showPicBundle);
                    }
                }
            });
            identityBackgroundDeleteImageView.setEnabled(false);
            identityFrontDeleteImageView.setEnabled(false);
            bankCardDeleteImageView.setEnabled(false);
            backCardNumberEditText.setEnabled(false);
            bankLayout.setEnabled(false);
        }
    }

    private void showIdentityBigImage(int position) {
        List<String> showImages = new ArrayList<>();
        if (!TextUtils.isEmpty(mDesigner.getUid_image1())) {
            showImages.add(mDesigner.getUid_image1());
        }
        if (!TextUtils.isEmpty(mDesigner.getUid_image2())) {
            showImages.add(mDesigner.getUid_image2());
        }

        if (showImages.size() == 2) {

        } else if (showImages.size() == 1) {
            position = 0;
        } else {
            return;
        }

        Bundle showPicBundle = new Bundle();
        showPicBundle.putInt(Constant.CURRENT_POSITION, position);
        showPicBundle.putStringArrayList(Constant.IMAGE_LIST,
                (ArrayList<String>) showImages);
        startActivity(ShowPicActivity.class, showPicBundle);
    }

    private void setImageWidthHeight() {
        int width = (int) (TDevice.getScreenWidth() - TDevice.dip2px(this, 42)) / 2;
        int height = (int) (width * ((float) 342 / 545));

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
        bankCardImageView.setLayoutParams(lp);
        identityBackgroundImageView.setLayoutParams(lp);
        identityFrontImageView.setLayoutParams(lp);
    }

    private void initData() {
        setMianHeadRightTitleEnable();

        nameEditText.setText(mDesigner.getRealname());
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDesigner.setRealname(s.toString());
                setMianHeadRightTitleEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        identityNumberEditText.setText(mDesigner.getUid());
        identityNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDesigner.setUid(s.toString());
                setMianHeadRightTitleEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (!TextUtils.isEmpty(mDesigner.getUid_image1())) {
            ImageShow.getImageShow().displayHalfScreenWidthThumnailImage(this, mDesigner.getUid_image1(),
                    identityFrontImageView);
            if (currentStatus == CURRENT_STATUS_EDIT) {
                identityFrontDeleteImageView.setVisibility(View.VISIBLE);
            } else {
                identityFrontDeleteImageView.setVisibility(View.GONE);
            }
            identityFrontDeleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDesigner.setUid_image1("");
                    initData();
                }
            });
        } else {
            identityFrontImageView.setImageResource(R.mipmap.icon_identity_front_example);
            identityFrontDeleteImageView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mDesigner.getUid_image2())) {
            ImageShow.getImageShow().displayHalfScreenWidthThumnailImage(this, mDesigner.getUid_image2(),
                    identityBackgroundImageView);
            if (currentStatus == CURRENT_STATUS_EDIT) {
                identityBackgroundDeleteImageView.setVisibility(View.VISIBLE);
            } else {
                identityBackgroundDeleteImageView.setVisibility(View.GONE);
            }
            identityBackgroundDeleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDesigner.setUid_image2("");
                    initData();
                }
            });
        } else {
            identityBackgroundImageView.setImageResource(R.mipmap.icon_identity_background_example);
            identityBackgroundDeleteImageView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mDesigner.getBank_card_image1())) {
            ImageShow.getImageShow().displayHalfScreenWidthThumnailImage(this, mDesigner.getBank_card_image1(),
                    bankCardImageView);
            if (currentStatus == CURRENT_STATUS_EDIT) {
                bankCardDeleteImageView.setVisibility(View.VISIBLE);
            } else {
                bankCardDeleteImageView.setVisibility(View.GONE);
            }
            bankCardDeleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDesigner.setBank_card_image1("");
                    initData();
                }
            });
        } else {
            bankCardImageView.setImageResource(R.mipmap.icon_back_example);
            bankCardDeleteImageView.setVisibility(View.GONE);

        }

        backCardNumberEditText.setText(mDesigner.getBank_card());
        backCardNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDesigner.setBank_card(s.toString());
                setMianHeadRightTitleEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bankContentTextView.setText(mDesigner.getBank());
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.identity_auth));
    }

    private void setMianHeadRightTitleEnable() {
        if (currentStatus == CURRENT_STATUS_EDIT) {
            if (!TextUtils.isEmpty(mDesigner.getRealname()) && !TextUtils.isEmpty(mDesigner.getUid())
                    && !TextUtils.isEmpty(mDesigner.getUid_image1()) && !TextUtils.isEmpty(mDesigner.getUid_image2())
                    && !TextUtils.isEmpty(mDesigner.getBank()) && !TextUtils.isEmpty(mDesigner.getBank_card())
                    && !TextUtils.isEmpty(mDesigner.getBank_card_image1())) {
                mMainHeadView.setRigthTitleEnable(true);
            } else {
                mMainHeadView.setRigthTitleEnable(false);
            }
        }
    }

    @OnClick({R.id.head_back_layout, R.id.bank_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.bank_layout:
                intentToBankChoose();
                break;
        }
    }

    private void intentToBankChoose() {
        ChooseItemIntent houseTypeIntent = new ChooseItemIntent(this);
        houseTypeIntent.setSingleChoose(Constant.REQUIRECODE_BANK, BusinessCovertUtil.getBankKeyByValue(mDesigner
                .getBank()), getString(R.string.bank));
        startActivityForResult(houseTypeIntent, Constant.REQUIRECODE_BANK);
    }

    private void showAuthTipDialog(){
        DesignerAuthCommitDialog designerAuthCommitDialog = new DesignerAuthCommitDialog(this,new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                updateDesignerIdentityInfo(mDesigner);
            }
        });
        designerAuthCommitDialog.show();
    }

    private void updateDesignerIdentityInfo(Designer designer) {
        UpdateDesignerIdentityInfoRequest updateDesignerIdentityInfoRequest = new UpdateDesignerIdentityInfoRequest();
        updateDesignerIdentityInfoRequest.setDesigner(designer);

        Api.updateDesignerIdentityInfo(updateDesignerIdentityInfoRequest, new ApiCallback<ApiResponse<String>>() {
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
                appManager.finishActivity(DesignerIdentityAuthActivity.this);
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

    private void pickPicture(int requestCode) {
        PhotoPickerIntent intent1 = new PhotoPickerIntent(this);
        intent1.setPhotoCount(1);
        intent1.setShowGif(false);
        intent1.setShowCamera(true);
        startActivityForResult(intent1, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data != null) {
            switch (requestCode) {
                case REQUESTCODE_PICK_IDENTITY_BACK:
                    pickPicResult(data, identityBackApiCallback);
                    break;
                case REQUESTCODE_PICK_IDENTITY_FRONT:
                    pickPicResult(data, identityFrontApiCallback);
                    break;
                case REQUESTCODE_PICK_BANK:
                    pickPicResult(data, bankApiCallback);
                    break;
                case Constant.REQUIRECODE_BANK:
                    ReqItemFinderImp.ItemMap itemMap = data.getParcelableExtra(Global
                            .RESPONSE_DATA);
                    mDesigner.setBank(itemMap.value);
                    initData();
                    break;
            }
        }
    }

    private ApiCallback<ApiResponse<String>> identityFrontApiCallback = new ApiCallback<ApiResponse<String>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<String> apiResponse) {
            mDesigner.setUid_image1(apiResponse.getData());
            initData();
        }

        @Override
        public void onFailed(ApiResponse<String> apiResponse) {

        }

        @Override
        public void onNetworkError(int code) {

        }
    };

    private ApiCallback<ApiResponse<String>> identityBackApiCallback = new ApiCallback<ApiResponse<String>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<String> apiResponse) {
            mDesigner.setUid_image2(apiResponse.getData());
            initData();
        }

        @Override
        public void onFailed(ApiResponse<String> apiResponse) {

        }

        @Override
        public void onNetworkError(int code) {

        }
    };

    private ApiCallback<ApiResponse<String>> bankApiCallback = new ApiCallback<ApiResponse<String>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<String> apiResponse) {
            mDesigner.setBank_card_image1(apiResponse.getData());
            initData();
        }

        @Override
        public void onFailed(ApiResponse<String> apiResponse) {

        }

        @Override
        public void onNetworkError(int code) {

        }
    };

    private void pickPicResult(Intent data, ApiCallback<ApiResponse<String>> apiCallback) {
        List<String> photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
        for (String path : photos) {
            Bitmap imageBitmap = ImageUtil.getImage(path);
            LogTool.d(TAG, "imageBitmap: path :" + path);
            if (null != imageBitmap) {
                upload_image(imageBitmap, apiCallback);
            }
        }
    }

    private void upload_image(final Bitmap bitmap, ApiCallback<ApiResponse<String>> apiCallback) {
        UploadPicRequest uploadPicRequest = new UploadPicRequest();
        uploadPicRequest.setBytes(com.jianfanjia.common.tool.ImageUtil.transformBitmapToBytes(bitmap));
        Api.uploadImage(uploadPicRequest, apiCallback);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_identity_auth;
    }
}
