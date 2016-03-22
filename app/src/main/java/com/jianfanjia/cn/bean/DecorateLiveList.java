package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description: com.jianfanjia.cn.bean
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-11 14:55
 */
public class DecorateLiveList implements Serializable{

    private static final long serialVersionUID = 5323623591343265226L;
    private List<DecorateLiveInfo> shares;

    private int total;

    public List<DecorateLiveInfo> getShares() {
        return shares;
    }

    public void setShares(List<DecorateLiveInfo> shares) {
        this.shares = shares;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
