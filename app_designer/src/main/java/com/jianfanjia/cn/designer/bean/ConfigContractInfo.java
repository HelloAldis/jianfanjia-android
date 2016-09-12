package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;

/**
 * Description: com.jianfanjia.cn.designer.bean
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-31 17:06
 */
public class ConfigContractInfo implements Serializable {

    private String requirementid;

    private long startAt;

    private String manager;

    public String getRequirementid() {
        return requirementid;
    }

    public void setRequirementid(String requirementid) {
        this.requirementid = requirementid;
    }

    public long getStartAt() {
        return startAt;
    }

    public void setStartAt(long startAt) {
        this.startAt = startAt;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}
