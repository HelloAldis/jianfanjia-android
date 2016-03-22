package com.jianfanjia.cn.activity.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.activity.LoginNewActivity_;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.tools.AuthUtil;
import com.jianfanjia.cn.tools.GeTuiManager;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.ShareUtil;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * Description:设置
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class SettingActivity extends SwipeBackActivity implements OnClickListener {
    private static final String TAG = SettingActivity.class.getName();
    private RelativeLayout aboutFragment = null;
    private RelativeLayout logoutLayout = null;
    private RelativeLayout helpLayout = null;
    private RelativeLayout shareLayout = null;
    private MainHeadView mainHeadView = null;
    private ShareUtil shareUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareUtil = new ShareUtil(this);
    }

    @Override
    public void initView() {
        initMainHeadView();
        helpLayout = (RelativeLayout) findViewById(R.id.help_layout);
        aboutFragment = (RelativeLayout) findViewById(R.id.about_layout);
        logoutLayout = (RelativeLayout) findViewById(R.id.logout_layout);
        shareLayout = (RelativeLayout) findViewById(R.id.share_layout);

    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_setting_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.more));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {
        aboutFragment.setOnClickListener(this);
        helpLayout.setOnClickListener(this);
        logoutLayout.setOnClickListener(this);
        shareLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_layout:
                startActivity(AboutActivity.class);
                break;
            case R.id.help_layout:
                startActivity(HelpActivity.class);
                break;
            case R.id.logout_layout:
                onClickExit();
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
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }
    }

    private void onClickExit() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(SettingActivity.this);
        dialog.setTitle("退出登录");
        dialog.setMessage("确定退出登录吗？");
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        GeTuiManager.cancelBind(getApplicationContext(), dataManager.getUserId());
                        dataManager.cleanData();
                        MyApplication.getInstance().clearCookie();
//                        MyApplication.getInstance().clearAppCache();
                        appManager.finishAllActivity();
                        AuthUtil.getInstance(SettingActivity.this).deleteOauth(SettingActivity.this, SHARE_MEDIA
                                .WEIXIN);
                        startActivity(LoginNewActivity_.class);
                        finish();
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogTool.d(TAG, "---onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogTool.d(TAG, "---onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogTool.d(TAG, "---onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogTool.d(TAG, "---onDestroy()");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
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