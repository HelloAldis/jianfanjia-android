package com.jianfanjia.cn.fragment;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: CaiGouNotifyFragment
 * @Description: 采购提醒
 * @date 2015-8-26 下午1:07:52
 */
public class CaiGouNotifyFragment extends BaseFragment implements
        SwitchFragmentListener, OnItemLongClickListener {
    private static final String TAG = CaiGouNotifyFragment.class.getName();
    private ListView caigouListView;
    private List<NotifyMessage> caigouList = new ArrayList<NotifyMessage>();
    private CaiGouNotifyAdapter caiGouAdapter = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUserVisibleHint(true);
        caiGouAdapter = new CaiGouNotifyAdapter(getActivity(), caigouList);
        caigouListView.setAdapter(caiGouAdapter);
    }

    @Override
    public void initView(View view) {
        caigouListView = (ListView) view
                .findViewById(R.id.tip_caigou__listview);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // fragment可见时加载数据
            LogTool.d(TAG, "CaiGouNotifyFragment 可见");
            initData();
        } else {
            // 不可见时不执行操作
            LogTool.d(TAG, "CaiGouNotifyFragment 不可见");
        }
    }

    private void initData() {
        caigouList = notifyMessageDao
                .getNotifyListByType(Constant.CAIGOU_NOTIFY);
        LogTool.d(TAG, "caigouList:" + caigouList);
    }

    @Override
    public void setListener() {
        caigouListView.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View v, int position,
                                   long id) {
        NotifyMessage notifyMessage = caigouList.get(position);
        LogTool.d(TAG, "notifyMessage:" + notifyMessage);
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
