package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
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
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;

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
	private int currentList;// 当前工序
	private int currentItem;// 当前节点
	private ProcessInfo processInfo = null;
	private String section = null;
	private String item = null;

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
		backView = (TextView) findViewById(R.id.comment_back);
		listView = (ListView) findViewById(R.id.comment_listview);
		etAddCommentView = (EditText) findViewById(R.id.add_comment);
		etAddCommentView.addTextChangedListener(textWatcher);
		sendCommentView = (Button) findViewById(R.id.btn_send);
		sendCommentView.setEnabled(false);
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
		case R.id.btn_send:
			commitComment();
			break;
		default:
			break;
		}
	}

	private void commitComment() {
		String content = etAddCommentView.getEditableText().toString();
		Log.i(TAG, content);
		if (!TextUtils.isEmpty(content)) {
			CommitCommentInfo commitCommentInfo = new CommitCommentInfo();
			commitCommentInfo.setContent(content);
			commitCommentInfo.set_id(processInfo.get_id());
			commitCommentInfo.setSection(section);
			commitCommentInfo.setItem(item);
			JianFanJiaApiClient.comment(this, commitCommentInfo,
					new JsonHttpResponseHandler() {
						@Override
						public void onStart() {
							LogTool.d(TAG, "onStart()");
							showWaitDialog();
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							LogTool.d(TAG, "JSONObject response:" + response);
							try {
								if (response.has(Constant.SUCCESS_MSG)) {
									makeTextLong(getString(R.string.comment_success));
									etAddCommentView.getEditableText().clear();
									sendCommentView.setEnabled(false);
									handlerSuccess();
								} else if (response.has(Constant.ERROR_MSG)) {
									makeTextLong(response.get(
											Constant.ERROR_MSG).toString());
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								makeTextLong(getString(R.string.load_failure));
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							LogTool.d(
									TAG,
									"Throwable throwable:"
											+ throwable.toString());
							hideWaitDialog();
							makeTextLong(getString(R.string.tip_no_internet));
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							LogTool.d(TAG, "throwable:" + throwable);
							hideWaitDialog();
							makeTextLong(getString(R.string.tip_no_internet));
						};
					});
		}
	}

	private void handlerSuccess() {
		refreshData();
	}

	private void refreshData() {
		if (dataManager.getDefaultProcessId() == null) {
			dataManager.requestProcessList(this);
		} else {
			dataManager.requestProcessInfoById(
					dataManager.getDefaultProcessId(), this);
		}
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
		LogTool.d(this.getClass().getName(), "onSuccess");
		hideWaitDialog();
		getCommentList();
		commentInfoAdapter.setList(commentInfoList);
		commentInfoAdapter.notifyDataSetChanged();
	}

	@Override
	public void loadFailture() {
		// TODO Auto-generated method stub

	}

}
