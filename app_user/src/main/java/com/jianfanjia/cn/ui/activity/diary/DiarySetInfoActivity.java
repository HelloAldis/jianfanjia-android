package com.jianfanjia.cn.ui.activity.diary;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.DiaryInfo;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.api.request.common.AddDiarySetFavoriteRequest;
import com.jianfanjia.api.request.common.DeleteDiarySetFavoriteRequest;
import com.jianfanjia.api.request.common.UpdateDiarySetRequest;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.guest.GetDiarySetInfoRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DiarySetStageItem;
import com.jianfanjia.cn.business.DataManagerNew;
import com.jianfanjia.cn.business.DiaryBusiness;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.cn.tools.ShareUtil;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.Event.CollectDiarySetEvent;
import com.jianfanjia.cn.ui.Event.RefreshDiarySetInfoEvent;
import com.jianfanjia.cn.ui.adapter.DiarySetInfoAdapter;
import com.jianfanjia.cn.ui.adapter.DiarySetLeftMenuAdapter;
import com.jianfanjia.cn.view.slidingmenu.SlidingMenu;
import com.jianfanjia.common.tool.ImageUtil;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;
import com.jianfanjia.common.tool.ToastUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.UMSsoHandler;
import com.yalantis.ucrop.UCrop;
import de.greenrobot.event.EventBus;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Description: com.jianfanjia.cn.ui.activity.diary
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-14 11:01
 */
public class DiarySetInfoActivity extends BaseActivity {

    private static final String TAG = DiarySetInfoActivity.class.getName();

    @Bind(R.id.slidingmenulayout)
    SlidingMenu mSlidingMenu;

    @Bind(R.id.diary_recycleview)
    RecyclerView mRecyclerView;

    @Bind(R.id.diary_recycleview_left)
    RecyclerView mRecyclerViewLeft;

    @Bind(R.id.rl_writediary)
    LinearLayout rlWriteDiary;

    private DiarySetInfoAdapter mDiaryAdapter;

    private List<DiaryInfo> mDiaryInfoList = new ArrayList<>();
    private LinearLayoutManager diarySetLinearLayoutManager;

    private DiarySetInfo mDiarySetInfo;

    private String diarySetId;

    @Bind(R.id.rl_head_view)
    RelativeLayout rlHeadView;

    @Bind(R.id.toolbar_share)
    ImageView ivShare;

    @Bind(R.id.toolbar_collect)
    ImageView ivCollect;

    @Bind(R.id.head_back)
    ImageView ivBackView;

    @Bind(R.id.tv_like_count)
    TextView tvLikeCount;

    private ShareUtil mShareUtil;

    List<DiarySetStageItem> diarySetStageItemList;
    String[] diarySetStageList;
    DiarySetLeftMenuAdapter mDiarySetLeftMenuAdapter;
    private boolean isHeadTransparent;
    private boolean isHasLoadOnce;

    public static void intentToDiarySet(Context context, DiarySetInfo diarySetInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.DIARYSET_INFO, diarySetInfo);
        IntentUtil.startActivity(context, DiarySetInfoActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();

        mShareUtil = new ShareUtil(this);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mDiarySetInfo = (DiarySetInfo) bundle.getSerializable(IntentConstant.DIARYSET_INFO);
            if (mDiarySetInfo != null) {
                diarySetId = mDiarySetInfo.get_id();
            }
            LogTool.d(TAG, "userid =" + dataManager.getUserId());
        }
    }

    private void initView() {
        initSlidingMenu();

        rlHeadView.setBackgroundColor(getResources().getColor(R.color.transparent_background));
        rlWriteDiary.setVisibility(View.GONE);

        diarySetStageList = getResources().getStringArray(R.array.arr_diary_stage_navigation);
        diarySetStageItemList = new ArrayList<>(diarySetStageList.length);
        for (String stage : diarySetStageList) {
            DiarySetStageItem diarySetStageItem = new DiarySetStageItem();
            diarySetStageItem.setItemName(stage);
            diarySetStageItemList.add(diarySetStageItem);
        }

        initRecyclerView();
        initRecyclerViewLeft();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogTool.d(this.getClass().getName(), "activity event =" + event.getRawY());
        return super.onTouchEvent(event);
    }

    private void initRecyclerView() {
        mDiaryAdapter = new DiarySetInfoAdapter(this, mDiaryInfoList);
        diarySetLinearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
                LogTool.d(this.getClass().getName(), "dy =" + dy);


                return super.scrollVerticallyBy(dy, recycler, state);
            }
        };
        mRecyclerView.setLayoutManager(diarySetLinearLayoutManager);
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

                LogTool.d(this.getClass().getName(), "dy =" + dy);

                totalOffsetY += dy;

                setWriteDiaryShowOrHidden(dy);
                setHeadViewShowOrHidden();
            }

            private void setHeadViewShowOrHidden() {
                int alpha = (int) (((float) totalOffsetY / TDevice.dip2px(DiarySetInfoActivity.this, 152)) * 255);
                alpha = alpha > 255 ? 255 : alpha;
                rlHeadView.setBackgroundColor(Color.argb(alpha, 240, 240, 240));
                isHeadTransparent = alpha <= 20;
                changeHeadUIShow();
            }

            private void setWriteDiaryShowOrHidden(int dy) {
                if (mDiarySetInfo.getAuthor() != null && !mDiarySetInfo.getAuthor().get_id().equals(DataManagerNew
                        .getInstance().getUserId())) {
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
                if (newState == RecyclerView.SCROLL_STATE_IDLE && move) {
                    move = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n = navigatePos - diarySetLinearLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < mRecyclerView.getChildCount()) {
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = mRecyclerView.getChildAt(n).getTop() - headHeightOffset;
                        //最后的移动
                        mRecyclerView.scrollBy(0, top);
                    }
                }
            }
        });
        mDiaryAdapter.setUploadDiarySetCoverPicListener(new DiarySetInfoAdapter.UploadDiarySetCoverPicListener() {
            @Override
            public void uploadDiarySetCoverPic() {
                pickPicture(Constant.REQUESTCODE_PICKER_HEAD_PIC, 1);
            }
        });
    }

    private void changeHeadUIShow() {
        if (isHeadTransparent) {
            setHeadUITransparent();
        } else {
            setHeadUIUnTransparent();
        }
    }

    private void setHeadUITransparent() {
        ViewCompat.setBackgroundTintList(ivBackView, ColorStateList.valueOf(Color.WHITE));
        ViewCompat.setBackgroundTintList(ivShare, ColorStateList.valueOf(Color.WHITE));

        tvLikeCount.setTextColor(Color.WHITE);

        ViewCompat.setBackgroundTintList(ivCollect, ColorStateList.valueOf(Color.WHITE));
    }

    private void setHeadUIUnTransparent() {
        ViewCompat.setBackgroundTintList(ivShare, ColorStateList.valueOf(Color.GRAY));
        ViewCompat.setBackgroundTintList(ivBackView, ColorStateList.valueOf(Color.GRAY));

        tvLikeCount.setTextColor(Color.GRAY);

        ViewCompat.setBackgroundTintList(ivCollect, ColorStateList.valueOf(Color.GRAY));
    }

    private void initRecyclerViewLeft() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewLeft.setLayoutManager(linearLayoutManager);
        mDiarySetLeftMenuAdapter = new DiarySetLeftMenuAdapter(this, diarySetStageItemList);
        mRecyclerViewLeft.setAdapter(mDiarySetLeftMenuAdapter);
        mDiarySetLeftMenuAdapter.setOnNavigateListener(new DiarySetLeftMenuAdapter.OnNavigateListener() {
            @Override
            public void navigateTo(final String itemName) {
                setCheckItem(itemName);
                mSlidingMenu.showContent();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        moveToPosition(getOneStageFisrtPos(itemName) +
                                mDiaryAdapter.getHeadViewCount());
                    }
                }, 300);
            }
        });
    }

    private boolean move;
    private int navigatePos;
    private int headHeightOffset;//recyclerview第一个可见item完全可见时的偏移量，bar.height + writediary.height

    private void moveToPosition(int n) {
        navigatePos = n;
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = diarySetLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = diarySetLinearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            mRecyclerView.smoothScrollToPosition(n);
            move = true;
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.smoothScrollBy(0, top - headHeightOffset);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            mRecyclerView.smoothScrollToPosition(n);
            move = true;
            //这里这个变量是用在RecyclerView滚动监听里面的
        }
    }

    private int getOneStageFisrtPos(String itemName) {
        int i = 0;
        for (DiaryInfo diaryInfo : mDiaryInfoList) {
            if (diaryInfo.getSection_label().equals(itemName)) {
                break;
            }
            i++;
        }
        return i;
    }

    private void initSlidingMenu() {
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
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
                if (!isHasLoadOnce) {
                    Hud.show(getUiContext());
                }
            }

            @Override
            public void onHttpDone() {
                Hud.dismiss();
            }

            @Override
            public void onSuccess(ApiResponse<DiarySetInfo> apiResponse) {
                isHasLoadOnce = true;

                DiarySetInfo diarySetInfo = apiResponse.getData();
                mDiarySetInfo = diarySetInfo;
                mDiaryAdapter.setDiarySetInfo(diarySetInfo);

                if (mDiarySetInfo.is_my_favorite()) {
                    ivCollect.setBackgroundResource(R.mipmap.icon_collect_img2);
                } else {
                    ivCollect.setBackgroundResource(R.mipmap.icon_collect_img1);
                }
                tvLikeCount.setText(DiaryBusiness.covertIntCountToShow(diarySetInfo.getFavorite_count()));

                mDiarySetInfo.setView_count(mDiarySetInfo.getView_count() + 1);

                initData();
                EventBus.getDefault().post(new RefreshDiarySetInfoEvent(mDiarySetInfo));
            }

            @Override
            public void onFailed(ApiResponse<DiarySetInfo> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        },this);
    }

    private void initData() {
        //把所有数量都清零
        for (DiarySetStageItem diarySetStageItem : diarySetStageItemList) {
            diarySetStageItem.setCount(0);
        }
        Map<String, List<DiaryInfo>> listMap = new HashMap<>();
        for (DiaryInfo diaryInfo : mDiarySetInfo.getDiaries()) {
            String sectionLabel = diaryInfo.getSection_label();
            for (DiarySetStageItem diarySetStageItem : diarySetStageItemList) {
                if (sectionLabel.equals(diarySetStageItem.getItemName())) {
                    if (listMap.containsKey(sectionLabel)) {
                        listMap.get(sectionLabel).add(diaryInfo);
                    } else {
                        List<DiaryInfo> diaryInfoList = new ArrayList<>();
                        diaryInfoList.add(diaryInfo);
                        listMap.put(sectionLabel, diaryInfoList);
                    }
                    diarySetStageItem.setCount(diarySetStageItem.getCount() + 1);
                    break;
                }
            }
        }
        mDiaryInfoList.clear();
        for (String stage : diarySetStageList) {
            if (listMap.containsKey(stage)) {
                mDiaryInfoList.addAll(listMap.get(stage));
            }
        }

        String laseStage = null;
        for (DiarySetStageItem diarySetStageItem : diarySetStageItemList) {
            if (diarySetStageItem.getCount() > 0) {
                laseStage = diarySetStageItem.getItemName();
                break;
            }
        }

        if (laseStage != null) {
            setCheckItem(laseStage);
        }

        mDiaryAdapter.notifyDataSetChanged();

        initHeadHeightOffset();
    }

    private void setCheckItem(String itemName) {
        for (DiarySetStageItem diarySetStageItem : diarySetStageItemList) {
            if (diarySetStageItem.getItemName().equals(itemName)) {
                diarySetStageItem.setCheck(true);
            } else {
                diarySetStageItem.setCheck(false);
            }
        }
        mDiarySetLeftMenuAdapter.notifyDataSetChanged();
    }

    private void initHeadHeightOffset() {
        if (mDiaryAdapter.isCanEdit()) {
            headHeightOffset = TDevice.dip2px(this, 96);
        } else {
            headHeightOffset = TDevice.dip2px(this, 48);
        }
    }

    private void editDiarySetInfo() {
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
        },this);
    }


    @OnClick({R.id.head_back_layout, R.id.rl_writediary, R.id.share_layout, R.id.iv_menu_open_or_close, R.id
            .toolbar_collect_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.rl_writediary:
                AddDiaryActivity.intentToAddDiary(this, null, mDiarySetInfo);
                break;
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.share_layout:
                shareDiarySet();
                break;
            case R.id.iv_menu_open_or_close:
                if (mSlidingMenu.isMenuShowing()) {
                    mSlidingMenu.showContent();
                } else {
                    mSlidingMenu.showMenu();
                }
                break;
            case R.id.toolbar_collect_layout:
                UiHelper.imageAddFavoriteAnim(ivCollect, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (mDiarySetInfo.is_my_favorite()) {
                            deleteDiarySetFavorite(diarySetId);
                            mDiarySetInfo.setIs_my_favorite(false);
                        } else {
                            addDiarySetFavorite(diarySetId);
                            mDiarySetInfo.setIs_my_favorite(true);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                break;
        }
    }

    private void addDiarySetFavorite(final String diarySetId) {
        AddDiarySetFavoriteRequest addDiarySetFavoriteRequest = new AddDiarySetFavoriteRequest();
        addDiarySetFavoriteRequest.setDiarySetid(diarySetId);

        Api.addDiarySetFavorite(addDiarySetFavoriteRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                makeTextShort(getString(R.string.str_collect_success));

                mDiarySetInfo.setIs_my_favorite(true);
                ivCollect.setBackgroundResource(R.mipmap.icon_collect_img2);
                mDiarySetInfo.setFavorite_count(mDiarySetInfo.getFavorite_count() + 1);
                tvLikeCount.setText(DiaryBusiness.covertIntCountToShow(mDiarySetInfo.getFavorite_count()));

                EventBus.getDefault().post(new CollectDiarySetEvent(diarySetId, true));
                EventBus.getDefault().post(new RefreshDiarySetInfoEvent(mDiarySetInfo));
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
                mDiarySetInfo.setIs_my_favorite(false);
            }

            @Override
            public void onNetworkError(int code) {
                mDiarySetInfo.setIs_my_favorite(false);
            }
        },this);
    }

    private void deleteDiarySetFavorite(final String diarySetId) {
        DeleteDiarySetFavoriteRequest deleteDiarySetFavoriteRequest = new DeleteDiarySetFavoriteRequest();
        deleteDiarySetFavoriteRequest.setDiarySetid(diarySetId);

        Api.deleteDairySetFavorite(deleteDiarySetFavoriteRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                makeTextShort(getString(R.string.str_cancel_collect_success));
                mDiarySetInfo.setIs_my_favorite(false);
                ivCollect.setBackgroundResource(R.mipmap.icon_collect_img1);
                mDiarySetInfo.setFavorite_count(mDiarySetInfo.getFavorite_count() - 1);
                tvLikeCount.setText(DiaryBusiness.covertIntCountToShow(mDiarySetInfo.getFavorite_count()));
                changeHeadUIShow();

                EventBus.getDefault().post(new CollectDiarySetEvent(diarySetId, false));
                EventBus.getDefault().post(new RefreshDiarySetInfoEvent(mDiarySetInfo));
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
                mDiarySetInfo.setIs_my_favorite(true);
            }

            @Override
            public void onNetworkError(int code) {
                mDiarySetInfo.setIs_my_favorite(true);
            }
        },this);
    }

    private void shareDiarySet() {
        if (mDiaryAdapter != null && mDiaryAdapter.getItemCount() > 0) {
            if (mDiarySetInfo.getDiaries() != null && mDiarySetInfo.getDiaries().size() > 0) {
                showPopwindow();
            } else {
                ToastUtil.showShortTost("您还没有发布装修日记哦");
            }
        }
    }

    private void showPopwindow() {

        DiarySetInfoAdapter.DiarySetInfoBaseInfoViewHolder viewHolder = (DiarySetInfoAdapter
                .DiarySetInfoBaseInfoViewHolder) mRecyclerView.findViewHolderForAdapterPosition(0);
        Drawable drawableDiarySetCover = viewHolder.ivDiarysetCover.getDrawable();
        Bitmap bitmapDiarySetCover = ImageUtil.drawableToBitmap(drawableDiarySetCover);

        mShareUtil.shareDiarySet(this, bitmapDiarySetCover, mDiarySetInfo.getTitle() + "（" + DiaryBusiness
                .getDiarySetDes(mDiarySetInfo) + "）", diarySetId, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        UMSsoHandler ssoHandler = SocializeConfig.getSocializeConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
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
        Api.uploadImage(uploadPicRequest, apiCallback,this);
    }

    private ApiCallback<ApiResponse<String>> uploadHeadImage = new ApiCallback<ApiResponse<String>>() {
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
            mDiarySetInfo.setCover_imageid(apiResponse.getData());
            editDiarySetInfo();
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
        return R.layout.activity_main_slidingmenu;
    }


}
