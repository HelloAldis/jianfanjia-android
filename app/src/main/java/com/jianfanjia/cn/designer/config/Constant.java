package com.jianfanjia.cn.designer.config;


import android.os.Environment;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.tools.FileUtil;
import com.jianfanjia.cn.designer.tools.SDCardHelper;

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
    public static final String BEAUTY_IMAG_PATH = SDCardHelper.getSDCardPublicDir(Environment.DIRECTORY_PICTURES);//
    // 保存美图,这个是对外公开的，所以保持在公共目录

    public static final String LOG_FILE = LOG_PATH + "/log.txt";//log文件
    public static final String ERROR_LOG_FILE = LOG_PATH + "/errorLog.txt";// errorlog文件

    public static final String CROP_PATH = IMAG_PATH + "/cropped.jpg";// 截图的图片路径
    public static final String TEMP_IMG = IMAG_PATH + "/current_camera_temp_path";//拍照的临时存储文件路径

    public static final String EXTRA_BUNDLE = "launchBundle";
    public static final String KEY_WORD = "不限";

    public static final int OWNER = 0;//首页
    public static final int MANAGE = 1;//工地管理
    public static final int MORE = 2;//我的

    public static final int HOME_PAGE_LIMIT = 20;//首页分页 每次加载20条
    public static final String IS_WEIXIN_FIRST_LOGIN = "is_weixin_first_login";
    public static final String FROM = "from";
    public static final String LIMIT = "limit";
    public static final String QUERY = "query";
    public static final int FROM_START = 0;

    public static final String HOTLINE_URL = "http://chat16.live800.com/live800/chatClient/chatbox" +
            ".jsp?companyID=611886&configID=139921&jid=3699665419";

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
    public static final int REQUESTCODE_PICKER_PIC = 0x11;

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
    public static final int OPERATE_ITEM = 7;

    //设计师方案列表item点击项
    public static final int PLAN_COMMENT_ITEM = 1;//留言
    public static final int PLAN_PREVIEW_ITEM = 2;//预览

    // 用户身份type
    public static final String IDENTITY_OWNER = "1";// 业主
    public static final String IDENTITY_DESIGNER = "2";// 设计师

    public static final String ISFIRST = "isFirst";// 是否是首次打开程序
    public static final String ACCOUNT = "account";// 账号
    public static final String PASSWORD = "password";// 密码
    public static final String USERNAME = "username";// 用户名称
    public static final String USERTYPE = "usertype";// 用户类型
    public static final String USERIMAGE_ID = "userimageid";// 用户头像id
    public static final String USER_ID = "userid";// 用户id
    public static final String USER_IS_LOGIN = "user_is_login";// 用户是否登录
    public static final String OPEN_ID = "open_id";//微信openid
    public static final String UNION_ID = "union_id";//微信union_id
    public static final String PROCESS_INFO = "process";
    public static final String SECTION = "section";

    public static final String OWNER_INFO = "owner_info";// 当前业主信息
    public static final String DEFAULT_PROCESS = "default_process";// 当前工地
    public static final String DESIGNER_PROCESS_LIST = "designer_process_list";// 设计师工地列表
    public static final String DEFAULT_PROCESSINFO = "default_processinfo.txt";// 默认工地信息
    public static final String DEFAULT_PROCESSINFO_ID = "1";

    public static final String DATA = "data";// 获取成功的数据
    public static final String ERROR_MSG = "err_msg";// 获取数据失败的返回信息
    public static final String SUCCESS_MSG = "msg";// 获取数据成功的返回信息

    // 提醒消息id
    public static final int CAIGOU_NOTIFY_ID = 1;
    public final static int FUKUAN_NOTIFY_ID = 2;
    public final static int YANQI_NOTIFY_ID = 3;
    public final static int YANSHOU_NOTIFY_ID = 4;

    // 设计师消息提醒类型designer_message_type
    public static final String TYPE_DELAY_MSG = "0";// 设计师提出改期提醒
    public static final String TYPE_CAIGOU_MSG = "1";// 采购提醒
    public static final String TYPE_SYSTEM_MSG = "2";//平台通知
    public static final String TYPE_PLAN_COMMENT_MSG = "3";//方案评论
    public static final String TYPE_SECTION_COMMENT_MSG = "4";//装修小节点评论
    public static final String TYPE_AUTH_TYPE_AGRAEE = "5";//基本信息认证通过
    public static final String TYPE_AUTH_TYPE_DISGREE = "6";//基本信息认证不通过
    public static final String TYPE_UID_TYPE_AGRAEE = "7";//身份证和银行卡审核通过
    public static final String TYPE_UID_TYPE_DISGRAEE = "8";//身份证和银行卡审核不通过
    public static final String TYPE_PROCESS_AGRAEE = "9";//工地审核通过
    public static final String TYPE_PROCESS_DISGRAEE = "10";//工地审核不通过
    public static final String TYPE_PRODUCT_AGRAEE = "11";//作品审核通过
    public static final String TYPE_PRODUCT_DISGRAEE = "12";//作品审核不通过
    public static final String TYPE_PRODUCT_OFFLINE = "13";//作品违规被下线
    public static final String TYPE_USER_APPOINT_MSG = "14";//业主预约提醒
    public static final String TYPE_USER_CONFIRM_HOSER_MSG = "15";//业主确认量房提醒
    public static final String TYPE_PLAN_CHOOSED_MSG = "16";//方案中标提醒
    public static final String TYPE_PLAN_NOT_CHOOSED_MSG = "17";//方案未中标提醒
    public static final String TYPE_CONFIRM_CONTRACT_MSG = "18";//业主确认合同提醒
    public static final String TYPE_REJECT_DELAY_MSG = "19";//业主拒绝改期
    public static final String TYPE_AGREE_DELAY_MSG = "20";//业主同意改期

    // 业主消息提醒状态
    public static final String UNREAD = "0";// 未读
    public static final String READ = "1";// 已读

    // 提醒类型
    public static final String YANQI_NOTIFY = "0";// 延期提醒
    public static final String CAIGOU_NOTIFY = "1";// 采购提醒
    public static final String FUKUAN_NOTIFY = "2";// 付款提醒
    public static final String CONFIRM_CHECK_NOTIFY = "3";// 确认验收提醒
    public static final String CONTRACT_NOTIFY = "4";// 确认合同提醒

    // 延期提醒状态,工序状态
    public static final String NO_START = "0";// 未开工
    public static final String DOING = "1";// 进行中
    public static final String FINISHED = "2";// 已完成
    public static final String YANQI_BE_DOING = "3";// 改期申请中
    public static final String YANQI_AGREE = "4";// 改期同意
    public static final String YANQI_REFUSE = "5";// 改期拒绝

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
            + R.mipmap.btn_icon_home_add;

    public static final String DEFALUT_PIC = DEFALUT_PIC_HEAD
            + R.mipmap.pix_default;

    public static final String CURRENT_LIST = "current_list";// 首页展开的第一道工序
    public static final String CURRENT_ITEM = "current_item";// 当前节点
    public static final String CURRENT_POSITION = "current_position";// 当前位置
    public static final String IMAGE_LIST = "image_list";
    public static final String PROCESS_NAME = "process_name";
    public static final String PROCESS_STATUS = "process_status";

    public static final String LAST_LOGIN_TIME = "last_login_time";// 上次登录时间
    public static final long DELAY_TIME = 24 * 60 * 60 * 1000L;// 延期过期时间为开工日期之后的24小时

    public static final String DOWNLOAD_URL = "download_url";// 下载链接

    public static final String SHUI_DIAN = "shui_dian";
    public static final String NI_MU = "ni_mu";
    public static final String YOU_QI = "you_qi";
    public static final String AN_ZHUANG = "an_zhuang";
    public static final String JUN_GONG = "jun_gong";

    public static final int REC_DESIGNER_TOTAL = 3;//推荐设计师总数
    public static final int ROST_REQUIREMTNE_TOTAL = 3;//能够发布需求总数

    public static final int LOVE_STYLE_TOTAL = 3;//能够选择的风格喜好数量

    //消息类型
    public static final int UPDATE_MANAAGE_FRAGMENT = 10;

    //通知搜索类型
    public static final String[] ALL = {TYPE_DELAY_MSG, TYPE_CAIGOU_MSG, TYPE_SYSTEM_MSG, TYPE_AUTH_TYPE_AGRAEE,
            TYPE_AUTH_TYPE_DISGREE, TYPE_UID_TYPE_AGRAEE, TYPE_UID_TYPE_DISGRAEE, TYPE_PROCESS_AGRAEE,
            TYPE_PROCESS_DISGRAEE, TYPE_PRODUCT_AGRAEE, TYPE_PRODUCT_DISGRAEE, TYPE_PRODUCT_OFFLINE,
            TYPE_USER_APPOINT_MSG, TYPE_USER_CONFIRM_HOSER_MSG, TYPE_PLAN_CHOOSED_MSG, TYPE_PLAN_NOT_CHOOSED_MSG,
            TYPE_CONFIRM_CONTRACT_MSG, TYPE_REJECT_DELAY_MSG, TYPE_AGREE_DELAY_MSG};
    public static final String[] SYSTEM = {TYPE_SYSTEM_MSG, TYPE_AUTH_TYPE_AGRAEE, TYPE_AUTH_TYPE_DISGREE,
            TYPE_UID_TYPE_AGRAEE, TYPE_UID_TYPE_DISGRAEE, TYPE_PROCESS_AGRAEE, TYPE_PROCESS_DISGRAEE,
            TYPE_PRODUCT_AGRAEE, TYPE_PRODUCT_DISGRAEE, TYPE_PRODUCT_OFFLINE};
    public static final String[] REQUIRE = {TYPE_USER_APPOINT_MSG, TYPE_USER_CONFIRM_HOSER_MSG,
            TYPE_PLAN_CHOOSED_MSG, TYPE_PLAN_NOT_CHOOSED_MSG, TYPE_CONFIRM_CONTRACT_MSG};
    public static final String[] SITE = {TYPE_DELAY_MSG, TYPE_CAIGOU_MSG, TYPE_REJECT_DELAY_MSG, TYPE_AGREE_DELAY_MSG};

    public static final String[] searchMsgCountType1 = {TYPE_DELAY_MSG, TYPE_CAIGOU_MSG, TYPE_SYSTEM_MSG,
            TYPE_AUTH_TYPE_AGRAEE, TYPE_AUTH_TYPE_DISGREE, TYPE_UID_TYPE_AGRAEE, TYPE_UID_TYPE_DISGRAEE,
            TYPE_PROCESS_AGRAEE, TYPE_PROCESS_DISGRAEE, TYPE_PRODUCT_AGRAEE, TYPE_PRODUCT_DISGRAEE,
            TYPE_PRODUCT_OFFLINE, TYPE_USER_APPOINT_MSG, TYPE_USER_CONFIRM_HOSER_MSG, TYPE_PLAN_CHOOSED_MSG,
            TYPE_PLAN_NOT_CHOOSED_MSG, TYPE_CONFIRM_CONTRACT_MSG, TYPE_REJECT_DELAY_MSG, TYPE_AGREE_DELAY_MSG};
    public static final String[] searchMsgCountType2 = {
            TYPE_PLAN_COMMENT_MSG, TYPE_SECTION_COMMENT_MSG
    };
}

