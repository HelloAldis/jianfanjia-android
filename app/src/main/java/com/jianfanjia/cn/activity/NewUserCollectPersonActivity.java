package com.jianfanjia.cn.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jianfanjia.cn.adapter.CollectPersonViewPageAdapter;
import com.jianfanjia.cn.base.BaseAnnotationActivity;
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
public class NewUserCollectPersonActivity extends BaseAnnotationActivity {

    @ViewById(R.id.viewPager)
    ViewPager loveStyleViewPager;

    @ViewById(R.id.act_reg_collect_content)
    TextView contentView;

    @ViewById(R.id.act_reg_collect_title)
    TextView titleView;

    @ViewById(R.id.btn_next)
    Button buttonNext;

    List<String> personList = new ArrayList<>();

    CollectPersonViewPageAdapter collectPersonViewPageAdapter;

    List<CollectPersonViewPageAdapter.LoveStyleItemInfo> loveStyleItemInfoList = new ArrayList<>();

    int lastSelectorPos = -1;
    int currentSelcetorPos = -1;

    @StringArrayRes(R.array.arr_person)
    protected String[] persons;

    protected int[] personImageIds = new int[]{
            R.mipmap.img_danshen, R.mipmap.img_twopeople, R.mipmap.img_threepeople,
            R.mipmap.img_threegenerations
    };

    @AfterViews
    protected void initView() {
        titleView.setText(getString(R.string.collect_person_title));
        contentView.setText(getString(R.string.collect_person_content));
        buttonNext.setText(getString(R.string.finish));
        buttonNext.setEnabled(false);

        for (int imageid : personImageIds) {
            CollectPersonViewPageAdapter.LoveStyleItemInfo loveStyleItemInfo = new CollectPersonViewPageAdapter.LoveStyleItemInfo();
            loveStyleItemInfo.setResId(imageid);
            loveStyleItemInfoList.add(loveStyleItemInfo);
        }

        collectPersonViewPageAdapter = new CollectPersonViewPageAdapter(this, loveStyleItemInfoList, new OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                currentSelcetorPos = position;
                CollectPersonViewPageAdapter.LoveStyleItemInfo loveStyleItemInfo = loveStyleItemInfoList.get(position);
                if (lastSelectorPos != -1) {//已经选择了一项
                    if (lastSelectorPos != currentSelcetorPos) {//现在选择的是另外一项
                        loveStyleItemInfo.setIsSelector(true);
                        loveStyleItemInfoList.get(lastSelectorPos).setIsSelector(false);
                        personList.remove(persons[lastSelectorPos]);
                        buttonNext.setEnabled(true);
                        personList.add(persons[position]);
                        contentView.setText(personList.toString());

                        lastSelectorPos = currentSelcetorPos;
                    } else {
                        //现在选择的是同一项，取消选中
                            loveStyleItemInfo.setIsSelector(false);
                            buttonNext.setEnabled(false);
                            contentView.setText(getString(R.string.collect_person_content));
                            personList.remove(persons[position]);

                            currentSelcetorPos = lastSelectorPos = -1;
                    }
                } else {//还没有选择任何项
                    loveStyleItemInfo.setIsSelector(true);
                    buttonNext.setEnabled(true);
                    personList.add(persons[position]);
                    contentView.setText(personList.toString());
                    lastSelectorPos = position;
                }
                collectPersonViewPageAdapter.notifyDataSetChanged();
            }
        });
        loveStyleViewPager.setAdapter(collectPersonViewPageAdapter);
    }

    @Click({R.id.head_back_layout,R.id.btn_next})
    protected void back(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                startActivity(MainActivity.class);
                finish();
                break;
            case R.id.head_back_layout:
                finish();
                break;
        }
    }


}
