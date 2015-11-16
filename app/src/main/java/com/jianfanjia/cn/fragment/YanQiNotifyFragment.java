package com.jianfanjia.cn.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DelayNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyDelayInfo;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.DelayInfoListener;
import com.jianfanjia.cn.interf.SwitchFragmentListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: YanQiNotifyFragment
 * @Description: 改期提醒
 * @date 2015-8-26 下午1:09:52
 */
public class YanQiNotifyFragment extends BaseFragment implements
        SwitchFragmentListener, OnItemLongClickListener, ApiUiUpdateListener, DelayInfoListener {
    private static final String TAG = YanQiNotifyFragment.class.getName();
    private ListView yanqiListView = null;
    private List<NotifyDelayInfo> delayList = new ArrayList<NotifyDelayInfo>();
    private NotifyDelayInfo notifyDelayInfo = null;
    private DelayNotifyAdapter delayAdapter = null;
    private String processid = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
            getRescheduleNotifyList();
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

    //获取改期提醒
    private void getRescheduleNotifyList() {
        JianFanJiaClient.getRescheduleAll(getActivity(), this, this);
    }

    @Override
    public void preLoad() {
        showWaitDialog(R.string.loading);
    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data:" + data.toString());
        hideWaitDialog();
        delayList = JsonParser.jsonToList(data.toString(), new TypeToken<List<NotifyDelayInfo>>() {
        }.getType());
        LogTool.d(TAG, "delayList:" + delayList);
        delayAdapter = new DelayNotifyAdapter(getActivity(), delayList, this);
        yanqiListView.setAdapter(delayAdapter);
    }

    @Override
    public void loadFailture(String error_msg) {
        hideWaitDialog();
    }

    @Override
    public void onClick(int position) {
        notifyDelayInfo = delayList.get(position);
        LogTool.d(TAG, " notifyDelayInfo:" + notifyDelayInfo);
        if (null != notifyDelayInfo) {
            processid = notifyDelayInfo.get_id();
            LogTool.d(TAG, "processid:" + processid);
            delayNotifyDialog();
        }
    }

    private void delayNotifyDialog() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(getActivity());
        dialog.setTitle("延期提醒");
        dialog.setMessage("确定要延期吗？");
        dialog.setPositiveButton(R.string.agree,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        agreeReschedule(processid);
                    }
                });
        dialog.setNegativeButton(R.string.refuse, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                refuseReschedule(processid);
            }
        });
        dialog.show();
    }

    // 用户同意改期
    private void agreeReschedule(String processid) {
        JianFanJiaClient.agreeReschedule(getActivity(), processid, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                LogTool.d(TAG, "data:" + data.toString());
                getRescheduleNotifyList();
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextLong(error_msg);
            }
        }, this);
    }

    // 用户拒绝改期
    private void refuseReschedule(String processid) {
        JianFanJiaClient.refuseReschedule(getActivity(), processid, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                LogTool.d(TAG, "data:" + data.toString());
                getRescheduleNotifyList();
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextLong(error_msg);
            }
        }, this);
    }

    @Override
    public void switchTab(int index) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_yanqi_notify;
    }


}
