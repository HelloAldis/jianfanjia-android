package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.PayNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyPayInfo;

/**
 * 
 * @ClassName: FuKuanNotifyFragment
 * @Description: ¸¶¿îÌáÐÑ
 * @author fengliang
 * @date 2015-8-26 ÏÂÎç1:08:44
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
			payInfo.setTitle("ÌáÐÑ¸¶¿î" + i);
			payInfo.setTime("2015-8-26");
			payInfo.setStage("ÄàÄ¾½×¶Î>Ä¾¹¤½×¶Î");
			payList.add(payInfo);
		}
		payAdapter = new PayNotifyAdapter(getActivity(), payList);
		listView.setAdapter(payAdapter);
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
