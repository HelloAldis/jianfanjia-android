package com.jianfanjia.cn.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jianfanjia.cn.adapter.MyProcessInfoAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.jianfanjia.cn.view.MainHeadView;

import java.util.Collections;
import java.util.List;

/**
 *
 * @ClassName: DesignerSiteActivity
 * @Description: 设计师工地
 * @author fengliang
 * @date 2015-9-11 上午10:02:42
 *
 */
public class MyProcessActivity extends BaseActivity implements
        OnClickListener, OnItemClickListener, ApiUiUpdateListener {
    private static final String TAG = MyProcessActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private ListView siteListView = null;
    private List<Process> siteList;
    private TextView errorText;
    private MyProcessInfoAdapter myProcessInfoAdapter = null;

    @Override
    public void initView() {
        initMainHeadView();
        siteListView = (ListView) findViewById(R.id.designer_site_listview);
//        siteList = dataManager.getProcessLists();
        if (siteList == null) {
            if (NetTool.isNetworkAvailable(this)) {
				/*LoadClientHelper.requestProcessList(this,
						new ProcessListRequest(this), this);*/
                JianFanJiaClient.get_Process_List(this,this,this);
            } else {
                siteList = dataManager.getProcessListsByCache();
            }
        }
        myProcessInfoAdapter = new MyProcessInfoAdapter(
                MyProcessActivity.this, siteList);
        siteListView.setAdapter(myProcessInfoAdapter);
        setEmptyView();
    }

    private void setEmptyView() {
        ViewStub mViewStub = (ViewStub) findViewById(R.id.empty);
        errorText = (TextView) mViewStub.inflate().findViewById(R.id.tv_error);
        errorText.setText("暂无工地数据");
        siteListView.setEmptyView(mViewStub);
    }

    @SuppressLint("ResourceAsColor")
    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.designer_site_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setMianTitle(getResources().getString(
                R.string.my_decoration_site));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {
        siteListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        Process siteInfo = siteList.get(position);
        LogTool.d(TAG, "_id=" + siteInfo.get_id());
        dataManager.setDefaultPro(position);
        Intent intent = new Intent();
        intent.putExtra("ProcessId", siteInfo.get_id());
        setResult(Constant.REQUESTCODE_CHANGE_SITE, intent);
        finish();
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        siteList = dataManager.getProcessLists();
        myProcessInfoAdapter.setList(siteList);
        myProcessInfoAdapter.notifyDataSetChanged();
    }

    private List<Process> getSwapProcessList(List<Process> siteList) {
        for (int i = 0; i < siteList.size(); i++) {
            if (i == dataManager.getDefaultPro()) {
                int index = siteList.indexOf(siteList.get(i));
                Collections.swap(siteList, 0, index);
            }
        }
        return siteList;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_process;
    }

}
