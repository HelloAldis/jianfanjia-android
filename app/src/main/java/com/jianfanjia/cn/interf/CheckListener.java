package com.jianfanjia.cn.interf;

import com.jianfanjia.cn.bean.DesignerCanOrderInfo;

import java.util.List;

/**
 * Created by Administrator on 2015/12/28.
 */
public interface CheckListener {
    void getCheckedData(List<DesignerCanOrderInfo> info);
}
