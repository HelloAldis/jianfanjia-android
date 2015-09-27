package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.PayNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.SwitchFragmentListener;
import com.jianfanjia.cn.tools.LogTool;

/**
 * 
 * @ClassName: FuKuanNotifyFragment
 * @Description: 付款提醒
 * @author fengliang
 * @date 2015-8-26 下午1:08:44
 * 
 */
public class FuKuanNotifyFragment extends BaseFragment implements
		SwitchFragmentListener, OnItemLongClickListener {
	private static final String TAG = FuKuanNotifyFragment.class.getName();
	private ListView fukuanListView = null;
	private List<NotifyMessage> payList = new ArrayList<NotifyMessage>();
	private PayNotifyAdapter payAdapter = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setUserVisibleHint(true);
		payAdapter = new PayNotifyAdapter(getActivity(), payList);
		fukuanListView.setAdapter(payAdapter);
	}

	@Override
	public void initView(View view) {
		fukuanListView = (ListView) view.findViewById(R.id.tip_pay__listview);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment可见时加载数据
			LogTool.d(TAG, "FuKuanNotifyFragment 可见");
			initData();
		} else {
			// 不可见时不执行操作
			LogTool.d(TAG, "FuKuanNotifyFragment 不可见");
		}
	}

	private void initData() {
		payList = notifyMessageDao.getNotifyListByType(Constant.FUKUAN_NOTIFY);
		LogTool.d(TAG, "payList:" + payList);
	}

	@Override
	public void setListener() {
		fukuanListView.setOnItemLongClickListener(this);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View v, int position,
			long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_fukuan_notify;
	}

	@Override
	public void switchTab(int index) {
		// TODO Auto-generated method stub

	}

}
