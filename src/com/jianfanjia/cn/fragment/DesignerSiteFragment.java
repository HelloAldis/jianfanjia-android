package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DesignerSiteInfoAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerSiteInfo;

/**
 * 
 * @ClassName: DesignerSiteFragment
 * @Description: 我的工地（设计师）
 * @author zhanghao
 * @date 2015-8-26 下午7:07:52
 * 
 */
public class DesignerSiteFragment extends BaseFragment {
	private ImageView headView;
	private ListView listView;
	private List<DesignerSiteInfo> caigouList = new ArrayList<DesignerSiteInfo>();
	private DesignerSiteInfo designerSiteInfo = null;
	private DesignerSiteInfoAdapter designerSiteInfoAdapter = null;

	@Override
	public void initView(View view) {
		headView = (ImageView) view.findViewById(R.id.designer_site_head);
		listView = (ListView) view.findViewById(R.id.designer_site_listview);
		for (int i = 0; i < 3; i++) {
			designerSiteInfo = new DesignerSiteInfo();
			designerSiteInfo.setName("zhanghao" + i);
			designerSiteInfo.setAddress("湖北省武汉市洪山区观澜花园" + i);
			designerSiteInfo.setStage("方案阶段");
			designerSiteInfo.setImageUrl(null);
			designerSiteInfo.setVillageName("关南小区");
			caigouList.add(designerSiteInfo);
		}
		designerSiteInfoAdapter = new DesignerSiteInfoAdapter(getActivity(),
				caigouList);
		listView.setAdapter(designerSiteInfoAdapter);
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

	@Override
	public int getLayoutId() {
		return R.layout.fragment_designer_site;
	}
}
