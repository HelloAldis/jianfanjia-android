package com.jianfanjia.cn.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import com.jianfanjia.cn.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.business.DataManagerNew;
import com.jianfanjia.cn.business.DiaryBusiness;
import com.jianfanjia.cn.ui.activity.common.CommonShowPicActivity;
import com.jianfanjia.cn.ui.activity.diary.DiaryDetailInfoActivity;
import com.jianfanjia.cn.ui.activity.diary.DiarySetInfoActivity;
import com.jianfanjia.cn.ui.interf.AddFavoriteCallback;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.common.tool.DateFormatTool;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;
import com.jianfanjia.common.tool.ToastUtil;
import me.iwf.photopicker.entity.AnimationRect;

/**
 * Description: com.jianfanjia.cn.ui.adapter 日记动态
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-13 10:50
 */
public class DiaryDynamicAdapter extends BaseLoadMoreRecycleAdapter<DiaryInfo> {


    public DiaryDynamicAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_fragment_diary, null);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DiaryViewHolder diaryViewHolder = (DiaryViewHolder) holder;
        final DiaryInfo diaryInfo = mDatas.get(position);
        LogTool.d(this.getClass().getName(), "diaryInfo =" + diaryInfo);

        final DiarySetInfo diarySetInfo = diaryInfo.getDiarySet();
        User author = diaryInfo.getAuthor();
        if (author != null) {
            if (!TextUtils.isEmpty(author.getImageid())) {
                imageShow.displayImageHeadWidthThumnailImage(context, author.getImageid(), diaryViewHolder.ivDiaryHead);
            } else {
                diaryViewHolder.ivDiaryHead.setImageResource(R.mipmap.icon_default_head);
            }
            if (author.get_id().equals(DataManagerNew.getInstance().getUserId())) {
                diaryViewHolder.tvDiaryDelete.setVisibility(View.VISIBLE);
                diaryViewHolder.tvDiaryDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        deleteDiary(position);
                        showTipDialog(context, position);
                    }
                });
            } else {
                diaryViewHolder.tvDiaryDelete.setVisibility(View.GONE);
            }
        } else {
            diaryViewHolder.tvDiaryDelete.setVisibility(View.GONE);
            diaryViewHolder.ivDiaryHead.setImageResource(R.mipmap.icon_default_head);
        }

        if (diarySetInfo != null) {
            diaryViewHolder.tvDiarySetTitle.setText(diarySetInfo.getTitle());
            diaryViewHolder.tvDiaryBaseInfo.setText(DiaryBusiness.getDiarySetDes(diarySetInfo));
        }
        diaryViewHolder.tvDiaryStage.setText(DiaryBusiness.getShowDiarySectionLabel(diaryInfo.getSection_label()));
        diaryViewHolder.tvDiaryGoingTime.setText(DateFormatTool.getHumReadDateString(diaryInfo.getCreate_at()));
        diaryViewHolder.tvCommentCount.setText(DiaryBusiness.getCommentCountShow(diaryInfo.getComment_count()));
        diaryViewHolder.tvLikeCount.setText(DiaryBusiness.getFavoriteCountShow(diaryInfo.getFavorite_count()));
        DiaryBusiness.setFavoriteAction(diaryViewHolder.tvLikeIcon, diaryViewHolder.rlDiaryLikeLayout, diaryInfo
                .is_my_favorite(), new AddFavoriteCallback() {
            @Override
            public void addFavoriteAction() {
                addFavorite(position);
            }
        });

        setContentText(diaryViewHolder.tvDiaryContent, diaryInfo);
        diaryViewHolder.rlDiaryCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDiaryInfo(diaryInfo, DiaryDetailInfoActivity.intentFromComment);
            }
        });
        diaryViewHolder.llDiarySet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDiarySetInfo(diarySetInfo);
            }
        });
        diaryViewHolder.llDiaryDetailInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDiaryInfo(diaryInfo, DiaryDetailInfoActivity.intentFromBaseinfo);
            }
        });

        buildPic(diaryViewHolder, diaryInfo);
    }

    private void gotoDiaryInfo(DiaryInfo diaryInfo, int intentFlag) {
        DiaryDetailInfoActivity.intentToDiaryDetailInfo(context, diaryInfo, intentFlag, null);
    }

    private void gotoDiarySetInfo(DiarySetInfo diarySetInfo) {
        DiarySetInfoActivity.intentToDiarySet(context, diarySetInfo);
    }

    private void buildPic(final DiaryViewHolder diaryViewHolder, final DiaryInfo diaryInfo) {
        int imageCount;
        if (diaryInfo.getImages() == null || (imageCount = diaryInfo.getImages().size()) == 0) {
            diaryViewHolder.glMultiplePic.setVisibility(View.GONE);
            diaryViewHolder.ivSinglerPic.setVisibility(View.GONE);
        } else {
            if (imageCount == 1) {
                diaryViewHolder.glMultiplePic.setVisibility(View.GONE);
                buildSinglePic(diaryInfo.getImages().get(0), diaryViewHolder.ivSinglerPic);
                diaryViewHolder.ivSinglerPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<AnimationRect> animationRectArrayList
                                = new ArrayList<>();
                        animationRectArrayList.add(AnimationRect.buildFromImageView(diaryViewHolder.ivSinglerPic));
                        gotoShowBigPic(0, diaryInfo.getImages(), animationRectArrayList);
                    }
                });
            } else {
                diaryViewHolder.ivSinglerPic.setVisibility(View.GONE);
                buildMultiPic(imageCount, diaryInfo.getImages(), diaryViewHolder.glMultiplePic);
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
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if (bitmapWidth > bitmapHeight) {
            viewWidth = ((int) TDevice.getScreenWidth() - TDevice.dip2px(context, 16 + 2) * 2) * 2 / 3;
            viewHeight = (int) (viewWidth * ((float) bitmapHeight / bitmapWidth));
        } else {
            viewWidth = ((int) TDevice.getScreenWidth() - TDevice.dip2px(context, 16 + 2) * 2) / 2;
            viewHeight = (int) (viewWidth * ((float) bitmapHeight / bitmapWidth));
        }
        layoutParams.width = viewWidth;
        layoutParams.height = viewHeight;
        LogTool.d(this.getClass().getName(), "ivSinglePic viewWidth =" + viewWidth + ",viewHeight =" + viewHeight);
        ivSinglerPic.setLayoutParams(layoutParams);

        imageShow.displayThumbnailImageByHeightAndWidth(imageid, ivSinglerPic, viewWidth, viewHeight);
    }

    private void gotoShowBigPic(int position, List<DiaryImageDetailInfo> diaryImageDetailInfos, List<AnimationRect>
            animationRectList) {
        List<String> imgs = new ArrayList<>();
        for (DiaryImageDetailInfo diaryImageDetailInfo : diaryImageDetailInfos) {
            imgs.add(diaryImageDetailInfo.getImageid());
        }
        LogTool.d(this.getClass().getName(), "position:" + position);
        CommonShowPicActivity.intentTo(context,(ArrayList<String>) imgs, (ArrayList<AnimationRect>)
                animationRectList, position);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    protected void buildMultiPic(int imageCount, final List<DiaryImageDetailInfo> diaryImageDetailInfos, final
    GridLayout gridLayout) {
        gridLayout.setVisibility(View.VISIBLE);

        final int count = imageCount > 9 ? 9 : imageCount;

        for (int i = 0; i < count; i++) {
            ImageView pic = (ImageView) gridLayout.getChildAt(i);
            pic.setVisibility(View.VISIBLE);
            int viewWidth = TDevice.dip2px(context, 100);
            int bitmapWidth = diaryImageDetailInfos.get(i).getWidth();
            int bitmapHeight = diaryImageDetailInfos.get(i).getHeight();
            int loadThumbnailWidth;
            int loadThumbnailHeight;
            if (bitmapHeight < bitmapWidth) {
                loadThumbnailWidth = viewWidth;
                loadThumbnailHeight = (int) ((((float) bitmapHeight) / bitmapWidth) * viewWidth);
            } else {
                loadThumbnailHeight = viewWidth;
                loadThumbnailWidth = (int) ((((float) bitmapWidth) / bitmapHeight) * viewWidth);
            }
            LogTool.d(this.getClass().getName(), "bitmapWidth =" + bitmapWidth + ",bitmapHeight =" + bitmapHeight);
            LogTool.d(this.getClass().getName(), "loadThumbnailWidth =" + loadThumbnailWidth + ",loadThumbnailHeight " +
                    "=" + loadThumbnailHeight);
            imageShow.displayThumbnailImageByHeightAndWidth(diaryImageDetailInfos.get(i).getImageid(), pic,
                    loadThumbnailWidth, loadThumbnailHeight);
//            imageShow.displayScreenWidthThumnailImage(context,diaryImageDetailInfos.get(i).getImageid(),pic);

            final int finalI = i;
            pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<AnimationRect> animationRectArrayList
                            = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        ImageView imageView = (ImageView) gridLayout.getChildAt(i);
                        LogTool.d(this.getClass().getName(),"view left position =" + imageView.getLeft());
                        if (imageView.getVisibility() == View.VISIBLE) {
                            AnimationRect rect = AnimationRect.buildFromImageView(imageView);
                            LogTool.d(this.getClass().getName(),"left position =" + rect.imageViewEntireRect.left);
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

    protected void setContentText(final TextView textView, final DiaryInfo diaryInfo) {
        if (diaryInfo.getShowHeight() == 0) {
            String content = diaryInfo.getContent();
            StaticLayout staticLayout = new StaticLayout(content, textView.getPaint(), (int) TDevice.getScreenWidth()
                    - TDevice.dip2px(context, 32), Layout
                    .Alignment.ALIGN_NORMAL, 1.5f, 0, false);
            if (staticLayout.getLineCount() > 5) {
                int end = staticLayout.getLineEnd(5);
                LogTool.d(this.getClass().getName(), "end =" + end);
                String endElp = "...     全文";
                CharSequence charSequence = content.subSequence(0, end - endElp.length());
                LogTool.d(this.getClass().getName(), charSequence.toString() + ",");

                String showContent = charSequence + endElp;
                SpannableString spannableStringBuilder = new SpannableString(showContent);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R
                                .color.blue_color)),
                        showContent.length() - 2, showContent.length(), Spannable
                                .SPAN_EXCLUSIVE_EXCLUSIVE);

                textView.setText(spannableStringBuilder);
                diaryInfo.setShowContent(spannableStringBuilder);
            } else {
                textView.setText(content);
                diaryInfo.setShowContent(new SpannableString(content));
            }
            textView.post(new Runnable() {
                @Override
                public void run() {
                    diaryInfo.setShowHeight(textView.getMeasuredHeight());
                    LogTool.d(this.getClass().getName(), "textView.getMeasuredHeight() =" + textView
                            .getMeasuredHeight());
                }
            });
        } else {
            textView.setText(diaryInfo.getShowContent());
        }
    }

    private void addFavorite(final int position) {
        final DiaryInfo diaryInfo = mDatas.get(position);

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
                notifyItemChanged(position);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        });
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
        deleteDiaryRequest.setDiaryid(mDatas.get(position).get_id());

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
                mDatas.remove(position);
                notifyItemRemoved(position);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                ToastUtil.showShortTost(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                ToastUtil.showShortTost(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    static class DiaryViewHolder extends RecyclerViewHolderBase {

        @Bind(R.id.ll_rootview)
        LinearLayout llRootView;

        @Bind(R.id.diary_head)
        ImageView ivDiaryHead;

        @Bind(R.id.ltm_diary_stage)
        TextView tvDiaryStage;

        @Bind(R.id.ltm_diaryset_title)
        TextView tvDiarySetTitle;

        @Bind(R.id.ltm_diary_delte)
        TextView tvDiaryDelete;

        @Bind(R.id.ltm_diary_baseinfo)
        TextView tvDiaryBaseInfo;

        @Bind(R.id.ltm_diary_content)
        TextView tvDiaryContent;

        @Bind(R.id.ltm_diary_goingtime)
        TextView tvDiaryGoingTime;

        @Bind(R.id.ltm_diary_comment_layout)
        RelativeLayout rlDiaryCommentLayout;

        @Bind(R.id.ltm_diary_like_layout)
        RelativeLayout rlDiaryLikeLayout;

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

        @Bind(R.id.ll_diaryset_baseinfo)
        LinearLayout llDiarySet;

        @Bind(R.id.ll_diary_detail_info)
        LinearLayout llDiaryDetailInfo;

        public DiaryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
