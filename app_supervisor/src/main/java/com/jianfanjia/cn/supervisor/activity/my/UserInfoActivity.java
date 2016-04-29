package com.jianfanjia.cn.supervisor.activity.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.OnClick;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.SuperVisor;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.supervisor.GetSuperVisorRequest;
import com.jianfanjia.api.request.supervisor.UpdateSuperVisorRequest;
import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.activity.LoginNewActivity;
import com.jianfanjia.cn.supervisor.api.Api;
import com.jianfanjia.cn.supervisor.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.supervisor.business.DataManagerNew;
import com.jianfanjia.cn.supervisor.config.Constant;
import com.jianfanjia.cn.supervisor.config.Global;
import com.jianfanjia.cn.supervisor.interf.PopWindowCallBack;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.AddPhotoDialog;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.common.tool.FileUtil;
import com.jianfanjia.common.tool.ImageUtil;
import com.jianfanjia.common.tool.LogTool;
import com.yalantis.ucrop.UCrop;

/**
 * @author fengliang
 * @ClassName: UserInfoActivity
 * @Description:用户个人信息(业主)
 * @date 2015-8-18 下午12:11:49
 */
public class UserInfoActivity extends BaseSwipeBackActivity implements
        OnClickListener, PopWindowCallBack {
    private static final String TAG = UserInfoActivity.class.getName();
    @Bind(R.id.head_layout)
    protected RelativeLayout headLayout = null;
    @Bind(R.id.ownerinfoLayout)
    protected RelativeLayout ownerInfoLayout = null;
    @Bind(R.id.ownerinfo_scrollview)
    protected ScrollView scrollView = null;
    @Bind(R.id.nameText)
    protected TextView nameText = null;
    @Bind(R.id.sexText)
    protected TextView sexText = null;
    @Bind(R.id.phoneText)
    protected TextView phoneText = null;
    @Bind(R.id.addressText)
    protected TextView addressText = null;
    @Bind(R.id.homeText)
    protected TextView homeText = null;
    @Bind(R.id.head_icon)
    protected ImageView headImageView = null;
    @Bind(R.id.ownerinfo_head_layout)
    protected MainHeadView mainHeadView;
    @Bind(R.id.error_include)
    protected RelativeLayout error_Layout;
    private SuperVisor user = null;
    private String sex = null;

    private boolean isUpdate = false;//是否更新，只有，更新了用户名或者头像才更新
    protected AddPhotoDialog popupWindow = null;

    private File mTmpFile = null;
    private String imageId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    public void initView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.userinfo));
    }

    private void setData() {
        scrollView.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(user.getImageid())) {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, headImageView);
        } else {
            imageShow.displayImageHeadWidthThumnailImage(this, user.getImageid(), headImageView);
        }
        nameText.setText(TextUtils.isEmpty(user.getUsername()) ? getString(R.string.ower)
                : user.getUsername());
        String sexInfo = user.getSex();
        if (!TextUtils.isEmpty(sexInfo)) {
            if (sexInfo.equals(Constant.SEX_MAN)) {
                sexText.setText(getString(R.string.man));
            } else if (sexInfo.equals(Constant.SEX_WOMEN)) {
                sexText.setText(getString(R.string.women));
            }
        } else {
            sexText.setText(getString(R.string.not_edit));
        }
        String city = user.getCity();
        if (TextUtils.isEmpty(city)) {
            addressText
                    .setText(getString(R.string.not_edit));
        } else {
            String province = user.getProvince();
            String district = user.getDistrict();
            addressText.setText(province + city + (TextUtils.isEmpty(district) ? ""
                    : district));
        }
        homeText.setText(TextUtils.isEmpty(user.getAddress()) ? getString(R.string.not_edit)
                : user.getAddress());
    }

    @OnClick({R.id.error_include, R.id.head_back_layout, R.id.head_layout, R.id.address_layout,
            R.id.name_layout, R.id.sex_layout, R.id.home_layout, R.id.logout_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                initData();
                break;
            case R.id.head_back_layout:
                showTipDialog();
                break;
            case R.id.head_layout:
                showPopWindow();
                break;
            case R.id.address_layout:
                Bundle address = new Bundle();
                address.putString(Constant.EDIT_PROVICE, user.getProvince());
                address.putString(Constant.EDIT_CITY, user.getCity());
                address.putString(Constant.EDIT_DISTRICT, user.getDistrict());
                address.putInt(EditCityActivity.PAGE, EditCityActivity.EDIT_USER_ADRESS);
                IntentUtil.startActivityForResult(this, EditCityActivity.class, address, Constant
                        .REQUESTCODE_EDIT_ADDRESS);
                break;
            case R.id.name_layout:
                Bundle name = new Bundle();
                name.putInt(Constant.EDIT_TYPE,
                        Constant.REQUESTCODE_EDIT_USERNAME);
                name.putString(Constant.EDIT_CONTENT, user.getUsername());
                IntentUtil.startActivityForResult(this, EditOwnerInfoActivity.class, name, Constant.REQUESTCODE_EDIT_USERNAME);
                break;
            case R.id.home_layout:
                Bundle home = new Bundle();
                home.putInt(Constant.EDIT_TYPE,
                        Constant.REQUESTCODE_EDIT_HOME);
                home.putString(Constant.EDIT_CONTENT, user.getAddress());
                IntentUtil.startActivityForResult(this, EditOwnerInfoActivity.class, home, Constant
                        .REQUESTCODE_EDIT_HOME);
                break;
            case R.id.sex_layout:
                showSexChooseDialog();
                break;
            case R.id.logout_layout:
                onClickExit();
                break;
            default:
                break;
        }
    }

    private void onClickExit() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(this);
        dialog.setTitle("退出登录");
        dialog.setMessage("确定退出登录吗？");
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DataManagerNew.loginOut();
                        appManager.finishAllActivity();
                        startActivity(LoginNewActivity.class);
                        finish();
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }

    protected void showPopWindow() {
        if (popupWindow == null) {
            popupWindow = new AddPhotoDialog(this, this);
        }
        popupWindow.show();
    }

    //提示是否放弃修改
    private void showTipDialog() {
        if (isUpdate) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        appManager.finishActivity(this);
    }

    // 显示选择性别对话框
    private void showSexChooseDialog() {
        final SuperVisor userInfo = new SuperVisor();
        CommonDialog commonDialog = DialogHelper
                .getPinterestDialogCancelable(this);
        View contentView = inflater.inflate(R.layout.sex_choose, null);
        RadioGroup radioGroup = (RadioGroup) contentView
                .findViewById(R.id.sex_radioGroup);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group.getCheckedRadioButtonId() == R.id.sex_radio0) {
                    sex = Constant.SEX_MAN;
                } else {
                    sex = Constant.SEX_WOMEN;
                }
                userInfo.setSex(sex);
            }
        });
        if (!TextUtils.isEmpty(userInfo.getSex())) {
            radioGroup.check(userInfo.getSex().equals(
                    Constant.SEX_MAN) ? R.id.sex_radio0 : R.id.sex_radio1);
            sex = userInfo.getSex().equals(Constant.SEX_MAN) ? Constant.SEX_MAN
                    : Constant.SEX_WOMEN;
        } else {
            radioGroup.check(R.id.sex_radio0);
            sex = Constant.SEX_MAN;
        }
        userInfo.setSex(sex);
        commonDialog.setContent(contentView);
        commonDialog.setTitle(getString(R.string.choose_sex));
        commonDialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (user.getSex() == null || !user.getSex().equals(userInfo.getSex())) {
                            updateSexInfo(userInfo);
                        }
                        dialog.dismiss();
                    }
                });
        commonDialog.setNegativeButton(R.string.no, null);
        commonDialog.show();
    }

    protected void updateSexInfo(SuperVisor userInfo) {
        UpdateSuperVisorRequest request = new UpdateSuperVisorRequest();
        request.setSupervisor(userInfo);

        Api.updateSuperVisorInfo(request, new ApiCallback<ApiResponse<String>>() {
                    @Override
                    public void onPreLoad() {

                    }

                    @Override
                    public void onHttpDone() {

                    }

                    @Override
                    public void onSuccess(ApiResponse<String> apiResponse) {
                        user.setSex(sex);
                        sexText.setText(user.getSex().equals(
                                Constant.SEX_MAN) ? getString(R.string.man) : getString(R.string.women));
                        dataManager.setOwnerInfo(user);
                    }

                    @Override
                    public void onFailed(ApiResponse<String> apiResponse) {

                    }

                    @Override
                    public void onNetworkError(int code) {

                    }
                });
    }

    // 修改设计师头像
    private void updateImageId(final SuperVisor userInfo) {
        UpdateSuperVisorRequest request = new UpdateSuperVisorRequest();
        request.setSupervisor(userInfo);

        Api.updateSuperVisorInfo(request, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                if (!TextUtils.isEmpty(userInfo.getImageid())) {
                    dataManager.setUserImagePath(userInfo.getImageid());
                }
                dataManager.setOwnerInfo(userInfo);
                isUpdate = true;
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

    private void initData() {
        GetSuperVisorRequest request = new GetSuperVisorRequest();
        Api.getSuperVisorInfoDetail(request, new ApiCallback<ApiResponse<SuperVisor>>() {
            @Override
            public void onPreLoad() {
                Hud.show(UserInfoActivity.this);
            }

            @Override
            public void onHttpDone() {
                Hud.dismiss();
            }

            @Override
            public void onSuccess(ApiResponse<SuperVisor> apiResponse) {
                user = apiResponse.getData();
                LogTool.d(TAG, "user: " + user);
                setData();
                error_Layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailed(ApiResponse<SuperVisor> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                scrollView.setVisibility(View.GONE);
                error_Layout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void firstItemClick() {
        mTmpFile = FileUtil.createTimeStampTmpFile();
        if (mTmpFile != null) {
            Intent cameraIntent = UiHelper.createShotIntent(mTmpFile);
            if (cameraIntent != null) {
                startActivityForResult(cameraIntent, Constant.REQUESTCODE_CAMERA);
            } else {
            }
        } else {
            makeTextShort(getString(R.string.tip_not_sdcard));
        }
    }

    @Override
    public void secondItemClick() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, Constant.REQUESTCODE_LOCATION);
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
                uploadImage(bitmap);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Toast.makeText(this, UCrop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(Bitmap bitmap) {
        UploadPicRequest request = new UploadPicRequest();
        request.setBytes(ImageUtil.transformBitmapToBytes(bitmap));
        Api.uploadImage(request, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                String imageid = apiResponse.getData();
                LogTool.d(TAG, "imageid:" + imageid);
                if (!TextUtils.isEmpty(imageid)) {
                    getImageId(imageid);
                }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constant.REQUESTCODE_EDIT_USERNAME:
                if (data != null) {
                    String name = data.getStringExtra(Constant.EDIT_CONTENT);
                    nameText.setText(name);
                    user.setUsername(name);
                    dataManager.setOwnerInfo(user);
                    dataManager.setUserName(name);
                    isUpdate = true;
                }
                break;
            case Constant.REQUESTCODE_EDIT_ADDRESS:
                if (data != null) {
                    String provice = data.getStringExtra(Constant.EDIT_PROVICE);
                    String city = data.getStringExtra(Constant.EDIT_CITY);
                    String district = data.getStringExtra(Constant.EDIT_DISTRICT);
                    if (!TextUtils.isEmpty(provice) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(district)) {
                        addressText.setText(provice + city + district);
                        user.setProvince(provice);
                        user.setCity(city);
                        user.setDistrict(district);
                        dataManager.setOwnerInfo(user);
                    }
                }
                break;
            case Constant.REQUESTCODE_EDIT_HOME:
                if (data != null) {
                    String address = data.getStringExtra(Constant.EDIT_CONTENT);
                    homeText.setText(address);
                    user.setAddress(address);
                    dataManager.setOwnerInfo(user);
                }
                break;
            case Constant.REQUESTCODE_CAMERA:// 拍照
                mTmpFile = new File(dataManager.getPicPath());
                if (mTmpFile != null) {
                    Uri uri = Uri.fromFile(mTmpFile);
                    LogTool.d(TAG, "uri:" + uri);
                    if (null != uri) {
                        beginCrop(uri);
                    }
                }
                break;
            case Constant.REQUESTCODE_LOCATION:// 本地选取
                if (data != null) {
                    Uri uri = data.getData();
                    LogTool.d(TAG, "uri:" + uri);
                    if (null != uri) {
                        beginCrop(uri);
                    }
                }
                break;
            case UCrop.REQUEST_CROP:
                handleCrop(resultCode, data);
                break;
            default:
                break;
        }
    }

    public void getImageId(String imageid) {
        LogTool.d(TAG, "imageid=" + imageid);
        imageId = imageid;
//        dataManager.setUserImagePath(imageId);
        if (TextUtils.isEmpty(imageId)) {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, headImageView);
        } else {
            imageShow.displayImageHeadWidthThumnailImage(this, imageId, headImageView);
            SuperVisor userInfo = new SuperVisor();
            userInfo.setImageid(imageId);
            updateImageId(userInfo);
        }
        if (mTmpFile != null && mTmpFile.exists()) {
            mTmpFile.delete();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info;
    }
}
