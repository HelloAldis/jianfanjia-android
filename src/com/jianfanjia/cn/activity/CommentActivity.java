package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.jianfanjia.cn.adapter.CommentInfoAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.CommentInfo;

/**
 * @class CommentActivity
 * @Description 显示评论列表
 * @author zhanghao
 * @date 2015-8-27 10:11
 * 
 */
public class CommentActivity extends BaseActivity implements OnClickListener {
	private TextView backView;// 返回视图
	private ListView listView;// 评论列表
	private Button sendCommentView;// 发送评论
	private EditText etAddCommentView;// 添加评论
	private CommentInfoAdapter commentInfoAdapter;// 评论列表adapter
	private List<CommentInfo> commentInfoList;
	private CommentInfo commentInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		backView = (TextView) findViewById(R.id.comment_back);
		listView = (ListView) findViewById(R.id.comment_listview);
		etAddCommentView = (EditText) findViewById(R.id.add_comment);
		sendCommentView = (Button) findViewById(R.id.btn_send);
		commentInfoList = new ArrayList<CommentInfo>();
		for (int i = 0; i < 4; i++) {
			commentInfo = new CommentInfo();
			commentInfo.setContent("这个设计方案还不错，这个风格很漂亮，我很喜欢");
			commentInfo.setUserName("zhanghao" + i);
			commentInfo.setUserIdentity("设计师");
			commentInfo.setTime("2015-8-27");
			commentInfoList.add(commentInfo);
		}
		commentInfoAdapter = new CommentInfoAdapter(this, commentInfoList);
		listView.setAdapter(commentInfoAdapter);
	}

	@Override
	public void setListener() {
		backView.setOnClickListener(this);
		sendCommentView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_comment;
	}

}
