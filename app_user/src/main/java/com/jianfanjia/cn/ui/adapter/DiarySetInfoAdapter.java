package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
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
import com.jianfanjia.api.request.common.DeleteDiaryRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.business.DataManagerNew;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.cn.ui.activity.common.ShowPicActivity;
import com.jianfanjia.cn.ui.activity.diary.AddDiaryActivity;
import com.jianfanjia.cn.ui.activity.diary.DiaryDetailInfoActivity;
import com.jianfanjia.cn.ui.activity.diary.DiarySetInfoActivity;
import com.jianfanjia.common.tool.DateFormatTool;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;
import com.jianfanjia.common.tool.ToastUtil;


/**
 * Name: DiarySetInfoAdapter 日记集详情
 * User: zhanghao
 * Date: 2015-10-15
 * Time: 13:44
 */
public class DiarySetInfoAdapter extends BaseRecyclerViewAdapter<DiaryInfo> {

    private static final int HEAD_TYPE = 0;
    private static final int CONTENT_TYPE = 1;

    private DiarySetInfo mDiarySetInfo;

    public DiarySetInfoAdapter(Context context, List<DiaryInfo> list) {
        super(context, list);
    }

    public void setDiarySetInfo(DiarySetInfo diarySetInfo) {
        mDiarySetInfo = diarySetInfo;
        setList(mDiarySetInfo.getDiaries());
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<DiaryInfo> list) {
        switch (getItemViewType(position)) {
            case CONTENT_TYPE:
                DiaryInfo dailyInfo = list.get(position - 1);
                DiaryViewHolder holder = (DiaryViewHolder) viewHolder;
                bindContentView(position - 1, dailyInfo, holder);
                break;
            case HEAD_TYPE:
                DiarySetInfoViewHolderHead holderHead = (DiarySetInfoViewHolderHead) viewHolder;
                bindHeadView(holderHead);
                break;
        }

    }

    private void bindHeadView(DiarySetInfoViewHolderHead viewHolder) {
        viewHolder.rlWirteDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddDiary();
            }
        });
    }

    private void bindContentView(final int position, final DiaryInfo diaryInfo, DiaryViewHolder diaryViewHolder) {

        User author = diaryInfo.getAuthor();
        if (author != null) {
            if (author.get_id().equals(DataManagerNew.getInstance().getUserId())) {
                diaryViewHolder.tvDailtDelete.setVisibility(View.VISIBLE);
                diaryViewHolder.tvDailtDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDiary(position);
                    }
                });
            } else {
                diaryViewHolder.tvDailtDelete.setVisibility(View.GONE);
            }
        } else {
            diaryViewHolder.tvDailtDelete.setVisibility(View.GONE);
        }

        diaryViewHolder.tvDailyStage.setText(diaryInfo.getSection_label());
        diaryViewHolder.tvDailyGoingTime.setText(DateFormatTool.getHumReadDateString(diaryInfo.getCreate_at()));
        diaryViewHolder.tvCommentCount.setText(diaryInfo.getComment_count() + "");
        diaryViewHolder.tvLikeCount.setText(diaryInfo.getFavorite_count() + "");
        setContentText(diaryViewHolder.tvDailyContent, diaryInfo.getContent());
        diaryViewHolder.rlDailyCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDiaryInfo(diaryInfo, DiaryDetailInfoActivity.intentFromBaseinfo);
            }
        });
        diaryViewHolder.rlDailyLikeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.DIARY_INFO, diaryInfo);
        bundle.putInt(DiaryDetailInfoActivity.IntentFlag, intentFlag);
        IntentUtil.startActivity(context, DiaryDetailInfoActivity.class, bundle);
    }

    private void gotoAddDiary(){
        Bundle bundle = new Bundle();
        DiarySetInfoActivity diarySetInfoActivity = (DiarySetInfoActivity)context;
        bundle.putSerializable(IntentConstant.DIARYSET_INFO_LIST, diarySetInfoActivity.getDiarySetInfoList());
        bundle.putString(AddDiaryActivity.CURRENT_DIARYSET_TITLE,mDiarySetInfo.getTitle());
        IntentUtil.startActivity(context, AddDiaryActivity.class, bundle);
    }

    private void buildPic(DiaryViewHolder diaryViewHolder, final DiaryInfo diaryInfo) {
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
                        gotoShowBigPic(0, diaryInfo.getImages());
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
        } else if (bitmapWidth < bitmapHeight) {
            viewWidth = ((int) TDevice.getScreenWidth() - TDevice.dip2px(context, 16 + 2) * 2) / 2;
            viewHeight = (int) (viewWidth * ((float) bitmapHeight / bitmapWidth));
        } else {
            viewWidth = ((int) TDevice.getScreenWidth() - TDevice.dip2px(context, 16 + 2) * 2) / 2;
            viewHeight = viewWidth;
        }
        layoutParams.width = viewWidth;
        layoutParams.height = viewHeight;
        LogTool.d(this.getClass().getName(), "ivSinglePic viewWidth =" + viewWidth + ",viewHeight =" + viewHeight);
        ivSinglerPic.setLayoutParams(layoutParams);

        imageShow.displayScreenWidthThumnailImage(context, imageid, ivSinglerPic);
    }

    private void gotoShowBigPic(int position, List<DiaryImageDetailInfo> diaryImageDetailInfos) {
        List<String> imgs = new ArrayList<>();
        for (DiaryImageDetailInfo diaryImageDetailInfo : diaryImageDetailInfos) {
            imgs.add(diaryImageDetailInfo.getImageid());
        }

        LogTool.d(this.getClass().getName(), "position:" + position);
        Bundle showPicBundle = new Bundle();
        showPicBundle.putInt(Constant.CURRENT_POSITION, position);
        showPicBundle.putStringArrayList(Constant.IMAGE_LIST,
                (ArrayList<String>) imgs);
        IntentUtil.startActivity(context, ShowPicActivity.class, showPicBundle);
    }

    protected void buildMultiPic(int imageCount, final List<DiaryImageDetailInfo> diaryImageDetailInfos, final
    GridLayout gridLayout) {
        gridLayout.setVisibility(View.VISIBLE);

        final int count = imageCount;
        for (int i = 0; i < count; i++) {
            final ImageView pic = (ImageView) gridLayout.getChildAt(i);
            pic.setVisibility(View.VISIBLE);
            imageShow.displayScreenWidthThumnailImage(context, diaryImageDetailInfos.get(i).getImageid(), pic);

            final int finalI = i;
            pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoShowBigPic(finalI, diaryImageDetailInfos);
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

    protected void setContentText(final TextView textView, final String content) {
        textView.setText(content);
        if (TextUtils.isEmpty(content)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                           int
                                                   oldRight, int oldBottom) {
                    textView.removeOnLayoutChangeListener(this);

                    int lineCount = textView.getLineCount();
                    if (lineCount > 6) {
                        int end = textView.getLayout().getLineEnd(5);
                        LogTool.d(this.getClass().getName(), "end =" + end);
                        CharSequence charSequence = content.subSequence(0, end - 3);
                        textView.setText(Html.fromHtml(charSequence + "<font color=\"#05b9fc\">...全文</font>"));
//                        textView.requestLayout();
                    }
                }
            });
        }
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

    @Override
    public void remove(int position) {
        if (list == null) return;
        list.remove(position);
        notifyItemRemoved(position + 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD_TYPE;
        } else {
            return CONTENT_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 1 : list.size() + 1;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(int viewType) {
        View view;
        switch (viewType) {
            case CONTENT_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_diaryset_info,
                        null);
                return new DiaryViewHolder(view);
            case HEAD_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_diarydetail_write,
                        null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new DiarySetInfoViewHolderHead(view);
        }
        return null;
    }

    class DiaryViewHolder extends RecyclerViewHolderBase {

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

        @Bind(R.id.tv_comment_count)
        TextView tvCommentCount;

        @Bind(R.id.gl_diary_multiple_pic)
        GridLayout glMultiplePic;

        @Bind(R.id.iv_diary_single_pic)
        ImageView ivSinglerPic;

        @Bind(R.id.ll_diary_detail_info)
        LinearLayout llDiaryDetailInfo;

        public DiaryViewHolder(View itemView) {
            super(itemView);
        }
    }

    class DiarySetInfoViewHolderHead extends RecyclerViewHolderBase {

        @Bind(R.id.rl_writediary)
        RelativeLayout rlWirteDiary;

        public DiarySetInfoViewHolderHead(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
