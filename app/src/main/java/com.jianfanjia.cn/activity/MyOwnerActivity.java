package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.adapter.MyOwerInfoAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

import java.util.List;

/**
 * @author fengliang
 * @ClassName: MyOwnerActivity
 * @Description:我的业主
 * @date 2015-9-16 上午9:31:59
 */
public class MyOwnerActivity extends BaseActivity implements OnClickListener,
        OnItemClickListener {
    private static final String TAG = MyOwnerActivity.class.getName();
    public static final String PROCESS = "process";
    private ListView ownerListView = null;
    private List<Process> ownerList;
    private MyOwerInfoAdapter myOwerInfoAdapter = null;
    private MainHeadView mainHeadView = null;
    protected RelativeLayout error_Layout;

    @Override
    public void initView() {
        initMainHeadView();
        ownerListView = (ListView) findViewById(R.id.my_ower_listview);
        // get_Designer_Owner();
        ownerList = dataManager.getProcessLists();
        if (ownerList == null && ownerList.size() == 0) {
            /*LoadClientHelper.requestProcessList(this, new ProcessListRequest(
					this), this);*/
            JianFanJiaClient.get_Process_List(this, this, this);
        }
        myOwerInfoAdapter = new MyOwerInfoAdapter(MyOwnerActivity.this,
                ownerList);
        ownerListView.setAdapter(myOwerInfoAdapter);
        setEmptyView();
    }

    private void setEmptyView() {
        error_Layout = (RelativeLayout) findViewById(R.id.error_include);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_ower_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setMianTitle(getResources().getString(R.string.my_ower));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {
        ownerListView.setOnItemClickListener(this);
        error_Layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            case R.id.error_include:
                JianFanJiaClient.get_Process_List(this, this, this);
                break;
            default:
                break;
        }
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        ownerList = dataManager.getProcessLists();
        myOwerInfoAdapter.setList(ownerList);
        myOwerInfoAdapter.notifyDataSetChanged();
        error_Layout.setVisibility(View.GONE);
    }

    @Override
    public void loadFailture(String errorMsg) {
        super.loadFailture(errorMsg);
        error_Layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        Process info = ownerList.get(position);
        String ownerId = info.get_id();
        LogTool.d(TAG, "info=" + ownerId);
        Intent intent = new Intent(MyOwnerActivity.this,
                OwnerInfoActivity.class);
        intent.putExtra(PROCESS, info);
        startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_owner;
    }

}
