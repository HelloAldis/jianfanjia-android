package com.jianfanjia.cn.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.DiaryImageDetailInfo;
import com.jianfanjia.api.model.DiaryInfo;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.api.model.User;
import com.jianfanjia.api.request.common.AddDiaryFavoriteRequest;
import com.jianfanjia.api.request.common.DeleteDiaryRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.business.DataManagerNew;
import com.jianfanjia.cn.business.DiaryBusiness;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.cn.ui.activity.common.CommonShowPicActivity;
import com.jianfanjia.cn.ui.activity.diary.AddDiaryActivity;
import com.jianfanjia.cn.ui.activity.diary.AddDiarySetActivity;
import com.jianfanjia.cn.ui.activity.diary.DiaryDetailInfoActivity;
import com.jianfanjia.cn.ui.interf.AddFavoriteCallback;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.common.tool.DateFormatTool;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;
import com.jianfanjia.common.tool.ToastUtil;
import me.iwf.photopicker.entity.AnimationRect;


/**
 * Name: DiarySetInfoAdapter 日记集详情
 * User: zhanghao
 * Date: 2015-10-15
 * Time: 13:44
 */
public class DiarySetInfoAdapter extends BaseRecyclerViewAdapter<DiaryInfo> {

    private static final int HEAD_TYPE = 0;
    private static final int CONTENT_TYPE = 1;
    private static final int WRITE_DIARY_TYPE = 2;
    public static final int FOOTER_TYPE = 3;//底部视图;

    private int headViewCount;

    private DiarySetInfo mDiarySetInfo;
    private UploadDiarySetCoverPicListener mUploadDiarySetCoverPicListener;

    private boolean isCanEdit;
    private int lastContentItemHeight;

    public DiarySetInfoAdapter(Context context, List<DiaryInfo> list) {
        super(context, list);
    }

    public void setDiarySetInfo(DiarySetInfo diarySetInfo) {
        mDiarySetInfo = diarySetInfo;
        initIsCanEdit();
    }


    private void initIsCanEdit() {
        if (mDiarySetInfo.getAuthorid().equals(DataManagerNew.getInstance().getUserId())) {
            isCanEdit = true;
            headViewCount = 2;
        } else {
            isCanEdit = false;
            headViewCount = 1;
        }
    }

    public boolean isCanEdit() {
        return isCanEdit;
    }

    public int getHeadViewCount(){
        return headViewCount;
    }

    public void setUploadDiarySetCoverPicListener(UploadDiarySetCoverPicListener uploadDiarySetCoverPicListener) {
        mUploadDiarySetCoverPicListener = uploadDiarySetCoverPicListener;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<DiaryInfo> list) {
        switch (getItemViewType(position)) {
            case CONTENT_TYPE:
                DiaryInfo dailyInfo = list.get(position - headViewCount);
                DiarySetDiaryViewHolder holder = (DiarySetDiaryViewHolder) viewHolder;
                bindContentView(position - headViewCount, dailyInfo, holder);
                if (position - headViewCount == list.size() - 1) {
                    ((DiarySetDiaryViewHolder) viewHolder).itemView.measure(0, 0);
                    LogTool.d("commentItemHeight height =" + ((DiarySetDiaryViewHolder)
                            viewHolder).itemView
                            .getMeasuredHeight());
                    lastContentItemHeight = ((DiarySetDiaryViewHolder) viewHolder).itemView
                            .getMeasuredHeight();
                }
                break;
            case HEAD_TYPE:
                DiarySetInfoBaseInfoViewHolder diarySetInfoBaseInfoViewHolder = (DiarySetInfoBaseInfoViewHolder)
                        viewHolder;
                bindBaseInfoView(diarySetInfoBaseInfoViewHolder);
                break;
            case WRITE_DIARY_TYPE:
                DiarySetInfoWriteDiaryViewHolder holderHead = (DiarySetInfoWriteDiaryViewHolder) viewHolder;
                bindHeadView(holderHead);
                break;
            case FOOTER_TYPE:
                bindFooter((DiaryDetailInfoAdapter.FooterViewHolder) viewHolder);
                break;
        }

    }

    private void bindBaseInfoView(DiarySetInfoBaseInfoViewHolder diaryViewHolder) {
        if (mDiarySetInfo == null) return;
        User author = mDiarySetInfo.getAuthor();
        if (author != null) {
            if (!TextUtils.isEmpty(author.getImageid())) {
                imageShow.displayImageHeadWidthThumnailImage(context, author.getImageid(), diaryViewHolder.ivDiaryHead);
            } else {
                diaryViewHolder.ivDiaryHead.setImageResource(R.mipmap.icon_default_head);
            }
            if (author.get_id().equals(DataManagerNew.getInstance().getUserId())) {
                diaryViewHolder.ivDiarysetEdit.setVisibility(View.VISIBLE);
                diaryViewHolder.ivDiarysetEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoEditDiarySet();
                    }
                });
                if (!TextUtils.isEmpty(mDiarySetInfo.getCover_imageid())) {//当前日记集的作者为当前用户，才可以修改封面
                    diaryViewHolder.tvDiarysetUpdateCover.setVisibility(View.GONE);
                    diaryViewHolder.ivDiarysetCover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mUploadDiarySetCoverPicListener != null) {
                                mUploadDiarySetCoverPicListener.uploadDiarySetCoverPic();
                            }
                        }
                    });
                } else {
                    diaryViewHolder.tvDiarysetUpdateCover.setVisibility(View.VISIBLE);
                    diaryViewHolder.tvDiarysetUpdateCover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mUploadDiarySetCoverPicListener != null) {
                                mUploadDiarySetCoverPicListener.uploadDiarySetCoverPic();
                            }
                        }
                    });
                }
            } else {
                diaryViewHolder.ivDiarysetEdit.setVisibility(View.GONE);
                diaryViewHolder.tvDiarysetUpdateCover.setVisibility(View.GONE);
            }
        } else {
            diaryViewHolder.ivDiarysetEdit.setVisibility(View.GONE);
            diaryViewHolder.ivDiaryHead.setImageResource(R.mipmap.icon_default_head);
            diaryViewHolder.tvDiarysetUpdateCover.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mDiarySetInfo.getCover_imageid())) {
            imageShow.displayScreenWidthThumnailImage(context, mDiarySetInfo.getCover_imageid(), diaryViewHolder
                    .ivDiarysetCover);
        } else {
            diaryViewHolder.ivDiarysetCover.setImageResource(R.mipmap.bg_diary_cover);
        }

        diaryViewHolder.tvDiarysetTitle.setText(mDiarySetInfo.getTitle());
        diaryViewHolder.tvDiarysetGoingTime.setText(DiaryBusiness.getDiarySetDes(mDiarySetInfo));
    }

    private void bindFooter(DiaryDetailInfoAdapter.FooterViewHolder viewHolder) {
        /*if (list.size() == 0) {
            viewHolder.tvFooterLoadNoMore.setText("当前还没有任何日记");
        } else {
            viewHolder.tvFooterLoadNoMore.setText("日记已加载完毕");
        }*/
        int totalHeight = (int) TDevice.getScreenHeight() - TDevice.getStatusBarHeight(context) - TDevice.dip2px
                (context, 48);
        LogTool.d("totalHeight height =" + totalHeight);
        int defaultFootHeight = TDevice.dip2px(context, 96);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.llFooterLoadNoMore
                .getLayoutParams();
        if (lastContentItemHeight - totalHeight >= 0) {
            layoutParams.height = defaultFootHeight;
            viewHolder.llFooterLoadNoMore.setLayoutParams(layoutParams);
        } else if (lastContentItemHeight - totalHeight > -defaultFootHeight && lastContentItemHeight - totalHeight <
                0) {
            layoutParams.height = defaultFootHeight + lastContentItemHeight - totalHeight;
        } else {
            layoutParams.height = totalHeight - lastContentItemHeight;
        }
        viewHolder.llFooterLoadNoMore.setLayoutParams(layoutParams);
    }

    private void bindHeadView(DiarySetInfoWriteDiaryViewHolder viewHolder) {
        viewHolder.rlWirteDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDiaryActivity.intentToAddDiary(context, null, mDiarySetInfo);
            }
        });
    }

    private void bindContentView(final int position, final DiaryInfo diaryInfo, DiarySetDiaryViewHolder
            diarySetDiaryViewHolder) {
        if (diaryInfo.getAuthorid().equals(DataManagerNew.getInstance().getUserId())) {
            diarySetDiaryViewHolder.tvDailtDelete.setVisibility(View.VISIBLE);
            diarySetDiaryViewHolder.tvDailtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    deleteDiary(position);
                    showTipDialog(context, position);
                }
            });
        } else {
            diarySetDiaryViewHolder.tvDailtDelete.setVisibility(View.GONE);
        }

        LogTool.d(diaryInfo.getContent());
        diarySetDiaryViewHolder.tvDailyStage.setText(DiaryBusiness.getShowDiarySectionLabel(diaryInfo
                .getSection_label()));
        diarySetDiaryViewHolder.tvDailyGoingTime.setText(DateFormatTool.covertLongToStringHasMiniAndChinese(diaryInfo
                .getCreate_at()));
        diarySetDiaryViewHolder.tvCommentCount.setText(DiaryBusiness.getCommentCountShow(diaryInfo.getComment_count()));
        diarySetDiaryViewHolder.tvLikeCount.setText(DiaryBusiness.getFavoriteCountShow(diaryInfo.getFavorite_count()));
        diarySetDiaryViewHolder.tvDailyContent.setText(diaryInfo.getContent());
        diarySetDiaryViewHolder.rlDailyCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDiaryInfo(diaryInfo, DiaryDetailInfoActivity.intentFromComment);
            }
        });
        DiaryBusiness.setFavoriteAction(diarySetDiaryViewHolder.tvLikeIcon, diarySetDiaryViewHolder
                .rlDailyLikeLayout, diaryInfo
                .is_my_favorite(), new AddFavoriteCallback() {
            @Override
            public void addFavoriteAction() {
                addFavorite(position);
            }
        });


        diarySetDiaryViewHolder.llDiaryDetailInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDiaryInfo(diaryInfo, DiaryDetailInfoActivity.intentFromBaseinfo);
            }
        });

        buildPic(diarySetDiaryViewHolder, diaryInfo);
    }


    private void gotoDiaryInfo(DiaryInfo diaryInfo, int intentFlag) {
        diaryInfo.setDiarySet(mDiarySetInfo);
        diaryInfo.setAuthor(mDiarySetInfo.getAuthor());
        DiaryDetailInfoActivity.intentToDiaryDetailInfo(context, diaryInfo, intentFlag, null);
    }

    protected void gotoEditDiarySet() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.DIARYSET_INFO, mDiarySetInfo);
        IntentUtil.startActivity(context, AddDiarySetActivity.class, bundle);
    }

    private void buildPic(final DiarySetDiaryViewHolder diarySetDiaryViewHolder, final DiaryInfo diaryInfo) {
        int imageCount;
        if (diaryInfo.getImages() == null || (imageCount = diaryInfo.getImages().size()) == 0) {
            diarySetDiaryViewHolder.glMultiplePic.setVisibility(View.GONE);
            diarySetDiaryViewHolder.ivSinglerPic.setVisibility(View.GONE);
        } else {
            if (imageCount == 1) {
                diarySetDiaryViewHolder.glMultiplePic.setVisibility(View.GONE);
                buildSinglePic(diaryInfo.getImages().get(0), diarySetDiaryViewHolder.ivSinglerPic);
                diarySetDiaryViewHolder.ivSinglerPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<AnimationRect> animationRectArrayList
                                = new ArrayList<>();
                        animationRectArrayList.add(AnimationRect.buildFromImageView(diarySetDiaryViewHolder
                                .ivSinglerPic));
                        gotoShowBigPic(0, diaryInfo.getImages(), animationRectArrayList);
                    }
                });
            } else {
                diarySetDiaryViewHolder.ivSinglerPic.setVisibility(View.GONE);
                buildMultiPic(imageCount, diaryInfo.getImages(), diarySetDiaryViewHolder.glMultiplePic);
            }
        }
    }

    protected void buildSinglePic(DiaryImageDetailInfo diaryImageDetailInfo, ImageView ivSinglerPic) {
        ivSinglerPic.setVisibility(View.VISIBLE);
        String imageid = diaryImageDetailInfo.getImageid();
        int bitmapWidth = diaryImageDetailInfo.getWidth();
        int bitmapHeight = diaryImageDetailInfo.getHeight();

        int viewWidth;
        int viewHeight;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivSinglerPic.getLayoutParams();
        if (bitmapWidth > bitmapHeight) {
            viewWidth = ((int) TDevice.getScreenWidth() - TDevice.dip2px(context, 16 + 2) * 2) * 2 / 3;
            viewHeight = (int) (viewWidth * ((float) bitmapHeight / bitmapWidth));
        } else if (bitmapWidth < bitmapHeight) {
            viewWidth = ((int) TDevice.getScreenWidth() - TDevice.dip2px(context, 16 + 2) * 2) / 2;
            viewHeight = (int) (viewWidth * ((float) bitmapHeight / bitmapWidth));
        } else {
            viewWidth = ((int) TDevice.getScreenWidth() - TDevice.dip2px(context, 16 + 2) * 2) / 2;
            viewHeight = viewWidth;
        }
        layoutParams.width = viewWidth;
        layoutParams.height = viewHeight;
        LogTool.d("ivSinglePic viewWidth =" + viewWidth + ",viewHeight =" + viewHeight);
        ivSinglerPic.setLayoutParams(layoutParams);

        imageShow.displayScreenWidthThumnailImage(context, imageid, ivSinglerPic);
    }

    private void gotoShowBigPic(int position, List<DiaryImageDetailInfo> diaryImageDetailInfos, List<AnimationRect>
            animationRectList) {
        List<String> imgs = new ArrayList<>();
        for (DiaryImageDetailInfo diaryImageDetailInfo : diaryImageDetailInfos) {
            imgs.add(diaryImageDetailInfo.getImageid());
        }
        LogTool.d("position:" + position);
        CommonShowPicActivity.intentTo(context, (ArrayList<String>) imgs, (ArrayList<AnimationRect>)
                animationRectList, position);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    protected void buildMultiPic(int imageCount, final List<DiaryImageDetailInfo> diaryImageDetailInfos, final
    GridLayout gridLayout) {
        gridLayout.setVisibility(View.VISIBLE);

        final int count = imageCount > 9 ? 9 : imageCount;
        for (int i = 0; i < count; i++) {
            final ImageView pic = (ImageView) gridLayout.getChildAt(i);
            pic.setVisibility(View.VISIBLE);
            imageShow.displayScreenWidthThumnailImage(context, diaryImageDetailInfos.get(i).getImageid(), pic);

            final int finalI = i;
            pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<AnimationRect> animationRectArrayList
                            = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        ImageView imageView = (ImageView) gridLayout.getChildAt(i);
                        LogTool.d("view left position =" + imageView.getLeft());
                        if (imageView.getVisibility() == View.VISIBLE) {
                            AnimationRect rect = AnimationRect.buildFromImageView(imageView);
                            LogTool.d("left position =" + rect.imageViewEntireRect.left);
                            animationRectArrayList.add(rect);
                        }
                    }
                    gotoShowBigPic(finalI, diaryImageDetailInfos, animationRectArrayList);
                }
            });
        }

        if (count < 9) {
            ImageView pic;
            switch (count) {
                case 8:
                    pic = (ImageView) gridLayout.getChildAt(8);
                    pic.setVisibility(View.INVISIBLE);
                    break;
                case 7:
                    for (int i = 8; i > 6; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 6:
                    for (int i = 8; i > 5; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }

                    break;
                case 5:
                    for (int i = 8; i > 5; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }
                    pic = (ImageView) gridLayout.getChildAt(5);
                    pic.setVisibility(View.INVISIBLE);
                    break;
                case 4:
                    for (int i = 8; i > 5; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }
                    pic = (ImageView) gridLayout.getChildAt(5);
                    pic.setVisibility(View.INVISIBLE);
                    pic = (ImageView) gridLayout.getChildAt(4);
                    pic.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    for (int i = 8; i > 2; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    for (int i = 8; i > 2; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }
                    pic = (ImageView) gridLayout.getChildAt(2);
                    pic.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    private void addFavorite(final int position) {
        final DiaryInfo diaryInfo = list.get(position);

        AddDiaryFavoriteRequest addDiaryFavoriteRequest = new AddDiaryFavoriteRequest();
        addDiaryFavoriteRequest.setDiaryid(diaryInfo.get_id());

        Api.addDiaryFavorite(addDiaryFavoriteRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                diaryInfo.setIs_my_favorite(true);
                diaryInfo.setFavorite_count(diaryInfo.getFavorite_count() + 1);
                notifyItemChanged(position + headViewCount);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        },context);
    }

    protected void showTipDialog(Context context, final int position) {
        CommonDialog commonDialog = DialogHelper.getPinterestDialogCancelable(context);
        commonDialog.setTitle(R.string.tip_delete_diary_title);
        commonDialog.setMessage(context.getString(R.string.tip_delete_diary));
        commonDialog.setNegativeButton(context.getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        commonDialog.setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteDiary(position);
            }
        });
        commonDialog.show();
    }


    protected void deleteDiary(final int position) {
        DeleteDiaryRequest deleteDiaryRequest = new DeleteDiaryRequest();
        deleteDiaryRequest.setDiaryid(list.get(position).get_id());

        Api.deleteDiaryInfo(deleteDiaryRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                Hud.show(context);
            }

            @Override
            public void onHttpDone() {
                Hud.dismiss();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                list.remove(position);
                notifyItemRemoved(position + headViewCount);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                ToastUtil.showShortTost(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                ToastUtil.showShortTost(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        },context);
    }

    @Override
    public void remove(int position) {
        if (list == null) return;
        list.remove(position);
        notifyItemRemoved(position + headViewCount);
    }

    @Override
    public int getItemViewType(int position) {
        if (isCanEdit) {
            if (position == 0) {
                return HEAD_TYPE;
            } else if (position == 1) {
                return WRITE_DIARY_TYPE;
            } else if (position >= 2 && position < 2 + list.size()) {
                return CONTENT_TYPE;
            } else if (position == list.size() + 2) {
                return FOOTER_TYPE;
            }
        } else {
            if (position == 0) {
                return HEAD_TYPE;
            } else if (position >= 1 && position < 1 + list.size()) {
                return CONTENT_TYPE;
            } else if (position == list.size() + 1) {
                return FOOTER_TYPE;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return null == list ? headViewCount : list.size() + headViewCount + 1;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(int viewType) {
        View view;
        switch (viewType) {
            case CONTENT_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_diaryset_info,
                        null);
                return new DiarySetDiaryViewHolder(view);
            case HEAD_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_diaryset_baseinfo,
                        null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new DiarySetInfoBaseInfoViewHolder(view);
            case WRITE_DIARY_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_diarydetail_write,
                        null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new DiarySetInfoWriteDiaryViewHolder(view);
            case FOOTER_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_diaryinfo_footer_space, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new DiaryDetailInfoAdapter.FooterViewHolder(view);
        }
        return null;
    }

    class DiarySetDiaryViewHolder extends RecyclerViewHolderBase {

        @Bind(R.id.ltm_diary_stage)
        TextView tvDailyStage;

        @Bind(R.id.ltm_diary_delte)
        TextView tvDailtDelete;

        @Bind(R.id.ltm_diary_content)
        TextView tvDailyContent;

        @Bind(R.id.ltm_diary_goingtime)
        TextView tvDailyGoingTime;

        @Bind(R.id.ltm_diary_comment_layout)
        RelativeLayout rlDailyCommentLayout;

        @Bind(R.id.ltm_diary_like_layout)
        RelativeLayout rlDailyLikeLayout;

        @Bind(R.id.tv_like_count)
        TextView tvLikeCount;

        @Bind(R.id.tv_like_icon)
        ImageView tvLikeIcon;

        @Bind(R.id.tv_comment_count)
        TextView tvCommentCount;

        @Bind(R.id.gl_diary_multiple_pic)
        GridLayout glMultiplePic;

        @Bind(R.id.iv_diary_single_pic)
        ImageView ivSinglerPic;

        @Bind(R.id.ll_diary_detail_info)
        LinearLayout llDiaryDetailInfo;

        public DiarySetDiaryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class DiarySetInfoWriteDiaryViewHolder extends RecyclerViewHolderBase {

        @Bind(R.id.rl_writediary)
        RelativeLayout rlWirteDiary;

        public DiarySetInfoWriteDiaryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class DiarySetInfoBaseInfoViewHolder extends RecyclerViewHolderBase {

        @Bind(R.id.iv_diaryset_cover_pic)
        public ImageView ivDiarysetCover;

        @Bind(R.id.tv_diaryset_update_cover)
        TextView tvDiarysetUpdateCover;

        @Bind(R.id.tv_diaryset_title)
        TextView tvDiarysetTitle;

        @Bind(R.id.tv_diaryset_goingtime)
        TextView tvDiarysetGoingTime;

        @Bind(R.id.iv_diaryset_edit)
        ImageView ivDiarysetEdit;

        @Bind(R.id.iv_diary_head)
        ImageView ivDiaryHead;

        public DiarySetInfoBaseInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface UploadDiarySetCoverPicListener {
        void uploadDiarySetCoverPic();
    }


}
