package com.jianfanjia.cn.config;

/**
 * Description:服务器地址及相关接口配置类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 12:46
 */
public class Url_New {
    public static String SEVER_IP = "101.200.191.159";
    //    public static String SEVER_IP = "www.jianfanjia.com";
//    public static String SEVER_PORT = "8888";
    public static String SEVER_PORT = "80";

    public static final String HTTPROOT = "http://" + SEVER_IP + ":"
            + SEVER_PORT + "/api/v2/app/";

    public static final String CONTRACT_URL = "http://" + SEVER_IP + "/tpl/user/agreement.html";

    public static final String ID = "id";

    //屏幕宽高
    public static final String WIDTH = "400";

    public static final String BIND_URL = HTTPROOT + "device/bind";
    // 登录
    public static final String LOGIN_URL = HTTPROOT + "user_login";
    // 注册
    public static final String REGISTER_URL = HTTPROOT + "user_signup";
    //检查手机号是否被占用
    public static final String VERIFY_PHONE = HTTPROOT + "verify_phone";
    // 获取短信验证码
    public static final String GET_CODE_URL = HTTPROOT + "send_verify_code";
    // 忘记密码
    public static final String UPDATE_PASS_URL = HTTPROOT + "update_pass";
    // 登出
    public static final String SIGNOUT_URL = HTTPROOT + "signout";
    // 检查版本
    public static final String UPDATE_VERSION_URL = HTTPROOT
            + "device/android_build_version";
    // 用户反馈 feedback
    public static final String FEEDBACK_URL = HTTPROOT + "feedback";
    // --------------------------------------------------业主-----------------------------------------------------------
    //业主获取移动端首页数据
    public static final String HOME_PAGE_DISIGNERS = HTTPROOT + "home_page_designers";
    //获取设计师信息主页
    public static final String DESIGNER_HOME_PAGE = HTTPROOT + "designer_home_page";
    //获取设计师作品
    public static final String SEARCH_DESIGNER_PRODUCT = HTTPROOT + "search_designer_product";
    //业主提交需求(post)
    public static final String POST_REQUIREMENT = HTTPROOT + "user_add_requirement";
    // 业主获取需求
    public static final String REQUIREMENT = HTTPROOT + "user/requirement";
    //业主获取需求列表(Get)
    public static final String REQUIREMENT_LIST = HTTPROOT + "user_my_requirement_list";
    //业主更新装修需求(Post)
    public static final String REQUIREMENT_UPDATE = HTTPROOT + "user_update_requirement";
    //业主获取自己可以预约的设计师列表(Post)
    public static final String REQUIREMENT_ORDER_DESIGNER_LIST = HTTPROOT + "designers_user_can_order";
    //业主获取我的意向设计师列表(Post)
    public static final String FAVORITE_DESIGNER_LIST = HTTPROOT + "favorite/designer/list";
    //业主添加设计师到意向列表（Post)
    public static final String ADD_FAVORITE_DESIGNER = HTTPROOT + "favorite/designer/add";
    //业主移除意向设计师列表的设计师(Post)
    public static final String DELETE_FAVORITE_DESIGNER = HTTPROOT + "favorite/designer/delete";
    //获取某个作品主页
    public static final String PRODUCT_HOME_PAGE = HTTPROOT + "product_home_page";
    //业主预约量房
    public static final String USER_ORDER_DESIGNER = HTTPROOT + "user_order_designer";
    //业主获取我预约的设计师
    public static final String USER_ORDERD_DESIGNERS = HTTPROOT + "user_ordered_designers";
    //业主更换预约了的设计师
    public static final String USER_CHANGE_ORDERD_DESIGNER = HTTPROOT + "user_change_ordered_designer";
    //业主确认设计师已量房
    public static final String DESIGNER_HOUSE_CHECKED = HTTPROOT + "designer_house_checked";
    //业主评价设计师
    public static final String EVALUATE_DESIGNER_BY_USER = HTTPROOT + "user_evaluate_designer";
    //业主获取我的方案
    public static final String USER_REQUIREMENT_PLANS = HTTPROOT + "user_requirement_plans";
    //业主选定方案
    public static final String USER_CHOOSE_PLAN = HTTPROOT + "user/plan/final";
    //用户获取某个方案的信息
    public static final String ONE_PLAN_INFO = HTTPROOT + "one_plan";
    //用户获取合同
    public static final String ONE_CONTRACT = HTTPROOT + "one_contract";
    //用户留言评论
    public static final String ADD_COMMENT = HTTPROOT + "add_comment";
    //用户获取留言评论并标记为已读
    public static final String GET_COMMENT = HTTPROOT + "topic_comments";
    // 业主提交装修流程 业主开启工地
    public static final String PROCESS = HTTPROOT + "user/process";
    //用户删除装修节点图片
    public static final String DELETE_PROCESS_PIC = HTTPROOT + "process/delete_image";

    // 获取业主的设计师
    public static final String GET_OWER_DESIGNER = HTTPROOT + "designer/" + ID
            + "/basicinfo";

    // 业主获取自己的个人资料和修改个人资料
    public static final String GET_OWER_INFO = HTTPROOT + "user/info";

    // 获取工地列表
    public static final String GET_PROCESS_LIST = HTTPROOT
            + "/process/list";
    // 用户上传图片
    public static final String UPLOAD_IMAGE = HTTPROOT + "image/upload";
    // 用户上传图片到装修流程
    public static final String POST_PROCESS_IMAGE = HTTPROOT + "process/image";
    // 评价装修流程
    public static final String POST_PROCESS_COMMENT = HTTPROOT
            + "process/comment";
    // 用户获取业主个人信息
    public static final String GET_ONE_OWNER_INFO = HTTPROOT + "user/" + ID
            + "/info";
    // 用户提交改期
    public static final String POST_RESCHDULE = HTTPROOT + "process/reschedule";
    // 用户同意改期
    public static final String AGREE_RESCHDULE = HTTPROOT
            + "process/reschedule/ok";
    // 用户拒绝改期
    public static final String REFUSE_RESCHDULE = HTTPROOT
            + "process/reschedule/reject";
    // 用户获取我的改期提醒
    public static final String GET_RESCHDULE_ALL = HTTPROOT
            + "process/reschedule/all";
    // 获取图片
    public static final String GET_IMAGE = HTTPROOT + "image/";
    //获取缩略图
    public static final String GET_THUMBNAIL_IMAGE = HTTPROOT + "thumbnail/" + WIDTH + "/";
    // 根据工地id获取某个工地
    public static final String GET_PROCESSINFO_BYID = HTTPROOT + "process/"
            + ID;
    // 业主确认对比验收完成
    public static final String CONFIRM_CHECK_DONE_BY_OWNER = HTTPROOT
            + "process/done_section";
    //获取装修美图
    public static final String GET_DECORATION_IMG = HTTPROOT
            + "";
}
