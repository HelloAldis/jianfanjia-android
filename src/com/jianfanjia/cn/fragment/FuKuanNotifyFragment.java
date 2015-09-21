package com.jianfanjia.cn.fragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
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
 * @Description: ��������
 * @author fengliang
 * @date 2015-8-26 ����1:08:44
 * 
 */
public class FuKuanNotifyFragment extends BaseFragment implements
		SwitchFragmentListener, OnItemLongClickListener {
	private ListView fukuanListView = null;
	private List<NotifyMessage> payList = new ArrayList<NotifyMessage>();
	private PayNotifyAdapter payAdapter = null;

	@Override
	public void initView(View view) {
		fukuanListView = (ListView) view.findViewById(R.id.tip_pay__listview);
		payAdapter = new PayNotifyAdapter(getActivity(), payList);
		fukuanListView.setAdapter(payAdapter);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment�ɼ�ʱ��������
			LogTool.d(this.getClass().getName(), "FuKuanNotifyFragment �ɼ�");
			initData();
		} else {
			// ���ɼ�ʱ��ִ�в���
			LogTool.d(this.getClass().getName(), "FuKuanNotifyFragment ���ɼ�");
		}
	}

	private void initData() {
		try {
			payList = daoManager.listByType(Constant.FUKUAN_NOTIFY);
			LogTool.d(this.getClass().getName(), "payList:" + payList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
