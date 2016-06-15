package com.jianfanjia.cn.ui.activity.diary;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.ui.adapter.DiarySetInfoAdapter;
import com.jianfanjia.cn.view.recycleview.itemdecoration.HorizontalDividerDecoration;
import com.jianfanjia.common.tool.TDevice;

/**
 * Description: com.jianfanjia.cn.ui.activity.diary
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-14 11:01
 */
public class DiarySetInfoActivity extends BaseSwipeBackActivity {

    @Bind(R.id.diary_recycleview)
    RecyclerView mRecyclerView;

    private DiarySetInfoAdapter mDiaryAdapter;

    private List<DiarySetInfo> mDiarySetInfoList;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        toolbar.setNavigationIcon(R.mipmap.icon_back);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRecyclerView();
    }

    private void initRecyclerView() {
        mDiarySetInfoList = new ArrayList<>();
        mDiaryAdapter = new DiarySetInfoAdapter(this, mDiarySetInfoList, new BaseRecyclerViewAdapter.OnItemEditListener() {


            @Override
            public void onItemAdd() {
                startActivity(AddDiaryActivity.class);
            }

            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onItemDelete(int position) {

            }
        });
        mDiaryAdapter.add(new DiarySetInfo());
        mDiaryAdapter.add(new DiarySetInfo());
        mDiaryAdapter.add(new DiarySetInfo());
        mDiaryAdapter.add(new DiarySetInfo());
        mDiaryAdapter.add(new DiarySetInfo());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new HorizontalDividerDecoration(TDevice.dip2px(this, 10),
                0, TDevice.dip2px(this, 10)));
        mRecyclerView.setAdapter(mDiaryAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_diaryinfo;
    }
}
