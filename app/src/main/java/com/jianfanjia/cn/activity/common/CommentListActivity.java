package com.jianfanjia.cn.activity.common;

import android.support.v7.widget.LinearLayoutManager;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.activity.common
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-07 15:21
 */
@EActivity(R.layout.activity_comment_list)
public class CommentListActivity extends SwipeBackActivity{

    @ViewById(R.id.pullrefresh_recycleview)
    protected PullToRefreshRecycleView refreshRecycleView;

    @ViewById(R.id.common_head)
    protected MainHeadView mainHeadView;

    @AfterViews
    protected void initAnnotationView(){
        mainHeadView.setMianTitle(getString(R.string.my_comment));

        refreshRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }



}
