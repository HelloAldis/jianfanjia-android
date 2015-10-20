package com.jianfanjia.cn.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jianfanjia.cn.adapter.DesignerByAppointAdapter;
import com.jianfanjia.cn.adapter.DesignerByIntentionInfoAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerByAppointInfo;
import com.jianfanjia.cn.bean.DesignerByIntentionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:预约设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class AppointDesignerActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = AppointDesignerActivity.class.getName();
    private TextView allText = null;
    private TextView moreText = null;
    private ListView marched_designer_listview = null;
    private ListView intention_designer_listview = null;
    private List<DesignerByAppointInfo> marchList = new ArrayList<DesignerByAppointInfo>();
    private List<DesignerByIntentionInfo> intentionList = new ArrayList<DesignerByIntentionInfo>();

    @Override
    public void initView() {
        allText = (TextView) findViewById(R.id.allText);
        moreText = (TextView) findViewById(R.id.moreText);
        marched_designer_listview = (ListView) findViewById(R.id.marched_designer_listview);
        intention_designer_listview = (ListView) findViewById(R.id.intention_designer_listview);
        initMarchList();
        initIntentionList();
    }

    private void initMarchList() {
        for (int i = 0; i < 4; i++) {
            DesignerByAppointInfo info = new DesignerByAppointInfo();
            info.setName("张三" + i);
            info.setMarchDegree("匹配度:100%");
            marchList.add(info);
        }
        DesignerByAppointAdapter adapter = new DesignerByAppointAdapter(AppointDesignerActivity.this, marchList);
        marched_designer_listview.setAdapter(adapter);
    }

    private void initIntentionList() {
        for (int i = 0; i < 4; i++) {
            DesignerByIntentionInfo info = new DesignerByIntentionInfo();
            info.setName("李四" + i);
            info.setStarLevel("三颗星");
            intentionList.add(info);
        }
        DesignerByIntentionInfoAdapter adapter = new DesignerByIntentionInfoAdapter(AppointDesignerActivity.this, intentionList);
        intention_designer_listview.setAdapter(adapter);
    }

    @Override
    public void setListener() {
        allText.setOnClickListener(this);
        moreText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.allText:

                break;
            case R.id.moreText:

                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_appoint_designer;
    }
}
