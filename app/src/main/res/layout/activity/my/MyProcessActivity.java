package layout.activity.my;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.activity.requirement.MyProcessDetailActivity_;
import com.jianfanjia.cn.adapter.MyProcessInfoAdapter;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

import java.util.List;

/**
 * @author fengliang
 * @ClassName: DesignerSiteActivity
 * @Description: 设计师工地
 * @date 2015-9-11 上午10:02:42
 */
public class MyProcessActivity extends SwipeBackActivity implements
        OnClickListener, OnItemClickListener, ApiUiUpdateListener {
    private static final String TAG = MyProcessActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private ListView siteListView = null;
    private List<ProcessInfo> siteList;
    private MyProcessInfoAdapter myProcessInfoAdapter = null;

    @Override
    public void initView() {
        initMainHeadView();
        siteListView = (ListView) findViewById(R.id.designer_site_listview);
//        siteList = dataManager.getProcessLists();
        if (siteList == null) {
            JianFanJiaClient.get_Process_List(this, this, this);
        }
        myProcessInfoAdapter = new MyProcessInfoAdapter(
                MyProcessActivity.this, siteList);
        siteListView.setAdapter(myProcessInfoAdapter);
    }

   /* private void comeMainActivity() {
        Intent intent = new Intent(MyProcessActivity.this, MainActivity.class);
        intent.putExtra(Constant.TAB_POSITION, Constant.MANAGE);
        startActivity(intent);
        finish();
    }*/

    @SuppressLint("ResourceAsColor")
    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.designer_site_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setMianTitle(getResources().getString(
                R.string.my_decoration_site));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
    }

    @Override
    public void setListener() {
        siteListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        ProcessInfo processInfo= siteList.get(position);
        LogTool.d(TAG, "_id=" + processInfo.get_id());
        Bundle bundle = new Bundle();
        bundle.putSerializable(Global.PROCESS_INFO,processInfo);
        startActivity(MyProcessDetailActivity_.class,bundle);
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        if (data != null) {
            siteList = JsonParser.jsonToList(data.toString(),
                    new TypeToken<List<ProcessInfo>>() {
                    }.getType());
            myProcessInfoAdapter.setList(siteList);
            myProcessInfoAdapter.notifyDataSetChanged();
        }
    }

   /* private List<Process> getSwapProcessList(List<Process> siteList) {
        for (int i = 0; i < siteList.size(); i++) {
            if (i == dataManager.getDefaultPro()) {
                int index = siteList.indexOf(siteList.get(i));
                Collections.swap(siteList, 0, index);
            }
        }
        return siteList;
    }*/

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_process;
    }

}
