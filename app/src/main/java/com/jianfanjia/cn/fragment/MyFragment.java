package com.jianfanjia.cn.fragment;

import android.view.View;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.activity.NotifyActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SettingActivity;
import com.jianfanjia.cn.base.BaseFragment;

/**
 * Description:我的
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MyFragment extends BaseFragment {
    private static final String TAG = MyFragment.class.getName();
    private RelativeLayout notifyLayout = null;
    private RelativeLayout my_designer_layout = null;
    private RelativeLayout my_site_layout = null;
    private RelativeLayout setting_layout = null;

    @Override
    public void initView(View view) {
        notifyLayout = (RelativeLayout) view.findViewById(R.id.notify_layout);
        my_designer_layout = (RelativeLayout) view.findViewById(R.id.my_designer_layout);
        my_site_layout = (RelativeLayout) view.findViewById(R.id.my_site_layout);
        setting_layout = (RelativeLayout) view.findViewById(R.id.setting_layout);
    }

    @Override
    public void setListener() {
        notifyLayout.setOnClickListener(this);
        my_designer_layout.setOnClickListener(this);
        my_site_layout.setOnClickListener(this);
        setting_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notify_layout:
                startActivity(NotifyActivity.class);
                break;
            case R.id.my_designer_layout:

                break;
            case R.id.my_site_layout:

                break;
            case R.id.setting_layout:
                startActivity(SettingActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }


}
