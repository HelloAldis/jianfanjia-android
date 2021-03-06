package com.jianfanjia.cn.designer.ui.activity.common;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Comment;
import com.jianfanjia.api.model.CommentList;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.request.common.AddCommentRequest;
import com.jianfanjia.api.request.common.GetCommentsRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.ui.adapter.CommentAdapter;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description:评论留言
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class CommentActivity extends BaseSwipeBackActivity {
    private static final String TAG = CommentActivity.class.getName();

    @Bind(R.id.my_comment_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.comment_listview)
    RecyclerView commentListView;

    @Bind(R.id.add_comment)
    EditText commentEdit;

    @Bind(R.id.btn_send)
    Button btnSend;

    private CommentAdapter commentAdapter = null;
    private String topicid = null;
    private String to = null;
    private String section = null;
    private String item = null;
    private String topictype = null;
    private List<Comment> comments = new ArrayList<>();
    private boolean isUpdate = false;//返回是否更新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFormIntent();
        initView();
        initData();
    }

    private void getDataFormIntent() {
        Intent intent = this.getIntent();
        Bundle commentBundle = intent.getExtras();
        topicid = commentBundle.getString(Global.TOPIC_ID);
        to = commentBundle.getString(Global.TO);
        section = commentBundle.getString(Global.SECTION);
        item = commentBundle.getString(Global.ITEM);
        topictype = commentBundle.getString(Global.TOPICTYPE);
        LogTool.d("topicid=" + topicid + " to=" + to + " section = " + section + " item" + item);
    }

    public void initView() {
        initMainHeadView();
        commentListView.setLayoutManager(new LinearLayoutManager(this));
        commentListView.setItemAnimator(new DefaultItemAnimator());
        commentListView.setHasFixedSize(true);
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(getResources().getColor(R.color.light_white_color));
        paint.setAntiAlias(true);
        commentListView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint)
                .showLastDivider().build());
        btnSend.setEnabled(false);
        getCommentList(topicid, 0, 10000, section, item);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.commentText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    private void initData() {
        LogTool.d("topicid=" + topicid + " to=" + to + " section = " + section + " item" + item);
        getCommentList(topicid, 0, 10000, section, item);
    }

    @OnClick({R.id.btn_send, R.id.head_back_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                back();
                break;
            case R.id.btn_send:
                String content = commentEdit.getText().toString().trim();
                addComment(topicid, topictype, section, item, content, to);
                break;
            default:
                break;
        }
    }

    @OnTextChanged(R.id.add_comment)
    public void onTextChanged(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            btnSend.setEnabled(false);
        } else {
            btnSend.setEnabled(true);
        }
    }

    //获取留言评论并标记为已读
    private void getCommentList(String topicid, int from, int limit, String section, String item) {
        GetCommentsRequest request = new GetCommentsRequest();
        request.setTopicid(topicid);
        request.setFrom(from);
        request.setLimit(limit);
        request.setSection(section);
        request.setItem(item);
        Api.getCommentList(request, getCommentCallback,this);
    }

    private ApiCallback<ApiResponse<CommentList>> getCommentCallback = new ApiCallback<ApiResponse<CommentList>>() {

        @Override
        public void onPreLoad() {
            Hud.show(getUiContext());
        }

        @Override
        public void onHttpDone() {
            Hud.dismiss();
        }

        @Override
        public void onSuccess(ApiResponse<CommentList> apiResponse) {
            CommentList commentList = apiResponse.getData();
            LogTool.d("commentList:" + commentList);
            if (null != commentList) {
                comments = commentList.getComments();
                LogTool.d("comments=" + comments);
                commentAdapter = new CommentAdapter(CommentActivity.this, comments);
                commentListView.setAdapter(commentAdapter);
            }
        }

        @Override
        public void onFailed(ApiResponse<CommentList> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.getMsg(code));
        }
    };

    //添加评论
    private void addComment(String topicid, String topictype, String section, String item, String content, String to) {
        AddCommentRequest request = new AddCommentRequest();
        request.setTopicid(topicid);
        request.setTopictype(topictype);
        request.setSection(section);
        request.setItem(item);
        request.setContent(content);
        request.setTo_userid(to);
        Api.addComment(request, this.addCommentCallback,this);
    }

    private ApiCallback<ApiResponse<Object>> addCommentCallback = new ApiCallback<ApiResponse<Object>>() {
        @Override
        public void onPreLoad() {
            Hud.show(getUiContext());
        }

        @Override
        public void onHttpDone() {
            Hud.dismiss();
        }

        @Override
        public void onSuccess(ApiResponse<Object> apiResponse) {
            Comment commentInfo = createCommentInfo(commentEdit.getEditableText().toString());
            comments.add(0, commentInfo);
            commentAdapter.notifyItemInserted(0);
            commentListView.scrollToPosition(0);
            commentEdit.setText("");
            isUpdate = true;
        }

        @Override
        public void onFailed(ApiResponse<Object> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.getMsg(code));
        }
    };

    protected Comment createCommentInfo(String content) {
        Comment comment = new Comment();
        comment.setTo(to);
        comment.setTopicid(topicid);
        comment.setTopictype(topictype);
        comment.setDate(Calendar.getInstance().getTimeInMillis());
        comment.setContent(content);
        comment.setUsertype(Constant.IDENTITY_DESIGNER);
        Designer user = new Designer();
        user.setUsername(dataManager.getUserName());
        user.setImageid(dataManager.getUserImagePath());
        comment.setByDesigner(user);
        return comment;
    }

    protected void back() {
        if (isUpdate) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        appManager.finishActivity(this);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_comment;
    }
}