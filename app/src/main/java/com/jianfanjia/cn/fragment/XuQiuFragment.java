package com.jianfanjia.cn.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.view.MainHeadView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Description:需求
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
@EFragment(R.layout.fragment_requirement)
public class XuQiuFragment extends BaseFragment implements OnClickListener {
    private static final String TAG = XuQiuFragment.class.getName();

    @ViewById(R.id.req_head)
    protected MainHeadView mainHeadView = null;

    @ViewById(R.id.req_tip)
    protected TextView req_tip;

    @ViewById(R.id.req_publish)
    protected Button req_publish;

    @ViewById
    protected RelativeLayout req_publish_wrap;

    @ViewById
    protected LinearLayout req_listview_wrap;

    @AfterViews
    protected void setVisiable() {
        req_listview_wrap.setVisibility(View.GONE);
        req_publish_wrap.setVisibility(View.VISIBLE);
    }

    @Override
    public void initView(View view) {
    }

    @Click({R.id.req_publish, R.id.head_right_title})
    protected void publish_requirement() {
        makeTextLong("发布需求");
    }

    @AfterViews
    protected void initMainHeadView() {
//        mainHeadView = view.findViewById()
        mainHeadView
                .setMianTitle(getResources().getString(R.string.xuqiu));
        mainHeadView.setRightTitle(getResources().getString(R.string.create));
//        mainHeadView.setRightTextListener(this);
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
        /*switch (v.getId()) {
            case R.id.head_right_title:
                startActivity(FeedBackActivity.class);
                break;
            default:
                break;
        }*/
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_requirement;
    }
}
