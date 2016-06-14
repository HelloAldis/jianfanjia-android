package com.jianfanjia.cn.ui.activity.diary;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.ui.adapter.DiaryDetailInfoAdapter;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.ui.activity.diary
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-14 11:05
 */
public class DiaryDetailInfoActivity extends BaseSwipeBackActivity {


    @Bind(R.id.mainhv_diary)
    MainHeadView mMainHeadView;

    @Bind(R.id.diary_recycleview)
    RecyclerView mRecyclerView;

    @Bind(R.id.add_comment)
    protected EditText commentEdit = null;

    @Bind(R.id.btn_send)
    protected Button btnSend = null;

    private DiaryDetailInfoAdapter mDiaryDetailInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_diary_detailinfo;
    }
}
