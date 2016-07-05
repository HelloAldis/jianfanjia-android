package com.jianfanjia.cn.ui.activity.diary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.DiaryImageDetailInfo;
import com.jianfanjia.api.model.DiaryInfo;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.api.request.common.AddDiaryRequest;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.business.DiaryBusiness;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.cn.ui.activity.common.CommonShowPicActivity;
import com.jianfanjia.cn.ui.adapter.AddDiaryGridViewAdapter;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.common.tool.ImageUtil;
import com.jianfanjia.common.tool.LogTool;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.entity.AnimationRect;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Description: com.jianfanjia.cn.ui.activity.diary
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-15 11:25
 */
public class AddDiaryActivity extends BaseSwipeBackActivity {
    private static final String TAG = AddDiaryActivity.class.getClass().getName();

    public static final String CURRENT_DIARYSET_TITLE = "current_diaryset_title";

    @Bind(R.id.mainhv_diary)
    MainHeadView mMainHeadView;

    @Bind(R.id.et_add_diary_content)
    EditText etAddDiaryContent;

    @Bind(R.id.gv_add_diary_pic)
    GridView gvAddDiaryPic;

    @Bind(R.id.tv_add_diary_dec_stage_content)
    TextView tvAddDiaryDecStageContent;

    @Bind(R.id.tv_add_diary_now_diaryset_content)
    TextView tvAddDiaryNowDiarySetContent;

    @Bind(R.id.iv_add_diary_now_diaryset_goto)
    ImageView ivAddDiaryNowDiarySetGoto;

    @Bind(R.id.rl_add_diary_now_diaryset)
    RelativeLayout rlAddDiaryNowDiarySet;

    public List<DiarySetInfo> mDiarySetInfoList;

    private List<String> mDiarySetTitleList;

    private String currentDiarySetTitle;

    private DiarySetInfo currentDiarySet;

    private DiaryInfo mDiaryInfo;

    private List<String> photos;

    private AddDiaryGridViewAdapter mAddDiaryGridViewAdapter;

    private boolean isAddDiaryByChooseMultipleDiarySet;

    private List<String> imageUrlList;
    private List<String> showImageUrlList = new ArrayList<>();
    private List<DiaryImageDetailInfo> mDiaryImageDetailInfoLists = new ArrayList<>();

    public static void intentToAddDiary(Context context, List<DiarySetInfo> diarySetInfoList, DiarySetInfo
            diarySetInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.DIARYSET_INFO_LIST, (ArrayList) diarySetInfoList);
        bundle.putSerializable(IntentConstant.DIARYSET_INFO, diarySetInfo);
        IntentUtil.startActivity(context, AddDiaryActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
        initData();
    }


    private void initView() {
        initMianView();
        initGridView();

        if (isAddDiaryByChooseMultipleDiarySet) {
            ivAddDiaryNowDiarySetGoto.setVisibility(View.VISIBLE);
            rlAddDiaryNowDiarySet.setEnabled(true);
        } else {
            ivAddDiaryNowDiarySetGoto.setVisibility(View.GONE);
            rlAddDiaryNowDiarySet.setEnabled(false);
        }
    }

    private void initMianView() {
        mMainHeadView.setMianTitle(getString(R.string.create_diary));
        mMainHeadView.setRightTitle(getString(R.string.str_publish));
        mMainHeadView.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDiaryInfo.setImages(mDiaryImageDetailInfoLists);
                addDiary(mDiaryInfo);
            }
        });
        mMainHeadView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDiaryImageDetailInfoLists.size() > 0 || !TextUtils.isEmpty(mDiaryInfo.getContent())) {
                    showTipDialog();
                } else {
                    appManager.finishActivity(AddDiaryActivity.class);
                }
            }
        });
    }

    private void initGridView() {
        imageUrlList = new ArrayList<>();
        imageUrlList.add(Constant.DEFALUT_ADD_DIARY_PIC);
        mAddDiaryGridViewAdapter = new AddDiaryGridViewAdapter(this, imageUrlList);
        mAddDiaryGridViewAdapter.setDeleteListener(new AddDiaryGridViewAdapter.DeleteListener() {
            @Override
            public void delete(int position) {
                deleteImageToDiaryData(imageUrlList.get(position));
                if (imageUrlList.size() == DiaryBusiness.UPLOAD_MAX_PIC_COUNT) {
                    imageUrlList.remove(position);
                    if (!imageUrlList.contains(Constant.DEFALUT_ADD_DIARY_PIC)) {
                        imageUrlList.add(Constant.DEFALUT_ADD_DIARY_PIC);
                    }
                } else {
                    imageUrlList.remove(position);
                }
                mAddDiaryGridViewAdapter.notifyDataSetChanged();
            }
        });
        gvAddDiaryPic.setAdapter(mAddDiaryGridViewAdapter);
        gvAddDiaryPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = imageUrlList.get(position);
                Log.i(this.getClass().getName(), "data:" + data);
                Log.i(this.getClass().getName(), "imageUrlList size=" + imageUrlList.size());
                if (data.equals(Constant.DEFALUT_ADD_DIARY_PIC)) {
                    pickPicture();
                } else {
                    ArrayList<AnimationRect> animationRectArrayList
                            = new ArrayList<>();
                    for (int i = 0; i < mAddDiaryGridViewAdapter.getCount() - 1; i++) {
                        ImageView imageView = (ImageView) gvAddDiaryPic.getChildAt(i).findViewById(R.id.img);
                        LogTool.d(this.getClass().getName(), "view left position =" + imageView.getLeft());
                        if (imageView.getVisibility() == View.VISIBLE) {
                            AnimationRect rect = AnimationRect.buildFromImageView(imageView);
                            LogTool.d(this.getClass().getName(), "left position =" + rect.imageViewEntireRect.left);
                            animationRectArrayList.add(rect);
                        }
                    }
                    showImageBig(position, animationRectArrayList);
                }
            }
        });

    }

    protected void showTipDialog() {
        CommonDialog commonDialog = DialogHelper.getPinterestDialogCancelable(this);
        commonDialog.setTitle(R.string.tip_confirm);
        commonDialog.setMessage(getString(R.string.abandon_diary_write));
        commonDialog.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        commonDialog.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                appManager.finishActivity(AddDiaryActivity.this);
            }
        });
        commonDialog.show();
    }

    private void showImageBig(int position, List<AnimationRect> animationRectList) {
        showImageUrlList.clear();
        for (String imageUrl : imageUrlList) {
            if (!imageUrl.equals(Constant.DEFALUT_ADD_DIARY_PIC)) {
                showImageUrlList.add(imageUrl);
            }
        }
        LogTool.d(this.getClass().getName(), "position:" + position);
        CommonShowPicActivity.intentTo(this, (ArrayList<String>) showImageUrlList, (ArrayList<AnimationRect>)
                animationRectList, position);
        overridePendingTransition(0, 0);
    }

    private void pickPicture() {
        PhotoPickerIntent intent1 = new PhotoPickerIntent(AddDiaryActivity.this);
        if (imageUrlList != null) {
            intent1.setPhotoCount(DiaryBusiness.UPLOAD_MAX_PIC_COUNT - imageUrlList.size() + 1);
        } else {
            intent1.setPhotoCount(DiaryBusiness.UPLOAD_MAX_PIC_COUNT);
        }
        intent1.setShowGif(false);
        intent1.setShowCamera(true);
        startActivityForResult(intent1, Constant.REQUESTCODE_PICKER_PIC);
    }

    private void setMianHeadRightTitleEnable() {
        if (!TextUtils.isEmpty(mDiaryInfo.getContent()) && mDiaryInfo.getContent().length() >= 15 && !TextUtils
                .isEmpty(mDiaryInfo.getSection_label()) &&
                !TextUtils.isEmpty(mDiaryInfo.getDiarySetid())) {
            mMainHeadView.setRigthTitleEnable(true);
        } else {
            mMainHeadView.setRigthTitleEnable(false);
        }
    }

    private void initData() {
        setMianHeadRightTitleEnable();

        etAddDiaryContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDiaryInfo.setContent(s.toString().trim());
                setMianHeadRightTitleEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setNowDiarySetTitle() {
        tvAddDiaryNowDiarySetContent.setText(currentDiarySetTitle);
        setMianHeadRightTitleEnable();
    }

    private void setDecStage() {
        LogTool.d(TAG, "section_label =" + mDiaryInfo.getSection_label());
        tvAddDiaryDecStageContent.setText(DiaryBusiness.getShowDiarySectionLabel(mDiaryInfo.getSection_label()));
        setMianHeadRightTitleEnable();
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentDiarySet = (DiarySetInfo) bundle.getSerializable(IntentConstant.DIARYSET_INFO);
            mDiarySetInfoList = (ArrayList<DiarySetInfo>) bundle.getSerializable(IntentConstant.DIARYSET_INFO_LIST);

            if (mDiarySetInfoList != null) {//没有传当前的日记本，默认选择最后一个日记本
                isAddDiaryByChooseMultipleDiarySet = true;
                chooseCurrentDiarySet();
            } else {
                isAddDiaryByChooseMultipleDiarySet = false;
                currentDiarySetTitle = currentDiarySet.getTitle();
            }
            LogTool.d(TAG, "currentDiarySetTitle =" + currentDiarySetTitle);

            mDiaryInfo = new DiaryInfo();
            setDiarySetInfo();
        }
    }

    private void chooseCurrentDiarySet() {
        long lastupdateTime = 0l;
        for (DiarySetInfo diarySetInfo : mDiarySetInfoList) {
            long updateTime = diarySetInfo.getLastupdate();
            if (updateTime > lastupdateTime) {
                lastupdateTime = updateTime;
            }
        }
        for (DiarySetInfo diarySetInfo : mDiarySetInfoList) {
            if (diarySetInfo.getLastupdate() == lastupdateTime) {
                currentDiarySet = diarySetInfo;
                currentDiarySetTitle = currentDiarySet.getTitle();
            }
        }
    }

    private void setDiarySetInfo() {
        mDiaryInfo.setDiarySetid(currentDiarySet.get_id());
        mDiaryInfo.setSection_label(DiaryBusiness.getNextSectionLable(currentDiarySet.getLatest_section_label()));

        setNowDiarySetTitle();
        setDecStage();
    }


    @OnClick({R.id.rl_add_diary_dec_stage, R.id.rl_add_diary_now_diaryset})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.rl_add_diary_dec_stage:
                gotoChooseDiaryStage();
                break;
            case R.id.rl_add_diary_now_diaryset:
                if (mDiarySetInfoList.size() > 1) {
                    gotoChooseDiarySetTitle();
                }
                break;
        }
    }

    private void gotoChooseDiarySetTitle() {
        Bundle bundle = new Bundle();
        bundle.putString(ChooseNowDiarySetActivity.CURRENT_CHOOSE_VALUE, currentDiarySet.get_id());
        bundle.putStringArrayList(ChooseNowDiarySetActivity.ALL_CAN_CHOOSE_VALUES, (ArrayList) mDiarySetInfoList);
        startActivityForResult(ChooseNowDiarySetActivity.class, bundle, Constant.REQUESTCODE_CHOOSE_DIARY_TITLE);
    }

    private void gotoChooseDiaryStage() {
        Bundle bundle = new Bundle();
        bundle.putString(ChooseDiaryStageActivity.CURRENT_CHOOSE_VALUE, mDiaryInfo.getSection_label());
        startActivityForResult(ChooseDiaryStageActivity.class, bundle, Constant.REQUESTCODE_CHOOSE_DIARY_STAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constant.REQUESTCODE_PICKER_PIC:
                if (data != null) {
                    this.photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                    LogTool.d(TAG, "all path :" + this.photos);
                    this.uploadImageSync();
                }
                break;
            case Constant.REQUESTCODE_CHOOSE_DIARY_STAGE:
                String chooseValue = data.getStringExtra(IntentConstant.RESPONSE_DATA);
                mDiaryInfo.setSection_label(chooseValue);
                setDecStage();
                break;
            case Constant.REQUESTCODE_CHOOSE_DIARY_TITLE:
                currentDiarySet = (DiarySetInfo) data.getSerializableExtra(IntentConstant.RESPONSE_DATA);
                currentDiarySetTitle = currentDiarySet.getTitle();
                setDiarySetInfo();
                break;
        }

    }

    private void addDiary(DiaryInfo diaryInfo) {
        AddDiaryRequest addDiaryRequest = new AddDiaryRequest();
        addDiaryRequest.setDiary(diaryInfo);

        Api.addDiaryInfo(addDiaryRequest, new ApiCallback<ApiResponse<DiaryInfo>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<DiaryInfo> apiResponse) {
                if (isAddDiaryByChooseMultipleDiarySet) {
                    DiarySetInfoActivity.intentToDiarySet(AddDiaryActivity.this, currentDiarySet);
                }
                appManager.finishActivity(AddDiaryActivity.this);
            }

            @Override
            public void onFailed(ApiResponse<DiaryInfo> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    private void upload_image(final Bitmap bitmap) {
        UploadPicRequest uploadPicRequest = new UploadPicRequest();
        uploadPicRequest.setBytes(com.jianfanjia.common.tool.ImageUtil.transformBitmapToBytes(bitmap));

        Api.uploadImage(uploadPicRequest, new ApiCallback<ApiResponse<String>>() {
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
                String imageid = apiResponse.getData();
                addImageToShow(imageid);
                addImageToDiaryData(imageid, bitmap.getWidth(), bitmap.getHeight());

                AddDiaryActivity.this.uploadImageSync();
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
                AddDiaryActivity.this.uploadImageSync();
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                AddDiaryActivity.this.uploadImageSync();
            }
        });
    }

    private void deleteImageToDiaryData(String imageid) {
        int pos = 0;
        for (DiaryImageDetailInfo diaryImageDetailInfo : mDiaryImageDetailInfoLists) {
            if (diaryImageDetailInfo.getImageid().equals(imageid)) {
                mDiaryImageDetailInfoLists.remove(pos);
                break;
            }
            pos++;
        }
    }

    private void addImageToDiaryData(String imageid, int width, int height) {
        DiaryImageDetailInfo diaryImageDetailInfo = new DiaryImageDetailInfo();
        diaryImageDetailInfo.setImageid(imageid);
        diaryImageDetailInfo.setWidth(width);
        diaryImageDetailInfo.setHeight(height);

        mDiaryImageDetailInfoLists.add(diaryImageDetailInfo);
    }

    private void addImageToShow(String imageid) {
        if (imageUrlList.size() != DiaryBusiness.UPLOAD_MAX_PIC_COUNT) {
            imageUrlList.add(imageUrlList.size() - 1, imageid);
        } else {
            imageUrlList.remove(DiaryBusiness.UPLOAD_MAX_PIC_COUNT - 1);
            imageUrlList.add(imageid);
        }
        mAddDiaryGridViewAdapter.notifyDataSetChanged();
    }

    public void uploadImageSync() {
        if (!this.photos.isEmpty()) {
            String path = this.photos.get(0);
            this.photos.remove(0);
            Bitmap imageBitmap = ImageUtil.getImage(path);
            LogTool.d(TAG, "imageBitmap: path :" + path);
            if (null != imageBitmap) {
                upload_image(imageBitmap);
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_diary_add;
    }
}
