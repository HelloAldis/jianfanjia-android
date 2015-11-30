package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.jianfanjia.cn.adapter.CommentAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.Comment;
import com.jianfanjia.cn.bean.CommentInfo;
import com.jianfanjia.cn.bean.User;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Description:评论留言
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class CommentActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = CommentActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private ListView commentListView = null;
    private EditText commentEdit = null;
    private Button btnSend = null;
    private CommentAdapter commentAdapter = null;

    private String topicid = null;
    private String to = null;
    private String section = null;
    private String item = null;
    private String topictype = null;

    private List<CommentInfo> commentList = new ArrayList<CommentInfo>();

    private boolean isUpdate = false;//返回是否更新

    @Override
    public void initView() {
        initMainHeadView();
        commentListView = (ListView) findViewById(R.id.comment_listview);
        commentEdit = (EditText) findViewById(R.id.add_comment);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setEnabled(false);
        Intent intent = this.getIntent();
        Bundle commentBundle = intent.getExtras();
        topicid = commentBundle.getString(Global.TOPIC_ID);
        to = commentBundle.getString(Global.TO);
        section = commentBundle.getString(Global.SECTION);
        item = commentBundle.getString(Global.ITEM);
        topictype = commentBundle.getString(Global.TOPICTYPE);
        LogTool.d(TAG, "topicid=" + topicid + " to=" + to + " section = " + section + " item" + item);
        getCommentList(topicid, 0, 10000, section, item);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_comment_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.commentText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {
        commentEdit.addTextChangedListener(textWatcher);
        btnSend.setOnClickListener(this);
    }

    @Override
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

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            String content = s.toString().trim();
            if (TextUtils.isEmpty(content)) {
                btnSend.setEnabled(false);
            } else {
                btnSend.setEnabled(true);
            }
        }
    };

    //获取留言评论并标记为已读
    private void getCommentList(String topicid, int from, int limit, String section, String item) {
        JianFanJiaClient.getCommentList(CommentActivity.this, topicid, from, limit, section, item, getCommentListener, this);
    }

    private ApiUiUpdateListener getCommentListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            showWaitDialog(R.string.loading);
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data);
            hideWaitDialog();
            Comment comment = JsonParser.jsonToBean(data.toString(), Comment.class);
            LogTool.d(TAG, "comment:" + comment);
            if (null != comment) {
                commentList = comment.getComments();
                if (null != commentList) {
                    commentAdapter = new CommentAdapter(CommentActivity.this, commentList);
                    commentListView.setAdapter(commentAdapter);
                }
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            hideWaitDialog();
        }
    };

    //添加评论
    private void addComment(String topicid, String topictype, String section, String item, String content, String to) {
        JianFanJiaClient.addComment(CommentActivity.this, topicid, topictype, section, item, content, to, addCommentListener, this);
    }

    private ApiUiUpdateListener addCommentListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            showWaitDialog(R.string.submiting);
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data);
            hideWaitDialog();
            CommentInfo commentInfo = createCommentInfo(commentEdit.getEditableText().toString());
            commentAdapter.addItem(commentInfo, 0);
            commentListView.setSelection(0);
            commentEdit.setText("");
            isUpdate = true;
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            hideWaitDialog();
        }
    };

    protected CommentInfo createCommentInfo(String content) {
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setTo(to);
        commentInfo.setTopicid(topicid);
        commentInfo.setTopictype(topictype);
        commentInfo.setDate(Calendar.getInstance().getTimeInMillis());
        commentInfo.setContent(content);
        commentInfo.setUsertype(Constant.IDENTITY_OWNER);
        User user = new User();
        user.setUsername(dataManager.getUserName());
        user.setImageid(dataManager.getUserImagePath());
        commentInfo.setByUser(user);
        return commentInfo;
    }

    protected void back() {
        if (isUpdate) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
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