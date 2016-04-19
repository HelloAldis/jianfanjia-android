package com.jianfanjia.api.model;

import java.util.List;

/**
 * Description: com.jianfanjia.cn.bean
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-11 14:55
 */
public class DecorateLiveList extends BaseModel {

    private List<DecorateLive> shares;
    private int total;

    public List<DecorateLive> getShares() {
        return shares;
    }

    public void setShares(List<DecorateLive> shares) {
        this.shares = shares;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
