package com.jianfanjia.cn.activity.ui.activity.home;

import android.os.Bundle;

import butterknife.OnClick;
import com.jianfanjia.cn.activity.AppManager;
import com.jianfanjia.cn.activity.tools.IntentUtil;
import com.jianfanjia.cn.activity.ui.activity.requirement.PublishRequirementActivity;
import com.jianfanjia.cn.activity.ui.activity.requirement.UpdateRequirementActivity;
import com.jianfanjia.cn.activity.R;

/**
 * Description: com.jianfanjia.cn.activity.home
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-04-08 14:35
 */
public class WebViewPackage365Activity extends WebViewActivity {

    private boolean isPublishOrUpdateRequirementInTop = false;//判断是否由需求相关屏启动

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getActionType();
        super.onCreate(savedInstanceState);
    }

    private void getActionType() {
        appManager = AppManager.getAppManager();
        if (appManager.currentActivity() instanceof PublishRequirementActivity || appManager.currentActivity()
                instanceof UpdateRequirementActivity) {
            isPublishOrUpdateRequirementInTop = true;
        }
    }

    @OnClick(R.id.btn_create_process)
    protected void createRequirement() {
        if (!isPublishOrUpdateRequirementInTop) {
            IntentUtil.startActivity(this, PublishRequirementActivity.class);
        }
        appManager.finishActivity(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_package365_detail;
    }
}
