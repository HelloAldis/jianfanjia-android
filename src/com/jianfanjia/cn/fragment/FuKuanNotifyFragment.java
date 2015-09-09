package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.PayNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyPayInfo;
import com.jianfanjia.cn.tools.LogTool;

/**
 * 
 * @ClassName: FuKuanNotifyFragment
 * @Description: 付款提醒
 * @author fengliang
 * @date 2015-8-26 下午1:08:44
 * 
 */
public class FuKuanNotifyFragment extends BaseFragment {
	private ListView listView;
	private List<NotifyPayInfo> payList = new ArrayList<NotifyPayInfo>();
	private NotifyPayInfo payInfo = null;
	private PayNotifyAdapter payAdapter = null;

	@Override
	public void initView(View view) {
		listView = (ListView) view.findViewById(R.id.tip_pay__listview);
		for (int i = 0; i < 3; i++) {
			payInfo = new NotifyPayInfo();
			payInfo.setTitle("提醒付款" + i);
			payInfo.setTime("2015-8-26");
			payInfo.setStage("泥木阶段>木工阶段");
			payList.add(payInfo);
		}
		payAdapter = new PayNotifyAdapter(getActivity(), payList);
		listView.setAdapter(payAdapter);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment可见时加载数据
			LogTool.d(this.getClass().getName(), "1111111111111111");
		} else {
			// 不可见时不执行操作
			LogTool.d(this.getClass().getName(), "222222222222222");
		}
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_fukuan_notify;
	}

}
