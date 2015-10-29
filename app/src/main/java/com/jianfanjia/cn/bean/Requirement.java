package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Name: Requirement
 * User: fengliang
 * Date: 2015-10-20
 * Time: 11:31
 */
public class Requirement implements Serializable {
    private String _id;
    private String status;
    private List<String> rec_designerids;
    private List<OrderDesignerInfo> designers;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getRec_designerids() {
        return rec_designerids;
    }

    public void setRec_designerids(List<String> rec_designerids) {
        this.rec_designerids = rec_designerids;
    }

    public List<OrderDesignerInfo> getDesigners() {
        return designers;
    }

    public void setDesigners(List<OrderDesignerInfo> designers) {
        this.designers = designers;
    }
}
