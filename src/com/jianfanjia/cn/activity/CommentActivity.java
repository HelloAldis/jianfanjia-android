package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.jianfanjia.cn.adapter.CommentInfoAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.CommentInfo;
import com.jianfanjia.cn.bean.CommitCommentInfo;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.LoadClientHelper;
import com.jianfanjia.cn.http.request.CommitCommentRequest;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * @class CommentActivity
 * @Description 显示评论列表
 * @author zhanghao
 * @date 2015-8-27 10:11
 * 
 */
public class CommentActivity extends BaseActivity implements OnClickListener,
		LoadDataListener {
	protected static final String TAG = CommentActivity.class.getName();
	private TextView backView = null;// 返回视图
	private ListView listView = null;// 评论列表
	private Button sendCommentView = null;// 发送评论
	private EditText etAddCommentView = null;// 添加评论
	private CommentInfoAdapter commentInfoAdapter = null;// 评论列表adapter
	private List<CommentInfo> commentInfoList = new ArrayList<CommentInfo>();
	private CommentInfo commentInfo = null;
	private CommitCommentInfo commitCommentInfo = null;
	private int currentList;// 当前工序
	private int currentItem;// 当前节点
	private ProcessInfo processInfo;
	private String section = null;
	private String item = null;
	private String content = null;
	private MainHeadView mainHeadView = null;

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
			if (!TextUtils.isEmpty(s.toString())) {
				sendCommentView.setEnabled(true);
			} else {
				sendCommentView.setEnabled(false);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			currentList = bundle.getInt(Constant.CURRENT_LIST, 0);
			currentItem = bundle.getInt(Constant.CURRENT_ITEM, 0);
		}
		getCommentList();
		commentInfoAdapter = new CommentInfoAdapter(this, commentInfoList);
		listView.setAdapter(commentInfoAdapter);
	}

	private void getCommentList() {
		processInfo = dataManager.getDefaultProcessInfo();
		if (processInfo != null) {
			section = processInfo.getSections().get(currentList).getName();
			item = processInfo.getSections().get(currentList).getItems()
					.get(currentItem).getName();
			commentInfoList = processInfo.getSections().get(currentList)
					.getItems().get(currentItem).getComments();
			Log.i(this.getClass().getName(), "itemsize =" + currentItem);
		}
	}

	@Override
	public void initView() {
		initMainHeadView();
		listView = (ListView) findViewById(R.id.comment_listview);
		etAddCommentView = (EditText) findViewById(R.id.add_comment);
		etAddCommentView.addTextChangedListener(textWatcher);
		sendCommentView = (Button) findViewById(R.id.btn_send);
		sendCommentView.setEnabled(false);
	}
	
	private void initMainHeadView() {
		mainHeadView = (MainHeadView) findViewById(R.id.comment_head_layout);
		mainHeadView.setBackListener(this);
		mainHeadView
				.setMianTitle(getResources().getString(R.string.comment));
		mainHeadView.setLayoutBackground(R.color.head_layout_bg);
		mainHeadView.setDividerVisable(View.VISIBLE);
	}

	@Override
	public void setListener() {
		sendCommentView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_back_layout:
			// startActivity(MainActivity.class);
			finish();
			break;
		case R.id.btn_send:
			content = etAddCommentView.getEditableText().toString();
			if (!TextUtils.isEmpty(content)) {
				commitComment();
				etAddCommentView.setText("");// 清楚输入框内容
			}
			break;
		default:
			break;
		}
	}

	private void commitComment() {
		commitCommentInfo = new CommitCommentInfo();
		commitCommentInfo.setContent(content);
		commitCommentInfo.set_id(processInfo.get_id());
		commitCommentInfo.setSection(section);
		commitCommentInfo.setItem(item);
		LoadClientHelper.postCommentInfo(this, new CommitCommentRequest(this,
				commitCommentInfo), this);

	}

	private void refreshData() {
		/*
		 * if (dataManager.getDefaultProcessId() == null) {
		 * LoadClientHelper.requestProcessInfoById(this, , listener) }
		 */
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTool.d(TAG, "---onResume()");
		listenerManeger.addPushMsgReceiveListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTool.d(TAG, "---onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		LogTool.d(TAG, "---onStop()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogTool.d(TAG, "---onDestroy()");
		listenerManeger.removePushMsgReceiveListener(this);
	}

	@Override
	public void onReceiveMsg(NotifyMessage message) {
		LogTool.d(TAG, "message=" + message);
		// sendNotifycation(message);
		showNotify(message);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_comment;
	}

	@Override
	public void loadSuccess() {
		super.loadSuccess();
		CommentInfo commentInfo = getCommentInfo();
		commentInfoAdapter.addItem(commentInfo);
		commentInfoAdapter.notifyDataSetChanged();
	}

	private CommentInfo getCommentInfo() {
		commentInfo = new CommentInfo();
		commentInfo.setBy(dataManager.getUserId());
		commentInfo.setContent(commitCommentInfo.getContent());
		commentInfo.setUserImageUrl(dataManager.getUserImagePath());
		commentInfo.setDate(Calendar.getInstance().getTimeInMillis());
		commentInfo.setUserName(dataManager.getUserName());
		commentInfo.setUsertype(dataManager.getUserType());
		return commentInfo;
	}

}
