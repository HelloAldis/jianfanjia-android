package com.jianfanjia.cn.fragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.CaiGouNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.SwitchFragmentListener;
import com.jianfanjia.cn.tools.LogTool;

/**
 * 
 * @ClassName: CaiGouNotifyFragment
 * @Description: 采购提醒
 * @author fengliang
 * @date 2015-8-26 下午1:07:52
 * 
 */
public class CaiGouNotifyFragment extends BaseFragment implements
		SwitchFragmentListener, OnItemLongClickListener {
	private ListView caigouListView;
	private List<NotifyMessage> caigouList = new ArrayList<NotifyMessage>();
	private CaiGouNotifyAdapter caiGouAdapter = null;

	@Override
	public void initView(View view) {
		caigouListView = (ListView) view
				.findViewById(R.id.tip_caigou__listview);
		caiGouAdapter = new CaiGouNotifyAdapter(getActivity(), caigouList);
		caigouListView.setAdapter(caiGouAdapter);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment可见时加载数据
			LogTool.d(this.getClass().getName(), "CaiGouNotifyFragment 可见");
			initData();
		} else {
			// 不可见时不执行操作
			LogTool.d(this.getClass().getName(), "CaiGouNotifyFragment 不可见");
		}
	}

	private void initData() {
		try {
			caigouList = daoManager.listByType(Constant.CAIGOU_NOTIFY);
			LogTool.d(this.getClass().getName(), "caigouList:" + caigouList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setListener() {
		caigouListView.setOnItemLongClickListener(this);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View v, int position,
			long id) {
		NotifyMessage notifyMessage = caigouList.get(position);
		LogTool.d(this.getClass().getName(), "notifyMessage:" + notifyMessage);
		return true;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_caigou_notify;
	}

	@Override
	public void switchTab(int index) {
		// TODO Auto-generated method stub

	}

}
