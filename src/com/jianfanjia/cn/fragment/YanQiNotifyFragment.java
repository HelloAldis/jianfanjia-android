package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DelayNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyDelayInfo;

/**
 * 
 * @ClassName: YanQiNotifyFragment
 * @Description: ÑÓÆÚÌáÐÑ
 * @author fengliang
 * @date 2015-8-26 ÏÂÎç1:09:52
 * 
 */
public class YanQiNotifyFragment extends BaseFragment {
	private ListView listView = null;
	private List<NotifyDelayInfo> caigouList = new ArrayList<NotifyDelayInfo>();
	private NotifyDelayInfo caiGouInfo = null;
	private DelayNotifyAdapter caiGouAdapter = null;

	@Override
	public void initView(View view) {
		listView = (ListView) view.findViewById(R.id.tip_delay__listview);
		for (int i = 0; i < 3; i++) {
			caiGouInfo = new NotifyDelayInfo();
			caiGouInfo.setContent("¸´ºÏ°å" + i);
			caiGouInfo.setTitle("ÌáÐÑ²É¹º½¨²Ä" + i);
			caiGouInfo.setTime("2015-8-26");
			caiGouInfo.setStage("ÄàÄ¾½×¶Î>Ä¾¹¤½×¶Î");
			caiGouInfo.setIsagree(i % 2);
			caigouList.add(caiGouInfo);
		}
		caiGouAdapter = new DelayNotifyAdapter(getActivity(), caigouList);
		listView.setAdapter(caiGouAdapter);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_yanqi_notify;
	}

}
