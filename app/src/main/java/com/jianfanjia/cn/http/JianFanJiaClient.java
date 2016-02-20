package com.jianfanjia.cn.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.jianfanjia.cn.bean.CommitCommentInfo;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.bean.OwnerUpdateInfo;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.bean.WeiXinRegisterInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.http.request.AddBeautyImgRequest;
import com.jianfanjia.cn.http.request.AddCollectionRequest;
import com.jianfanjia.cn.http.request.AddCommentRequest;
import com.jianfanjia.cn.http.request.AddFavoriteDesignerRequest;
import com.jianfanjia.cn.http.request.AddPicToSectionItemRequest;
import com.jianfanjia.cn.http.request.AgreeRescheduleRequest;
import com.jianfanjia.cn.http.request.BindingPhoneRequest;
import com.jianfanjia.cn.http.request.BindingWeiXinRequest;
import com.jianfanjia.cn.http.request.ChangeOrderedDesignerRequest;
import com.jianfanjia.cn.http.request.CheckVersionRequest;
import com.jianfanjia.cn.http.request.ChoosePlanByUserRequest;
import com.jianfanjia.cn.http.request.CommitCommentRequest;
import com.jianfanjia.cn.http.request.ConformMeasureHouseRequest;
import com.jianfanjia.cn.http.request.DeleteBeautyImgRequest;
import com.jianfanjia.cn.http.request.DeleteCollectionRequest;
import com.jianfanjia.cn.http.request.DeleteFavoriteDesignerRequest;
import com.jianfanjia.cn.http.request.DeletePicToSectionItemRequest;
import com.jianfanjia.cn.http.request.DesignerHomePageRequest;
import com.jianfanjia.cn.http.request.EvaluateDesignerRequest;
import com.jianfanjia.cn.http.request.FavoriteDesignerListRequest;
import com.jianfanjia.cn.http.request.FeedBackRequest;
import com.jianfanjia.cn.http.request.ForgetPswRequest;
import com.jianfanjia.cn.http.request.GetAllRescheduleRequest;
import com.jianfanjia.cn.http.request.GetBeautyImgListRequest;
import com.jianfanjia.cn.http.request.GetCollectionRequest;
import com.jianfanjia.cn.http.request.GetCommentsRequest;
import com.jianfanjia.cn.http.request.GetContractRequest;
import com.jianfanjia.cn.http.request.GetDecorationImgRequest;
import com.jianfanjia.cn.http.request.GetDesignerPlansByUserRequest;
import com.jianfanjia.cn.http.request.GetHomeProductRequest;
import com.jianfanjia.cn.http.request.GetOrderDesignerListByUserRequest;
import com.jianfanjia.cn.http.request.GetOrderedDesignerRequest;
import com.jianfanjia.cn.http.request.GetPlanInfoRequest;
import com.jianfanjia.cn.http.request.GetProcessInfoRequest;
import com.jianfanjia.cn.http.request.GetProcessListRequest;
import com.jianfanjia.cn.http.request.GetProductHomePageRequest;
import com.jianfanjia.cn.http.request.GetRequirementListRequest;
import com.jianfanjia.cn.http.request.HomePageRequest;
import com.jianfanjia.cn.http.request.LoginRequest;
import com.jianfanjia.cn.http.request.LogoutRequest;
import com.jianfanjia.cn.http.request.OrderDesignerByUserRequest;
import com.jianfanjia.cn.http.request.OwnerFinishCheckRequest;
import com.jianfanjia.cn.http.request.PostCollectOwnerInfoRequest;
import com.jianfanjia.cn.http.request.PostProcessRequest;
import com.jianfanjia.cn.http.request.PostRequirementRequest;
import com.jianfanjia.cn.http.request.PostRescheduleRequest;
import com.jianfanjia.cn.http.request.RefreshSessionRequest;
import com.jianfanjia.cn.http.request.RefuseRescheduleRequest;
import com.jianfanjia.cn.http.request.RegisterRequest;
import com.jianfanjia.cn.http.request.SearchDecorationImgRequest;
import com.jianfanjia.cn.http.request.SearchDesignerProductRequest;
import com.jianfanjia.cn.http.request.SearchDesignerRequest;
import com.jianfanjia.cn.http.request.SendVerificationRequest;
import com.jianfanjia.cn.http.request.UpdateOwnerInfoRequest;
import com.jianfanjia.cn.http.request.UpdateRequirementRequest;
import com.jianfanjia.cn.http.request.UploadPicRequestNew;
import com.jianfanjia.cn.http.request.UploadRegisterIdRequest;
import com.jianfanjia.cn.http.request.UserByOwnerInfoRequest;
import com.jianfanjia.cn.http.request.VerifyPhoneRequest;
import com.jianfanjia.cn.http.request.WeiXinLoginRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.ImageUtil;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author zhanghao
 * @ClassName: JianFanJiaApi
 * @Description: http接口类
 * @date 2015-8-19 下午2:17:12
 */
public class JianFanJiaClient {
    public static final String TAG = JianFanJiaClient.class.getName();

    /**
     * 上传个推clientid
     *
     * @param context
     * @param clientId
     * @param listener
     */
    public static void uploadRegisterId(Context context, String clientId,
                                        ApiUiUpdateListener listener, Object tag) {
        UploadRegisterIdRequest uploadRegisterIdRequest = new UploadRegisterIdRequest(context, clientId);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("cid", clientId);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(uploadRegisterIdRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查手机号是否被占用
     *
     * @param context
     * @param phone
     * @param listener
     * @param tag
     */
    public static void verifyPhone(Context context, String phone, ApiUiUpdateListener listener, Object tag) {
        VerifyPhoneRequest verifyPhoneRequest = new VerifyPhoneRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("phone", phone);
            LogTool.d(TAG, "verifyPhone --" + verifyPhoneRequest.getUrl() + "---" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(verifyPhoneRequest, jsonParams.toString(), listener, tag);
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
                             ApiUiUpdateListener listener, Object tag) {
        LoginRequest loginRequest = new LoginRequest(context, username, password);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("phone", username);
            jsonParams.put("pass", password);
            LogTool.d(TAG, "login --" + loginRequest.getUrl() + "---" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(loginRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void weixinLogin(Context context, WeiXinRegisterInfo weiXinRegisterInfo, ApiUiUpdateListener listener, Object tag) {
        WeiXinLoginRequest weiXinLoginRequest = new WeiXinLoginRequest(context, weiXinRegisterInfo);
        LogTool.d(TAG, "weixinLogin --" + weiXinLoginRequest.getUrl() + "---" + JsonParser.beanToJson(weiXinRegisterInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(weiXinLoginRequest, JsonParser.beanToJson(weiXinRegisterInfo), listener, tag);
    }

    /**
     * 用户登出
     *
     * @param context
     * @param listener
     * @param tag
     */
    public static void logout(Context context, ApiUiUpdateListener listener, Object tag) {
        LogoutRequest logoutRequest = new LogoutRequest(context);
        LogTool.d(TAG, "logout " + logoutRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(logoutRequest, listener, tag);
    }

    /**
     * 用户更新密码
     *
     * @param context
     * @param registerInfo
     * @param listener
     * @param tag
     */
    public static void update_psw(Context context, RegisterInfo registerInfo, ApiUiUpdateListener listener, Object tag) {
        ForgetPswRequest forgetPswRequest = new ForgetPswRequest(context);
        LogTool.d(TAG, "update_psw --" + forgetPswRequest.getUrl() + "---" + JsonParser.beanToJson(registerInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(forgetPswRequest, JsonParser.beanToJson(registerInfo), listener, tag);
    }


    /**
     * 检查版本
     *
     * @param context
     * @param listener
     * @param tag
     */
    public static void checkVersion(Context context,
                                    ApiUiUpdateListener listener, Object tag) {
        CheckVersionRequest checkVersionRequest = new CheckVersionRequest(context);
        LogTool.d(TAG, "checkVersion --" + checkVersionRequest.getUrl());
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
    public static void feedBack(Context context, String content, String version, String platform,
                                ApiUiUpdateListener listener, Object tag) {
        FeedBackRequest feedBackRequest = new FeedBackRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("content", content);
            jsonParams.put("version", version);
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
                                         ApiUiUpdateListener listener, Object tag) {
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
     * 收集新用户的个人偏好
     *
     * @param context
     * @param ownerInfo
     * @param listener
     * @param tag
     */
    public static void post_collect_ownerinfo(Context context, OwnerInfo ownerInfo, ApiUiUpdateListener listener, Object tag) {
        PostCollectOwnerInfoRequest postCollectOwnerInfoRequest = new PostCollectOwnerInfoRequest(context);
        LogTool.d(TAG, "post_collect_ownerinfo --" + postCollectOwnerInfoRequest.getUrl() + "--" + JsonParser.beanToJson(ownerInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(postCollectOwnerInfoRequest, JsonParser.beanToJson(ownerInfo), listener, tag);
    }

    /**
     * @param context
     * @author zhanghao
     * @Description 获取业主需求
     */
    public static void get_Requirement_List(Context context, ApiUiUpdateListener listener, Object tag) {
        GetRequirementListRequest getRequirementListRequest = new GetRequirementListRequest(context);
        LogTool.d(TAG, "get_Requirement_list --" + getRequirementListRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(getRequirementListRequest, listener, tag);
    }

    /**
     * 发布需求
     *
     * @param context
     * @param requirementInfo
     * @param listener
     * @param tag
     */
    public static void add_Requirement(Context context, RequirementInfo requirementInfo, ApiUiUpdateListener listener, Object tag) {
        PostRequirementRequest postRequirementRequest = new PostRequirementRequest(context, requirementInfo);
        LogTool.d(TAG, "add_Requirement --" + postRequirementRequest.getUrl() + "--" + JsonParser.beanToJson(requirementInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(postRequirementRequest, JsonParser.beanToJson(requirementInfo), listener, tag);
    }

    /**
     * 更新装修需求
     *
     * @param context
     * @param requirementInfo
     * @param listener
     * @param tag
     */
    public static void update_Requirement(Context context, RequirementInfo requirementInfo, ApiUiUpdateListener listener, Object tag) {
        UpdateRequirementRequest updateRequirementRequest = new UpdateRequirementRequest(context, requirementInfo);
        LogTool.d(TAG, "add_Requirement --" + updateRequirementRequest.getUrl() + "--" + JsonParser.beanToJson(requirementInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(updateRequirementRequest, JsonParser.beanToJson(requirementInfo), listener, tag);
    }


    /**
     * @param context
     * @author zhanghao
     * @decription 业主提交装修流程 配置工地
     */
    public static void post_Owner_Process(Context context,
                                          String requirementid, String final_planid, ApiUiUpdateListener listener, Object tag) {
        PostProcessRequest postProcessRequest = new PostProcessRequest(context, requirementid, final_planid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            jsonParams.put("final_planid", final_planid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(postProcessRequest, jsonParams.toString(), listener, tag);
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
                                ApiUiUpdateListener listener, Object tag) {
        RegisterRequest registerRequest = new RegisterRequest(context, registerInfo);
        LogTool.d(TAG, "register  " + registerRequest.getUrl() + "--" + JsonParser.beanToJson(registerInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(registerRequest, JsonParser.beanToJson(registerInfo), listener, tag);
    }

    /**
     * 绑定手机号
     *
     * @param context
     * @param registerInfo
     * @param listener
     * @param tag
     */
    public static void bindingPhone(Context context, RegisterInfo registerInfo,
                                    ApiUiUpdateListener listener, Object tag) {
        BindingPhoneRequest bindingPhoneRequest = new BindingPhoneRequest(context, registerInfo.getPhone());
        LogTool.d(TAG, "register  " + bindingPhoneRequest.getUrl() + "--" + JsonParser.beanToJson(registerInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(bindingPhoneRequest, JsonParser.beanToJson(registerInfo), listener, tag);
    }

    /**
     * 绑定微信
     *
     * @param context
     * @param listener
     * @param tag
     */
    public static void bindingWeixin(Context context, String openid, String unionid,
                                     ApiUiUpdateListener listener, Object tag) {
        BindingWeiXinRequest bindingWeiXinRequest = new BindingWeiXinRequest(context, unionid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("wechat_openid", openid);
            jsonParams.put("wechat_unionid", unionid);
            LogTool.d(TAG, "bindingWeixin  " + bindingWeiXinRequest.getUrl() + "--" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(bindingWeiXinRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主刷新session
     *
     * @param context
     * @param uid
     * @param listener
     * @param tag
     */
    public static void refreshSession(Context context, String uid, ApiUiUpdateListener listener, Object tag) {
        RefreshSessionRequest refreshSessionRequest = new RefreshSessionRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", uid);
            LogTool.d(TAG, "refreshSession  " + refreshSessionRequest.getUrl() + "--" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(refreshSessionRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拿到我的意向设计师列表
     *
     * @param context
     * @param from
     * @param limit
     * @param listener
     * @param tag
     */
    public static void get_MyFavoriteDesignerList(Context context, int from, int limit, ApiUiUpdateListener listener, Object tag) {
        FavoriteDesignerListRequest favoriteDesignerListRequest = new FavoriteDesignerListRequest(context, from, limit);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("from", from);
            jsonParams.put("limit", limit);
            LogTool.d(TAG, "get_MyFavoriteDesignerList --" + favoriteDesignerListRequest.getUrl() + "---" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(favoriteDesignerListRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param listener
     * @author zhanghao
     * @description 业主获取个人信息
     */
    public static void get_Owner_Info(Context context,
                                      ApiUiUpdateListener listener, Object tag) {
        UserByOwnerInfoRequest userByOwnerInfoRequest = new UserByOwnerInfoRequest(context);
        LogTool.d(TAG, "get_Owner_Info：" + userByOwnerInfoRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(userByOwnerInfoRequest, listener, tag);
    }

    /**
     * @param context
     * @param listener
     * @author zhanghao
     * @description 获取我的工地列表
     */
    public static void get_Process_List(Context context,
                                        ApiUiUpdateListener listener, Object tag) {
        GetProcessListRequest processListRequest = new GetProcessListRequest(context);
        LogTool.d("JianFanJiaApiClient", "get_Process_List" + processListRequest.getUrl());
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
                                      ApiUiUpdateListener listener, Object tag) {
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
                                       ApiUiUpdateListener listener, Object tag) {
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
                                        ApiUiUpdateListener listener, Object tag) {
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
    public static void getRescheduleAll(Context context,
                                        ApiUiUpdateListener listener, Object tag) {
        GetAllRescheduleRequest getAllRescheduleRequest = new GetAllRescheduleRequest(context);
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(getAllRescheduleRequest, listener, tag);
    }

    /**
     * 评论装修流程
     *
     * @param commitCommentInfo
     * @param listener
     * @author zhanghao
     */
    public static void comment(Context context,
                               CommitCommentInfo commitCommentInfo,
                               ApiUiUpdateListener listener, Object tag) {
        CommitCommentRequest commitCommentRequest = new CommitCommentRequest(context);
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(commitCommentRequest, JsonParser.beanToJson(commitCommentInfo), listener, tag);
    }


    /**
     * 用户上传图片
     *
     * @param context
     * @param listener
     */
    public static void uploadImage(Context context, Bitmap bitmap,
                                   ApiUiUpdateListener listener, Object tag) {
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
                                            ApiUiUpdateListener listener, Object tag) {
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
     * 删除节点图片
     *
     * @param imageid
     * @param context
     * @param imageid
     * @param section
     * @param item
     * @param index
     * @param listener
     * @param tag
     */
    public static final void deleteImageToProcess(Context context, String imageid, String section, String item, int index, ApiUiUpdateListener listener, Object tag) {
        DeletePicToSectionItemRequest deletePicToSectionItemRequest = new DeletePicToSectionItemRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", imageid);
            jsonParams.put("section", section);
            jsonParams.put("item", item);
            jsonParams.put("index", index);
            LogTool.d(TAG, "deleteImageToProcess -" + deletePicToSectionItemRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(deletePicToSectionItemRequest, jsonParams.toString(), listener, tag);
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
                                             ApiUiUpdateListener listener, Object tag) {
        GetProcessInfoRequest getProcessInfoRequest = new GetProcessInfoRequest(context, processid);
        String getProcessUrl = Url_New.getInstance().GET_PROCESSINFO_BYID.replace(Url_New.ID,
                processid);
        getProcessInfoRequest.setUrl(getProcessUrl);
        LogTool.d(TAG, "processItemDone -" + getProcessInfoRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(getProcessInfoRequest, listener, tag);
    }

    /**
     * 修改业主个人信息
     *
     * @param context
     * @param ownerInfo
     * @param listener
     */
    public static void put_OwnerInfo(Context context,
                                     OwnerUpdateInfo ownerInfo, ApiUiUpdateListener listener, Object tag) {
        UpdateOwnerInfoRequest userByOwnerInfoUpdateRequest = new UpdateOwnerInfoRequest(context, ownerInfo);
        LogTool.d(TAG, "put_OwnerInfo -" + userByOwnerInfoUpdateRequest.getUrl() + JsonParser.beanToJson(ownerInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(userByOwnerInfoUpdateRequest, JsonParser.beanToJson(ownerInfo), listener, tag);
    }

    /**
     * 业主确认验收
     *
     * @param context
     * @param processid
     * @param section
     * @param listener
     */
    public static void confirm_CheckDoneByOwner(Context context, String processid,
                                                String section, ApiUiUpdateListener listener, Object tag) {
        OwnerFinishCheckRequest ownerFinishCheckRequest = new OwnerFinishCheckRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", processid);
            jsonParams.put("section", section);
            LogTool.d(TAG, "confirm_CheckDoneByOwner -" + ownerFinishCheckRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(ownerFinishCheckRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取首页数据
     *
     * @param context
     * @param from
     * @param limit
     * @param listener
     * @param tag
     */
    public static void getHomePageDesigners(Context context, int from, int limit, ApiUiUpdateListener listener, Object tag) {
        HomePageRequest homePageRequest = new HomePageRequest(context, from, limit);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("from", from);
            jsonParams.put("limit", limit);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(homePageRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取设计师信息主页
     *
     * @param context
     * @param designerid
     * @param listener
     * @param tag
     */
    public static void getDesignerHomePage(Context context, String designerid, ApiUiUpdateListener listener, Object tag) {
        DesignerHomePageRequest homePageRequest = new DesignerHomePageRequest(context, designerid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", designerid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(homePageRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取设计师作品
     *
     * @param context
     * @param designerid
     * @param from
     * @param limit
     * @param listener
     * @param tag
     */
    public static void searchDesignerProduct(Context context, String decType, String designStyle, String houseType, String decArea, String designerid, int from, int limit, ApiUiUpdateListener listener, Object tag) {
        SearchDesignerProductRequest productRequest = new SearchDesignerProductRequest(context, designerid, from, limit);
        JSONObject jsonParams = new JSONObject();
        try {
            JSONObject params1 = new JSONObject();
            if (!TextUtils.isEmpty(decType) && !decType.equals(Constant.KEY_WORD)) {
                params1.put("dec_type", decType);
            }
            if (!TextUtils.isEmpty(designStyle) && !designStyle.equals(Constant.KEY_WORD)) {
                params1.put("dec_style", designStyle);
            }
            if (!TextUtils.isEmpty(houseType) && !houseType.equals(Constant.KEY_WORD)) {
                params1.put("house_type", houseType);
            }
            if (!TextUtils.isEmpty(decArea) && !decArea.equals(Constant.KEY_WORD)) {
                params1.put("house_area", decArea);
            }
            if (!TextUtils.isEmpty(decArea)) {
                params1.put("designerid", designerid);
            }
            JSONObject params = new JSONObject();
            jsonParams.put("query", params1);
            jsonParams.put("search_word", "");
            jsonParams.put("from", from);
            jsonParams.put("limit", limit);
            LogTool.d(TAG, "jsonParams:" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(productRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加设计师到意向列表
     *
     * @param context
     * @param designerid
     * @param listener
     * @param tag
     */
    public static void addFavoriteDesigner(Context context, String designerid, ApiUiUpdateListener listener, Object tag) {
        AddFavoriteDesignerRequest addFavoriteDesignerRequest = new AddFavoriteDesignerRequest(context, designerid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", designerid);
            LogTool.d(TAG, "addFavoriteDesigner --" + "jsonParams:" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(addFavoriteDesignerRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除意向设计师
     *
     * @param context
     * @param designerid
     * @param listener
     * @param tag
     */
    public static void deleteFavoriteDesigner(Context context, String designerid, ApiUiUpdateListener listener, Object tag) {
        DeleteFavoriteDesignerRequest deleteFavoriteDesignerRequest = new DeleteFavoriteDesignerRequest(context, designerid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", designerid);
            LogTool.d(TAG, "deleteFavoriteDesignerRequest --" + deleteFavoriteDesignerRequest.getUrl() + "  jsonParams:" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(deleteFavoriteDesignerRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取某个作品主页
     *
     * @param context
     * @param productid
     * @param listener
     * @param tag
     */
    public static void getProductHomePage(Context context, String productid, ApiUiUpdateListener listener, Object tag) {
        GetProductHomePageRequest getProductHomePageRequest = new GetProductHomePageRequest(context, productid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", productid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getProductHomePageRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主获取自己可以预约的设计师列表
     *
     * @param context
     * @param requirementid
     * @param listener
     * @param tag
     */
    public static void getOrderDesignerListByUser(Context context, String requirementid, ApiUiUpdateListener listener, Object tag) {
        GetOrderDesignerListByUserRequest getOrderDesignerListByUserRequest = new GetOrderDesignerListByUserRequest(context, requirementid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getOrderDesignerListByUserRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主预约设计师
     *
     * @param context
     * @param requirementid
     * @param designerids
     * @param listener
     * @param tag
     */
    public static void orderDesignerByUser(Context context, String requirementid, List<String> designerids, ApiUiUpdateListener listener, Object tag) {
        OrderDesignerByUserRequest orderDesignerByUserRequest = new OrderDesignerByUserRequest(context, requirementid, designerids);
        JSONObject jsonParams = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            for (String designerid : designerids) {
                array.put(designerid);
            }
            jsonParams.put("requirementid", requirementid);
            jsonParams.put("designerids", array);
            LogTool.d(TAG, "jsonParams:" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(orderDesignerByUserRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主获取我预约了的设计师
     *
     * @param context
     * @param requirementid
     * @param listener
     * @param tag
     */
    public static void getOrderedDesignerList(Context context, String requirementid, ApiUiUpdateListener listener, Object tag) {
        GetOrderedDesignerRequest getOrderedDesignerRequest = new GetOrderedDesignerRequest(context, requirementid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            LogTool.d(TAG, "jsonParams:" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getOrderedDesignerRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主获取我的方案
     *
     * @param context
     * @param requirementid
     * @param designerid
     * @param listener
     * @param tag
     */
    public static void getDesignerPlansByUser(Context context, String requirementid, String designerid, ApiUiUpdateListener listener, Object tag) {
        GetDesignerPlansByUserRequest getDesignerPlansByUserRequest = new GetDesignerPlansByUserRequest(context, requirementid, designerid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            jsonParams.put("designerid", designerid);
            LogTool.d(TAG, "jsonParams:" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getDesignerPlansByUserRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主确认已量房
     *
     * @param context
     * @param requirementid
     * @param designerid
     * @param listener
     * @param tag
     */
    public static void confirmMeasureHouse(Context context, String requirementid, String designerid, ApiUiUpdateListener listener, Object tag) {
        ConformMeasureHouseRequest conformMeasureHouseRequest = new ConformMeasureHouseRequest(context, requirementid, designerid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            jsonParams.put("designerid", designerid);
            LogTool.d(TAG, "confirmMeasureHouse" + " -- " + conformMeasureHouseRequest.getUrl() + "--jsonParams:" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(conformMeasureHouseRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主选定方案
     *
     * @param context
     * @param requirementid
     * @param designerid
     * @param planid
     * @param listener
     * @param tag
     */
    public static void chooseDesignerPlan(Context context, String requirementid, String designerid, String planid, ApiUiUpdateListener listener, Object tag) {
        ChoosePlanByUserRequest choosePlanByUserRequest = new ChoosePlanByUserRequest(context, requirementid, designerid, planid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            jsonParams.put("designerid", designerid);
            jsonParams.put("planid", planid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(choosePlanByUserRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户获取某个方案信息
     *
     * @param context
     * @param planid
     * @param listener
     * @param tag
     */
    public static void getPlanInfo(Context context, String planid, ApiUiUpdateListener listener, Object tag) {
        GetPlanInfoRequest getPlanInfoRequest = new GetPlanInfoRequest(context, planid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", planid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getPlanInfoRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主评价设计师
     *
     * @param context
     * @param requirementid
     * @param designerid
     * @param service_attitude
     * @param respond_speed
     * @param comment
     * @param is_anonymous
     * @param listener
     * @param tag
     */
    public static void evaluateDesignerByUser(Context context, String requirementid, String designerid, int service_attitude, int respond_speed, String comment, String is_anonymous, ApiUiUpdateListener listener, Object tag) {
        EvaluateDesignerRequest evaluateDesignerRequest = new EvaluateDesignerRequest(context, requirementid, designerid, service_attitude, respond_speed, comment, is_anonymous);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("designerid", designerid);
            jsonParams.put("requirementid", requirementid);
            jsonParams.put("service_attitude", service_attitude);
            jsonParams.put("respond_speed", respond_speed);
            jsonParams.put("comment", comment);
            jsonParams.put("is_anonymous", is_anonymous);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(evaluateDesignerRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户获取合同
     *
     * @param context
     * @param requirementid
     * @param listener
     * @param tag
     */
    public static void getContractInfo(Context context, String requirementid, ApiUiUpdateListener listener, Object tag) {
        GetContractRequest getContractRequest = new GetContractRequest(context, requirementid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getContractRequest, jsonParams.toString(), listener, tag);
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
    public static void addComment(Context context, String topicid, String topictype, String section, String item, String content, String to, ApiUiUpdateListener listener, Object tag) {
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
    public static void getCommentList(Context context, String topicid, int from, int limit, String section, String item, ApiUiUpdateListener listener, Object tag) {
        GetCommentsRequest getCommentsRequest = new GetCommentsRequest(context, topicid, from, limit);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("topicid", topicid);
            jsonParams.put("from", from);
            jsonParams.put("limit", limit);
            jsonParams.put("section", section);
            jsonParams.put("item", item);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getCommentsRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主更换预约了的设计师
     *
     * @param context
     * @param requirementid
     * @param old_designerid
     * @param new_designerid
     * @param listener
     * @param tag
     */
    public static void changeOrderedDesignerByUser(Context context, String requirementid, String old_designerid, String new_designerid, ApiUiUpdateListener listener, Object tag) {
        ChangeOrderedDesignerRequest changeOrderedDesignerRequest = new ChangeOrderedDesignerRequest(context, requirementid, old_designerid, new_designerid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            jsonParams.put("old_designerid", old_designerid);
            jsonParams.put("new_designerid", new_designerid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(changeOrderedDesignerRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 游客搜索装修美图
     *
     * @param context
     * @param query
     * @param lastupdate
     * @param searchWord
     * @param from
     * @param limit
     * @param listener
     * @param tag
     */
    public static void searchDecorationImg(Context context, String section, String house_type, String dec_style, String searchWord, int lastUpdate, int from, int limit, ApiUiUpdateListener listener, Object tag) {
        SearchDecorationImgRequest searchDecorationImgRequest = new SearchDecorationImgRequest(context, section, house_type, dec_style, searchWord, lastUpdate, from, limit);
        JSONObject jsonParams = new JSONObject();
        try {
            JSONObject params1 = new JSONObject();
            if (!TextUtils.isEmpty(section) && !section.equals(Constant.KEY_WORD)) {
                params1.put("section", section);
            }
            if (!TextUtils.isEmpty(house_type) && !house_type.equals(Constant.KEY_WORD)) {
                params1.put("house_type", house_type);
            }
            if (!TextUtils.isEmpty(dec_style) && !dec_style.equals(Constant.KEY_WORD)) {
                params1.put("dec_style", dec_style);
            }
            JSONObject params2 = new JSONObject();
            params2.put("lastupdate", lastUpdate);
            jsonParams.put("query", params1);
            jsonParams.put("sort", params2);
            jsonParams.put("search_word", searchWord);
            jsonParams.put("from", from);
            jsonParams.put("limit", limit);
            LogTool.d(TAG, "jsonParams:" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(searchDecorationImgRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 游客获取装修美图首页
     *
     * @param context
     * @param id
     * @param listener
     * @param tag
     */
    public static void getDecorationImgInfo(Context context, String id, ApiUiUpdateListener listener, Object tag) {
        GetDecorationImgRequest getDecorationImgRequest = new GetDecorationImgRequest(context, id);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", id);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getDecorationImgRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户收藏作品
     *
     * @param context
     * @param productid
     * @param listener
     * @param tag
     */
    public static void addCollectionByUser(Context context, String productid, ApiUiUpdateListener listener, Object tag) {
        AddCollectionRequest addCollectionRequest = new AddCollectionRequest(context, productid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", productid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(addCollectionRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户删除收藏的作品
     *
     * @param context
     * @param productid
     * @param listener
     * @param tag
     */
    public static void deleteCollectionByUser(Context context, String productid, ApiUiUpdateListener listener, Object tag) {
        DeleteCollectionRequest deleteCollectionRequest = new DeleteCollectionRequest(context, productid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", productid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(deleteCollectionRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户获取收藏作品列表
     *
     * @param context
     * @param from
     * @param limit
     * @param listener
     * @param tag
     */
    public static void getCollectListByUser(Context context, int from, int limit, ApiUiUpdateListener listener, Object tag) {
        GetCollectionRequest getCollectionRequest = new GetCollectionRequest(context, from, limit);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("from", from);
            jsonParams.put("limit", limit);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getCollectionRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户收藏装修美图
     *
     * @param context
     * @param id
     * @param listener
     * @param tag
     */
    public static void addBeautyImgByUser(Context context, String id, ApiUiUpdateListener listener, Object tag) {
        AddBeautyImgRequest addBeautyImgRequest = new AddBeautyImgRequest(context, id);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", id);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(addBeautyImgRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户获取收藏的装修美图列表
     *
     * @param context
     * @param from
     * @param limit
     * @param listener
     * @param tag
     */
    public static void getBeautyImgListByUser(Context context, int from, int limit, ApiUiUpdateListener listener, Object tag) {
        GetBeautyImgListRequest getBeautyImgListRequest = new GetBeautyImgListRequest(context, from, limit);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("from", from);
            jsonParams.put("limit", limit);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getBeautyImgListRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户删除收藏的装修美图
     *
     * @param context
     * @param id
     * @param listener
     * @param tag
     */
    public static void deleteBeautyImgByUser(Context context, String id, ApiUiUpdateListener listener, Object tag) {
        DeleteBeautyImgRequest deleteBeautyImgRequest = new DeleteBeautyImgRequest(context, id);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", id);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(deleteBeautyImgRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 拿到首页作品列表
     *
     * @param context
     * @param limit
     * @param listener
     * @param tag
     */
    public static void getTopProducts(Context context, int limit, ApiUiUpdateListener listener, Object tag) {
        GetHomeProductRequest getHomeProductRequest = new GetHomeProductRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("limit", limit);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getHomeProductRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 游客搜索设计师
     *
     * @param context
     * @param decType
     * @param decHouseType
     * @param decStyle
     * @param decFee
     * @param searchWord
     * @param authedProductCount
     * @param from
     * @param limit
     * @param listener
     * @param tag
     */
    public static void searchDesigner(Context context, String decType, String decHouseType, String decStyle, String decFee, String searchWord, int authedProductCount, int from, int limit, ApiUiUpdateListener listener, Object tag) {
        SearchDesignerRequest searchDesignerRequest = new SearchDesignerRequest(context, decType, decHouseType, decStyle, decFee, searchWord, authedProductCount, from, limit);
        JSONObject jsonParams = new JSONObject();
        try {
            JSONObject params1 = new JSONObject();
            if (!TextUtils.isEmpty(decType) && !decType.equals(Constant.KEY_WORD)) {
                params1.put("dec_types", decType);
            }
            if (!TextUtils.isEmpty(decHouseType) && !decHouseType.equals(Constant.KEY_WORD)) {
                params1.put("dec_house_types", decHouseType);
            }
            if (!TextUtils.isEmpty(decStyle) && !decStyle.equals(Constant.KEY_WORD)) {
                params1.put("dec_styles", decStyle);
            }
            if (!TextUtils.isEmpty(decFee) && !decFee.equals(Constant.KEY_WORD)) {
                params1.put("design_fee_range", decFee);
            }
            JSONObject params2 = new JSONObject();
            params2.put("authed_product_count", authedProductCount);
            jsonParams.put("query", params1);
            jsonParams.put("sort", params2);
            jsonParams.put("search_word", searchWord);
            jsonParams.put("from", from);
            jsonParams.put("limit", limit);
            LogTool.d(TAG, "jsonParams:" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(searchDesignerRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
