package com.jianfanjia.cn.fragment;

import android.view.View;
import android.view.View.OnClickListener;

import com.jianfanjia.cn.activity.FeedBackActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description:需求
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class XuQiuFragment extends BaseFragment  implements OnClickListener{
    private static final String TAG = XuQiuFragment.class.getName();
    private MainHeadView mainHeadView = null;

    @Override
    public void initView(View view) {
        initMainHeadView(view);
    }
    private void initMainHeadView(View view) {
        mainHeadView = (MainHeadView) view.findViewById(R.id.my_xuqiu_head_layout);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.xuqiu));
        mainHeadView.setRightTitle(getResources().getString(R.string.create));
        mainHeadView.setRightTextListener(this);
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.GONE);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }
    @Override
    public void setListener() {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_right_title:
                startActivity(FeedBackActivity.class);
                break;
            default:
                break;
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_xu_qiu;
    }
}
