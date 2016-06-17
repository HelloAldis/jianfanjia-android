package com.jianfanjia.cn.ui.activity.diary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.DiaryInfo;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.business.DiaryBusiness;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.ui.activity.common.ShowPicActivity;
import com.jianfanjia.cn.ui.adapter.AddDiaryGridViewAdapter;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.common.tool.ImageUtil;
import com.jianfanjia.common.tool.LogTool;
import me.iwf.photopicker.PhotoPickerActivity;
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

    public List<DiarySetInfo> mDiarySetInfoList;

    private List<String> mDiarySetTitleList;

    private String currentDiarySetTitle;

    private DiarySetInfo currentDiarySet;

    private DiaryInfo mDiaryInfo;

    private List<String> photos;

    private AddDiaryGridViewAdapter mAddDiaryGridViewAdapter;

    private List<String> imageUrlList;
    private List<String> showImageUrlList = new ArrayList<>();

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
    }

    private void initGridView() {
        imageUrlList = new ArrayList<>();
        imageUrlList.add(Constant.HOME_ADD_PIC);
        mAddDiaryGridViewAdapter = new AddDiaryGridViewAdapter(this, imageUrlList);
        gvAddDiaryPic.setAdapter(mAddDiaryGridViewAdapter);
        gvAddDiaryPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = imageUrlList.get(position);
                Log.i(this.getClass().getName(), "data:" + data);
                Log.i(this.getClass().getName(), "imageUrlList size=" + imageUrlList.size());
                if (data.equals(Constant.HOME_ADD_PIC)) {
                    pickPicture();
                } else {
                    showImageBig(position);
                }
            }
        });
    }

    private void showImageBig(int position) {
        showImageUrlList.clear();
        for (String imageUrl : imageUrlList) {
            if (!imageUrl.equals(Constant.HOME_ADD_PIC)) {
                showImageUrlList.add(imageUrl);
            }
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constant.IMAGE_LIST,
                (ArrayList<String>) showImageUrlList);
        bundle.putInt(Constant.CURRENT_POSITION, position);
        startActivity(ShowPicActivity.class, bundle);
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

    private void initMianView() {
        mMainHeadView.setMianTitle(getString(R.string.add_diary));
        mMainHeadView.setRightTitle(getString(R.string.str_publish));
        mMainHeadView.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mMainHeadView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appManager.finishActivity(AddDiaryActivity.class);
            }
        });
    }

    private void setMianHeadRightTitleEnable() {
        if (true) {
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
                mDiaryInfo.setContent(s.toString());
                setMianHeadRightTitleEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setNowDiarySetTitle();
        setDecStage();
    }

    private void setNowDiarySetTitle() {
        tvAddDiaryNowDiarySetContent.setText(currentDiarySetTitle);
    }

    private void setDecStage() {
        tvAddDiaryDecStageContent.setText(mDiaryInfo.getSection_label());
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentDiarySetTitle = bundle.getString(CURRENT_DIARYSET_TITLE);
            mDiarySetInfoList = (ArrayList<DiarySetInfo>) bundle.getSerializable(IntentConstant.DIARYSET_INFO_LIST);


            if (currentDiarySetTitle == null) {//没有传当前的日记本，默认选择最后一个日记本
                chooseCurrentDiarySet();
            } else {
                for (DiarySetInfo diarySetInfo : mDiarySetInfoList) {
                    if (currentDiarySetTitle.equals(diarySetInfo.getTitle())) {
                        currentDiarySet = diarySetInfo;
                        break;
                    }
                }
            }
            LogTool.d(TAG, "currentDiarySetTitle =" + currentDiarySetTitle);

            mDiarySetTitleList = new ArrayList<>();
            for (DiarySetInfo diarySetInfo : mDiarySetInfoList) {
                mDiarySetTitleList.add(diarySetInfo.getTitle());
            }

            mDiaryInfo = new DiaryInfo();
            mDiaryInfo.setSection_label(currentDiarySet.getLatest_section_label());
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
        }

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
                if (imageUrlList.size() != DiaryBusiness.UPLOAD_MAX_PIC_COUNT) {
                    imageUrlList.add(imageUrlList.size() - 1, apiResponse.getData());
                } else {
                    imageUrlList.remove(DiaryBusiness.UPLOAD_MAX_PIC_COUNT - 1);
                    imageUrlList.add(apiResponse.getData());
                }
                mAddDiaryGridViewAdapter.notifyDataSetChanged();

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
