package com.jianfanjia.cn.config;

/**
 * Description:全局变量类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 12:47
 */
public class Global {

    public static final String REQUIREMENT_INFO = "requirement_info";
    public static final String REQUIREMENT_ID = "requirement_id";

    public static final String DESIGNER_INF0 = "designer_info";
    public static final String DESIGNER_ID = "designer_id";

    public static final String PLAN_STATUS0 = "0";//已预约但没有响应
    public static final String PLAN_STATUS1 = "1";//已拒绝业主
    public static final String PLAN_STATUS2 = "2";//已响应但没有确认量房
    public static final String PLAN_STATUS3 = "3";//提交了方案
    public static final String PLAN_STATUS4 = "4";//方案未被选中
    public static final String PLAN_STATUS5 = "5";//方案已经选中
    public static final String PLAN_STATUS6 = "6";//已确认量房但没有方案
    public static final String PLAN_STATUS7 = "7";//设计师无响应导致响应过期
    public static final String PLAN_STATUS8 = "8";//设计师规定时间内没有提交方案

}
