package com.jianfanjia.cn.designer.ui.activity.my;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiClient;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.ui.activity.LoginNewActivity;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.tools.AuthUtil;
import com.jianfanjia.cn.designer.tools.GeTuiManager;
import com.jianfanjia.cn.designer.tools.ShareUtil;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.dialog.CommonDialog;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;
import com.jianfanjia.common.tool.LogTool;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Description:设置
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class SettingActivity extends BaseSwipeBackActivity {
    private static final String TAG = SettingActivity.class.getName();

    @Bind(R.id.about_layout)
    RelativeLayout aboutFragment;

    @Bind(R.id.logout_layout)
    RelativeLayout logoutLayout;

    @Bind(R.id.my_setting_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.cache_size)
    TextView cacheSizeView;

    private ShareUtil shareUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareUtil = new ShareUtil(this);
        initView();
    }

    private void initView() {
        initMainHeadView();
        cacheSizeView.setText(UiHelper.caculateCacheSize());
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.my_setting));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @OnClick({R.id.about_layout, R.id.logout_layout, R.id.head_back_layout,R.id.clear_cache_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_layout:
                startActivity(AboutActivity.class);
                break;
            case R.id.logout_layout:
                onClickExit();
                break;
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.clear_cache_layout:
                onClickCleanCache();
                break;
            default:
                break;
        }
    }

    /**
     * 清空缓存
     */
    private void onClickCleanCache() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(this);
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
                        ApiClient.clearCookie();
                        appManager.finishAllActivity();
                        AuthUtil.getInstance(SettingActivity.this).deleteOauth(SettingActivity.this, SHARE_MEDIA
                                .WEIXIN);
                        startActivity(LoginNewActivity.class);
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

}