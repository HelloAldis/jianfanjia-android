package com.jianfanjia.cn.designer.activity.my;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.LoginNewActivity_;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.tools.AuthUtil;
import com.jianfanjia.cn.designer.tools.GeTuiManager;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.ShareUtil;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.dialog.CommonDialog;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.androidannotations.annotations.Click;

import butterknife.Bind;

/**
 * Description:设置
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class SettingActivity extends BaseActivity {
    private static final String TAG = SettingActivity.class.getName();

    @Bind(R.id.help_layout)
    RelativeLayout aboutFragment;

    @Bind(R.id.logout_layout)
    RelativeLayout logoutLayout;

    @Bind(R.id.my_setting_head_layout)
    MainHeadView mainHeadView;

    private ShareUtil shareUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareUtil = new ShareUtil(this);
        initView();
    }

    private void initView() {
        initMainHeadView();
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.my_setting));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @Click({R.id.about_layout, R.id.logout_layout, R.id.head_back_layout})
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

}