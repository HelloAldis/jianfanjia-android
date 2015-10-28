package com.jianfanjia.cn.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

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

    @Override
    public void initView() {
        initMainHeadView();
        commentListView = (ListView) findViewById(R.id.comment_listview);
        commentEdit = (EditText) findViewById(R.id.add_comment);
        btnSend = (Button) findViewById(R.id.btn_send);
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

                } else {
                    makeTextLong("请输入内容");
                }
                break;
            default:
                break;
        }
    }

    //获取留言评论并标记为已读
    private void getCommentList(String topicid, int from, int limit) {
        JianFanJiaClient.getCommentList(CommentActivity.this, topicid, from, limit, getCommentListener, this);
    }

    private ApiUiUpdateListener getCommentListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            showWaitDialog(R.string.loading);
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data);
            makeTextLong(data.toString());
            hideWaitDialog();
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            hideWaitDialog();
        }
    };

    //添加评论
    private void addComment(String topicid, String topictype, String content, String to) {
        JianFanJiaClient.addComment(CommentActivity.this, topicid, topictype, content, to, addCommentListener, this);
    }

    private ApiUiUpdateListener addCommentListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            showWaitDialog(R.string.submiting);
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data);
            makeTextLong(data.toString());
            hideWaitDialog();
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            hideWaitDialog();
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_comment;
    }
}