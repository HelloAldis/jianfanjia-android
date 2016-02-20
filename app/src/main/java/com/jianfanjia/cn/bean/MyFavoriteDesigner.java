package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 我的意向设计师实体类
 * Description: com.jianfanjia.cn.bean
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-21 13:48
 */
public class MyFavoriteDesigner implements Serializable {
    private int total;
    private List<DesignerInfo> designers;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DesignerInfo> getDesigners() {
        return designers;
    }

    public void setDesigners(List<DesignerInfo> designers) {
        this.designers = designers;
    }
}
