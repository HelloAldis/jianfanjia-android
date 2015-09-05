package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.jianfanjia.cn.activity.OwnerInfoActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.MyOwerInfoAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.MyOwnerInfo;

/**
 * 
 * @ClassName: OwerFragment
 * @Description: 我的业主（设计师）
 * @author zhanghao
 * @date 2015-8-26 下午7:07:52
 * 
 */
public class OwnerFragment extends BaseFragment implements OnItemClickListener {
	private ImageView headView;
	private ListView ownerListView;
	private List<MyOwnerInfo> caigouList = new ArrayList<MyOwnerInfo>();
	private MyOwnerInfo myOwerInfo = null;
	private MyOwerInfoAdapter myOwerInfoAdapter = null;

	@Override
	public void initView(View view) {
		headView = (ImageView) view.findViewById(R.id.my_ower_head);
		ownerListView = (ListView) view.findViewById(R.id.my_ower_listview);
		headView = (ImageView) view.findViewById(R.id.my_ower_head);
		for (int i = 0; i < 3; i++) {
			myOwerInfo = new MyOwnerInfo();
			myOwerInfo.setName("zhanghao" + i);
			myOwerInfo.setAddress("湖北省武汉市洪山区观澜花园" + i);
			myOwerInfo.setStage("方案阶段");
			myOwerInfo.setImageUrl(null);
			caigouList.add(myOwerInfo);
		}
		myOwerInfoAdapter = new MyOwerInfoAdapter(getActivity(), caigouList);
		ownerListView.setAdapter(myOwerInfoAdapter);
	}

	@Override
	public void setListener() {
		headView.setOnClickListener(this);
		ownerListView.setOnItemClickListener(this);
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		startActivity(OwnerInfoActivity.class);
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_my_ower;
	}
}
