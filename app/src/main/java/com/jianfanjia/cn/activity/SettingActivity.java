package com.jianfanjia.cn.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.igexin.sdk.PushManager;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.UpdateVersion;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.service.UpdateService;
import com.jianfanjia.cn.tools.FileUtil;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

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
                checkVersion();
                break;
            case R.id.share_layout:
                startActivity(ShareActivity.class);
                break;
            case R.id.clear_cache_layout:
                onClickCleanCache();
                break;
            case R.id.head_back_layout:
                finish();
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
                        logout();
                        dialog.dismiss();
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
        File file = new File(Constant.COMMON_PATH);

        fileSize += FileUtil.getDirSize(filesDir);
        fileSize += FileUtil.getDirSize(file);

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
        dialog.setTitle("清空缓存？");
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

    /**
     * 获取最新版本
     */
    public void showNewVersion(String message, final UpdateVersion updateVersion) {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(SettingActivity.this);
        dialog.setTitle("版本更新");
        dialog.setMessage(message);
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startUpdateService(updateVersion.getDownload_url());
                    }

                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }

    private void startUpdateService(String download_url) {
        if (download_url == null)
            return;
        Intent intent = new Intent(this, UpdateService.class);
        intent.putExtra(Constant.DOWNLOAD_URL, download_url);
        startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogTool.d(TAG, "---onResume()");
        if (isOpen) {
            toggleButton.setChecked(true);
        } else {
            toggleButton.setChecked(false);
        }
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

    // 检查版本
    private void checkVersion() {
        JianFanJiaClient.checkVersion(this, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {
                showWaitDialog("检查新版本");
            }

            @Override
            public void loadSuccess(Object data) {
                hideWaitDialog();
                if (data != null) {
                    UpdateVersion updateVersion = JsonParser
                            .jsonToBean(data.toString(),
                                    UpdateVersion.class);
                    if (updateVersion != null) {
                        if (Integer.parseInt(updateVersion
                                .getVersion_code()) > MyApplication
                                .getInstance().getVersionCode()) {
                            showNewVersion(
                                    "有新的版本啦，版本号："
                                            + updateVersion
                                            .getVersion_name(),
                                    updateVersion);
                        } else {
                            makeTextLong("当前已经是最新版本啦！");
                        }
                    }
                }
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextLong(getString(R.string.tip_error_internet));
                hideWaitDialog();
            }
        }

                , this);
    }

    // 退出登录
    private void logout() {
        JianFanJiaClient.logout(this,
                new ApiUiUpdateListener() {

                    @Override
                    public void preLoad() {
                        showWaitDialog();
                    }

                    @Override
                    public void loadSuccess(Object data) {
                        hideWaitDialog();
                        makeTextLong("退出成功");
                        PushManager.getInstance().stopService(
                                SettingActivity.this);// 完全终止SDK的服务
                        activityManager.exit();
                        startActivity(LoginActivity.class);
                        finish();
                    }

                    @Override
                    public void loadFailture(String error_msg) {
                        hideWaitDialog();
                        makeTextLong(getString(R.string.tip_error_internet));
                    }
                }, this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }
}