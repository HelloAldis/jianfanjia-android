package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Comment;
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
import com.jianfanjia.cn.business.DiaryBusiness;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.cn.ui.activity.common.ShowPicActivity;
import com.jianfanjia.cn.ui.activity.diary.DiaryDetailInfoActivity;
import com.jianfanjia.cn.ui.activity.diary.DiarySetInfoActivity;
import com.jianfanjia.common.tool.DateFormatTool;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;
import com.jianfanjia.common.tool.ToastUtil;

/**
 * Name: DiaryDetailInfoAdapter 日记详情
 * User: fengliang
 * Date: 2015-10-28
 * Time: 17:10
 */
public class DiaryDetailInfoAdapter extends BaseRecyclerViewAdapter<Comment> {

    private static final String TAG = DiaryDetailInfoAdapter.class.getName();

    public static final int DIARY_DETAIL_TYPE = 0;//日历详情
    public static final int COMMENT_TYPE = 1;//评论
    private DiaryInfo mDiaryInfo;
    private AddCommentListener mAddCommentListener;

    public DiaryDetailInfoAdapter(Context context, List<Comment> commentList, DiaryInfo diaryInfo) {
        super(context, commentList);
        this.mDiaryInfo = diaryInfo;
    }

    public void setAddCommentListener(AddCommentListener addCommentListener) {
        mAddCommentListener = addCommentListener;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return DIARY_DETAIL_TYPE;
        } else {
            return COMMENT_TYPE;
        }
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<Comment> list) {
        switch (getItemViewType(position)) {
            case DIARY_DETAIL_TYPE:
                bindDiary((DiaryDynamicAdapter.DiaryViewHolder) viewHolder);
                break;
            case COMMENT_TYPE:
                bindComment((CommentViewHolder) viewHolder, position - 1, list);
                break;
        }
    }

    private void bindDiary(DiaryDynamicAdapter.DiaryViewHolder diaryViewHolder) {
        final DiarySetInfo diarySetInfo = mDiaryInfo.getDiarySet();
        User author = mDiaryInfo.getAuthor();
        if (author != null) {
            if (!TextUtils.isEmpty(author.getImageid())) {
                imageShow.displayImageHeadWidthThumnailImage(context, author.getImageid(), diaryViewHolder.ivDailyHead);
            } else {
                diaryViewHolder.ivDailyHead.setImageResource(R.mipmap.icon_default_head);
            }
            if (author.get_id().equals(DataManagerNew.getInstance().getUserId())) {
                diaryViewHolder.tvDailtDelete.setVisibility(View.VISIBLE);
                diaryViewHolder.tvDailtDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDiary();
                    }
                });
            } else {
                diaryViewHolder.tvDailtDelete.setVisibility(View.GONE);
            }
        } else {
            diaryViewHolder.tvDailtDelete.setVisibility(View.GONE);
            diaryViewHolder.ivDailyHead.setImageResource(R.mipmap.icon_default_head);
        }

        if (diarySetInfo != null) {
            diaryViewHolder.tvCellName.setText(diarySetInfo.getTitle());
            diaryViewHolder.tvDiaryBaseInfo.setText(DiaryBusiness.getDiarySetDes(diarySetInfo));
        }
        diaryViewHolder.tvDailyStage.setText(DiaryBusiness.getShowDiarySectionLabel(mDiaryInfo.getSection_label()));
        diaryViewHolder.tvDailyGoingTime.setText(DateFormatTool.getHumReadDateString(mDiaryInfo.getCreate_at()));
        diaryViewHolder.tvCommentCount.setText(DiaryBusiness.getCommentCountShow(mDiaryInfo.getComment_count()));
        diaryViewHolder.tvLikeCount.setText(DiaryBusiness.getFavoriteCountShow(mDiaryInfo.getFavorite_count()));
        diaryViewHolder.tvDailyContent.setText(mDiaryInfo.getContent());
        diaryViewHolder.rlDailyCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        diaryViewHolder.rlDailyLikeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                gotoDiaryInfo(mDiaryInfo, DiaryDetailInfoActivity.intentFromBaseinfo);
            }
        });

        buildPic(diaryViewHolder, mDiaryInfo);
    }

    private void gotoDiaryInfo(DiaryInfo diaryInfo, int intentFlag) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.DIARY_INFO, diaryInfo);
        bundle.putInt(DiaryDetailInfoActivity.IntentFlag, intentFlag);
        IntentUtil.startActivity(context, DiaryDetailInfoActivity.class, bundle);
    }

    private void gotoDiarySetInfo(DiarySetInfo diarySetInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.DIARYSET_INFO, diarySetInfo);
        IntentUtil.startActivity(context, DiarySetInfoActivity.class, bundle);
    }

    private void buildPic(DiaryDynamicAdapter.DiaryViewHolder diaryViewHolder, final DiaryInfo diaryInfo) {
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

    protected void deleteDiary() {
        DeleteDiaryRequest deleteDiaryRequest = new DeleteDiaryRequest();
        deleteDiaryRequest.setDiaryid(mDiaryInfo.get_id());

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


    private void bindComment(CommentViewHolder viewHolder, int position, List<Comment> list) {
        final Comment commentInfo = list.get(position);
        CommentViewHolder holder = viewHolder;
        holder.itemContentView.setText(commentInfo.getContent());
        String userType = commentInfo.getUsertype();
        String imageid = Constant.DEFALUT_OWNER_PIC;
        String userName = "";
        if (userType.equals(Constant.IDENTITY_OWNER)) {
            holder.itemIdentityView.setText(context.getString(R.string.ower));
            holder.itemIdentityView.setTextColor(context.getResources().getColor(R.color.orange_color));
            imageid = commentInfo.getByUser().getImageid();
            userName = commentInfo.getByUser().getUsername();
        } else if (userType.equals(Constant.IDENTITY_DESIGNER)) {
            holder.itemIdentityView.setText(context.getString(R.string.designer));
            holder.itemIdentityView.setTextColor(context.getResources().getColor(R.color.blue_color));
            imageid = commentInfo.getByDesigner().getImageid();
            userName = commentInfo.getByDesigner().getUsername();
        } else if (userType.equals(Constant.IDENTITY_SUPERVISOR)) {
            holder.itemIdentityView.setText(context.getString(R.string.supervisor));
            holder.itemIdentityView.setTextColor(context.getResources().getColor(R.color.green_color));
            imageid = commentInfo.getBySupervisor().getImageid();
            userName = commentInfo.getBySupervisor().getUsername();
        }
        holder.itemNameView.setText(userName);
        holder.itemTimeView.setText(DateFormatTool.longToStringHasMini(commentInfo.getDate()));
        LogTool.d(TAG, "imageid=" + imageid);
        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(context, imageid, holder.itemHeadView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, holder.itemHeadView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddCommentListener != null) {
                    mAddCommentListener.addCommentListener(commentInfo.getByUser());
                }
            }
        });
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(int viewType) {
        View view;
        switch (viewType) {
            case DIARY_DETAIL_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_fragment_diary,
                        null);
                return new DiaryDynamicAdapter.DiaryViewHolder(view);
            case COMMENT_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_comment,
                        null);
                return new CommentViewHolder(view);
        }
        return null;
    }

    static class CommentViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.list_item_comment_username)
        TextView itemNameView;// 评论人名称
        @Bind(R.id.list_item_comment_pubtime)
        TextView itemTimeView;// 评论时间
        @Bind(R.id.list_item_comment_content)
        TextView itemContentView;// 评论内容
        @Bind(R.id.list_item_comment_userrole)
        TextView itemIdentityView;// 评论人身份
        @Bind(R.id.list_item_comment_userhead)
        ImageView itemHeadView;// 评论人头像

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface AddCommentListener {
        void addCommentListener(User toUser);
    }
}


