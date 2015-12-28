package com.jianfanjia.cn.config;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.tools.FileUtil;

/**
 * Description:常量类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 12:46
 */
public class Constant {
    public static final String SHARED_DATA = "jianfanjia_data";
    public static final String SHARED_USER = "jianfanjia_user";

    public static final String LOG_FILE_DIR = "/MyLog";
    public static final String APK_FILE_DIR = "/Apk";
    public static final String PIC_FILE_DIR = "/pic";
    public static final String IMG_FILE_DIR = "/BeautyImg";

    public static final String LOG_PATH = FileUtil.getAppCache(MyApplication.getInstance(), LOG_FILE_DIR);// log存放路径
    public static final String APK_PATH = FileUtil.getAppCache(MyApplication.getInstance(), APK_FILE_DIR);// 下载apk存放路径
    public static final String IMAG_PATH = FileUtil.getAppCache(MyApplication.getInstance(), PIC_FILE_DIR);// 保存照片
    public static final String BEAUTY_IMAG_PATH = FileUtil.getAppCache(MyApplication.getInstance(), IMG_FILE_DIR);// 保存美图

    public static final String LOG_FILE = LOG_PATH + "/log.txt";//log文件
    public static final String ERROR_LOG_FILE = LOG_PATH + "/errorLog.txt";// errorlog文件

    public static final String CROP_PATH = IMAG_PATH + "/cropped.jpg";// 截图的图片路径
    public static final String TEMP_IMG = IMAG_PATH + "/current_camera_temp_path";//拍照的临时存储文件路径

    public static final String EXTRA_BUNDLE = "launchBundle";
    public static final String KEY_WORD = "不限";

    public static final int HOME = 0;//首页
    public static final int DECORATE = 1;//装修美图
    public static final int MANAGE = 2;//工地管理
    public static final int MY = 3;//我的

    public static final int HOME_PAGE_LIMIT = 10;//首页分页 每次加载10条

    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }

    public static final int REQUESTCODE_CAMERA = 1;// 拍照
    public static final int REQUESTCODE_LOCATION = 2;// 本地相册选取
    public static final int REQUESTCODE_CROP = 3;// 裁剪
    public static final int REQUESTCODE_SHOW_PROCESS_PIC = 8;//显示照片列表
    public static final int REQUESTCODE_GOTO_COMMENT = 7;//调用评论
    public static final int REQUESTCODE_CHECK = 9;//调用评论

    public static final int REQUIRECODE_CITY = 0x00;
    public static final int REQUIRECODE_HOUSETYPE = 0x01;
    public static final int REQUIRECODE_PERSONS = 0x02;
    public static final int REQUIRECODE_LOVESTYLE = 0x03;
    public static final int REQUIRECODE_LOVEDESISTYLE = 0x04;
    public static final int REQUIRECODE_BUSI_DECORATETYPE = 0x05;
    public static final int REQUIRECODE_WORKTYPE = 0x06;
    public static final int REQUIRECODE_DESISEX = 0x07;

    // editactivity
    public static final int REQUESTCODE_EDIT_USERNAME = 4;
    public static final int REQUESTCODE_EDIT_HOME = 5;
    public static final int REQUESTCODE_EDIT_ADDRESS = 9;
    public static final String EDIT_TYPE = "inputType";
    public static final String EDIT_CONTENT = "content";

    public static final String EDIT_PROVICE = "provice";
    public static final String EDIT_CITY = "city";
    public static final String EDIT_DISTRICT = "district";

    // item点击项
    public static final int CONFIRM_ITEM = 1;
    public static final int ADD_ITEM = 2;
    public static final int IMG_ITEM = 3;
    public static final int COMMENT_ITEM = 4;
    public static final int DELAY_ITEM = 5;
    public static final int CHECK_ITEM = 6;

    //设计师方案列表item点击项
    public static final int PLAN_COMMENT_ITEM = 1;//留言
    public static final int PLAN_PREVIEW_ITEM = 2;//预览

    // 用户身份type
    public static final String IDENTITY_OWNER = "1";// 业主
    public static final String IDENTITY_DESIGNER = "2";// 设计师

    public static final String ISOPEN = "isOpen";// 消息推送是否开启
    public static final String ISFIRST = "isFirst";// 是否是首次打开程序
    public static final String ACCOUNT = "account";// 账号
    public static final String PASSWORD = "password";// 密码
    public static final String USERNAME = "username";// 用户名称
    public static final String USERTYPE = "usertype";// 用户类型
    public static final String USERIMAGE_ID = "userimageid";// 用户头像id
    public static final String USER_ID = "userid";// 用户id
    public static final String USER_IS_LOGIN = "user_is_login";// 用户是否登录
    public static final String USERINFO_UPDATE = "userinfo_is update";// 用户信息是否更新
    public static final String ISCONFIG_PROCESS = "is_config_process";// 是否配置过工地

    public static final String FINAL_DESIGNER_ID = "final_designerid";// 业主的设计师id
    public static final String FINAL_OWNER_ID = "final_owner_id";// 业主id
    // public static final String PROCESSINFO_ID = "processinfo_id";// 当前工地id
    public static final String PROCESSINFO_REFLECT = "processinfo_reflect";// 工地映射
    public static final String OWNER_INFO = "owner_info";// 当前业主信息
    public static final String DESIGNER_INFO = "designer_info";// 当前设计师信息
    public static final String DEFAULT_PROCESS = "default_process";// 当前工地
    public static final String DESIGNER_PROCESS_LIST = "designer_process_list";// 设计师工地列表
    public static final String DEFAULT_PROCESSINFO = "default_processinfo.txt";// 默认工地信息
    public static final String DEFAULT_PROCESSINFO_ID = "1";

    public static final String DATA = "data";// 获取成功的数据
    public static final String ERROR_MSG = "err_msg";// 获取数据失败的返回信息
    public static final String SUCCESS_MSG = "msg";// 获取数据成功的返回信息

    public static final int LIST_ITEM_TAG = 1;
    public static final int LIST_ITEM = 2;

    // 提醒消息id
    public static final int CAIGOU_NOTIFY_ID = 1;
    public final static int FUKUAN_NOTIFY_ID = 2;
    public final static int YANQI_NOTIFY_ID = 3;
    public final static int YANSHOU_NOTIFY_ID = 4;

    // 提醒类型
    public static final String YANQI_NOTIFY = "0";// 延期提醒
    public static final String CAIGOU_NOTIFY = "1";// 采购提醒
    public static final String FUKUAN_NOTIFY = "2";// 付款提醒
    public static final String CONFIRM_CHECK_NOTIFY = "3";// 确认验收提醒

    public static final String CHECK_REFUSE = "0";// 验收拒绝
    public static final String CHECK_AGREE = "1";// 验收同意

    // 延期提醒状态,工序状态
    public static final String NO_START = "0";// 未开工
    public static final String DOING = "1";// 进行中
    public static final String FINISHED = "2";// 已完成
    public static final String YANQI_BE_DOING = "3";// 改期申请中
    public static final String YANQI_AGREE = "4";// 改期同意
    public static final String YANQI_REFUSE = "5";// 改期拒绝

    /*// 工序状态
    public static final int NOT_START = 0;// 未开工
    public static final int WORKING = 1;// 正在进行中
    public static final int FINISH = 2;// 已完工
    public static final int OWNER_APPLY_DELAY = 3;// 业主申请延期中
    public static final int DESIGNER_APPLY_DELAY = 4;// 设计师申请延期中*/

    public static final String SEX_MAN = "0";
    public static final String SEX_WOMEN = "1";

    public static final String DESIGNER_NOT_AUTH_TYPE = "0";// 设计师未认证
    public static final String DESIGNER_NOW_AUTH_TYPE = "1";// 设计师正在审核中
    public static final String DESIGNER_FINISH_AUTH_TYPE = "2";// 设计师完成认证

    public static final String DEFALUT_PIC_HEAD = "drawable://";

    public static final String HOME_ADD_PIC = DEFALUT_PIC_HEAD
            + R.mipmap.btn_icon_home_add;

    public static final String HOME_DEFAULT_PIC = DEFALUT_PIC_HEAD
            + R.drawable.btn_default_bg;

    public static final String DEFALUT_OWNER_PIC = DEFALUT_PIC_HEAD
            + R.mipmap.icon_default_head;

    public static final String DEFALUT_ADD_PIC = DEFALUT_PIC_HEAD
            + R.mipmap.add;

    public static final String DEFALUT_PIC = DEFALUT_PIC_HEAD
            + R.mipmap.pix_default;

    public static final String CURRENT_LIST = "current_list";// 首页展开的第一道工序
    public static final String CURRENT_ITEM = "current_item";// 当前节点
    public static final String CURRENT_POSITION = "current_position";// 当前位置
    public static final String IMAGE_LIST = "image_list";
    public static final String SITE_ID = "site_id";
    public static final String PROCESS_NAME = "process_name";
    public static final String PROCESS_STATUS = "process_status";

    public static final int LOAD_SUCCESS = 0;// 数据加载成功
    public static final int LOAD_FAILURE = 1;// 数据加载失败

    public static final String COOKIES_CONFIG = "cookies_config";// cookies配置
    public static final String LAST_LOGIN_TIME = "last_login_time";// 上次登录时间
    public static final long LOGIN_EXPIRE = 24 * 60 * 60 * 1000L;// 登录过期时间为24小时
    public static final long DELAY_TIME = 24 * 60 * 60 * 1000L;// 延期过期时间为开工日期之后的24小时


    public static final String DOWNLOAD_URL = "download_url";// 下载链接

    public static final String INTENT_ACTION_USERINFO_CHANGE = "INTENT_ACTION_USERINFO_CHANGE";// 用户修改个人信息
    public static final String INTENT_ACTION_USER_IMAGE_CHANGE = "INTENT_ACTION_USER_IMAGE_CHANGE";// 用户修改头像

    public static final String SHUI_DIAN = "shui_dian";
    public static final String NI_MU = "ni_mu";
    public static final String YOU_QI = "you_qi";
    public static final String AN_ZHUANG = "an_zhuang";
    public static final String JUN_GONG = "jun_gong";

    public static final int REC_DESIGNER_TOTAL = 3;//推荐设计师总数
    public static final int ROST_REQUIREMTNE_TOTAL = 3;//能够发布需求总数

    public static final int LOVE_STYLE_TOTAL = 3;//能够选择的风格喜好数量

    public static final String KEY = "Item";

    public static final int UPDATE_FRAGMENT = 10;
    public static final int UPDATE_REQUIRE_FRAGMENT = 20;

    //消息类型
    public static final int UPDATE_HOME_FRAGMENT = 10;
    public static final int UPDATE_FAVORITE_FRAGMENT = 20;
    public static final int UPDATE_PRODUCT_FRAGMENT = 30;
    public static final int UPDATE_BEAUTY_FRAGMENT = 40;
}
