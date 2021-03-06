package com.jianfanjia.cn.designer.config;

/**
 * Description:全局变量类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 12:47
 */
public class Global {

    public static final String WEB_VIEW_URL = "web_view_url";
    public static final String WEB_VIEW_URL_DEC_STRATEGY = "/view/article/";
    public static final String WEB_VIEW_URL_SAFEGUARD = "/view/zt/safeguard/";
    public static final String WEB_VIEW_URL_SUPERVISION = "/view/zt/supervision/";


    public static final String ISREGIISTER = "isregister";//是否是注册

    public static final String REGISTER_INFO = "register_info";
    public static final String REGISTER = "register";

    public static final String REQUIREMENT_INFO = "requirement_info";
    public static final String REQUIREMENT_ID = "requirement_id";
    public static final String REQUIREMENT_STATUS = "requirement_status";
    public static final String IS_PUBLISHREQUIREMENT = "is_publishrequirement";
    public static final String REQUIREMENT_DESIGNER_NUM = "requirement_designer_num";
    public static final String OWNERINFO = "ownerinfo";
    public static final String REQUIREMENG_ACTION_TYPE = "requirement_action_type";

    public static final String PROCESS_INFO = "process_info";
    public static final String PROCESS_ID = "process_id";

    public static final String USER_ID = "userid";
    public static final String PROCESS = "process";
    public static final String DESIGNER_ID = "designer_id";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_INFO = "product_info";
    public static final String EVALUATION = "evaluation";
    public static final String PLAN_ID = "planid";
    public static final String PLAN_DETAIL = "plan_datail";
    public static final String IMAGE_ID = "image_id";
    public static final String DESIGNER_NAME = "designer_name";
    public static final String DESIGNER_INFO = "designer_info";
    public static final String TOPIC_ID = "topic_id";
    public static final String TO = "to";
    public static final String SECTION = "section";
    public static final String ITEM = "item";
    public static final String TOPICTYPE = "topictype";
    public static final String SPEED = "speed";
    public static final String ATTITUDE = "attitude";
    public static final String DECORATION_ID = "decoration_id";
    public static final String CELL = "cell";
    public static final String PHONE = "phone";
    public static final String POSITION = "position";
    public static final String MSG_ID = "msg_id";
    public static final String TEAM_INFO = "team_info";

    public static final String TOPIC_PLAN = "0";//方案评论
    public static final String TOPIC_NODE = "1";//节点评论

    public static final String PLAN_STATUS0 = "0";//已预约但没有响应
    public static final String PLAN_STATUS1 = "1";//已拒绝业主
    public static final String PLAN_STATUS2 = "2";//已响应但没有确认量房
    public static final String PLAN_STATUS3 = "3";//提交了方案
    public static final String PLAN_STATUS4 = "4";//方案未被选中
    public static final String PLAN_STATUS5 = "5";//方案已经选中
    public static final String PLAN_STATUS6 = "6";//已确认量房但没有方案
    public static final String PLAN_STATUS7 = "7";//设计师无响应导致响应过期
    public static final String PLAN_STATUS8 = "8";//设计师规定时间内没有提交方案
    public static final String PLAN_STATUS9 = "9";//业主选定了方案，其他设计师还是 未响应，未量房，无方案   过期

    public static final String REQUIREMENT_STATUS0 = "0";//未预约任何设计师
    public static final String REQUIREMENT_STATUS1 = "1";//预约过设计师但是没有一个设计师响应过
    public static final String REQUIREMENT_STATUS2 = "2";//有一个或多个设计师响应但没有人量完房
    public static final String REQUIREMENT_STATUS3 = "3";//有一个或多个设计师提交了方案但是没有选定方案
    public static final String REQUIREMENT_STATUS4 = "4";//选定了方案但是还没有配置合同
    public static final String REQUIREMENT_STATUS5 = "5";//配置了工地
    public static final String REQUIREMENT_STATUS6 = "6";//有一个或多个设计师量完房但是没有人上传方案
    public static final String REQUIREMENT_STATUS7 = "7";//配置了合同但是没有配置工地
    public static final String REQUIREMENT_STATUS8 = "8";//已经完工

    public static final String DEC_TYPE_HOME = "0";//家装
    public static final String DEC_TYPE_BUSINESS = "1";//商装
    public static final String DEC_TYPE_SOFT = "2";//软装

    public static final String WORK_TYPE_DESIGNER = "2";//纯设计
    
    public static final String PHONE_MATCH = "^" +
            "(13[0-9]{9}|15[012356789][0-9]{8}|18[0123456789][0-9]{8}|147[0-9]{8}|170[0-9]{8}|177[0-9]{8})$";
    public static final String PASSWORD_MATCH = "^[\\@A-Za-z0-9\\!\\#\\$\\%\\^\\&\\*\\.\\~]{6,30}$";
    public static final String EMAIL_MATCH = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(" +
            "([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    public static final int PIC_WIDTH_NODE = 100;//节点图片的缩略图宽度100dp
    public static final int PIC_WIDTH_UPLOAD_WIDTH = 600;//上传头像的宽度统一为600px
    public static final int PIC_WIDTH_SHOW_WIDTH = 60;//头像显示统一为60dp

    public static final String ACTION_UPDATE = "com.jianfanjia.action.update";
    public static final String FORCE_UPDATE = "1";
    public static final String REC_UPDATE = "0";

    public static final String REQUIRE_DATA = "require_data";
    public static final String RESPONSE_DATA = "response_data";

    public static int SECTION_POSITION = 0;
    public static int HOUSE_TYPE_POSITION = 0;
    public static int DEC_STYLE_POSITION = 0;
}
