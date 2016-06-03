package com.jianfanjia.cn.designer.ui.activity.my_info_auth.team_info;

import android.content.DialogInterface;
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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Team;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.designer.AddOneTeamRequest;
import com.jianfanjia.api.request.designer.UpdateOneTeamRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.BusinessCovertUtil;
import com.jianfanjia.cn.designer.tools.ImageShow;
import com.jianfanjia.cn.designer.tools.IntentUtil;
import com.jianfanjia.cn.designer.ui.activity.common.EditCityActivity;
import com.jianfanjia.cn.designer.ui.activity.common.ShowPicActivity;
import com.jianfanjia.cn.designer.ui.activity.common.choose_item.ChooseItemIntent;
import com.jianfanjia.cn.designer.ui.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.dialog.CommonDialog;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;
import com.jianfanjia.common.tool.ImageUtil;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth.team_info
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-25 20:13
 */
public class DesignerEditTeamActivity extends BaseSwipeBackActivity {

    public static final int CURRENT_STATUS_EDIT = 0;
    public static final int CURRENT_STATUS_PRIVIEW = 1;

    public static final int FROM_UPDATE_INTENT = 2;
    public static final int FROM_ADD_INTENT = 3;

    public static final String INTENT_FROM_FLAG = "intent_from_flag";

    private static final int REQUESTCODE_PICK_IDENTITY_BACK = 100;
    private static final int REQUESTCODE_PICK_IDENTITY_FRONT = 120;
    private static final String TAG = DesignerEditTeamActivity.class.getName();

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

    @Bind(R.id.sex_content)
    TextView sexTextView;

    @Bind(R.id.sex_layout)
    RelativeLayout sexLayout;

    @Bind(R.id.address_content)
    TextView addrTextView;

    @Bind(R.id.address_layout)
    RelativeLayout addrLayout;

    @Bind(R.id.work_company_content)
    EditText workCompanyEditext;

    @Bind(R.id.work_year_content)
    EditText workYearEditext;

    @Bind(R.id.workingon_site_content)
    EditText workingonSiteEditText;

    @Bind(R.id.goodat_type_content)
    TextView goodAtTextView;

    @Bind(R.id.goodat_type_layout)
    RelativeLayout goodAtTypeLayout;

    private int currentStatus = CURRENT_STATUS_PRIVIEW;//默认进来是预览状态
    private int intentFrom;

    private Team mTeam;

    private void changeViewShowEditOrPreview() {
        if (currentStatus == CURRENT_STATUS_EDIT) {
            nameEditText.setEnabled(true);
            identityNumberEditText.setEnabled(true);
            workCompanyEditext.setEnabled(true);
            workingonSiteEditText.setEnabled(true);
            workYearEditext.setEnabled(true);
            workCompanyEditext.setEnabled(true);
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
            sexLayout.setEnabled(true);
            addrLayout.setEnabled(true);
            goodAtTypeLayout.setEnabled(true);
            identityBackgroundDeleteImageView.setEnabled(true);
            identityFrontDeleteImageView.setEnabled(true);
        } else {
            nameEditText.setEnabled(false);
            identityNumberEditText.setEnabled(false);
            workCompanyEditext.setEnabled(false);
            workingonSiteEditText.setEnabled(false);
            workYearEditext.setEnabled(false);
            workCompanyEditext.setEnabled(false);
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
            sexLayout.setEnabled(false);
            addrLayout.setEnabled(false);
            goodAtTypeLayout.setEnabled(false);
            identityBackgroundDeleteImageView.setEnabled(false);
            identityFrontDeleteImageView.setEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();
        initView();
        initData();
    }

    private void initView() {
        initMainView();

        changeViewByStatus();

        setImageWidthHeight();
    }

    private void setImageWidthHeight() {
        int width = (int) (TDevice.getScreenWidth() - TDevice.dip2px(this, 42)) / 2;
        int height = (int) (width * ((float) 342 / 545));

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
        identityBackgroundImageView.setLayoutParams(lp);
        identityFrontImageView.setLayoutParams(lp);
    }

    private void changeViewByStatus() {
        if (currentStatus == CURRENT_STATUS_EDIT) {
            mMainHeadView.setRightTitle(getString(R.string.commit));
            mMainHeadView.setRightTextListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (intentFrom == FROM_UPDATE_INTENT) {
                        updateDesignerTeamInfo(mTeam);
                    } else {
                        addDesignerOneTeam(mTeam);
                    }
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
    }

    private void showIdentityBigImage(int position){
        List<String> showImages = new ArrayList<>();
        if(!TextUtils.isEmpty(mTeam.getUid_image1())){
            showImages.add(mTeam.getUid_image1());
        }
        if(!TextUtils.isEmpty(mTeam.getUid_image2())){
            showImages.add(mTeam.getUid_image2());
        }

        if(showImages.size() == 2){

        }else if(showImages.size() == 1){
            position = 1;
        }else{
            return;
        }

        Bundle showPicBundle = new Bundle();
        showPicBundle.putInt(Constant.CURRENT_POSITION, position);
        showPicBundle.putStringArrayList(Constant.IMAGE_LIST,
                (ArrayList<String>) showImages);
        startActivity(ShowPicActivity.class, showPicBundle);
    }


    private void initMainView() {
        if (intentFrom == FROM_UPDATE_INTENT) {
            mMainHeadView.setMianTitle(getString(R.string.update_team_auth));
        } else {
            mMainHeadView.setMianTitle(getString(R.string.add_team_auth));
        }
    }

    private void setMianHeadRightTitleEnable() {
        if (currentStatus == CURRENT_STATUS_EDIT) {
            if (!TextUtils.isEmpty(mTeam.getManager()) && !TextUtils.isEmpty(mTeam.getUid())
                    && !TextUtils.isEmpty(mTeam.getUid_image1()) && !TextUtils.isEmpty(mTeam.getUid_image2())
                    && !TextUtils.isEmpty(mTeam.getDistrict()) && !TextUtils.isEmpty(mTeam.getCompany())
                    && !TextUtils.isEmpty(mTeam.getGood_at()) && !TextUtils.isEmpty(mTeam.getWorking_on())
                    && mTeam.getWork_year() > 0 && !TextUtils.isEmpty(mTeam.getSex())) {
                mMainHeadView.setRigthTitleEnable(true);
            } else {
                mMainHeadView.setRigthTitleEnable(false);
            }
        }
    }

    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mTeam = (Team) bundle.getSerializable(Global.TEAM_INFO);
            intentFrom = bundle.getInt(INTENT_FROM_FLAG, FROM_UPDATE_INTENT);
        }
        if (mTeam == null) {
            mTeam = new Team();
        }

        if (intentFrom == FROM_UPDATE_INTENT) {
            currentStatus = CURRENT_STATUS_PRIVIEW;
        } else {
            currentStatus = CURRENT_STATUS_EDIT;
        }
    }

    private void initData() {
        setMianHeadRightTitleEnable();

        nameEditText.setText(mTeam.getManager());
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTeam.setManager(s.toString());
                setMianHeadRightTitleEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        identityNumberEditText.setText(mTeam.getUid());
        identityNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTeam.setUid(s.toString());
                setMianHeadRightTitleEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (!TextUtils.isEmpty(mTeam.getUid_image1())) {
            ImageShow.getImageShow().displayHalfScreenWidthThumnailImage(this, mTeam.getUid_image1(),
                    identityFrontImageView);
            if (currentStatus == CURRENT_STATUS_EDIT) {
                identityFrontDeleteImageView.setVisibility(View.VISIBLE);
            }else {
                identityFrontDeleteImageView.setVisibility(View.GONE);
            }
            identityFrontDeleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTeam.setUid_image1("");
                    initData();
                }
            });

        } else {
            identityFrontImageView.setImageResource(R.mipmap.icon_identity_front_example);
            identityFrontDeleteImageView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mTeam.getUid_image2())) {
            ImageShow.getImageShow().displayHalfScreenWidthThumnailImage(this, mTeam.getUid_image2(),
                    identityBackgroundImageView);
            if (currentStatus == CURRENT_STATUS_EDIT) {
                identityBackgroundDeleteImageView.setVisibility(View.VISIBLE);
            }else {
                identityBackgroundDeleteImageView.setVisibility(View.GONE);
            }
            identityBackgroundDeleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTeam.setUid_image2("");
                    initData();
                }
            });

        } else {
            identityBackgroundImageView.setImageResource(R.mipmap.icon_identity_background_example);
            identityBackgroundDeleteImageView.setVisibility(View.GONE);
        }

        String sexInfo = mTeam.getSex();
        if (!TextUtils.isEmpty(sexInfo)) {
            if (sexInfo.equals(Constant.SEX_MAN)) {
                sexTextView.setText(getString(R.string.man));
            } else if (sexInfo.equals(Constant.SEX_WOMEN)) {
                sexTextView.setText(getString(R.string.women));
            }
        } else {
            sexTextView.setText(null);
        }

        String city = mTeam.getCity();
        if (TextUtils.isEmpty(city)) {
            addrTextView
                    .setText(null);
        } else {
            String province = mTeam.getProvince();
            String district = mTeam.getDistrict();
            addrTextView.setText(province + " " + city + " " + (TextUtils.isEmpty(district) ? ""
                    : district));
        }

        workCompanyEditext.setText(mTeam.getCompany());
        workCompanyEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTeam.setCompany(s.toString());
                setMianHeadRightTitleEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (mTeam.getWork_year() != 0) {
            workYearEditext.setText(mTeam.getWork_year() + "");
        }
        workYearEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    mTeam.setWork_year(Integer.parseInt(s.toString()));
                } else {
                    mTeam.setWork_year(0);
                }
                setMianHeadRightTitleEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        workingonSiteEditText.setText(mTeam.getWorking_on());
        workingonSiteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTeam.setWorking_on(s.toString());
                setMianHeadRightTitleEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        goodAtTextView.setText(mTeam.getGood_at());
    }

    @OnClick({R.id.head_back_layout, R.id.goodat_type_layout, R.id.address_layout, R.id.sex_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.goodat_type_layout:
                intentToEditGoodAtWork();
                break;
            case R.id.sex_layout:
                showSexChooseDialog();
                break;
            case R.id.address_layout:
                intentToEditCity();
                break;
        }
    }

    private void addDesignerOneTeam(Team team) {
        AddOneTeamRequest addOneTeamRequest = new AddOneTeamRequest();
        addOneTeamRequest.setTeam(team);

        Api.addOneTeam(addOneTeamRequest, new ApiCallback<ApiResponse<String>>() {
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
                appManager.finishActivity(DesignerEditTeamActivity.this);
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

    private void updateDesignerTeamInfo(Team team) {
        UpdateOneTeamRequest updateOneTeamRequest = new UpdateOneTeamRequest();
        updateOneTeamRequest.setTeam(team);

        Api.updateOneTeam(updateOneTeamRequest, new ApiCallback<ApiResponse<String>>() {
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
                appManager.finishActivity(DesignerEditTeamActivity.this);
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

    private void intentToEditGoodAtWork() {
        ChooseItemIntent houseTypeIntent = new ChooseItemIntent(this);
        houseTypeIntent.setSingleChoose(Constant.REQUIRECODE_GOODAT_WORKOFTYPE, BusinessCovertUtil
                .getGoodAtTypeOfWorkByValue(mTeam.getGood_at()),getString(R.string.goodat_type));
        startActivityForResult(houseTypeIntent, Constant.REQUIRECODE_GOODAT_WORKOFTYPE);
    }

    private void intentToEditCity() {
        Bundle address = new Bundle();
        address.putString(Constant.EDIT_PROVICE, mTeam.getProvince());
        address.putString(Constant.EDIT_CITY, mTeam.getCity());
        address.putString(Constant.EDIT_DISTRICT, mTeam.getDistrict());
        address.putInt(EditCityActivity.PAGE, EditCityActivity.EDIT_USER_ADRESS);
        IntentUtil.startActivityForResult(this, EditCityActivity.class, address, Constant
                .REQUESTCODE_EDIT_ADDRESS);
    }

    private void showSexChooseDialog() {
        CommonDialog commonDialog = DialogHelper
                .getPinterestDialogCancelable(this);
        View contentView = inflater.inflate(R.layout.sex_choose, null);
        final RadioGroup radioGroup = (RadioGroup) contentView
                .findViewById(R.id.sex_radioGroup);
        if (!TextUtils.isEmpty(mTeam.getSex())) {
            radioGroup.check(mTeam.getSex().equals(
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
                            mTeam.setSex(Constant.SEX_MAN);
                        } else {
                            mTeam.setSex(Constant.SEX_WOMEN);
                        }
                        initData();
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
                case Constant.REQUESTCODE_EDIT_ADDRESS:
                    String provice = data.getStringExtra(Constant.EDIT_PROVICE);
                    String city = data.getStringExtra(Constant.EDIT_CITY);
                    String district = data.getStringExtra(Constant.EDIT_DISTRICT);
                    if (!TextUtils.isEmpty(provice) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(district)) {
                        mTeam.setProvince(provice);
                        mTeam.setCity(city);
                        mTeam.setDistrict(district);
                        initData();
                    }
                    break;
                case Constant.REQUIRECODE_GOODAT_WORKOFTYPE:
                    ReqItemFinderImp.ItemMap itemMap = data.getParcelableExtra(Global
                            .RESPONSE_DATA);
                    mTeam.setGood_at(itemMap.value);
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
            mTeam.setUid_image1(apiResponse.getData());
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
            mTeam.setUid_image2(apiResponse.getData());
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
        return R.layout.activity_designer_auth_edit_team;
    }
}
