package com.jianfanjia.cn.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.jianfanjia.cn.adapter.CollectAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.CollectionInfo;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description:我的收藏
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class CollectActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = CollectActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private RecyclerView collectionListview = null;
    private LinearLayoutManager mLayoutManager = null;
    private CollectAdapter collectAdapter = null;

    @Override
    public void initView() {
        initMainHeadView();
        collectionListview = (RecyclerView) findViewById(R.id.collection_listview);
        mLayoutManager = new LinearLayoutManager(CollectActivity.this);
        collectionListview.setLayoutManager(mLayoutManager);
        collectionListview.setItemAnimator(new DefaultItemAnimator());
        collectionListview.setHasFixedSize(true);
        getCollectionList(0, 5, getCollectionListListener);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_collect_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.my_favorite));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {

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

    private void getCollectionList(int from, int limit, ApiUiUpdateListener listener) {
        JianFanJiaClient.getCollectListByUser(CollectActivity.this, from, limit, listener, this);
    }

    private ApiUiUpdateListener getCollectionListListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            CollectionInfo collectionInfo = JsonParser.jsonToBean(data.toString(), CollectionInfo.class);
            LogTool.d(TAG, "collectionInfo:" + collectionInfo);
            if (null != collectionInfo) {
                LogTool.d(TAG, "collectionInfo=" + collectionInfo.getProducts());
                collectAdapter = new CollectAdapter(CollectActivity.this, collectionInfo.getProducts());
                collectionListview.setAdapter(collectAdapter);
            }
        }

        @Override
        public void loadFailture(String error_msg) {

        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_collect;
    }
}
