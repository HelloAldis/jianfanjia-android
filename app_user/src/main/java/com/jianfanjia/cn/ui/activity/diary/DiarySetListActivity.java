package com.jianfanjia.cn.ui.activity.diary;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import com.jianfanjia.api.model.DiaryInfo;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.adapter.DiarySetListAdapter;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

/**
 * Description: com.jianfanjia.cn.ui.activity.diary
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-13 15:45
 */
public class DiarySetListActivity extends BaseSwipeBackActivity {

    @Bind(R.id.mainhv_diary)
    MainHeadView mMainHeadView;

    @Bind(R.id.pullrefresh_recycleview)
    PullToRefreshRecycleView mRecyclerView;

    private boolean isEditStatus = false;//判断当前页面是否是编辑页面

    private DiarySetListAdapter mDiarySetListAdapter;
    private List<DiaryInfo> mDiaryInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        initMainView();
        initRecycleView();

    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.my_diary));
        mMainHeadView.setRightTitle(getString(R.string.edit));
        mMainHeadView.setRightTitleColor(R.color.grey_color);
        mMainHeadView.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditStatus = !isEditStatus;
                mDiarySetListAdapter.setIsEdit(isEditStatus);
                mDiarySetListAdapter.notifyDataSetChanged();
                if (isEditStatus) {
                    mMainHeadView.setRightTitle(getString(R.string.finish));
                } else {
                    mMainHeadView.setRightTitle(getString(R.string.edit));
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllDiary();
    }

    private void getAllDiary(){

    }

    private void initRecycleView() {
        mDiaryInfoList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getAllDiary();
            }
        });
        mRecyclerView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(this));
        mDiarySetListAdapter = new DiarySetListAdapter(this, mDiaryInfoList, new BaseRecyclerViewAdapter
                .OnItemEditListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(DiarySetInfoActivity.class);
            }

            @Override
            public void onItemAdd() {
                startActivity(AddDiarySetActivity.class);
            }

            @Override
            public void onItemDelete(int position) {
                showTipDialog(position);
            }
        });
        mRecyclerView.setAdapter(mDiarySetListAdapter);

        mDiarySetListAdapter.add(new DiaryInfo());
        mDiarySetListAdapter.add(new DiaryInfo());
    }

    //显示放弃提交提醒
    protected void showTipDialog(final int position) {
        CommonDialog commonDialog = DialogHelper.getPinterestDialogCancelable(this);
        commonDialog.setTitle(R.string.tip_delete_diary_title);
        commonDialog.setMessage(getString(R.string.tip_delete_diary));
        commonDialog.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        commonDialog.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DiaryInfo diaryInfo = mDiaryInfoList.get(position);
//                deleteOneProduct(product.get_id(), position);
            }
        });
        commonDialog.show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_diarylist;
    }
}
