package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.jianfanjia.cn.R;
import com.jianfanjia.cn.adapter.CaiGouNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyCaiGouInfo;
import com.jianfanjia.cn.tools.LogTool;

/**
 * 
 * @ClassName: CaiGouNotifyFragment
 * @Description: 采购提醒
 * @author fengliang
 * @date 2015-8-26 下午1:07:52
 * 
 */
public class CaiGouNotifyFragment extends BaseFragment {
	private ListView listView;
	private List<NotifyCaiGouInfo> caigouList = new ArrayList<NotifyCaiGouInfo>();
	private NotifyCaiGouInfo caiGouInfo = null;
	private CaiGouNotifyAdapter caiGouAdapter = null;

	@Override
	public void initView(View view) {
		listView = (ListView) view.findViewById(R.id.tip_caigou__listview);
		for (int i = 0; i < 3; i++) {
			caiGouInfo = new NotifyCaiGouInfo();
			caiGouInfo.setContent("复合板" + i);
			caiGouInfo.setTitle("提醒采购建材" + i);
			caiGouInfo.setTime("2015-8-26");
			caiGouInfo.setStage("泥木阶段>木工阶段");
			caigouList.add(caiGouInfo);
		}
		caiGouAdapter = new CaiGouNotifyAdapter(getActivity(), caigouList);
		listView.setAdapter(caiGouAdapter);
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
		return R.layout.fragment_caigou_notify;
	}

}
