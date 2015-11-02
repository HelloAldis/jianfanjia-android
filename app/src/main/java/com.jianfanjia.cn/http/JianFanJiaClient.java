package com.jianfanjia.cn.http;

import android.content.Context;
import android.graphics.Bitmap;

import com.jianfanjia.cn.bean.DesignerUpdateInfo;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.http.request.AddCommentRequest;
import com.jianfanjia.cn.http.request.AddPicToCheckRequest;
import com.jianfanjia.cn.http.request.AddPicToSectionItemRequest;
import com.jianfanjia.cn.http.request.AgreeRescheduleRequest;
import com.jianfanjia.cn.http.request.CheckVersionRequest;
import com.jianfanjia.cn.http.request.DeletePicRequest;
import com.jianfanjia.cn.http.request.FeedBackRequest;
import com.jianfanjia.cn.http.request.GetAllRescheduleRequest;
import com.jianfanjia.cn.http.request.GetCommentsRequest;
import com.jianfanjia.cn.http.request.LoginRequest;
import com.jianfanjia.cn.http.request.LogoutRequest;
import com.jianfanjia.cn.http.request.NotifyOwnerCheckRequest;
import com.jianfanjia.cn.http.request.PostRescheduleRequest;
import com.jianfanjia.cn.http.request.PostSectionFinishRequest;
import com.jianfanjia.cn.http.request.ProcessInfoRequest;
import com.jianfanjia.cn.http.request.ProcessListRequest;
import com.jianfanjia.cn.http.request.RefuseRescheduleRequest;
import com.jianfanjia.cn.http.request.RegisterRequest;
import com.jianfanjia.cn.http.request.SendVerificationRequest;
import com.jianfanjia.cn.http.request.UploadPicRequestNew;
import com.jianfanjia.cn.http.request.UploadRegisterIdRequest;
import com.jianfanjia.cn.http.request.UserByDesignerInfoRequest;
import com.jianfanjia.cn.http.request.UserByDesignerInfoUpdateRequest;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.ImageUtil;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zhanghao
 * @ClassName: JianFanJiaApi
 * @Description: http接口类
 * @date 2015-8-19 下午2:17:12
 */
public class JianFanJiaClient {

    public static final String TAG = "JianFanJiaClient.class";

    /**
     * 上传个推clientid
     *
     * @param context
     * @param clientId
     * @param listener
     */
    public static void uploadRegisterId(Context context, String clientId,
                                        LoadDataListener listener, Object tag) {
        UploadRegisterIdRequest uploadRegisterIdRequest = new UploadRegisterIdRequest(context, clientId);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("clientId", clientId);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(uploadRegisterIdRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param listener
     * @author zhanghao
     */
    public static void login(Context context, String username, String password,
                             LoadDataListener listener, Object tag) {
        LoginRequest loginRequest = new LoginRequest(context, username, password);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("phone", username);
            jsonParams.put("pass", password);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(loginRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户登出
     *
     * @param context
     * @param listener
     * @param tag
     */
    public static void logout(Context context, LoadDataListener listener, Object tag) {
//		HttpRestClient.get(context, Url.SIGNOUT_URL, handler);
        LogoutRequest logoutRequest = new LogoutRequest(context);
        LogTool.d(TAG, "logout " + logoutRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(logoutRequest, listener, tag);

    }

    /**
     * 检查版本
     *
     * @param context
     * @param listener
     * @param tag
     */
    public static void checkVersion(Context context,
                                    LoadDataListener listener, Object tag) {
        CheckVersionRequest checkVersionRequest = new CheckVersionRequest(context);
        LogTool.d(TAG, "checkVersion " + checkVersionRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(checkVersionRequest, listener, tag);
    }

    /**
     * 用户反馈
     *
     * @param context
     * @param content
     * @param listener
     * @param tag
     */
    public static void feedBack(Context context, String content, String platform,
                                LoadDataListener listener, Object tag) {
        FeedBackRequest feedBackRequest = new FeedBackRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("content", content);
            jsonParams.put("platform", platform);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(feedBackRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送验证码
     *
     * @param phone
     * @param listener
     * @param tag
     * @author zhanghao
     */
    public static void send_verification(Context context, String phone,
                                         LoadDataListener listener, Object tag) {
        SendVerificationRequest sendVerificationRequest = new SendVerificationRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("phone", phone);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(sendVerificationRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户注册
     *
     * @param registerInfo
     * @param listener
     * @author zhanghao
     */
    public static void register(Context context, RegisterInfo registerInfo,
                                LoadDataListener listener, Object tag) {
        RegisterRequest registerRequest = new RegisterRequest(context);
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(registerRequest, JsonParser.beanToJson(registerInfo), listener, tag);
    }

    /**
     * @param context
     * @param listener
     * @author zhanghao
     * @description 设计师获取个人信息
     */
    public static void get_Designer_Info(Context context,
                                         LoadDataListener listener, Object tag) {
        UserByDesignerInfoRequest userByOwnerInfoRequest = new UserByDesignerInfoRequest(context);
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(userByOwnerInfoRequest, listener, tag);
    }

   /* *//**
     * @param context
     * @param hanlder
     * @author zhanghao
     * @description 设计师获取我的业主
     *//*
    public static void get_Designer_Owner(Context context,
                                          AsyncHttpResponseHandler hanlder) {
        HttpRestClient.get(context, Url.GET_DESIGNER_PROCESS, hanlder);
    }*/

    /**
     * @param context
     * @param listener
     * @author zhanghao
     * @description 获取我的工地列表
     */
    public static void get_Process_List(Context context,
                                        LoadDataListener listener, Object tag) {
        ProcessListRequest processListRequest = new ProcessListRequest(context);
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(processListRequest, listener, tag);
    }

    /**
     * 用户提交改期
     *
     * @param context
     * @param processId
     * @param userId
     * @param designerId
     * @param section
     * @param newDate
     * @param listener
     */
    public static void postReschedule(Context context, String processId,
                                      String userId, String designerId, String section, String newDate,
                                      LoadDataListener listener, Object tag) {
        PostRescheduleRequest postRescheduleRequest = new PostRescheduleRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("processid", processId);
            jsonParams.put("userid", userId);
            jsonParams.put("designerid", designerId);
            jsonParams.put("section", section);
            jsonParams.put("new_date",
                    DateFormatTool.covertStringToLong(newDate));
            LogTool.d("JianFanJiaApiClient", "jsonParams：" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(postRescheduleRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户同意改期
     *
     * @param context
     * @param processid
     * @param listener
     */
    public static void agreeReschedule(Context context, String processid,
                                       LoadDataListener listener, Object tag) {
        AgreeRescheduleRequest agreeRescheduleRequest = new AgreeRescheduleRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("processid", processid);
            LogTool.d("JianFanJiaApiClient", "jsonParams：" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(agreeRescheduleRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户拒绝改期
     *
     * @param context
     * @param processid
     * @param listener
     */
    public static void refuseReschedule(Context context, String processid,
                                        LoadDataListener listener, Object tag) {
        RefuseRescheduleRequest refuseRescheduleRequest = new RefuseRescheduleRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("processid", processid);
            LogTool.d("JianFanJiaApiClient", "jsonParams：" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(refuseRescheduleRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户获取我的改期提醒
     *
     * @param context
     * @param listener
     */
    public static void rescheduleAll(Context context,
                                     LoadDataListener listener, Object tag) {
//        HttpRestClient.get(context, Url.GET_RESCHDULE_ALL, hanlder);
        GetAllRescheduleRequest getAllRescheduleRequest = new GetAllRescheduleRequest(context);
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(getAllRescheduleRequest, listener, tag);
    }

    /**
     * 用户上传图片
     *
     * @param context
     * @param listener
     */
    public static void uploadImage(Context context, Bitmap bitmap,
                                   LoadDataListener listener, Object tag) {
        UploadPicRequestNew uploadPicRequest = new UploadPicRequestNew(context);
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(uploadPicRequest, ImageUtil.transformBitmapToBytes(bitmap), listener, tag);
    }

    /**
     * 用户上传图片到装修流程
     *
     * @param context
     * @param siteId
     * @param section
     * @param item
     * @param imageId
     * @param listener
     */
    public static void submitImageToProcess(Context context, String siteId,
                                            String section, String item, String imageId,
                                            LoadDataListener listener, Object tag) {
        AddPicToSectionItemRequest addPicToSectionItemRequest = new AddPicToSectionItemRequest(context, siteId, section, item, imageId);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", siteId);
            jsonParams.put("section", section);
            jsonParams.put("item", item);
            jsonParams.put("imageid", imageId);
            LogTool.d(TAG, "submitImageToProcess -" + addPicToSectionItemRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(addPicToSectionItemRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设计师提交验收图片
     *
     * @param context
     * @param siteId
     * @param section
     * @param key
     * @param imageId
     * @param listener
     */
    public static void submitYanShouImage(Context context, String siteId,
                                          String section, String key, String imageId,
                                          LoadDataListener listener, Object tag) {
        AddPicToCheckRequest addPicToCheckRequest = new AddPicToCheckRequest(context, siteId, section, key, imageId);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", siteId);
            jsonParams.put("section", section);
            jsonParams.put("key", key);
            jsonParams.put("imageid", imageId);
            LogTool.d(TAG, "submitYanShouImage -" + addPicToCheckRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(addPicToCheckRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户完工装修流程小节点
     *
     * @param context
     * @param siteId
     * @param section
     * @param item
     * @param listener
     */
    public static void processItemDone(Context context, String siteId,
                                       String section, String item, LoadDataListener listener, Object tag) {
        PostSectionFinishRequest postSectionFinishRequest = new PostSectionFinishRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", siteId);
            jsonParams.put("section", section);
            jsonParams.put("item", item);
            LogTool.d(TAG, "processItemDone -" + postSectionFinishRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(postSectionFinishRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据id拿到工地信息
     *
     * @param context
     * @param processid
     * @param listener
     */
    public static void get_ProcessInfo_By_Id(Context context, String processid,
                                             LoadDataListener listener, Object tag) {
        ProcessInfoRequest processInfoRequest = new ProcessInfoRequest(context, processid);
        String getProcessUrl = Url.GET_PROCESSINFO_BYID.replace(Url.ID,
                processid);
        processInfoRequest.setUrl(getProcessUrl);
        LogTool.d(TAG, "processItemDone -" + processInfoRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(processInfoRequest, listener, tag);
    }

    /**
     * 修改设计师个人信息
     *
     * @param context
     * @param designerInfo
     * @param listener
     */
    public static void put_DesignerInfo(Context context,
                                        DesignerUpdateInfo designerInfo, LoadDataListener listener, Object tag) {
        UserByDesignerInfoUpdateRequest userByDesignerInfoUpdateRequest = new UserByDesignerInfoUpdateRequest(context, designerInfo);
        LogTool.d(TAG, "put_OwnerInfo -" + userByDesignerInfoUpdateRequest.getUrl());
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(userByDesignerInfoUpdateRequest, JsonParser.beanToJson(userByDesignerInfoUpdateRequest), listener, tag);
    }


    /**
     * 设计师提醒业主验收
     *
     * @param context
     * @param processid
     * @param section
     * @param listener
     */
    public static void confirm_canCheckBydesigner(Context context,
                                                  String processid, String section, LoadDataListener listener, Object tag) {
        NotifyOwnerCheckRequest notifyOwnerCheckRequest = new NotifyOwnerCheckRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", processid);
            jsonParams.put("section", section);
            LogTool.d(TAG, "confirm_canCheckBydesigner -" + notifyOwnerCheckRequest.getUrl() + "----" + jsonParams.toString());

            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(notifyOwnerCheckRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        /**
     * 设计师删除验收照片
     *
     * @param context
     * @param processId
     * @param section
     * @param key
     * @param listener
     * @param tag
     */
    public static void deleteYanshouImgByDesigner(Context context,
                                                  String processId, String section, String key,
                                                  LoadDataListener listener, Object tag) {
        DeletePicRequest deletePicRequest = new DeletePicRequest(context, processId, section, key);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", processId);
            jsonParams.put("section", section);
            jsonParams.put("key", key);
            LogTool.d(TAG, "deleteYanshouImgByDesigner -" + deletePicRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(deletePicRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户添加评论留言
     *
     * @param context
     * @param topicid
     * @param topictype
     * @param content
     * @param to
     * @param listener
     * @param tag
     */
    public static void addComment(Context context, String topicid, String topictype,String section,String item, String content, String to, LoadDataListener listener, Object tag) {
        AddCommentRequest addCommentRequest = new AddCommentRequest(context, topicid, topictype, content, to);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("topicid", topicid);
            jsonParams.put("topictype", topictype);
            jsonParams.put("content", content);
            jsonParams.put("section", section);
            jsonParams.put("item", item);
            jsonParams.put("to", to);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(addCommentRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户获取留言评论并标记为已读
     *
     * @param context
     * @param topicid
     * @param from
     * @param limit
     * @param listener
     * @param tag
     */
    public static void getCommentList(Context context, String topicid, int from, int limit,String section,String item, LoadDataListener listener, Object tag) {
        GetCommentsRequest getCommentsRequest = new GetCommentsRequest(context, topicid, from, limit);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("topicid", topicid);
            jsonParams.put("from", from);
            jsonParams.put("section", section);
            jsonParams.put("item", item);
            jsonParams.put("limit", limit);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getCommentsRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
