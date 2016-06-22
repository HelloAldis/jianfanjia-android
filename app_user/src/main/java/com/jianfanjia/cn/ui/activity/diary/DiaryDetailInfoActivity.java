package com.jianfanjia.cn.ui.activity.diary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.adapter.DiaryDetailInfoAdapter;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.ui.activity.diary
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-14 11:05
 */
public class DiaryDetailInfoActivity extends BaseSwipeBackActivity {

    public static final String IntentFlag = "intent_flag";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        mDiaryInfo = (DiaryInfo) getIntent().getSerializableExtra(IntentConstant.DIARY_INFO);
        if (mDiaryInfo != null) {
            diaryId = mDiaryInfo.get_id();
        }
        getDiaryInfo(diaryId);
    }

    private void initView() {
        initMainView();
        btnSend.setEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(this));
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
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<DiaryInfo> apiResponse) {
                setInitData(apiResponse.getData());
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

    private void setInitData(DiaryInfo diaryInfo) {
        if (diaryInfo.is_deleted()) {
            mDiaryInfo.setIs_deleted(true);
            backBecauseOfDelete();
        } else {
            to = diaryInfo.get_id();//默认是回复作者的
            getCommentList(mDiaryInfo.get_id(), 0, Constant.HOME_PAGE_LIMIT);
        }
    }

    private void backBecauseOfDelete() {
        Intent intent = getIntent();
        intent.putExtra(IntentConstant.DIARY_INFO, mDiaryInfo);
        setResult(RESULT_OK, intent);
        appManager.finishActivity(this);
    }

    private ApiCallback<ApiResponse<CommentList>> getCommentCallback = new ApiCallback<ApiResponse<CommentList>>() {
        @Override
        public void onPreLoad() {
            showWaitDialog(R.string.loading);
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
                        mDiaryInfo);
                mDiaryDetailInfoAdapter.setAddCommentListener(new DiaryDetailInfoAdapter.AddCommentListener() {
                    @Override
                    public void addCommentListener(User toUser) {
                        prepareAddComment(toUser);
                    }
                });
                mRecyclerView.setAdapter(mDiaryDetailInfoAdapter);
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

    private void prepareAddComment(User toUser) {
        if (toUser.get_id().equals(diaryId)) {
            return;
        }
        to = toUser.get_id();
        if (!TextUtils.isEmpty(toUser.getUsername())) {
            replayHint = "回复 " + toUser.getUsername() + " ：";
        } else {
            replayHint = "回复 业主 ：";
        }
        commentEdit.setHint(replayHint);
    }

    @OnClick({R.id.btn_send, R.id.head_back_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                content = replayHint + commentEdit.getText().toString().trim();
                addComment();
                break;
            case R.id.head_back_layout:
                backBecauseOfDelete();
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
        Api.addComment(request, this.addCommentCallback);
    }

    private ApiCallback<ApiResponse<Object>> addCommentCallback = new ApiCallback<ApiResponse<Object>>() {
        @Override
        public void onPreLoad() {
            showWaitDialog(R.string.loading);
        }

        @Override
        public void onHttpDone() {
            hideWaitDialog();
        }

        @Override
        public void onSuccess(ApiResponse<Object> apiResponse) {
            Comment commentInfo = createCommentInfo(content);
            mCommentList.add(1, commentInfo);
            mDiaryDetailInfoAdapter.notifyItemInserted(1);
            mRecyclerView.scrollToPosition(0);
            commentEdit.setText("");
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
        commentInfo.setByUser(user);
        return commentInfo;
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_diary_detailinfo;
    }
}
