package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jianfanjia.cn.adapter.CollectLoveStyleViewPageAdapter;
import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.OnItemClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-14 11:08
 */
@EActivity(R.layout.activity_register_collect_req)
public class NewUserCollectLoveStyleActivity extends BaseAnnotationActivity {

    @ViewById(R.id.viewPager)
    ViewPager loveStyleViewPager;

    @ViewById(R.id.act_reg_collect_content)
    TextView contentView;

    @ViewById(R.id.act_reg_collect_title)
    TextView titleView;

    @ViewById(R.id.btn_next)
    Button buttonNext;

    CollectLoveStyleViewPageAdapter collectLoveStyleViewPageAdapter;

    List<CollectLoveStyleViewPageAdapter.LoveStyleItemInfo> loveStyleItemInfoList = new ArrayList<>();

    List<String> lovestyleList = new ArrayList<>();

    @StringArrayRes(R.array.arr_decstyle)
    protected String[] decstyles;

    protected int[] loveStyleImageIds = new int[]{
            R.mipmap.img_oushi, R.mipmap.img_zhongshi, R.mipmap.img_meishi,
            R.mipmap.img_xiandai, R.mipmap.img_dizhonghai, R.mipmap.img_dongnanya, R.mipmap.img_tianyuan
    };

    @AfterViews
    protected void initView() {
        titleView.setText(getString(R.string.collect_lovestyle_title));
        contentView.setText(getString(R.string.collect_lovestyle_content));
        notifyViewRefresh();

        for (int imageid : loveStyleImageIds) {
            CollectLoveStyleViewPageAdapter.LoveStyleItemInfo loveStyleItemInfo = new CollectLoveStyleViewPageAdapter.LoveStyleItemInfo();
            loveStyleItemInfo.setImageid(imageid);
            loveStyleItemInfoList.add(loveStyleItemInfo);
        }

        collectLoveStyleViewPageAdapter = new CollectLoveStyleViewPageAdapter(this, loveStyleItemInfoList, new OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                CollectLoveStyleViewPageAdapter.LoveStyleItemInfo loveStyleItemInfo = loveStyleItemInfoList.get(position);
                if (loveStyleItemInfo.isSelector()) {
                    loveStyleItemInfo.setIsSelector(false);
                    lovestyleList.remove(decstyles[position]);
                } else {
                    if (lovestyleList.size() < Constant.LOVE_STYLE_TOTAL) {
                        loveStyleItemInfo.setIsSelector(true);
                        lovestyleList.add(decstyles[position]);
                    } else {
                        makeTextShort(getString(R.string.collect_lovestyle_tip));
                        return;
                    }
                }
                notifyViewRefresh();
//                loveStyleItemInfoList.get(position).setIsSelector(true);
                collectLoveStyleViewPageAdapter.notifyDataSetChanged();
            }
        });
        loveStyleViewPager.setAdapter(collectLoveStyleViewPageAdapter);
    }

    protected void notifyViewRefresh(){
        if(lovestyleList.size() > 0){
            buttonNext.setEnabled(true);
            contentView.setText(lovestyleList.toString());
        }else{
            buttonNext.setEnabled(false);
            contentView.setText(getString(R.string.collect_lovestyle_content));
        }
    }

    @Click({R.id.head_back_layout, R.id.btn_next})
    protected void back(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                intentToCollectPerson();
                break;
            case R.id.head_back_layout:
                finish();
                break;
        }
    }

    protected void intentToCollectPerson() {
        Intent intent = new Intent(this, NewUserCollectPersonActivity_.class);
        startActivity(intent);
    }


}
