package com.jianfanjia.cn.ui.activity.diary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.DiaryInfo;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.api.request.common.UpdateDiarySetRequest;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.guest.GetDiarySetInfoRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.ui.adapter.DiarySetInfoAdapter;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;
import com.yalantis.ucrop.UCrop;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Description: com.jianfanjia.cn.ui.activity.diary
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-14 11:01
 */
public class DiarySetInfoActivity extends BaseSwipeBackActivity {

    private static final String TAG = DiarySetInfoActivity.class.getName();
    @Bind(R.id.diary_recycleview)
    RecyclerView mRecyclerView;

    @Bind(R.id.rl_writediary)
    LinearLayout rlWriteDiary;

    private DiarySetInfoAdapter mDiaryAdapter;

    private List<DiaryInfo> mDiaryInfoList;

    private ArrayList<DiarySetInfo> mDiarySetInfoList;

    private DiarySetInfo mDiarySetInfo;

    private String diarySetId;

    @Bind(R.id.rl_head_view)
    RelativeLayout rlHeadView;

    @Bind(R.id.toolbar_share)
    ImageView ivShare;

    @Bind(R.id.toolbar_share_layout)
    RelativeLayout rlShareLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mDiarySetInfo = (DiarySetInfo) bundle.getSerializable(IntentConstant.DIARYSET_INFO);
            mDiarySetInfoList = (ArrayList<DiarySetInfo>) bundle.getSerializable(IntentConstant.DIARYSET_INFO_LIST);
            if (mDiarySetInfo != null) {
                diarySetId = mDiarySetInfo.get_id();
            }
            LogTool.d(TAG,"userid =" + dataManager.getUserId());
        }
    }

    public ArrayList<DiarySetInfo> getDiarySetInfoList() {
        return mDiarySetInfoList;
    }

    private void initView() {
        rlHeadView.setBackgroundColor(getResources().getColor(R.color.transparent_background));
        rlWriteDiary.setVisibility(View.GONE);

        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDiarySetDetail();
    }

    private void getDiarySetDetail() {
        if (diarySetId == null) return;
        GetDiarySetInfoRequest getDiarySetInfoRequest = new GetDiarySetInfoRequest();
        getDiarySetInfoRequest.setDiarySetid(diarySetId);

        Api.getDiarySetInfo(getDiarySetInfoRequest, new ApiCallback<ApiResponse<DiarySetInfo>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<DiarySetInfo> apiResponse) {
                DiarySetInfo diarySetInfo = apiResponse.getData();
                LogTool.d(this.getClass().getName(), "diarysetinfo has diary.size(） = " + diarySetInfo.getDiaries()
                        .size());
                mDiarySetInfo.setDiaries(diarySetInfo.getDiaries());
                mDiarySetInfo.setView_count(diarySetInfo.getView_count());
                mDiarySetInfo.setAuthor(diarySetInfo.getAuthor());
                mDiaryAdapter.setDiarySetInfo(mDiarySetInfo);
            }

            @Override
            public void onFailed(ApiResponse<DiarySetInfo> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    private void initRecyclerView() {
        mDiaryAdapter = new DiarySetInfoAdapter(this, mDiaryInfoList);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            int space = TDevice.dip2px(DiarySetInfoActivity.this, 10);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = 0;
                outRect.right = 0;
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = 0;
                    outRect.bottom = 0;
                } else {
                    outRect.top = 0;
                    outRect.bottom = space;
                }
            }
        });
        mRecyclerView.setAdapter(mDiaryAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int totalOffsetY;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalOffsetY += dy;
                setWriteDiaryShowOrHidden(dy);
                setHeadViewShowOrHidden();

                LogTool.d(this.getClass().getName(), "dy =" + dy);
                LogTool.d(this.getClass().getName(), "recyclerview getScrollY =" + totalOffsetY);
            }

            private void setHeadViewShowOrHidden() {
                int alpha = (int) (((float) totalOffsetY / TDevice.dip2px(DiarySetInfoActivity.this, 152)) * 255);
                alpha = alpha > 230 ? 230 : alpha;
                if (alpha == 0) {
                    ivShare.setImageResource(R.mipmap.icon_share);
                } else {
                    ivShare.setImageResource(R.mipmap.icon_share2);
                }
                rlHeadView.setBackgroundColor(Color.argb(alpha, 240, 240, 240));

                LogTool.d(this.getClass().getName(), "recyclerview alpha =" + alpha);
            }

            private void setWriteDiaryShowOrHidden(int dy) {
                if (!mDiarySetInfo.getAuthor().get_id().equals(dataManager.getUserId())) {
                    return;
                }
                if (dy > 0 && totalOffsetY >= TDevice.dip2px(DiarySetInfoActivity.this, 152)) {
                    rlWriteDiary.setVisibility(View.VISIBLE);
                } else if (dy < 0 && totalOffsetY < TDevice.dip2px(DiarySetInfoActivity.this, 152)) {
                    rlWriteDiary.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        mDiaryAdapter.setUploadDiarySetCoverPicListener(new DiarySetInfoAdapter.UploadDiarySetCoverPicListener() {
            @Override
            public void uploadDiarySetCoverPic() {
                pickPicture(Constant.REQUESTCODE_PICKER_HEAD_PIC, 1);
            }
        });
    }

    private void edieDiarySetInfo(){
        UpdateDiarySetRequest updateDiarySetRequest = new UpdateDiarySetRequest();
        updateDiarySetRequest.setDiary_set(mDiarySetInfo);

        Api.updateDiarySetInfo(updateDiarySetRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                mDiaryAdapter.notifyItemChanged(0);
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
            case UCrop.REQUEST_CROP:
                handleCrop(resultCode, data);
                break;
        }
    }

    private void pickPicture(int requestCode, int totalCount) {
        PhotoPickerIntent intent1 = new PhotoPickerIntent(this);
        intent1.setPhotoCount(totalCount);
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

    private void upload_image(final Bitmap bitmap, ApiCallback<ApiResponse<String>> apiCallback) {
        UploadPicRequest uploadPicRequest = new UploadPicRequest();
        uploadPicRequest.setBytes(com.jianfanjia.common.tool.ImageUtil.transformBitmapToBytes(bitmap));
        Api.uploadImage(uploadPicRequest, apiCallback);
    }

    private ApiCallback<ApiResponse<String>> uploadHeadImage = new ApiCallback<ApiResponse<String>>() {
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
            mDiarySetInfo.setCover_imageid(apiResponse.getData());
            edieDiarySetInfo();
        }

        @Override
        public void onFailed(ApiResponse<String> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_diaryinfo;
    }
}
