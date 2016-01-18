package com.jianfanjia.cn.designer.interf;

/**
 * Description: com.jianfanjia.cn.interf
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-17 20:17
 */
public interface ApiDataUpdateListenter {
    void pre();// 请求之前的准备操作

    void all();// 框架请求成功后的通用处理

    void onSuccess(Object data);// 数据正确后的处理

    void onFailure(String error_msg);// 数据错误后的处理
}
