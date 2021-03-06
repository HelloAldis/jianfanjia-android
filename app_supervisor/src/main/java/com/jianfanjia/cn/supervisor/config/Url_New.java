package com.jianfanjia.cn.supervisor.config;

import android.text.TextUtils;

import com.jianfanjia.cn.supervisor.BuildConfig;

/**
 * Description:服务器地址及相关接口配置类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 12:46
 */
public class Url_New {
    private static Url_New instance;
    public String SEVER_IP = BuildConfig.SERVER_URL;
    //    public String SEVER_PORT = "";
    public String MOBILE_SERVER_URL = BuildConfig.MOBILE_SERVER_URL;

    public static Url_New getInstance() {
        if (instance == null) {
            instance = new Url_New();
        }
        return instance;
    }

    private Url_New() {
    }

    public static String buildUrl(String url) {
        if (!TextUtils.isEmpty(url) && url.contains(BuildConfig.MOBILE_SERVER_URL)) {
            return url;
        } else {
            return BuildConfig.MOBILE_SERVER_URL + url;
        }
    }


    public String HTTPROOT = SEVER_IP + "/api/v2/app/";

    //365基础包详情页
    public String PACKGET365_DETAIL_URL= "/view/zt/365package/index.html";

    //分享的链接
    public String SHARE_IMAGE = "http://" + SEVER_IP +  "/zt/mobile/sharemito.html?title=";
    //分享的APP的logo图片链接
    public String SHARE_APP_LOGO = "http://" + SEVER_IP + "/zt/mobile/logo.png";

    //官方微博
    public static final String WEIBO_URL = "http://weibo.com/u/5691975473?topnav=1&wvr=6&topsug=1&is_all=1";

    public static String ID = "id";

    //屏幕宽高
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";

    // 登录
    public String LOGIN_URL = HTTPROOT + "supervisor_login";

    //业主刷新session
    public String REFRESH_SESSION = HTTPROOT + "supervisor_refresh_session";

    //更新监理的个人资料
    public String UPDATE_SUPERVISOR_INFO = HTTPROOT + "supervisor/info/update";
    //获取监理的个人资料
    public String GET_SUPERVISOR_INFO = HTTPROOT + "supervisor/info/get";

    // 注册
    public String REGISTER_URL = HTTPROOT + "designer_signup";
    public String BIND_URL = HTTPROOT + "device/bind";
    // 微信登录
    public String WEIXIN_LOGIN_URL = HTTPROOT + "user_wechat_login";
    //业主绑定手机号
    public String BIND_PHONE = HTTPROOT + "user_bind_phone";
    //业主绑定微信
    public String BIND_WEIXIN = HTTPROOT + "user_bind_wechat";
    //检查手机号是否被占用
    public String VERIFY_PHONE = HTTPROOT + "verify_phone";
    // 获取短信验证码
    public String GET_CODE_URL = HTTPROOT + "send_verify_code";
    // 忘记密码
    public String UPDATE_PASS_URL = HTTPROOT + "update_pass";
    // 检查版本
    public String UPDATE_VERSION_URL = HTTPROOT
            + "device/supervisor_android_build_version";
    // --------------------------------------------------业主-----------------------------------------------------------
    // 业主获取需求
    public String REQUIREMENT = HTTPROOT + "user/requirement";
    //业主获取需求列表(Get)
    public String REQUIREMENT_LIST = HTTPROOT + "user_my_requirement_list";
    //业主更新装修需求(Post)
    public String REQUIREMENT_UPDATE = HTTPROOT + "user_update_requirement";
    //业主预约量房
    public String USER_ORDER_DESIGNER = HTTPROOT + "user_order_designer";
    //业主确认设计师已量房
    public String DESIGNER_HOUSE_CHECKED = HTTPROOT + "designer_house_checked";
    //业主评价设计师
    public String EVALUATE_DESIGNER_BY_USER = HTTPROOT + "user_evaluate_designer";
    //用户获取某个方案的信息
    public String ONE_PLAN_INFO = HTTPROOT + "one_plan";
    //用户获取合同
    public String ONE_CONTRACT = HTTPROOT + "one_contract";
    //用户留言评论
    public String ADD_COMMENT = HTTPROOT + "add_comment";
    //用户获取留言评论并标记为已读
    public String GET_COMMENT = HTTPROOT + "topic_comments";
    // 业主提交装修流程 业主开启工地
    public String PROCESS = HTTPROOT + "user/process";
    //用户删除装修节点图片
    public String DELETE_PROCESS_PIC = HTTPROOT + "process/delete_image";

    // 业主获取自己的个人资料和修改个人资料
    public String GET_OWER_INFO = HTTPROOT + "user/info";

    // 获取工地列表
    public String GET_PROCESS_LIST = HTTPROOT
            + "process/list";
    // 用户上传图片
    public String UPLOAD_IMAGE = HTTPROOT + "image/upload";
    // 用户上传图片到装修流程
    public String POST_PROCESS_IMAGE = HTTPROOT + "process/image";
    // 评价装修流程
    public String POST_PROCESS_COMMENT = HTTPROOT
            + "process/comment";
    // 用户提交改期
    public String POST_RESCHDULE = HTTPROOT + "process/reschedule";
    // 用户同意改期
    public String AGREE_RESCHDULE = HTTPROOT
            + "process/reschedule/ok";
    // 用户拒绝改期
    public String REFUSE_RESCHDULE = HTTPROOT
            + "process/reschedule/reject";
    // 用户获取我的改期提醒
    public String GET_RESCHDULE_ALL = HTTPROOT
            + "process/reschedule/all";
    // 获取图片
    public String GET_IMAGE = HTTPROOT + "image/";
    //获取缩略图
    public String GET_THUMBNAIL_IMAGE = HTTPROOT + "thumbnail/" + WIDTH + "/";
    //获取缩略图,通过指定宽高
    public String GET_THUMBNAIL_IMAGE2 = HTTPROOT + "thumbnail2/" + WIDTH + "/" + HEIGHT + "/";

    // 根据工地id获取某个工地
    public String GET_PROCESSINFO_BYID = HTTPROOT + "process/"
            + ID;
    // 业主确认对比验收完成
    public String CONFIRM_CHECK_DONE_BY_OWNER = HTTPROOT
            + "process/done_section";
    // ----------------------------------------------------设计师----------------------------------------------------------
    //设计师获取所有的需求列表
    public String GET_ALL_REQUIREMENT_LIST = HTTPROOT + "designer_get_user_requirements";

    //设计师拒绝用户的需求
    public String REFUSE_REQUIREMENT = HTTPROOT + "designer/user/reject";

    //设计师响应业主的需求
    public String RESPONSE_REQUIREMENT = HTTPROOT + "designer/user/ok";

    //设计师配置合同
    public String DESIGNER_CONFIG_CONTRACT = HTTPROOT + "config_contract";

    //设计师获取我的方案
    public String USER_REQUIREMENT_PLANS = HTTPROOT + "designer_requirement_plans";

    // 用户反馈 feedback
    public String FEEDBACK_URL = HTTPROOT + "feedback";
    // 设计师获取个人信息
    public String GET_DESIGNER_INFO = HTTPROOT + "designer/info";
    // 获取设计师的工地列表
    public String GET_DESIGNER_PROCESS = HTTPROOT
            + "/process/list";
    // 设计师提交验收图片
    public String SUBMIT_YAHSHOU_IMAGE = HTTPROOT
            + "process/ysimage";
    // 用户完工装修流程小节点
    public String POST_PROCESS_DONE_ITEM = HTTPROOT
            + "process/done_item";
    // 设计师确认可以开始验收
    public String CONFIRM_CHECK_BY_DESIGNER = HTTPROOT
            + "process/can_ys";
    // 设计师删除验收图片
    public String DELETE_YANSHOU_IMG_BY_DESIGNER = HTTPROOT
            + "process/ysimage/delete";

    //设计师未读通知个数
    public String GET_UNREAD_MSG_COUNT = HTTPROOT + "unread_designer_message_count";
    //设计师搜索业主通知
    public String SEARCH_USER_MSG = HTTPROOT + "search_designer_message";
    //设计师通知详情
    public String GET_USER_MSG_DETAIL = HTTPROOT + "designer_message_detail";
    //设计师获取评论消息列表
    public String SEARCH_USER_COMMENT = HTTPROOT + "search_designer_comment";
    //设计师提醒业主确认量房
    public String NOTIFY_OWNER_MEASURE_HOUSE = HTTPROOT + "designer_remind_user_house_check";
}
