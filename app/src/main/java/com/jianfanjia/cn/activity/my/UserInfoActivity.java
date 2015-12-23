package com.jianfanjia.cn.activity.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jianfanjia.cn.activity.*;
import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.bean.OwnerUpdateInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.PopWindowCallBack;
import com.jianfanjia.cn.tools.FileUtil;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.soundcloud.android.crop.Crop;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author fengliang
 * @ClassName: UserInfoActivity
 * @Description:用户个人信息(业主)
 * @date 2015-8-18 下午12:11:49
 */
@EActivity(R.layout.activity_owner_info)
public class UserInfoActivity extends BaseAnnotationActivity implements
        OnClickListener, PopWindowCallBack {
    private static final String TAG = UserInfoActivity.class.getName();
    @ViewById(R.id.head_layout)
    protected RelativeLayout headLayout = null;
    @ViewById(R.id.ownerinfoLayout)
    protected RelativeLayout ownerInfoLayout = null;
    @ViewById(R.id.ownerinfo_scrollview)
    protected ScrollView scrollView = null;
    @ViewById(R.id.nameText)
    protected TextView nameText = null;
    @ViewById(R.id.sexText)
    protected TextView sexText = null;
    @ViewById(R.id.phoneText)
    protected TextView phoneText = null;
    @ViewById(R.id.addressText)
    protected TextView addressText = null;
    @ViewById(R.id.homeText)
    protected TextView homeText = null;
    @ViewById(R.id.head_icon)
    protected ImageView headImageView = null;
    @ViewById(R.id.btn_confirm)
    protected Button btn_confirm = null;
    @ViewById(R.id.ownerinfo_head_layout)
    protected MainHeadView mainHeadView;
    @ViewById(R.id.error_include)
    protected RelativeLayout error_Layout;
    private OwnerInfo ownerInfo = null;
    private OwnerUpdateInfo ownerUpdateInfo = null;
    private String sex = null;

    private boolean isUpdate = false;

    private File mTmpFile = null;
    private String imageId = null;


    @AfterViews
    public void initAnnotationView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.userinfo));
        setConfimEnable(false);
        initData();
    }

    private void setConfimEnable(boolean enabled) {
        isUpdate = enabled;
        btn_confirm.setEnabled(enabled);
    }

    private void setData() {
        setOwnerUpdateInfo();

        scrollView.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(ownerInfo.getImageid())) {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, headImageView);
        } else {
            imageShow.displayImageHeadWidthThumnailImage(this, ownerInfo.getImageid(), headImageView);
        }
        nameText.setText(TextUtils.isEmpty(ownerInfo.getUsername()) ? getString(R.string.ower)
                : ownerInfo.getUsername());
        String sexInfo = ownerInfo.getSex();
        if (!TextUtils.isEmpty(sexInfo)) {
            if (sexInfo.equals(Constant.SEX_MAN)) {
                sexText.setText("男");
            } else if (sexInfo.equals(Constant.SEX_WOMEN)) {
                sexText.setText("女");
            }
        } else {
            sexText.setText(getString(R.string.not_edit));
        }
        phoneText
                .setText(TextUtils.isEmpty(ownerInfo.getPhone()) ? getString(R.string.not_edit)
                        : ownerInfo.getPhone());
        String city = ownerInfo.getCity();
        if (TextUtils.isEmpty(city)) {
            addressText
                    .setText(getString(R.string.not_edit));
        } else {
            String province = ownerInfo.getProvince();
            String district = ownerInfo.getDistrict();
            addressText
                    .setText(province + city + (TextUtils.isEmpty(district) ? ""
                            : district));
        }
        homeText.setText(TextUtils.isEmpty(ownerInfo.getAddress()) ? getString(R.string.not_edit)
                : ownerInfo.getAddress());
    }

    @Click({R.id.error_include, R.id.head_back_layout, R.id.head_layout, R.id.btn_confirm, R.id.address_layout, R.id.name_layout, R.id.sex_layout, R.id.home_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                initData();
                break;
            case R.id.head_back_layout:
                showTipDialog();
                break;
            case R.id.head_layout:
                showPopWindow(ownerInfoLayout);
                break;
            case R.id.btn_confirm:
                if (ownerUpdateInfo != null) {
                    put_Owner_Info();
                }
                break;
            case R.id.address_layout:
                Intent address = new Intent(UserInfoActivity.this,
                        CityEditActivity_.class);
                address.putExtra(Constant.EDIT_PROVICE, ownerUpdateInfo.getProvince());
                address.putExtra(Constant.EDIT_CITY, ownerUpdateInfo.getCity());
                address.putExtra(Constant.EDIT_DISTRICT, ownerUpdateInfo.getDistrict());
                startActivityForResult(address, Constant.REQUESTCODE_EDIT_ADDRESS);
                break;
            case R.id.name_layout:
                Intent name = new Intent(UserInfoActivity.this,
                        EditOwnerInfoActivity.class);
                name.putExtra(Constant.EDIT_TYPE,
                        Constant.REQUESTCODE_EDIT_USERNAME);
                name.putExtra(Constant.EDIT_CONTENT, ownerUpdateInfo.getUsername());
                startActivityForResult(name, Constant.REQUESTCODE_EDIT_USERNAME);
                break;
            case R.id.home_layout:
                Intent home = new Intent(UserInfoActivity.this,
                        EditOwnerInfoActivity.class);
                home.putExtra(Constant.EDIT_TYPE,
                        Constant.REQUESTCODE_EDIT_HOME);
                home.putExtra(Constant.EDIT_CONTENT, ownerUpdateInfo.getAddress());
                startActivityForResult(home, Constant.REQUESTCODE_EDIT_HOME);
                break;
            case R.id.sex_layout:
                showSexChooseDialog();
                break;
            default:
                break;
        }
    }

    //提示是否放弃修改
    private void showTipDialog() {
        if (isUpdate) {
            final CommonDialog commonDialog = DialogHelper
                    .getPinterestDialogCancelable(this);
            commonDialog.setTitle(R.string.tip_update);
            commonDialog.setMessage(getString(R.string.abandon_update));
            commonDialog.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    commonDialog.dismiss();
                }
            });
            commonDialog.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    commonDialog.dismiss();
                    appManager.finishActivity(UserInfoActivity.this);
                }
            });
            commonDialog.show();
        } else {
            appManager.finishActivity(this);
        }
    }

    // 显示选择性别对话框
    private void showSexChooseDialog() {
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
            }
        });
        if (ownerUpdateInfo != null) {
            if (!TextUtils.isEmpty(ownerUpdateInfo.getSex())) {
                radioGroup.check(ownerUpdateInfo.getSex().equals(
                        Constant.SEX_MAN) ? R.id.sex_radio0 : R.id.sex_radio1);
                sex = ownerUpdateInfo.getSex().equals(Constant.SEX_MAN) ? Constant.SEX_MAN
                        : Constant.SEX_WOMEN;
            } else {
                radioGroup.check(R.id.sex_radio0);
                sex = Constant.SEX_MAN;
            }
        }
        commonDialog.setContent(contentView);
        commonDialog.setTitle("选择性别");
        commonDialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ownerUpdateInfo != null) {
                            if (ownerUpdateInfo.getSex() == null
                                    || !ownerUpdateInfo.getSex().equals(sex)) {
                                ownerUpdateInfo.setSex(sex);
                                setConfimEnable(true);
                                sexText.setText(sex.equals(Constant.SEX_MAN) ? "男"
                                        : "女");
                            }
                        }
                        dialog.dismiss();
                    }
                });
        commonDialog.setNegativeButton(R.string.no, null);
        commonDialog.show();
    }

    // 修改设计师个人资料
    private void put_Owner_Info() {
        JianFanJiaClient.put_OwnerInfo(this, ownerUpdateInfo,
                new ApiUiUpdateListener() {

                    @Override
                    public void preLoad() {
                        showWaitDialog();
                    }

                    @Override
                    public void loadSuccess(Object data) {
                        hideWaitDialog();
                        setConfimEnable(false);
                        if (!TextUtils.isEmpty(ownerUpdateInfo.getUsername())) {
                            dataManager.setUserName(ownerUpdateInfo
                                    .getUsername());
                        }
                        if (!TextUtils.isEmpty(ownerUpdateInfo.getImageid())) {
                            dataManager.setUserImagePath(ownerUpdateInfo.getImageid());
                        }
                        dataManager.setOwnerInfo(ownerInfo);
                        setResult(RESULT_OK);
                        appManager.finishActivity(UserInfoActivity.this);
                    }

                    @Override
                    public void loadFailture(String error_msg) {
                        hideWaitDialog();
                        makeTextLong(error_msg);
                    }
                }, this);
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        if (data.toString() != null) {
            ownerInfo = JsonParser.jsonToBean(data.toString(),OwnerInfo.class);
            setData();
            error_Layout.setVisibility(View.GONE);
        } else {
            scrollView.setVisibility(View.GONE);
            error_Layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        super.loadFailture(error_msg);
        scrollView.setVisibility(View.GONE);
        error_Layout.setVisibility(View.VISIBLE);
    }

    private void setOwnerUpdateInfo() {
        ownerUpdateInfo = new OwnerUpdateInfo();
        ownerUpdateInfo.setImageid(ownerInfo.getImageid());
        ownerUpdateInfo.setAddress(ownerInfo.getAddress());
        ownerUpdateInfo.setCity(ownerInfo.getCity());
        ownerUpdateInfo.setDistrict(ownerInfo.getDistrict());
        ownerUpdateInfo.setProvince(ownerInfo.getProvince());
        ownerUpdateInfo.setSex(ownerInfo.getSex());
        ownerUpdateInfo.setUsername(ownerInfo.getUsername());
    }

    private void initData() {
        JianFanJiaClient.get_Owner_Info(this, this, this);
    }

    @Override
    public void firstItemClick() {
        mTmpFile = FileUtil.createTmpFile(this);
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
        Crop.of(source, destination).asSquare().withMaxSize(Global.PIC_WIDTH_UPLOAD_WIDTH, Global.PIC_WIDTH_UPLOAD_WIDTH).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
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
                JianFanJiaClient.uploadImage(this, bitmap, new ApiUiUpdateListener() {
                    @Override
                    public void preLoad() {

                    }

                    @Override
                    public void loadSuccess(Object data) {
                        if (data != null) {
                            String imageid = dataManager.getCurrentUploadImageId();
                            LogTool.d(TAG, "imageid:" + imageid);
                            if (!TextUtils.isEmpty(imageid)) {
                                getImageId(imageid);
                            }
                        }
                    }

                    @Override
                    public void loadFailture(String msg) {

                    }
                }, this);
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
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
                    if (ownerUpdateInfo != null) {
                        if (TextUtils.isEmpty(ownerUpdateInfo.getUsername())
                                || !name.equals(ownerUpdateInfo.getUsername())) {
                            ownerUpdateInfo.setUsername(name);
                            setConfimEnable(true);
                        }
                    }
                }
                break;
            case Constant.REQUESTCODE_EDIT_ADDRESS:
                if (data != null) {
                    String provice = data.getStringExtra(Constant.EDIT_PROVICE);
                    String city = data.getStringExtra(Constant.EDIT_CITY);
                    String district = data.getStringExtra(Constant.EDIT_DISTRICT);
                    if (ownerUpdateInfo != null) {
                        if (!TextUtils.isEmpty(provice) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(district)) {
                            addressText.setText(provice + city + district);
                            ownerUpdateInfo.setProvince(provice);
                            ownerUpdateInfo.setCity(city);
                            ownerUpdateInfo.setDistrict(district);
                            setConfimEnable(true);
                        }
                    }
                }
                break;
            case Constant.REQUESTCODE_EDIT_HOME:
                if (data != null) {
                    String address = data.getStringExtra(Constant.EDIT_CONTENT);
                    homeText.setText(address);
                    if (ownerUpdateInfo != null) {
                        if (TextUtils.isEmpty(ownerUpdateInfo.getAddress())
                                || !ownerUpdateInfo.getAddress().equals(address)) {
                            setConfimEnable(true);
                            ownerUpdateInfo.setAddress(address);
                        }
                    }
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
            case Crop.REQUEST_CROP:
                handleCrop(resultCode, data);
                /*if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        // 得到返回来的数据，是bitmap类型的数据
                        Bitmap bitmap = extras.getParcelable("data");
                        LogTool.d(TAG, "avatar - bitmap = " + bitmap);
                        Uri uri = Crop.getOutput(data);
                        String imgPath = PhotoUtils.savaPicture(bitmap);
                        LogTool.d(TAG, "imgPath==" + imgPath);
                        if (!TextUtils.isEmpty(imgPath)) {
//						uploadManager.uploadPortrait(imgPath, this);
                            JianFanJiaClient.uploadImage(this, bitmap, new ApiUiUpdateListener() {
                                @Override
                                public void preLoad() {

                                }

                                @Override
                                public void loadSuccess(Object data) {
                                    if (data != null) {
                                        String imageid = data.toString();
                                        LogTool.d(TAG, "imageid:" + imageid);
                                        if (!TextUtils.isEmpty(imageid)) {
                                            getImageId(imageid);
                                        }
                                    }
                                }

                                @Override
                                public void loadFailture(String msg) {

                                }
                            }, this);
                        }
                    }*/
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
        }
        if (ownerUpdateInfo != null) {
            ownerUpdateInfo.setImageid(imageId);
        }
        if (mTmpFile != null && mTmpFile.exists()) {
            mTmpFile.delete();
        }
        setConfimEnable(true);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Constant.REQUESTCODE_CROP);
    }

}
