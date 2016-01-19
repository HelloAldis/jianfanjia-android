package com.jianfanjia.cn.designer.fragment;

import android.view.View;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.my.CustomerServiceActivity;
import com.jianfanjia.cn.designer.activity.my.SettingActivity;
import com.jianfanjia.cn.designer.base.BaseFragment;

/**
 * Description:更多
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MoreFragment extends BaseFragment {
    private static final String TAG = MoreFragment.class.getName();
    private RelativeLayout collect_layout = null;
    private RelativeLayout setting_layout = null;
    private RelativeLayout kefu_layout = null;
    private RelativeLayout my_info_layout = null;

    @Override
    public void initView(View view) {
        collect_layout = (RelativeLayout) view.findViewById(R.id.collect_layout);
        setting_layout = (RelativeLayout) view.findViewById(R.id.setting_layout);
        my_info_layout = (RelativeLayout) view.findViewById(R.id.frag_my_info_layout);
        kefu_layout = (RelativeLayout) view.findViewById(R.id.kefu_layout);
    }

    @Override
    public void setListener() {
        collect_layout.setOnClickListener(this);
        setting_layout.setOnClickListener(this);
        my_info_layout.setOnClickListener(this);
        kefu_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collect_layout:

                break;
            case R.id.frag_my_info_layout:

                break;
            case R.id.kefu_layout:
                startActivity(CustomerServiceActivity.class);
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
        return R.layout.fragment_more;
    }


}
