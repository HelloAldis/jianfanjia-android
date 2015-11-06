package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.jianfanjia.cn.adapter.CommentAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.Comment;
import com.jianfanjia.cn.bean.CommentInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

import java.util.ArrayList;
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

    private List<CommentInfo> commentList = new ArrayList<CommentInfo>();

    @Override
    public void initView() {
        initMainHeadView();
        Intent intent = this.getIntent();
        Bundle commentBundle = intent.getExtras();
        topicid = commentBundle.getString(Constant.TOPIC_ID);
        to = commentBundle.getString(Constant.TO);
        section = commentBundle.getString(Constant.SECTION);
        item = commentBundle.getString(Constant.ITEM);
        LogTool.d(TAG, "topicid=" + topicid + " to=" + to + " section = " + section + " item" + item);
        commentListView = (ListView) findViewById(R.id.comment_listview);
        commentEdit = (EditText) findViewById(R.id.add_comment);
        btnSend = (Button) findViewById(R.id.btn_send);

        getCommentList(topicid, 0, 10000, section, item);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.comment_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.comment));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            case R.id.btn_send:
                String content = commentEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    addComment(topicid, Constant.TOPIC_NODE, section, item, content, to);
                } else {
                    makeTextLong("请输入内容");
                }
                break;
            default:
                break;
        }
    }

    //获取留言评论并标记为已读
    private void getCommentList(String topicid, int from, int limit, String section, String item) {
        JianFanJiaClient.getCommentList(CommentActivity.this, topicid, from, limit, section, item, getCommentListener, this);
    }

    private LoadDataListener getCommentListener = new LoadDataListener() {
        @Override
        public void preLoad() {
            showWaitDialog(R.string.loading);
        }

        @Override
        public void loadSuccess(BaseResponse data) {
            LogTool.d(TAG, "data:" + data.getData());
            hideWaitDialog();
            Comment comment = JsonParser.jsonToBean(data.getData().toString(), Comment.class);
            LogTool.d(TAG, "comment:" + comment);
            if (null != comment) {
                commentList = comment.getComments();
                if (null != commentList && commentList.size() > 0) {
                    commentAdapter = new CommentAdapter(CommentActivity.this, commentList);
                    commentListView.setAdapter(commentAdapter);
                }
            }
        }

        @Override
        public void loadFailture() {
            makeTextLong(getString(R.string.tip_error_internet));
            hideWaitDialog();
        }
    };

    //添加评论
    private void addComment(String topicid, String topictype, String section, String item, String content, String to) {
        JianFanJiaClient.addComment(CommentActivity.this, topicid, topictype, section, item, content, to, addCommentListener, this);
    }

    private LoadDataListener addCommentListener = new LoadDataListener() {
        @Override
        public void preLoad() {
            showWaitDialog(R.string.submiting);
        }

        @Override
        public void loadSuccess(BaseResponse data) {
            LogTool.d(TAG, "data:" + data.toString());
            hideWaitDialog();
            commentEdit.setText("");
            getCommentList(topicid, 0, 10000, section, item);
        }

        @Override
        public void loadFailture() {
            makeTextLong(getString(R.string.tip_error_internet));
            hideWaitDialog();
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_comment;
    }
}