package com.jianfanjia.cn.ui.activity.diary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Comment;
import com.jianfanjia.api.model.CommentList;
import com.jianfanjia.api.model.DiaryInfo;
import com.jianfanjia.api.model.User;
import com.jianfanjia.api.request.common.AddCommentRequest;
import com.jianfanjia.api.request.common.GetCommentsRequest;
import com.jianfanjia.api.request.guest.GetDiaryInfoRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.cn.ui.Event.RefreshDiaryInfoEvent;
import com.jianfanjia.cn.ui.adapter.DiaryDetailInfoAdapter;
import com.jianfanjia.cn.ui.interf.PauseOnScrollListenter;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;
import com.nostra13.universalimageloader.core.ImageLoader;
import de.greenrobot.event.EventBus;

/**
 * Description: com.jianfanjia.cn.ui.activity.diary
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-14 11:05
 */
public class DiaryDetailInfoActivity extends BaseSwipeBackActivity {

    public static final String INTENT_FLAG = "intent_flag";
    public static final String BY_USER = "byUser";
    public static final int intentFromComment = 0;
    public static final int intentFromBaseinfo = 1;
    private static final String TAG = DiaryDetailInfoActivity.class.getName();

    @Bind(R.id.mainhv_diary)
    MainHeadView mMainHeadView;

    @Bind(R.id.diary_recycleview)
    RecyclerView mRecyclerView;

    @Bind(R.id.add_comment)
    protected EditText commentEdit = null;

    @Bind(R.id.btn_send)
    protected Button btnSend = null;

    private List<Comment> mCommentList = new ArrayList<>();

    private DiaryDetailInfoAdapter mDiaryDetailInfoAdapter;
    private DiaryInfo mDiaryInfo;
    private String diaryId;
    private String to;//评论发送给谁
    private String replayHint = "";//回复谁
    private String content;//回复的类容
    private int intentFrom;
    private User byUser;//我被谁评论过
    private String toCommentId;
    private Comment byComment;

    public static void intentToDiaryDetailInfo(Context context, DiaryInfo diaryInfo, int
            intentFromFlag, User byUser) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.DIARY_INFO, diaryInfo);
        bundle.putSerializable(INTENT_FLAG, intentFromFlag);
        bundle.putSerializable(BY_USER, byUser);
        IntentUtil.startActivity(context, DiaryDetailInfoActivity.class, bundle);
    }

    public static void intentToDiaryDetailInfoByComment(Context context, DiaryInfo diaryInfo, int
            intentFromFlag, User byUser, Comment byComment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.DIARY_INFO, diaryInfo);
        bundle.putSerializable(INTENT_FLAG, intentFromFlag);
        bundle.putSerializable(BY_USER, byUser);
        bundle.putSerializable(IntentConstant.COMMENT_INFO, byComment);
        IntentUtil.startActivity(context, DiaryDetailInfoActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        mDiaryInfo = (DiaryInfo) intent.getSerializableExtra(IntentConstant.DIARY_INFO);
        intentFrom = intent.getIntExtra(INTENT_FLAG, -1);
        byUser = (User) intent.getSerializableExtra(BY_USER);
        byComment = (Comment) intent.getSerializableExtra(IntentConstant.COMMENT_INFO);
        if (mDiaryInfo != null) {
            diaryId = mDiaryInfo.get_id();
        }
        getDiaryInfo(diaryId);
    }

    private void initView() {
        initMainView();
        btnSend.setEnabled(false);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            int topSpace = TDevice.dip2px(DiaryDetailInfoActivity.this, 10);
            int space = TDevice.dip2px(DiaryDetailInfoActivity.this, 1);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = 0;
                outRect.right = 0;
                if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1) {
                    outRect.top = topSpace;
                } else {
                    outRect.top = space;
                }
            }
        });
        mRecyclerView.addOnScrollListener(new PauseOnScrollListenter(ImageLoader.getInstance(), true, true));
    }

    @OnTextChanged(R.id.add_comment)
    public void onTextChanged(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            btnSend.setEnabled(false);
        } else {
            btnSend.setEnabled(true);
        }
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.diary_content));
    }

    //获取留言评论并标记为已读
    private void getCommentList(String topicid, int from, int limit) {
        GetCommentsRequest request = new GetCommentsRequest();
        request.setTopicid(topicid);
        request.setFrom(from);
        request.setLimit(limit);
        Api.getCommentList(request, getCommentCallback);
    }

    private void getDiaryInfo(String diaryid) {
        GetDiaryInfoRequest getDiaryInfoRequest = new GetDiaryInfoRequest();
        getDiaryInfoRequest.setDiaryid(diaryid);

        Api.getDiaryInfo(getDiaryInfoRequest, new ApiCallback<ApiResponse<DiaryInfo>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
            }

            @Override
            public void onSuccess(ApiResponse<DiaryInfo> apiResponse) {
                setInitData(apiResponse.getData());
            }

            @Override
            public void onFailed(ApiResponse<DiaryInfo> apiResponse) {
                hideWaitDialog();
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                hideWaitDialog();
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    private void setInitData(DiaryInfo diaryInfo) {
        if (diaryInfo.is_deleted()) {
            deleteDiarySuccess();
        } else {
            refreshDiarySomeData(diaryInfo);
            getCommentList(mDiaryInfo.get_id(), 0, 1000);
        }
    }

    private void refreshDiarySomeData(DiaryInfo diaryInfo) {
        mDiaryInfo.setComment_count(diaryInfo.getComment_count());
        mDiaryInfo.setIs_my_favorite(diaryInfo.is_my_favorite());
        mDiaryInfo.setFavorite_count(diaryInfo.getFavorite_count());
        mDiaryInfo.setView_count(diaryInfo.getView_count());

        EventBus.getDefault().post(new RefreshDiaryInfoEvent(mDiaryInfo));
    }

    public void deleteDiarySuccess() {
        mDiaryInfo.setIs_deleted(true);
        if (!mDiaryInfo.getAuthor().get_id().equals(dataManager.getUserId())) {
            makeTextShort(getString(R.string.tip_diary_delte));
        }
        EventBus.getDefault().post(new RefreshDiaryInfoEvent(mDiaryInfo));
        appManager.finishActivity(this);
    }

    private ApiCallback<ApiResponse<CommentList>> getCommentCallback = new ApiCallback<ApiResponse<CommentList>>() {
        @Override
        public void onPreLoad() {
        }

        @Override
        public void onHttpDone() {
            hideWaitDialog();
        }

        @Override
        public void onSuccess(ApiResponse<CommentList> apiResponse) {
            CommentList commentList = apiResponse.getData();
            LogTool.d(TAG, "commentList:" + commentList);
            if (null != commentList) {
                mCommentList = commentList.getComments();
                mDiaryDetailInfoAdapter = new DiaryDetailInfoAdapter(DiaryDetailInfoActivity.this, mCommentList,
                        mDiaryInfo, mRecyclerView);
                mDiaryDetailInfoAdapter.setAddCommentListener(new DiaryDetailInfoAdapter.AddCommentListener() {
                    @Override
                    public void addCommentListener(User toUser, Comment comment) {
                        prepareAddComment(toUser, comment);
                    }
                });
                mRecyclerView.setAdapter(mDiaryDetailInfoAdapter);
                initViewShowAndData();
            }
        }

        @Override
        public void onFailed(ApiResponse<CommentList> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
        }
    };

    public void showSoftKeyBoard() {
        commentEdit.requestFocus();
        mRecyclerView.scrollToPosition(1);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(commentEdit, InputMethodManager.SHOW_FORCED);
    }

    public void hideSoftKeyBorad() {
        commentEdit.clearFocus();
        commentEdit.setText("");
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(commentEdit.getWindowToken(), 0);
    }

    private void initViewShowAndData() {
        if (intentFrom == intentFromComment) {
            if (byUser != null) {
                to = byUser.get_id();
                if (!TextUtils.isEmpty(byUser.getUsername())) {
                    replayHint = "回复 " + byUser.getUsername() + " ：";
                }
                commentEdit.setHint(replayHint);
            } else {
                to = mDiaryInfo.getAuthorid();
            }
            if (byComment != null) {
                toCommentId = byComment.get_id();
            }
            showSoftKeyBoard();
        } else {
            to = mDiaryInfo.getAuthorid();//默认是回复作者的
        }
    }


    private void prepareAddComment(User toUser, Comment comment) {
        if (toUser != null && !toUser.get_id().equals
                (dataManager.getUserId())) {
            to = toUser.get_id();
            if (!TextUtils.isEmpty(toUser.getUsername())) {
                replayHint = "回复 " + toUser.getUsername() + " ：";
            }
            showSoftKeyBoard();
        } else {
            to = mDiaryInfo.getAuthor().get_id();
            replayHint = "";
        }
        toCommentId = comment.get_id();
        commentEdit.setHint(replayHint);
        LogTool.d(this.getClass().getName(), " to = " + to);
    }

    @OnClick({R.id.btn_send, R.id.head_back_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                content = replayHint + commentEdit.getText().toString().trim();
                addComment();
                break;
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
        }
    }

    //添加评论
    private void addComment() {
        AddCommentRequest request = new AddCommentRequest();
        request.setTopicid(diaryId);
        request.setTopictype(Global.TOPIC_DIARY);
        request.setContent(content);
        request.setTo_userid(to);
        request.setTo_commentid(toCommentId);
        Api.addComment(request, this.addCommentCallback);
    }

    private ApiCallback<ApiResponse<Object>> addCommentCallback = new ApiCallback<ApiResponse<Object>>() {
        @Override
        public void onPreLoad() {
        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<Object> apiResponse) {
            updateCommentCountShow();
            nativeAddCommentShow();
            hideSoftKeyBorad();
        }

        @Override
        public void onFailed(ApiResponse<Object> apiResponse) {
            makeTextLong(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
        }
    };

    private void updateCommentCountShow() {
        mDiaryInfo.setComment_count(mDiaryInfo.getComment_count() + 1);
        mDiaryDetailInfoAdapter.notifyItemChanged(0);
        EventBus.getDefault().post(new RefreshDiaryInfoEvent(mDiaryInfo));
    }

    private void nativeAddCommentShow() {
        Comment commentInfo = createCommentInfo(content);
        mCommentList.add(0, commentInfo);
        mDiaryDetailInfoAdapter.notifyItemInserted(1);
        mRecyclerView.scrollToPosition(1);
    }

    protected Comment createCommentInfo(String content) {
        Comment commentInfo = new Comment();
        commentInfo.setTopicid(diaryId);
        commentInfo.setTopictype(Global.TOPIC_DIARY);
        commentInfo.setDate(Calendar.getInstance().getTimeInMillis());
        commentInfo.setContent(content);
        commentInfo.setUsertype(Constant.IDENTITY_OWNER);
        User user = new User();
        user.setUsername(dataManager.getUserName());
        user.setImageid(dataManager.getUserImagePath());
        user.set_id(dataManager.getUserId());
        commentInfo.setByUser(user);
        return commentInfo;
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_diary_detailinfo;
    }
}
