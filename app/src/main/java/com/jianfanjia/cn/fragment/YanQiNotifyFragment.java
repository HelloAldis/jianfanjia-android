package com.jianfanjia.cn.fragment;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DelayNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyDelayInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.DelayInfoListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: YanQiNotifyFragment
 * @Description: 改期提醒
 * @date 2015-8-26 下午1:09:52
 */
public class YanQiNotifyFragment extends BaseFragment implements ApiUiUpdateListener, DelayInfoListener, PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = YanQiNotifyFragment.class.getName();
    private PullToRefreshRecycleView yanqiListView = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
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
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        yanqiListView = (PullToRefreshRecycleView) view.findViewById(R.id.tip_delay__listview);
        yanqiListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        yanqiListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        yanqiListView.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        yanqiListView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
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
        yanqiListView.setOnRefreshListener(this);
        errorLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                getRescheduleNotifyList();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getRescheduleNotifyList();
        yanqiListView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        yanqiListView.onRefreshComplete();
    }

    //获取改期提醒
    private void getRescheduleNotifyList() {
        JianFanJiaClient.getRescheduleAll(getActivity(), this, this);
    }

    @Override
    public void preLoad() {
        showWaitDialog(R.string.loading);
        yanqiListView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data:" + data.toString());
        hideWaitDialog();
        delayList = JsonParser.jsonToList(data.toString(), new TypeToken<List<NotifyDelayInfo>>() {
        }.getType());
        LogTool.d(TAG, "delayList:" + delayList);
        if (null != delayList && delayList.size() > 0) {
            delayAdapter = new DelayNotifyAdapter(getActivity(), delayList, this);
            yanqiListView.setAdapter(delayAdapter);
            yanqiListView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.GONE);
        } else {
            yanqiListView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        hideWaitDialog();
        yanqiListView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(int position, String status, String role) {
        LogTool.d(TAG, "position=" + position + " status=" + status + " role=" + role);
        if (role.equals(Constant.IDENTITY_DESIGNER)) {
            if (status.equals(Constant.YANQI_BE_DOING)) {
                notifyDelayInfo = delayList.get(position);
                LogTool.d(TAG, " notifyDelayInfo:" + notifyDelayInfo);
                if (null != notifyDelayInfo) {
                    processid = notifyDelayInfo.getProcessid();
                    LogTool.d(TAG, "processid:" + processid);
                    delayNotifyDialog();
                }
            }
        }
    }

    private void delayNotifyDialog() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(getActivity());
        dialog.setTitle("改期提醒");
        dialog.setMessage("确定要改期吗？");
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
    public int getLayoutId() {
        return R.layout.fragment_yanqi_notify;
    }

}
