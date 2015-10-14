package com.jianfanjia.cn.interf;

import com.jianfanjia.cn.base.BaseResponse;

public interface RequestInterfece {
    void pre();// 请求之前的准备操作

    void all();// 框架请求成功后的通用处理

    void onSuccess(BaseResponse baseResponse);// 数据正确后的处理

    void onFailure(BaseResponse baseResponse);// 数据错误后的处理
}
