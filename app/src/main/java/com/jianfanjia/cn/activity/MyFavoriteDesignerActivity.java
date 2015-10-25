package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.jianfanjia.cn.adapter.MyFavoriteDesignerAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.bean.MyFavoriteDesigner;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-21 10:22
 */
@EActivity
public class MyFavoriteDesignerActivity extends BaseActivity {

    @ViewById(R.id.act_my_favorite_designer_head_layout)
    MainHeadView mainHeadView;

    @ViewById(R.id.act_my_favorite_designer_listview)
    ListView listview;

    List<DesignerInfo> designerInfos;

    @Bean
    MyFavoriteDesignerAdapter myFavoriteDesignerAdapter;

    @AfterViews
    protected void init() {
        mainHeadView.setMianTitle("我的意向设计师");
        listview.setAdapter(myFavoriteDesignerAdapter);
        initData();
    }

    protected void initData() {
        JianFanJiaClient.get_MyFavoriteDesignerList(this, "0", "10000", this, this);
    }

    @Click(R.id.head_back)
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
            MyFavoriteDesigner myFavoriteDesigner = JsonParser.jsonToBean(data.toString(), MyFavoriteDesigner.class);
            if (myFavoriteDesigner != null) {
                if (myFavoriteDesigner.getDesigners().size() > 0) {
                    myFavoriteDesignerAdapter.addItems(myFavoriteDesigner.getDesigners());
                } else {

                }
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_favorite_designer;
    }

    @Override
    public void initView() {

    }

    @Override
    public void setListener() {

    }
}
