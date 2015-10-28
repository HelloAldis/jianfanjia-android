package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.jianfanjia.cn.adapter.DesignerByAppointAdapter;
import com.jianfanjia.cn.adapter.DesignerByIntentionInfoAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerCanOrderInfo;
import com.jianfanjia.cn.bean.DesignerCanOrderListInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:替换设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class ReplaceDesignerActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    private static final String TAG = ReplaceDesignerActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private TextView moreText = null;
    private ListView marched_designer_listview = null;
    private ListView intention_designer_listview = null;
    private List<DesignerCanOrderInfo> rec_designer = new ArrayList<DesignerCanOrderInfo>();
    private List<DesignerCanOrderInfo> favorite_designer = new ArrayList<DesignerCanOrderInfo>();
    private DesignerByAppointAdapter designerByAppointAdapter = null;
    private DesignerByIntentionInfoAdapter designerByIntentionInfoAdapter = null;
    private String requestmentid = null;
    private String designerid = null;
    private int totalCount = 1;//总可预约数

    private List<String> designerids = new ArrayList<String>();

    @Override
    public void initView() {
        initMainHeadView();
        moreText = (TextView) findViewById(R.id.moreText);
        marched_designer_listview = (ListView) findViewById(R.id.marched_designer_listview);
        intention_designer_listview = (ListView) findViewById(R.id.intention_designer_listview);
        Intent intent = this.getIntent();
        requestmentid = intent.getStringExtra(Global.REQUIREMENT_ID);
        designerid = intent.getStringExtra(Global.DESIGNER_ID);
        LogTool.d(TAG, "requestmentid:" + requestmentid + " designerid:" + designerid);

        getOrderDesignerList(requestmentid);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_appoint_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setRightTextListener(this);
        mainHeadView
                .setMianTitle(totalCount + getResources().getString(R.string.appoint));
        mainHeadView.setRightTitle(getResources().getString(R.string.appointText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {
        intention_designer_listview.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            case R.id.moreText:
                makeTextLong("更多");
                break;
            case R.id.head_right_title:

                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DesignerCanOrderInfo info = favorite_designer.get(position);
        String designerid = info.get_id();
        CheckBox ctb = (CheckBox) view.findViewById(R.id.list_item_check);
        ctb.toggle();
        // 将CheckBox的选中状况记录下来
        designerByIntentionInfoAdapter.getIsSelected().put(position, ctb.isChecked());
        // 调整选定条目
        if (ctb.isChecked()) {
            designerids.add(designerid);
            totalCount--;
        } else {
            designerids.remove(designerid);
            totalCount++;
        }
        dataNotifyChanged();
    }

    private void dataNotifyChanged() {
        designerByIntentionInfoAdapter.notifyDataSetChanged();
        LogTool.d(TAG, "totalCount=" + totalCount);
        mainHeadView
                .setMianTitle(totalCount + getResources().getString(R.string.appoint));
    }

    //获取自己可以预约的设计师
    private void getOrderDesignerList(String requestmentid) {
        JianFanJiaClient.getOrderDesignerListByUser(ReplaceDesignerActivity.this, requestmentid, getOrderDesignerListener, this);
    }

    private ApiUiUpdateListener getOrderDesignerListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            showWaitDialog(R.string.loding);
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data);
            hideWaitDialog();
            DesignerCanOrderListInfo designerCanOrderListInfo = JsonParser.jsonToBean(data.toString(), DesignerCanOrderListInfo.class);
            LogTool.d(TAG, "designerCanOrderListInfo:" + designerCanOrderListInfo);
            if (null != designerCanOrderListInfo) {
                rec_designer = designerCanOrderListInfo.getRec_designer();
                favorite_designer = designerCanOrderListInfo.getFavorite_designer();
                designerByAppointAdapter = new DesignerByAppointAdapter(ReplaceDesignerActivity.this, rec_designer);
                marched_designer_listview.setAdapter(designerByAppointAdapter);
                designerByIntentionInfoAdapter = new DesignerByIntentionInfoAdapter(ReplaceDesignerActivity.this, favorite_designer);
                intention_designer_listview.setAdapter(designerByIntentionInfoAdapter);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            hideWaitDialog();
        }
    };

    private void replaceDesignerByUser(String requirementid, String old_designerid, String new_designerid) {
        JianFanJiaClient.ChangeOrderedDesignerByUser(ReplaceDesignerActivity.this, requirementid, old_designerid, new_designerid, replaceDesignerListener, this);
    }

    private ApiUiUpdateListener replaceDesignerListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            showWaitDialog(R.string.submiting);
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data);
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            hideWaitDialog();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_replace_designer;
    }
}
