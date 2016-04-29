package com.jianfanjia.cn.supervisor.activity.my;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.application.MyApplication;
import com.jianfanjia.cn.supervisor.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description:关于
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class AboutActivity extends BaseSwipeBackActivity implements OnClickListener {
    private static final String TAG = AboutActivity.class.getName();

    @Bind(R.id.about_head_layout)
    protected MainHeadView mainHeadView;

    @Bind(R.id.about_version)
    TextView currentVersion;// 当前版本

    private String versionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        initMainHeadView();
        versionInfo = String.format(getString(R.string.about_version), MyApplication.getInstance().getVersionName());
        currentVersion.setText(versionInfo);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.more));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @OnClick({R.id.head_back_layout,R.id.follow_weixin_layout, R.id.follow_weibo_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.follow_weixin_layout:
                UiHelper.copy(getString(R.string.jianfanjia_weixin), this);
                break;
            case R.id.follow_weibo_layout:
                startActivity(AboutWeiBoActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }
}
