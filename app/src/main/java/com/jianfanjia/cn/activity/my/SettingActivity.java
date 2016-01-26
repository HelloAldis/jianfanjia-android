package com.jianfanjia.cn.activity.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jianfanjia.cn.activity.LoginNewActivity_;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.tools.AuthUtil;
import com.jianfanjia.cn.tools.FileUtil;
import com.jianfanjia.cn.tools.GeTuiManager;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.ShareUtil;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.UMSsoHandler;

import java.io.File;

/**
 * Description:设置
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class SettingActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
    private static final String TAG = SettingActivity.class.getName();
    private RelativeLayout feedbackFragment = null;
    private RelativeLayout aboutFragment = null;
    private ToggleButton toggleButton = null;
    private RelativeLayout toggleRelativeLayout = null;
    private RelativeLayout logoutLayout = null;
    private RelativeLayout helpLayout = null;
    private RelativeLayout current_version_layout = null;
    private RelativeLayout shareLayout = null;
    private RelativeLayout clearCacheLayout = null;
    private TextView currentVersion = null;
    private TextView cacheSizeView = null;
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
        feedbackFragment = (RelativeLayout) findViewById(R.id.feedback_layout);
        helpLayout = (RelativeLayout) findViewById(R.id.help_layout);
        aboutFragment = (RelativeLayout) findViewById(R.id.about_layout);
        toggleButton = (ToggleButton) findViewById(R.id.mespush_toggle);
        toggleRelativeLayout = (RelativeLayout) findViewById(R.id.mespush_layout);
        logoutLayout = (RelativeLayout) findViewById(R.id.logout_layout);
        shareLayout = (RelativeLayout) findViewById(R.id.share_layout);
        current_version_layout = (RelativeLayout) findViewById(R.id.current_version_layout);
        clearCacheLayout = (RelativeLayout) findViewById(R.id.clear_cache_layout);
        cacheSizeView = (TextView) findViewById(R.id.cache_size);
        currentVersion = (TextView) findViewById(R.id.current_version);

        caculateCacheSize();

        if (GeTuiManager.isPushTurnOn(getApplicationContext())) {
            toggleButton.setChecked(true);
        } else {
            toggleButton.setChecked(false);
        }
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_setting_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.my_setting));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {
        feedbackFragment.setOnClickListener(this);
        aboutFragment.setOnClickListener(this);
        helpLayout.setOnClickListener(this);
        toggleButton.setOnCheckedChangeListener(this);
        toggleRelativeLayout.setOnClickListener(this);
        logoutLayout.setOnClickListener(this);
        current_version_layout.setOnClickListener(this);
        shareLayout.setOnClickListener(this);
        clearCacheLayout.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean check) {
        LogTool.d(TAG, "check:" + check);
        if(check){
            GeTuiManager.turnOnPush(getApplicationContext());
        }else{
            GeTuiManager.turnOffPush(getApplicationContext());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback_layout:
                startActivity(FeedBackActivity.class);
                break;
            case R.id.about_layout:
                startActivity(AboutActivity.class);
                break;
            case R.id.help_layout:
                startActivity(HelpActivity.class);
                break;
            case R.id.logout_layout:
                onClickExit();
                break;
            case R.id.current_version_layout:
//                checkVersion();
                break;
            case R.id.share_layout:
                shareUtil.shareApp(this, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        LogTool.d("onComplete","status =" + i);
                    }
                });
                break;
            case R.id.clear_cache_layout:
                onClickCleanCache();
                break;
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.mespush_layout:
                toggleButton.toggle();
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
                        MyApplication.getInstance().clearAppCache();
                        appManager.finishAllActivity();
                        AuthUtil.getInstance(SettingActivity.this).deleteOauth(SettingActivity.this,SHARE_MEDIA.WEIXIN);
                        startActivity(LoginNewActivity_.class);
                        finish();
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }

    /**
     * 计算缓存的大小
     */
    private void caculateCacheSize() {
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = ImageLoader.getInstance().getDiskCache().getDirectory();
        fileSize += FileUtil.getDirSize(filesDir);

        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (MyApplication.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            File externalCacheDir = getExternalCacheDir();
            fileSize += FileUtil.getDirSize(externalCacheDir);
        }
        if (fileSize > 0)
            cacheSize = FileUtil.formatFileSize(fileSize);
        cacheSizeView.setText(cacheSize);
    }

    /**
     * 清空缓存
     */
    private void onClickCleanCache() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(SettingActivity.this);
        dialog.setTitle("清空缓存");
        dialog.setMessage("确定清空缓存吗？");
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyApplication.getInstance().clearAppCache();
                        cacheSizeView.setText("0KB");
                        dialog.dismiss();
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