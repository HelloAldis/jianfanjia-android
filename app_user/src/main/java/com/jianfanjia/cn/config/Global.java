package com.jianfanjia.cn.config;

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
    public static final String WEB_VIEW_URL_YIJIA= "/weixin/jian/index.html";
    public static final String WEB_VIEW_URL_CHENGDAITONG = "/view/zt/loan/index.html";

    public static final String TOPIC_PLAN = "0";//方案评论
    public static final String TOPIC_NODE = "1";//节点评论
    public static final String TOPIC_DIARY = "2";//日记评论

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
    public static final String REQUIREMENT_STATUS8 = "8";//已完工

    public static final String DEC_TYPE_HOME = "0";//家装
    public static final String DEC_TYPE_BUSINESS = "1";//商装

    public static final String DEC_PROGRESS0 = "0";//我想看一看
    public static final String DEC_PROGRESS1 = "1";//正在做准备
    public static final String DEC_PROGRESS2 = "2";//已经开始装修

    public static final String PHONE_MATCH = "^" +
            "(13[0-9]{9}|15[012356789][0-9]{8}|18[0123456789][0-9]{8}|147[0-9]{8}|170[0-9]{8}|177[0-9]{8})$";
    public static final String PASSWORD_MATCH = "^[\\@A-Za-z0-9\\!\\#\\$\\%\\^\\&\\*\\.\\~]{6,30}$";

    public static final int PIC_WIDTH_NODE = 100;//节点图片的缩略图宽度100dp
    public static final int PIC_WIDTH_UPLOAD_WIDTH = 600;//上传头像的宽度统一为600px
    public static final int PIC_WIDTH_SHOW_WIDTH = 60;//头像显示统一为60dp

    public static final String ACTION_UPDATE = "com.jianfanjia.action.update";
    public static final String FORCE_UPDATE = "1";
    public static final String REC_UPDATE = "0";

    public static final String PURE_DESIGNER = "2";//纯设计
}
