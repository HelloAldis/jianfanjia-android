package com.jianfanjia.cn.activity.my;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.adapter.MyFavoriteDesignerAdapter;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.bean.MyFavoriteDesigner;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-21 10:22
 */
@EActivity(R.layout.activity_my_favorite_designer)
public class MyFavoriteDesignerActivity extends BaseAnnotationActivity {

    @ViewById(R.id.act_my_favorite_designer_head_layout)
    MainHeadView mainHeadView;

    @ViewById(R.id.act_my_favorite_designer_listview)
    SwipeMenuListView listview;

    MyFavoriteDesigner myFavoriteDesigner;

    @Bean
    MyFavoriteDesignerAdapter myFavoriteDesignerAdapter;

    private SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(MyApplication.dip2px(MyFavoriteDesignerActivity.this, 90f));
            // set a icon
            deleteItem.setIcon(R.mipmap.ic_delete);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    @AfterViews
    protected void init() {
        mainHeadView.setMianTitle(getString(R.string.my_favorite_designer));
        listview.setMenuCreator(creator);
        listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                JianFanJiaClient.deleteFavoriteDesigner(MyFavoriteDesignerActivity.this, myFavoriteDesigner.getDesigners().get(position).get_id(), new ApiUiUpdateListener() {
                    @Override
                    public void preLoad() {
                        showWaitDialog();
                    }

                    @Override
                    public void loadSuccess(Object data) {
                        hideWaitDialog();
                        myFavoriteDesigner.getDesigners().remove(position);
                        myFavoriteDesignerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void loadFailture(String error_msg) {
                        hideWaitDialog();
                        makeTextShort(error_msg);
                    }
                }, MyFavoriteDesignerActivity.this);
                return false;
            }
        });
        listview.setAdapter(myFavoriteDesignerAdapter);
        initData();
    }

    protected void initData() {
        JianFanJiaClient.get_MyFavoriteDesignerList(this, 0, 100, this, this);
    }

    @Click(R.id.head_back_layout)
    protected void back() {
        finish();
    }

    @ItemClick(R.id.act_my_favorite_designer_listview)
    protected void clickItem(DesignerInfo designerInfo) {
        Bundle designerBundle = new Bundle();
        designerBundle.putString(Global.DESIGNER_ID, designerInfo.get_id());
        LogTool.d(this.getClass().getName(), designerInfo.get_id());
        startActivity(DesignerInfoActivity.class, designerBundle);
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        if (data != null) {
            myFavoriteDesigner = JsonParser.jsonToBean(data.toString(), MyFavoriteDesigner.class);
            if (myFavoriteDesigner != null) {
                myFavoriteDesignerAdapter.setDesignerInfos(myFavoriteDesigner.getDesigners());
            }
        }
    }

}
