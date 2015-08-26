package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.MyOwerInfoAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.MyOwerInfo;

/**
 * 
 * @ClassName: OwerFragment
 * @Description: 我的业主（设计师）
 * @author zhanghao
 * @date 2015-8-26 下午7:07:52
 * 
 */
public class OwerFragment extends BaseFragment{
	
	private ImageView headView;
	private ListView listView;
	private List<MyOwerInfo> caigouList = new ArrayList<MyOwerInfo>();
	private MyOwerInfo myOwerInfo = null;
	private MyOwerInfoAdapter myOwerInfoAdapter = null;

	@Override
	public void initView(View view) {
		headView = (ImageView)view.findViewById(R.id.my_ower_head);
		listView = (ListView) view.findViewById(R.id.tip_caigou__listview);
		for (int i = 0; i < 3; i++) {
			myOwerInfo = new MyOwerInfo();
			myOwerInfo.setName("zhanghao"+i);
			myOwerInfo.setAddress("湖北省武汉市洪山区观澜花园"+i);
			myOwerInfo.setStage("方案阶段");
			myOwerInfo.setImageUrl(null);
			caigouList.add(myOwerInfo);
		}
		myOwerInfoAdapter = new MyOwerInfoAdapter(getActivity(), caigouList);
		listView.setAdapter(myOwerInfoAdapter);
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_my_ower;
	}


	@Override
	public void setListener() {
		headView.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.my_ower_head:
			
			break;
		default:
			break;
		}
	}

}
