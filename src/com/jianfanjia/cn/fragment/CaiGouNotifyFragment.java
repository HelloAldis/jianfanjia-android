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
 * @Description: �ɹ�����
 * @author fengliang
 * @date 2015-8-26 ����1:07:52
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
			caiGouInfo.setContent("���ϰ�" + i);
			caiGouInfo.setTitle("���Ѳɹ�����" + i);
			caiGouInfo.setTime("2015-8-26");
			caiGouInfo.setStage("��ľ�׶�>ľ���׶�");
			caigouList.add(caiGouInfo);
		}
		caiGouAdapter = new CaiGouNotifyAdapter(getActivity(), caigouList);
		listView.setAdapter(caiGouAdapter);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment�ɼ�ʱ��������
			LogTool.d(this.getClass().getName(), "1111111111111111");
		} else {
			// ���ɼ�ʱ��ִ�в���
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
