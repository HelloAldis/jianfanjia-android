package com.jianfanjia.cn.activity.ui.activity.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.activity.base.BaseActivity;
import com.jianfanjia.cn.activity.config.Constant;
import com.jianfanjia.cn.activity.constant.IntentConstant;
import com.jianfanjia.cn.activity.ui.adapter.CollectLoveStyleViewPageAdapter;
import com.jianfanjia.cn.activity.ui.interf.OnItemClickListener;
import com.jianfanjia.api.model.User;
import com.jianfanjia.cn.activity.R;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-14 11:08
 */
public class NewUserCollectLoveStyleActivity extends BaseActivity {

    @Bind(R.id.viewPager)
    ViewPager loveStyleViewPager;

    @Bind(R.id.act_reg_collect_content)
    TextView contentView;

    @Bind(R.id.act_reg_collect_title)
    TextView titleView;

    @Bind(R.id.btn_next)
    Button buttonNext;

    protected String[] decstyles;

    User ownerInfo;

    CollectLoveStyleViewPageAdapter collectLoveStyleViewPageAdapter;

    List<CollectLoveStyleViewPageAdapter.LoveStyleItemInfo> loveStyleItemInfoList = new ArrayList<>();

    ArrayList<String> lovestyleList = new ArrayList<>();

    ArrayList<String> lovestyleNumber = new ArrayList<>();

    protected int[] loveStyleImageIds = new int[]{
            R.mipmap.img_oushi, R.mipmap.img_zhongshi, R.mipmap.img_xiandai,
            R.mipmap.img_dizhonghai, R.mipmap.img_meishi, R.mipmap.img_dongnanya,
            R.mipmap.img_tianyuan
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
    }

    private void initView() {
        decstyles = getResources().getStringArray(R.array.arr_decstyle);
        Intent intent = getIntent();
        ownerInfo = (User) intent.getSerializableExtra(IntentConstant.OWNERINFO);

        titleView.setText(getString(R.string.collect_lovestyle_title));
        contentView.setText(getString(R.string.collect_lovestyle_content));
        notifyViewRefresh();

        for (int imageid : loveStyleImageIds) {
            CollectLoveStyleViewPageAdapter.LoveStyleItemInfo loveStyleItemInfo = new CollectLoveStyleViewPageAdapter
                    .LoveStyleItemInfo();
            loveStyleItemInfo.setImageid(imageid);
            loveStyleItemInfoList.add(loveStyleItemInfo);
        }

        collectLoveStyleViewPageAdapter = new CollectLoveStyleViewPageAdapter(this, loveStyleItemInfoList, new
                OnItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        CollectLoveStyleViewPageAdapter.LoveStyleItemInfo loveStyleItemInfo = loveStyleItemInfoList.get
                                (position);
                        if (loveStyleItemInfo.isSelector()) {
                            loveStyleItemInfo.setIsSelector(false);
                            lovestyleList.remove(decstyles[position]);
                            lovestyleNumber.remove(position + "");
                        } else {
                            if (lovestyleList.size() < Constant.LOVE_STYLE_TOTAL) {
                                loveStyleItemInfo.setIsSelector(true);
                                lovestyleList.add(decstyles[position]);
                                lovestyleNumber.add(position + "");
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

    private void notifyViewRefresh() {
        if (lovestyleList.size() > 0) {
            buttonNext.setEnabled(true);
            contentView.setText(lovestyleList.toString().substring(1, lovestyleList.toString().length() - 1));
            contentView.setTextColor(getResources().getColor(R.color.orange_color));
        } else {
            buttonNext.setEnabled(false);
            contentView.setText(getString(R.string.collect_lovestyle_content));
            contentView.setTextColor(getResources().getColor(R.color.light_black_color));
        }
    }

    @OnClick({R.id.head_back_layout, R.id.btn_next})
    protected void back(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                intentToCollectPerson();
                break;
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
        }
    }

    private void intentToCollectPerson() {
        if (ownerInfo == null) {
            ownerInfo = new User();
        }
        ownerInfo.setDec_styles(lovestyleNumber);
        Bundle ownerBundle = new Bundle();
        ownerBundle.putSerializable(IntentConstant.OWNERINFO, ownerInfo);
        startActivity(NewUserCollectPersonActivity.class, ownerBundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_collect_req;
    }
}
