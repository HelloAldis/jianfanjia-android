package com.jianfanjia.cn.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.ShareUtil;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.UMSsoHandler;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description:关于
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class AboutActivity extends SwipeBackActivity implements OnClickListener {
    private static final String TAG = AboutActivity.class.getName();

    @Bind(R.id.about_head_layout)
    protected MainHeadView mainHeadView;

    @Bind(R.id.about_version)
    TextView currentVersion;// 当前版本

    @Bind(R.id.follow_weixin_layout)
    RelativeLayout followWeixinLayout;

    @Bind(R.id.follow_weibo_layout)
    RelativeLayout followWeiBoLayout;

    @Bind(R.id.share_layout)
    RelativeLayout shareLayout;

    private String versionInfo;
    private ShareUtil shareUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareUtil = new ShareUtil(this);
        initView();
    }

    public void initView() {
        initMainHeadView();
        versionInfo = String.format(getString(R.string.about_version), MyApplication.getInstance().getVersionName());
        currentVersion.setText(versionInfo);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.about));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @OnClick({R.id.head_back_layout, R.id.share_layout, R.id.follow_weixin_layout, R.id.follow_weibo_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.share_layout:
                shareUtil.shareApp(this, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        LogTool.d("onComplete", "status =" + i);
                    }
                });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMSsoHandler ssoHandler = SocializeConfig.getSocializeConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
