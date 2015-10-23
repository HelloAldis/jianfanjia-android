package com.jianfanjia.cn.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.jianfanjia.cn.base.BaseActivity;
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
    private Button btnSend = null;

    @Override
    public void initView() {
        initMainHeadView();
        commentListView = (ListView) findViewById(R.id.comment_listview);
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
                break;
            default:
                break;
        }
    }

    private void getCommentList() {

    }

    private void addComment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_comment;
    }
}