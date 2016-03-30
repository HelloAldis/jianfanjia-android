package com.jianfanjia.cn.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.jianfanjia.cn.business.DataManagerNew;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.cn.tools.MessageUtil;

/**
 * Description:推送消息监听广播
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 15:30
 */
public class PushMsgReceiver extends BroadcastReceiver {
    private static final String TAG = PushMsgReceiver.class.getName();
    private DataManagerNew dataManager = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        dataManager = DataManagerNew.getInstance();
        //-------------------------------------------------
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            LogTool.d(TAG, "build  = null");
            return;
        }
        LogTool.d(TAG, "onReceive() action=" + bundle.getInt("action"));
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");
                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(
                        context, taskid, messageid, 90001);
                LogTool.d(TAG, "第三方回执接口调用" + (result ? "成功" : "失败"));
                if (payload != null) {
                    String data = new String(payload);
                    LogTool.d(TAG, "receiver payload : " + data);
                    MessageUtil.parseMessage(context,data);
                }
                break;
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                LogTool.d(TAG, "cid:" + cid);
                boolean isLogin = dataManager.isLogin();
                LogTool.d(TAG, "isLogin:" + isLogin);
                if (isLogin) {
                    PushManager.getInstance().bindAlias(context.getApplicationContext(), dataManager.getUserId());
                }
                break;
            case PushConsts.THIRDPART_FEEDBACK:
            /*
             * String appid = bundle.getString("appid"); String taskid =
			 * bundle.getString("taskid"); String actionid =
			 * bundle.getString("actionid"); String result =
			 * bundle.getString("result"); long timestamp =
			 * bundle.getLong("timestamp");
			 *
			 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo",
			 * "taskid = " + taskid); Log.d("GetuiSdkDemo", "actionid = " +
			 * actionid); Log.d("GetuiSdkDemo", "result = " + result);
			 * Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
			 */
                break;
            default:
                break;
        }
    }

}
