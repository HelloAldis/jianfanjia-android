package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Name: DesignerCanOrderListInfo
 * User: fengliang
 * Date: 2015-10-22
 * Time: 09:50
 */
public class DesignerCanOrderListInfo implements Serializable {
    private List<DesignerCanOrderInfo> rec_designer;

    private List<DesignerCanOrderInfo> favorite_designer;

    public List<DesignerCanOrderInfo> getRec_designer() {
        return rec_designer;
    }

    public void setRec_designer(List<DesignerCanOrderInfo> rec_designer) {
        this.rec_designer = rec_designer;
    }

    public List<DesignerCanOrderInfo> getFavorite_designer() {
        return favorite_designer;
    }

    public void setFavorite_designer(List<DesignerCanOrderInfo> favorite_designer) {
        this.favorite_designer = favorite_designer;
    }
}
