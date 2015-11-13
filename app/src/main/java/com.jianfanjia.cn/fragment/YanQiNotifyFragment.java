package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DelayNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.NotifyDelayInfo;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.interf.SwitchFragmentListener;
import com.jianfanjia.cn.tools.LogTool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: YanQiNotifyFragment
 * @Description: 延期提醒
 * @date 2015-8-26 下午1:09:52
 */
public class YanQiNotifyFragment extends BaseFragment implements
        SwitchFragmentListener, OnItemLongClickListener {
    private static final String TAG = YanQiNotifyFragment.class.getName();
    private ListView yanqiListView = null;
    private List<NotifyDelayInfo> delayList = new ArrayList<NotifyDelayInfo>();
    private NotifyDelayInfo notifyDelayInfo = null;
    private DelayNotifyAdapter delayAdapter = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogTool.d(TAG, "dataManager=" + dataManager);
    }

    @Override
    public void initView(View view) {
        yanqiListView = (ListView) view.findViewById(R.id.tip_delay__listview);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // fragment可见时加载数据
            LogTool.d(TAG, "YanQiNotifyFragment 可见");
            getRescheduleAll();
        } else {
            // 不可见时不执行操作
            LogTool.d(TAG, "YanQiNotifyFragment 不可见");
        }
    }

    @Override
    public void setListener() {
        yanqiListView.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View v, int position,
                                   long id) {
        // TODO Auto-generated method stub
        return false;
    }

    // /用户获取我的改期提醒
    private void getRescheduleAll() {
        JianFanJiaClient.rescheduleAll(getActivity(), new LoadDataListener() {
            @Override
            public void preLoad() {
                showWaitDialog();
            }

            @Override
            public void loadSuccess(BaseResponse baseResponse) {
                hideWaitDialog();
            }

            @Override
            public void loadFailture() {
                hideWaitDialog();
            }
        }, this);
    }

    // 用户同意改期
    private void agreeReschedule(String processid) {
        JianFanJiaClient.agreeReschedule(getActivity(), processid, new LoadDataListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(BaseResponse baseResponse) {

            }

            @Override
            public void loadFailture() {

            }
        }, this);
    }

    // 用户拒绝改期
    private void refuseReschedule(String processid) {
        JianFanJiaClient.refuseReschedule(getActivity(), processid, new LoadDataListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(BaseResponse baseResponse) {

            }

            @Override
            public void loadFailture() {

            }
        }, this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_yanqi_notify;
    }

    @Override
    public void switchTab(int index) {
        // TODO Auto-generated method stub

    }

}
