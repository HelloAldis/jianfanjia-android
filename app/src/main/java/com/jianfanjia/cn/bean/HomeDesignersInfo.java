package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Name: HomeDesignersInfo
 * User: fengliang
 * Date: 2015-10-20
 * Time: 11:29
 */
public class HomeDesignersInfo implements Serializable {


    private Requirement requirement;
    private List<DesignerListInfo> designers;

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public List<DesignerListInfo> getDesigners() {
        return designers;
    }

    public void setDesigners(List<DesignerListInfo> designers) {
        this.designers = designers;
    }
}
