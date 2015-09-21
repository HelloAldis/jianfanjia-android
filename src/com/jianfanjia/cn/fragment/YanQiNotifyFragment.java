package com.jianfanjia.cn.fragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DelayNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.SwitchFragmentListener;
import com.jianfanjia.cn.tools.LogTool;

/**
 * 
 * @ClassName: YanQiNotifyFragment
 * @Description: 延期提醒
 * @author fengliang
 * @date 2015-8-26 下午1:09:52
 * 
 */
public class YanQiNotifyFragment extends BaseFragment implements
		SwitchFragmentListener, OnItemLongClickListener {
	private ListView yanqiListView = null;
	private List<NotifyMessage> delayList = new ArrayList<NotifyMessage>();
	private NotifyMessage notifyMessage = null;
	private DelayNotifyAdapter delayAdapter = null;

	@Override
	public void initView(View view) {
		yanqiListView = (ListView) view.findViewById(R.id.tip_delay__listview);
		delayAdapter = new DelayNotifyAdapter(getActivity(), delayList);
		yanqiListView.setAdapter(delayAdapter);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment可见时加载数据
			LogTool.d(this.getClass().getName(), "YanQiNotifyFragment 可见");
			initData();
		} else {
			// 不可见时不执行操作
			LogTool.d(this.getClass().getName(), "YanQiNotifyFragment 不可见");
		}
	}

	private void initData() {
		try {
			delayList = daoManager.listByType(Constant.YANQI_NOTIFY);
			LogTool.d(this.getClass().getName(), "delayList:" + delayList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setListener() {
		yanqiListView.setOnItemLongClickListener(this);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View v, int position,
			long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_yanqi_notify;
	}

	@Override
	public void switchTab(int index) {
		// TODO Auto-generated method stub

	}

}
